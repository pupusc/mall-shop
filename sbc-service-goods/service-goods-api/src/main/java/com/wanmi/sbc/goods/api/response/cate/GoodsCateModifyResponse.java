package com.wanmi.sbc.goods.api.response.cate;

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
 * com.wanmi.sbc.goods.api.response.goodscate.GoodsCateModifyResponse
 * 修改商品分类信息响应对象
 * @author lipeng
 * @dateTime 2018/11/1 下午4:54
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCateModifyResponse implements Serializable {

    private static final long serialVersionUID = -1322648910128916216L;

    @ApiModelProperty(value = "商品类目")
    private List<GoodsCateVO> goodsCateListVOList;
}
