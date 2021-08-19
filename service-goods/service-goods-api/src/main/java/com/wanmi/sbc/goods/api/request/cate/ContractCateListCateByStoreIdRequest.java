package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>查询店铺已签约的类目列表请求类</p>
 * author: sunkun
 * Date: 2018-11-05
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractCateListCateByStoreIdRequest implements Serializable {

    private static final long serialVersionUID = -3598899150212525663L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;
}
