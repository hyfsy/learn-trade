package com.hyf.trade.settlementday;

import java.util.Calendar;

/**
 * 股指期权：在中国市场，50ETF期权以及沪深300股指期权等通常是每个月的第四个星期三为交割日，
 * 如果这一天恰好是法定节假日，那么就会顺延到下一个交易日。
 * <p>
 * 商品期权：交割日通常为标的期货合约交割月前一个月的倒数第五个交易日，遇法定节假日顺延。
 * 但不同的商品期权可能会有一定差异，如豆粕期权、白糖期权等。
 *
 * @author baB_hyf
 * @date 2025/04/12
 */
public class StockIndexOptionSettlementDayStrategy extends AbstractStockIndexSettlementDayStrategy {

    @Override
    protected int getMonthTime() {
        return 4;
    }

    @Override
    protected int getWeekTime() {
        return Calendar.WEDNESDAY;
    }

    @Override
    public String getName() {
        return "股指期权交割日";
    }
}
