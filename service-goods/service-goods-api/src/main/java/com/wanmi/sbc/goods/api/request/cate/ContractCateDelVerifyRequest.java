package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-05
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractCateDelVerifyRequest implements Serializable {

    private static final long serialVersionUID = 5732120721622553793L;

    /**
     * 分类主键
     */
    @ApiModelProperty(value = "分类主键")
    @NotNull
    private Long cateId;

    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    @NotNull
    private Long storeId;

}
