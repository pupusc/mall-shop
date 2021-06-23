package com.wanmi.sbc.goods.api.request.cate;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsCateDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分类更新请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsCateSaveRequest extends BaseRequest {

    private static final long serialVersionUID = -3490501303943686411L;

    /**
     * 商品分类信息
     */
    @ApiModelProperty(value = "商品分类信息")
    private GoodsCateDTO goodsCate;
}
