package com.wanmi.sbc.goods.api.request.common;

import com.wanmi.sbc.goods.bean.dto.GoodsDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/11/2 10:03
 * @version: 1.0
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsCommonModifyRequest extends GoodsDTO implements Serializable {
    private static final long serialVersionUID = 2049416699616473526L;
}
