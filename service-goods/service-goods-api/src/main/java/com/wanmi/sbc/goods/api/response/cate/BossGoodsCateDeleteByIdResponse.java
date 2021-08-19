package com.wanmi.sbc.goods.api.response.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>根据主键删除商品分类请求类</p>
 * author: sunkun
 * Date: 2018-11-06
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BossGoodsCateDeleteByIdResponse implements Serializable {

    private static final long serialVersionUID = 508235636593223918L;

    @ApiModelProperty(value = "商品分类Id")
    private List<Long> longList;
}
