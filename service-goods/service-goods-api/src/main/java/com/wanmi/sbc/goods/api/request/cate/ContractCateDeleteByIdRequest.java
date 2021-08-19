package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>根据主键删除签约分类请求类</p>
 * author: sunkun
 * Date: 2018-11-05
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractCateDeleteByIdRequest implements Serializable {

    private static final long serialVersionUID = -9020085229615439008L;

    @ApiModelProperty(value = "主键")
    @NotNull
    private Long contractCateId;

}
