package com.wanmi.sbc.goods.api.request.enterprise.goods;

import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCustomerPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
public class EnterprisePriceSaveRequest {


    @NotBlank
    @ApiModelProperty(value = "企业购 商品ID")
    private String goodsInfoId;

    private Long storeId;

    /**
     * 企业购 设价类型,0:按市场价 1:按会员等级设价 2:按购买数量设价
     */
    @NotNull
    @ApiModelProperty(value = "企业购 设价类型")
    private Integer enterprisePriceType;

    /**
     * 企业购 以折扣设价 0:否 1:是
     */
    @ApiModelProperty(value = "企业购 以折扣设价")
    private Boolean enterpriseDiscountFlag;

    /**
     * 企业购 按客户单独定价,0:否 1:是
     */
    @ApiModelProperty(value = "企业购 按客户单独定价")
    private Boolean enterpriseCustomerFlag;

    @ApiModelProperty(name = "等级折扣信息")
    private List<StoreLevelVO> storeLevels;

    @ApiModelProperty("客户单独设价折扣信息")
    private List<GoodsCustomerPriceVO> goodsCustomerPrices;

    @ApiModelProperty("购买区间折扣信息")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices;
}

