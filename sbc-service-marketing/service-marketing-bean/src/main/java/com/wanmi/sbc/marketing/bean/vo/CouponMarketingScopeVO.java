package com.wanmi.sbc.marketing.bean.vo;

import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: lq
 * @CreateTime:2018-09-12 09:34
 * @Description:优惠券商品作用范围
 */
@ApiModel
@Data
public class CouponMarketingScopeVO implements Serializable {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "优惠券商品作用范围id")
    private String marketingScopeId;

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    private String couponId;


    /**
     * 营销范围类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    @ApiModelProperty(value = "优惠券营销范围")
    private ScopeType scopeType;

    /**
     * 分类层级
     */
    @ApiModelProperty(value = "分类层级")
    private Integer cateGrade;

    /**
     * 营销id,可以为0(全部)，brand_id(品牌id)，cate_id(分类id), goods_info_id(货品id)
     */
    @ApiModelProperty(value = "优惠券关联的商品范围id(可以为0(全部)，brand_id(品牌id)，cate_id(分类id), goods_info_id(货品id))")
    private String scopeId;


}
