package com.wanmi.sbc.index.requst;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.Data;

/**
 * <p>积分订单商品兑换请求结构</p>
 * Created by yinxianzhi on 2019-05-20-上午9:20.
 */
@Data
public class VersionRequest extends BaseRequest {

    private static final long serialVersionUID = 3228778527828317959L;

    /**
     * 积分商品Id
     */
    private Boolean falshFlag = Boolean.FALSE;



}
