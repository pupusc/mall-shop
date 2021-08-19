package com.wanmi.sbc.goods.api.response.goods;

import com.wanmi.sbc.goods.bean.vo.GoodsImageVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSimpleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @discription 查询商品简易信息
 * @author yangzhen
 * @date 2020/9/3 11:31
 * @param
 * @return
 */
@ApiModel
@Data
public class GoodsDetailSimpleResponse implements Serializable {

    private static final long serialVersionUID = -6641896293423917872L;


    /**
     * @discription 商品简易信息
     * @author yangzhen
     * @date 2020/9/8 18:54
     * @param
     * @return
     */
    private GoodsVO goods;


}
