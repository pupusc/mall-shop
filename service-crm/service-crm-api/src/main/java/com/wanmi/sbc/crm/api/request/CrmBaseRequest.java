package com.wanmi.sbc.crm.api.request;

import com.wanmi.sbc.common.base.BaseRequest;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2019-10-14 10:16
 */
public class CrmBaseRequest extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 970930809997077706L;

    /**
     * 统一参数校验入口
     */
    public void checkParam(){}
}