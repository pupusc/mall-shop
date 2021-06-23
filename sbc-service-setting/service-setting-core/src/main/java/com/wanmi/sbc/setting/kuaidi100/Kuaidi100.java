package com.wanmi.sbc.setting.kuaidi100;

import lombok.Data;

import java.io.Serializable;

/**
 * 快递100
 * Created by CHENLI on 2017/5/24.
 */
@Data
public class Kuaidi100 implements Serializable {

    /**
     * 快递100 api key
     */
    private String deliveryKey;

    /**
     * 客户key
     */
    private String customerKey;

}
