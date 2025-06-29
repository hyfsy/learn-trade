package com.hyf.trade.core;

import com.hyf.trade.client.ResultTable;
import com.hyf.trade.client.ResultTableClient;
import com.hyf.trade.util.CalendarUtil;
import com.hyf.trade.util.StrategyUtil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

/**
 * 第二天的一字封单计算
 * <p>
 * (2)
 * 量化涨停封单：
 * 这个一般来说可能很多兄弟们都不太了解，直接看流通Z即可，1亿对应1万手，封单大于10倍的一字封单视为超预期有效，十足定方向。
 * 普通涨停板收盘时能有3倍是正常，次日有溢价预期；大于5倍明天就是加速预期的走势。
 * 举个例子，下图中（2025.3.6）云鼎科技的流通Z为3.58亿，周四封单位13.3万手，接近三倍的封单，理应有3%-6%的溢价。
 *
 * @author baB_hyf
 * @date 2025/06/29
 */
public class SealedOrderAmountCalculator {

    public static void main(String[] args) {
        SealedOrderAmountCalcParam param = new SealedOrderAmountCalcParam();
        param.setCurrentCalendar(CalendarUtil.parse_yyyy_MM_dd("2025.3.6"));
        param.add("湖北广电", "195.1万");
        param.add("岩山科技", "1880万");
        param.add("云鼎科技", "162万");
        List<Result> results = calc(param);
        for (Result result : results) {
            System.out.println("====================");
            System.out.println("股票名称：" + result.getStockName());
            System.out.println("预期封单(亿): " + result.getExpectedSealedOrderAmount().toPlainString());
            System.out.println("近期抛压(亿): " + result.getSellPressure().toPlainString());
        }
        System.out.println();
    }

    public static List<Result> calc(SealedOrderAmountCalcParam param) {

        // 当天晚上进行计算，计算第二天一字封单
        String question = "当前交易日涨停，当前交易日成交量，当前交易日收盘价，后一个交易日涨停价，当前交易日时5日vol，当前交易日时10日vol";
        question = StrategyUtil.transformStrategy(question, param.getCurrentCalendar());

        ResultTableClient client = ResultTableClient.getReal();
        ResultTable resultTable = client.createResultTable(question);

        List<Result> results = new ArrayList<>();

        for (Map.Entry<String, String> entry : param.getStockNameAndRecentMaxSellPressureVolumeMap().entrySet()) {
            String stockName = entry.getKey();
            String recentMaxSellPressureVolume = entry.getValue();

            ResultTable.Stock stock = resultTable.getStockByName(stockName);
            if (stock == null) {
                Result result = new Result();
                result.setStockName(stockName);
                result.setExpectedSealedOrderAmount(BigDecimal.ZERO);
                result.setSellPressure(BigDecimal.ZERO);
                results.add(result);
                continue;
                // throw new IllegalStateException("Stock is null, name: " + stockName);
            }

            String yyyy_mm_dd = CalendarUtil.to_simple_yyyy_MM_dd(param.getCurrentCalendar());
            Calendar nextDay = CalendarUtil.copy(param.getCurrentCalendar());
            nextDay.add(Calendar.DAY_OF_YEAR, 1);
            String next_yyyy_mm_dd = CalendarUtil.to_simple_yyyy_MM_dd(nextDay);

            StockNumber closePrice = StockNumber.of(stock.getValue("收盘价:不复权[" + yyyy_mm_dd + "]"));
            StockNumber volume = StockNumber.of(stock.getValue("成交量[" + yyyy_mm_dd + "]"));
            StockNumber vol_5 = StockNumber.of(stock.getValue("5日vol[" + yyyy_mm_dd + "]"));
            StockNumber vol_10 = StockNumber.of(stock.getValue("10日vol[" + yyyy_mm_dd + "]"));
            StockNumber recentMaxSellPressureVolumeU = StockNumber.of(recentMaxSellPressureVolume);

            StockNumber nextLimitUpAmount = StockNumber.of(stock.getValue("涨停价[" + next_yyyy_mm_dd + "]"));
            StockNumber nextSealedOrderAmount = nextLimitUpAmount.multiply(volume);
            StockNumber vol5SealedOrderAmount = nextLimitUpAmount.multiply(vol_5);
            StockNumber vol10SealedOrderAmount = nextLimitUpAmount.multiply(vol_10);
            StockNumber recentMaxSellPressureSealedOrderAmount = nextLimitUpAmount.multiply(recentMaxSellPressureVolumeU);
            StockNumber expectedSealedOrderAmount = nextSealedOrderAmount
                    .add(vol5SealedOrderAmount)
                    .add(vol10SealedOrderAmount)
                    .add(recentMaxSellPressureSealedOrderAmount)
                    .divide(StockNumber.of(4));

            Result result = new Result();
            result.setStockName(stockName);
            result.setExpectedSealedOrderAmount(expectedSealedOrderAmount.get(StockNumber.Unit.HUNDRED_MILLION));
            result.setSellPressure(recentMaxSellPressureSealedOrderAmount.get(StockNumber.Unit.HUNDRED_MILLION));
            results.add(result);
        }
        return results;
    }

