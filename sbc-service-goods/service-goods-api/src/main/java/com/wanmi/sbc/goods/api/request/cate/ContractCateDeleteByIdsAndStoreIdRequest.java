package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p>根据主键列表和店铺id删除签约分类</p>
 * author: sunkun
 * Date: 2018-11-05
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractCateDeleteByIdsAndStoreIdRequest implements Serializable {

    private static final long serialVersionUID = 166561515376781656L;

    /**
     * 组件集合
     */
    @ApiModelProperty(value = "组件集合")
    @NotNull
    @Size(min = 1)
    private List<Long> ids;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;
}
