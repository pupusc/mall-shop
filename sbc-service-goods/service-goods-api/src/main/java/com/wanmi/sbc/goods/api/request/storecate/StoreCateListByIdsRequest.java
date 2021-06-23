package com.wanmi.sbc.goods.api.request.storecate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 根据分类id批量查询请求结构
 * @author: wanggang
 * @createDate: 2018/11/1 10:00
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCateListByIdsRequest implements Serializable {

    private static final long serialVersionUID = -990668819846698015L;

    /**
     * 批量分类id
     */
    @ApiModelProperty(value = "批量分类id")
    @NotNull
    private List<Long> cateIds;
}
