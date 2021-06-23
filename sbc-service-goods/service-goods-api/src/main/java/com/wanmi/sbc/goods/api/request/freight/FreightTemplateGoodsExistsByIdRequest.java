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
 * 根据单品运费模板id验证单品运费模板请求
 * Created by daiyitian on 2018/10/31.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreightTemplateGoodsExistsByIdRequest implements Serializable {

    private static final long serialVersionUID = 9109386834043212524L;

    /**
     * 单品运费模板id
     */
    @ApiModelProperty(value = "单品运费模板id")
    @NotNull
    private Long freightTempId;

}
