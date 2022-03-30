package com.soybean.mall.wx.mini.order.bean.request;

import lombok.Data;

@Data
public class WxAcceptReturnAftersaleRequest {


    private String out_aftersale_id;

    @Data
    public class AddressInfo {

        private String receiver_name;
        private String detailed_address;
        private String tel_number;
        private String country;
        private String province;
        private String city;
        private String town;
    }
}
