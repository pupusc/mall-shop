package com.wanmi.sbc.goods.api.response.cate;

import com.wanmi.sbc.goods.bean.vo.GoodsCateSimpleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
public class GoodsCateSImpleListResponse implements Serializable {

    private static final long serialVersionUID = -8172004765448343736L;

    @ApiModelProperty(value = "商品类目")
    private List<GoodsCateSimpleVO> goodsCateVOList;



}
