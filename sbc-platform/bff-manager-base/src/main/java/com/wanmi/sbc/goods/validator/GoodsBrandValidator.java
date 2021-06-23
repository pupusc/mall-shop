package com.wanmi.sbc.goods.validator;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandSaveRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

/**
 * 品牌提交验证器
 * Created by daiyitian on 17/5/4.
 */
@Component
public class GoodsBrandValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return GoodsBrandSaveRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GoodsBrandSaveRequest saveRequest = (GoodsBrandSaveRequest)target;
        //验证品牌名称
        if(Objects.isNull(saveRequest.getGoodsBrand())
                || StringUtils.isBlank(saveRequest.getGoodsBrand().getBrandName())
                || ValidateUtil.isOverLen(saveRequest.getGoodsBrand().getBrandName(), 30)){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //验证品牌昵称
        if(ValidateUtil.isOverLen(saveRequest.getGoodsBrand().getNickName(), 30)){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //限制拼音长度
        if(ValidateUtil.isOverLen(saveRequest.getGoodsBrand().getPinYin(), 45)){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //限制简拼长度
        if(ValidateUtil.isOverLen(saveRequest.getGoodsBrand().getSPinYin(), 45)){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }
}
