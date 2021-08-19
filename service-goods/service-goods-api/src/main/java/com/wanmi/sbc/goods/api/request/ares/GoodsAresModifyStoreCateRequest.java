package com.wanmi.sbc.goods.api.request.ares;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/11/5 10:52
 * @version: 1.0
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsAresModifyStoreCateRequest implements Serializable {

    private static final long serialVersionUID = 7768438718746962809L;

    @ApiModelProperty(value = "参数")
    @NotNull
    private Object[] objs;
}
