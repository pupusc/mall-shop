package com.wanmi.ares.report.customer.dao;

public enum CustomerReportTable {
    CUSTOMER_DAY("customer_day"),
    CUSTOMER_RECENT_SEVEN("customer_recent_seven"),
    CUSTOMER_RECENT_THIRTY("customer_recent_thirty"),
    CUSTOMER_MONTH("customer_month"),
    CUSTOMER_LEVEL_DAY("customer_level_day"),
    CUSTOMER_LEVEL_RECENT_SEVEN("customer_level_recent_seven"),
    CUSTOMER_LEVEL_RECENT_THIRTY("customer_level_recent_thirty"),
    CUSTOMER_LEVEL_MONTH("customer_level_month"),
    CUSTOMER_REGION_MONTH("customer_region_month"),
    CUSTOMER_REGION_DAY("customer_region_day"),
    CUSTOMER_REGION_RECENT_SEVEN("customer_region_recent_seven"),
    CUSTOMER_REGION_RECENT_THIRTY("customer_region_recent_thirty")
    ;


    private String name;

    CustomerReportTable(final String value) {
        name = value;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
