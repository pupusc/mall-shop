package com.wanmi.sbc.pay.utils;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.pay.model.root.PayChannelItem;
import com.wanmi.sbc.pay.model.root.PayGateway;

import java.util.Objects;

/**
 * <p>账务交易验证器</p>
 * Created by of628-wenzhi on 2017-08-08-下午2:47.
 */
public class PayValidates {

    /**
     * 网关校验
     * @param gateway
     */
    public static void verifyGateway(PayGateway gateway){
        if(Objects.isNull(gateway) || gateway.getIsOpen() == IsOpen.NO){
            throw new SbcRuntimeException("K-100001");
        }
    }

    public static void verfiyPayChannelItem(PayChannelItem channelItem){
        if(Objects.isNull(channelItem) || channelItem.getIsOpen() == IsOpen.NO){
            throw new SbcRuntimeException("K-100001");
        }
    }
}
