package com.wanmi.sbc.elastic.api.request.goods;

import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * es删除分类平台request
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class EsCateDeleteRequest implements Serializable {

    /**
     * 需要删除的idList
     */
    @ApiModelProperty(value = "需要删除的idList")
    private List<Long> deleteIds;

    /**
     * 替换的分类
     */
    @ApiModelProperty(value = "替换的分类")
    private GoodsCateVO insteadCate;

    /**
     * 获取默认分类
     */
    @ApiModelProperty(value = "获取默认分类")
    private boolean isDefault;

}
