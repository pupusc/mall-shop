package com.wanmi.sbc.goods.api.request.storecate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/11/1 10:00
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCateListByStoreCateIdAndIsHaveSelfRequest implements Serializable {

    private static final long serialVersionUID = -990668819846698015L;

    @ApiModelProperty(value = "店铺分类Id")
    @NotNull
    private Long storeCateId;

    @ApiModelProperty(value = "是否加入包含自身对象")
    @NotNull
    private boolean isHaveSelf;
}
