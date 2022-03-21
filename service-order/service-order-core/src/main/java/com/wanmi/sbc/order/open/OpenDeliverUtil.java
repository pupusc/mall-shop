package com.wanmi.sbc.order.open;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.order.open.model.OrderDeliverInfoResDTO;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

/**
 * @author Liang Jun
 * @desc 履约中台签名工具
 * @date 2022-02-22 13:24:00
 */
public class OpenDeliverUtil {

    @Value("${open.deliver.secret.key}")
    private static String secretKey;

    @Value("${open.deliver.callback.url}")
    private static String callbackUrl;

    public static String buildCallbackData(OrderDeliverInfoResDTO resultDTO) {
        if (Objects.isNull(resultDTO)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return JSON.toJSONString(resultDTO);
    }

    public static String getCallbackUrl() {
        if (Objects.isNull(callbackUrl)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "未找到履约中台的回调地址");
        }
        return callbackUrl;
    }
}
