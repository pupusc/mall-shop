package com.wanmi.sbc.elastic.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class EsBrandDeleteByIdsRequest implements Serializable {

    /**
     * 需要删除的idList
     */
    @ApiModelProperty(value = "需要删除的idList")
    private List<Long> deleteIds;

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

}
