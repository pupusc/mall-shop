package com.wanmi.sbc.goods.api.request.freight;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 店铺运费模板验证名称数据结构
 * Created by sunkun on 2018/5/3.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreightTemplateStoreExistsNameRequest implements Serializable {

    private static final long serialVersionUID = -8107078231538944644L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

    /**
     * 运费模板名称
     */
    @ApiModelProperty(value = "运费模板名称")
    @NotBlank
    private String freightTempName;
}
