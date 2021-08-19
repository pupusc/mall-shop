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
public class EsCateUpdateNameRequest implements Serializable {

    @ApiModelProperty(value = "商品类目")
    private List<GoodsCateVO> goodsCateListVOList;

}
