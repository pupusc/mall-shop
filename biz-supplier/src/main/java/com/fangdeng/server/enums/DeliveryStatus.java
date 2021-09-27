package com.fangdeng.server.enums;

public enum DeliveryStatus {
    UN_DELIVERY(0, "未发货"),

    DELIVERY_COMPLETE(1, "已发货"),

    PART_DELIVERY(2, "部分发货");

    private Integer key;

    private String value;

    DeliveryStatus( Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static DeliveryStatus getDeliveryStatus(Integer key){
        for (DeliveryStatus typeEnum : DeliveryStatus.values()) {
            if (typeEnum.key.equals(key)) {
                return typeEnum;
            }
        }
        return UN_DELIVERY;
    }
}
