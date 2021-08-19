package com.wanmi.sbc.goods.api.response.cate;

import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.response.goodscate.GoodsCateAddResponse
 * 新增商品分类信息响应对象
 * @author lipeng
 * @dateTime 2018/11/1 下午4:54
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCateAddResponse implements Serializable {

    private static final long serialVersionUID = -1322648910128916216L;

    @ApiModelProperty(value = "签约分类")
    private GoodsCateVO goodsCate;
}
