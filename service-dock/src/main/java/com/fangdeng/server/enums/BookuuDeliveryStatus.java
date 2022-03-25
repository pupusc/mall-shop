package com.fangdeng.server.enums;

public enum BookuuDeliveryStatus {
    WAIT_AUDIT(0, "待审核",0),

    AUDITED(1, "已审核",0),

    DISTRIBUTING(2, "正在配货",0),

    WAIT_DELIVERY(3, "准备发货",0),

    SALED(4, "已销售",1),

    DELIVERYED(5, "已发货",1),
    REFUND(6, "退换货",0),

    CANCEL(7, "取消",3),

    HANGUP(8, "挂起",0);

    private Integer key;

    private String value;

    private Integer deliveryStatus;

    BookuuDeliveryStatus( Integer key, String value,Integer deliveryStatus) {
        this.key = key;
        this.value = value;
        this.deliveryStatus = deliveryStatus;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Integer getDeliveryStatus() {
        return deliveryStatus;
    }

    public static Integer getDeliveryStatusByKey(Integer key){
        for (BookuuDeliveryStatus typeEnum : BookuuDeliveryStatus.values()) {
            if (typeEnum.key.equals(key)) {
                return typeEnum.getDeliveryStatus();
            }
        }
        return null;
    }
}
