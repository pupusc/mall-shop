package com.wanmi.sbc.goods.bean.vo;

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
public class CouponInfoForScopeNamesVO implements Serializable {

    private static final long serialVersionUID = 8270069437627689630L;

    /**
     * 优惠券主键Id
     */
    @ApiModelProperty(value = "优惠券主键Id")
    private String couponId;

    /**
     * 指定商品-只有scopeName
     */
    @ApiModelProperty(value = "关联的商品范围名称集合，如分类名、品牌名")
    private  List<String> scopeNames = new ArrayList<>();



}
