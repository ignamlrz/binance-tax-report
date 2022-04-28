package org.taxreport.binance.util.data;

public interface IntervalSearchable {
    boolean isSearchable(long beginTime, long endTime);
    Object search(long beginTime, long endTime);
}
