package com.wanmi.sbc.goods.api.request.cate;

import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.request.goodscate.GoodsCateAddRequest
 * 添加商品分类请求对象
 * @author lipeng
 * @dateTime 2018/11/1 下午4:50
 */
@ApiModel
@Data
public class GoodsCateAddRequest implements Serializable {

    private static final long serialVersionUID = 5238680462886849978L;

    @ApiModelProperty(value = "商品分类")
    private GoodsCateVO goodsCate;
}
