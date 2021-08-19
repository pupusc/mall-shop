package com.wanmi.sbc.goods.api.constant;

/**
 * 预约、预售错误码
 */
public final class AppointmentAndBookingSaleErrorCode {
    private AppointmentAndBookingSaleErrorCode() {
    }

    /**
     * 很抱歉，您的订单中包含预约中的商品，请修改后重新提交！
     */
    public final static String CONTAIN_APPOINTMENT_SALE = "K-600017";

    /**
     * 很抱歉，您的订单中包含预售中的商品，请修改后重新提交！
     */
    public final static String CONTAIN_BOOKING_SALE = "K-600018";

}
