package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @desc 临时用do层对象转换成bo层对象
 * @date 2022-05-26 14:06:00
 */
public final class DO2BOUtils {
    private DO2BOUtils() {}

    public static <D, B> B objA2objB(D objDO, Class<B> bClass) {
        if (objDO == null) {
            return null;
        }

        B objBO;
        try {
            objBO = bClass.newInstance();
        } catch (Exception e) {
            throw new SbcRuntimeException(e);
        }

        BeanUtils.copyProperties(objDO, objBO);
        return objBO;
    }

    public static <D, B> List<B> objA2objB4List(List<D> objDOs, Class<B> bClass) {
        if (objDOs == null) {
            return null;
        }
        List<B> objBOs = new ArrayList<>();
        for (D objDO : objDOs) {
            objBOs.add(objA2objB(objDO, bClass));
        }
        return objBOs;
    }
}