    public static enum StockType {
        MAIN_BOARD(10), // 主板
        GROWTH_ENTERPRISE_MARKET(20), // 创业板
        STAR_MARKET(20), // 科创板
        BEIJING_STOCK_EXCHANGE(30), // 北交所
        ;

        int amplitude; // 幅度

        StockType(int amplitude) {
            this.amplitude = amplitude;
        }

        public int getAmplitude() {
            return amplitude;
        }
    }

    // private BigDecimal calcLimitUpAmount(BigDecimal currentAmount, StockType stockType) {
    //
    // }

    public static class SealedOrderAmountCalcParam {
        private Calendar            currentCalendar                            = Calendar.getInstance();
        // 股票名称 -> 近期最大一天的成交量，如xx万
        private Map<String, String> stockNameAndRecentMaxSellPressureVolumeMap = new LinkedHashMap<>();

        public SealedOrderAmountCalcParam add(String stockName, String recentMaxSellPressureVolume) {
            stockNameAndRecentMaxSellPressureVolumeMap.put(stockName, recentMaxSellPressureVolume);
            return this;
        }

        public Calendar getCurrentCalendar() {
            return currentCalendar;
        }

        public SealedOrderAmountCalcParam setCurrentCalendar(Calendar currentCalendar) {
            this.currentCalendar = currentCalendar;
            return this;
        }

        public Map<String, String> getStockNameAndRecentMaxSellPressureVolumeMap() {
            return stockNameAndRecentMaxSellPressureVolumeMap;
        }
    }

    public static class StockNumber {
        private final BigDecimal number;
        private       int        round_mode = BigDecimal.ROUND_HALF_UP;
        private       int        tail_num   = 2;

        private StockNumber(BigDecimal number) {
            this.number = number;
        }

        public static StockNumber of(long number) {
            return of(parse(String.valueOf(number)));
        }

        public static StockNumber of(String number) {
            return of(parse(number));
        }

        public static StockNumber of(BigDecimal number) {
            return new StockNumber(number);
        }

        private static BigDecimal parse(String number) {

            Unit[] values = Unit.values();
            for (Unit unit : values) {
                boolean containsUnit = number.contains(unit.name);
                if (containsUnit) {
                    number = number.replace(unit.name, "");
                    return new BigDecimal(number).movePointRight(unit.shift + 2); // 2表示手数转为数量
                }
            }

            return new BigDecimal(number);
        }

        public StockNumber add(StockNumber number) {
            return of(get().add(number.get()));
        }

        public StockNumber subtract(StockNumber number) {
            return of(get().subtract(number.get()));
        }

        public StockNumber multiply(StockNumber number) {
            return of(get().multiply(number.get()));
        }

        public StockNumber divide(StockNumber number) {
            return of(get().divide(number.get(), MathContext.DECIMAL64));
        }

        public void setTail_num(int tail_num) {
            this.tail_num = tail_num;
        }

        public void setRound_mode(int round_mode) {
            this.round_mode = round_mode;
        }

        public BigDecimal get() {
            return number.setScale(tail_num, round_mode);
        }

        public BigDecimal get(Unit unit) {
            return number.movePointLeft(unit.shift).setScale(tail_num, round_mode);
        }

        @Override
        public String toString() {
            return number.toString();
        }

        public static enum Unit {
            YUAN("元", 0),
            TEN_THOUSAND("万", 4),
            HUNDRED_MILLION("亿", 8),
            HAND("手", 2),
            TEN_THOUSAND_HAND("万手", 6),
            HUNDRED_MILLION_HAND("亿手", 10),
            ;
            String name;
            int    shift;

            Unit(String name, int shift) {
                this.name = name;
                this.shift = shift;
            }
        }
    }

    public static class Result {

        private String     stockName;
        private BigDecimal expectedSealedOrderAmount;
        private BigDecimal sellPressure;

        public String getStockName() {
            return stockName;
        }

        public void setStockName(String stockName) {
            this.stockName = stockName;
        }

        public BigDecimal getExpectedSealedOrderAmount() {
            return expectedSealedOrderAmount;
        }

        public void setExpectedSealedOrderAmount(BigDecimal expectedSealedOrderAmount) {
            this.expectedSealedOrderAmount = expectedSealedOrderAmount;
        }

        public BigDecimal getSellPressure() {
            return sellPressure;
        }

        public void setSellPressure(BigDecimal sellPressure) {
            this.sellPressure = sellPressure;
        }
    }
}
