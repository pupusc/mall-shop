package com.wanmi.sbc.goods.api.response.storecate;

import com.wanmi.sbc.goods.bean.vo.StoreCateResponseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Author: bail
 * Time: 2017/11/13.10:25
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCateAddResponse implements Serializable {

    private static final long serialVersionUID = 4830809452869579507L;

    @ApiModelProperty(value = "店铺分类")
    private StoreCateResponseVO storeCateResponseVO;
}
