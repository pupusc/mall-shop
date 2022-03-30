package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxAcceptReturnAftersaleRequest {

    @JSONField(name = "out_aftersale_id")
    private String outAftersaleId;
    @JSONField(name = "address_info")
    private AddressInfo addressInfo;

    @Data
    public class AddressInfo {

        @JSONField(name = "receiver_name")
        private String receiverName;
        @JSONField(name = "detailed_address")
        private String detailedAddress;
        @JSONField(name = "tel_number")
        private String telNumber;
        private String country;
        private String province;
        private String city;
        private String town;
    }
}
