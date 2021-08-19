package com.wanmi.sbc.setting.api.request.searchterms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: 魏文号
 * @Description:
 * @Date: 2020-04-16
 */
@ApiModel
@Data
public class AssociationLongTailWordDeleteByIdRequest implements Serializable {

    private static final long serialVersionUID = -498790238259861362L;

    /**
     * 联想词ID
     */
    @ApiModelProperty(value = "联想词ID")
    @NotNull
    private Long associationLongTailWordId;

}
