package com.wanmi.sbc.goods.api.response.cate;

import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.goodscate.GoodsCateByIdsResponse
 * 根据分类编号批量查询商品分类信息响应对象
 * @author lipeng
 * @dateTime 2018/11/1 下午4:44
 */
@ApiModel
@Data
public class GoodsCateByIdsResponse implements Serializable {

    private static final long serialVersionUID = -6218405550154118844L;

    @ApiModelProperty(value = "商品分类")
    private List<GoodsCateVO> goodsCateVOList;
}
