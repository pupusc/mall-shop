package com.wanmi.sbc.customer.api.response.store;

import com.wanmi.sbc.customer.bean.enums.StoreResponseState;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateSumVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺基本信息
 * (安全考虑只保留必要信息,隐藏前端会员无需知道的信息)
 * Created by bail on 2017/11/29.
 */
@ApiModel
@Data
public class BossStoreBaseInfoResponse {
    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    private Long storeId;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long companyInfoId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;

    /**
     * 店铺店招
     */
    @ApiModelProperty(value = "店铺店招")
    private String storeSign;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型",dataType = "com.wanmi.sbc.customer.bean.enums.CompanyType")
    private Integer companyType;

    /**
     * 联系人名字
     */
    @ApiModelProperty(value = "联系人名字")
    private String contactPerson;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contactMobile;

    /**
     * 联系邮箱
     */
    @ApiModelProperty(value = "联系邮箱")
    private String contactEmail;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private Long provinceId;

    /**
     * 地市
     */
    @ApiModelProperty(value = "地市")
    private Long cityId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 扁平化商品信息，一对多关系
     */
    @ApiModelProperty(value = "扁平化商品信息")
    private List<String> skuIds = new ArrayList<>();

    /**
     * 店铺响应状态
     */
    @ApiModelProperty(value = "店铺响应状态")
    private StoreResponseState storeResponseState;

    /**
     * 店铺是否被关注
     */
    @ApiModelProperty(value = "店铺是否被关注")
    private Boolean isFollowed = Boolean.FALSE;

    /**
     * 店铺评价信息
     */
    private StoreEvaluateSumVO storeEvaluateSumVO;

    /**
     * 关注总数
     */
    private Long followSum;

    /**
     * 商品总数
     */
    private Long goodsSum;

}
