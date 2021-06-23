package com.wanmi.sbc.goods.api.response.cate;

import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>根据主键查询签约分类响应类</p>
 * author: sunkun
 * Date: 2018-11-05
 */
@ApiModel
@Data
public class ContractCateByIdResponse implements Serializable {

    private static final long serialVersionUID = -2952290407262241668L;

    /**
     * 签约分类主键
     */
    @ApiModelProperty(value = "签约分类主键")
    private Long contractCateId;

    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    private Long storeId;

    /**
     * 商品分类
     */
    @ApiModelProperty(value = "商品分类")
    private GoodsCateVO goodsCate;

    /**
     * 分类扣率
     */
    @ApiModelProperty(value = "分类扣率")
    private BigDecimal cateRate;

    /**
     * 资质图片路径
     */
    @ApiModelProperty(value = "资质图片路径")
    private String qualificationPics;
}
