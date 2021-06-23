package com.wanmi.sbc.goods.api.request.freight;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 根据运费模板id查询单品运费模板请求
 * Created by daiyitian on 2018/10/31.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreightTemplateGoodsByIdRequest implements Serializable {


    private static final long serialVersionUID = -3381987177603611250L;

    /**
     * 运费模板id
     */
    @ApiModelProperty(value = "运费模板id")
    @NotNull
    private Long freightTempId;

}
