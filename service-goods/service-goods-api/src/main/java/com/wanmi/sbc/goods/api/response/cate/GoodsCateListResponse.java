package com.wanmi.sbc.goods.api.response.cate;

import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.goodscate.GoodsCateListResponse
 *
 * @author lipeng
 * @dateTime 2018/11/2 上午9:36
 */
@ApiModel
@Data
public class GoodsCateListResponse implements Serializable {

    private static final long serialVersionUID = -8172004765448343736L;

    @ApiModelProperty(value = "商品类目")
    private List<GoodsCateVO> goodsCateVOList;
}
