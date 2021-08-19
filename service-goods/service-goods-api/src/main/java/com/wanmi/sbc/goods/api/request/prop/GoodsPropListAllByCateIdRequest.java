package com.wanmi.sbc.goods.api.request.prop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/10/31 14:43
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsPropListAllByCateIdRequest implements Serializable {

    private static final long serialVersionUID = -3910225480627878963L;

    /**
     * 类目ID
     */
    @ApiModelProperty(value = "类目ID")
    @NotNull
    private Long cateId;
}
