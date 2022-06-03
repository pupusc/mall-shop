package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Liang Jun
 * @date 2022-05-31 22:51:00
 */
public class ParamValidator {

    public static <T> void validPropValueExist(String propName, String propValue, Integer pkValue, Mapper<T> mapper, Class<T> entityClass) {
        if (StringUtils.isBlank(propValue)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, propName + "属性不能为空");
        }
        Example example = new Example(entityClass);
        Example.Criteria criteria = example.createCriteria().andEqualTo(propName, propValue).andNotEqualTo("delFlag", 1);
        if (pkValue != null) {
            criteria.andNotEqualTo("id", pkValue);
        }
        int count = mapper.selectCountByExample(example);
        if (count > 0) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, propName + "属性已经存在");
        }
    }
}
