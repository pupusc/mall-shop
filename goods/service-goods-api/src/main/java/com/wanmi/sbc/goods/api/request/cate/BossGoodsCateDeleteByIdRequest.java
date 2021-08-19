package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据主键删除商品分类请求类</p>
 * author: sunkun
 * Date: 2018-11-06
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BossGoodsCateDeleteByIdRequest implements Serializable {

    private static final long serialVersionUID = 8224650712299833889L;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Long cateId;
}
