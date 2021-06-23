package com.wanmi.sbc.goods.bean.dto;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.goods.bean.enums.CouponType;
import com.wanmi.sbc.goods.bean.enums.ScopeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: songhanlin
 * @Date: Created In 10:18 AM 2018/9/12
 * @Description: 优惠券信息
 */
@ApiModel
@Data
public class CouponInfoForScopeNamesDTO implements Serializable {

    private static final long serialVersionUID = 8270069437627689630L;

    /**
     * 优惠券主键Id
     */
    @ApiModelProperty(value = "优惠券主键Id")
    private String couponId;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 是否平台优惠券 1平台 0店铺
     */
    @ApiModelProperty(value = "是否平台优惠券")
    private DefaultFlag platformFlag;

    /**
     * 营销范围类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    @ApiModelProperty(value = "优惠券营销范围")
    private ScopeType scopeType;


    /**
     * 优惠券类型 0通用券 1店铺券 2运费券
     */
    @ApiModelProperty(value = "优惠券类型")
    private CouponType couponType;

    /**
     * 关联分类-只有分类名
     */
    @ApiModelProperty(value = "优惠券分类名集合")
    private List<String> cateNames = new ArrayList<>();

    /**
     * 指定商品-只有scopeName
     */
    @ApiModelProperty(value = "关联的商品范围名称集合，如分类名、品牌名")
    private  List<String> scopeNames = new ArrayList<>();

    /**
     * 促销范围Ids
     */
    @ApiModelProperty(value = "优惠券关联的商品范围id集合(可以为0(全部)，brand_id(品牌id)，cate_id(分类id), goods_info_id(货品id))")
    private List<String> scopeIds = new ArrayList<>();

    /**
     * 优惠券分类Ids
     */
    @ApiModelProperty(value = "优惠券分类Id集合")
    private List<String> cateIds = new ArrayList<>();


}
