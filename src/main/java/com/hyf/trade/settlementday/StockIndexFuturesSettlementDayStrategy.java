package com.hyf.trade.settlementday;

import java.util.Calendar;

/**
 * 在中国市场，股指期货的交割日通常是合约到期月份的第三个星期五。如果该日恰逢法定假日，则交割日期顺延至下一个交易日。
 *
 * @author baB_hyf
 * @date 2025/04/12
 */
public class StockIndexFuturesSettlementDayStrategy extends AbstractStockIndexSettlementDayStrategy {

    @Override
    protected int getMonthTime() {
        return 3;
    }

    @Override
    protected int getWeekTime() {
        return Calendar.FRIDAY;
    }

    @Override
    public String getName() {
        return "股指期货交割日";
    }
}
