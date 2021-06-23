package com.wanmi.sbc.goods.validator;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateSaveRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 分类提交验证器
 * Created by daiyitian on 17/5/4.
 */
@Component
public class GoodsCateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return GoodsCateSaveRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GoodsCateSaveRequest saveRequest = (GoodsCateSaveRequest) target;
        //验证分类名
        if (saveRequest.getGoodsCate() == null || StringUtils.isBlank(saveRequest.getGoodsCate().getCateName()) || ValidateUtil.isOverLen(saveRequest.getGoodsCate().getCateName(), 45)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //限制图片长度
        if (ValidateUtil.isOverLen(saveRequest.getGoodsCate().getCateImg(), 255)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //限制拼音长度
        if (ValidateUtil.isOverLen(saveRequest.getGoodsCate().getPinYin(), 45)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //限制简拼长度
        if (ValidateUtil.isOverLen(saveRequest.getGoodsCate().getSPinYin(), 45)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }
}
