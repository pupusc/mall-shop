package com.wanmi.sbc.goods.api.response.standard;

import com.wanmi.sbc.common.enums.BoolFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.standard.StandardImportStandardRequest
 *
 * @author lipeng
 * @dateTime 2018/11/9 下午2:53
 */
@ApiModel
@Data
public class StandardImportStandardRequest implements Serializable {

    private static final long serialVersionUID = -7097674492738684146L;

    /**
     * 批量商品编号
     */
    @ApiModelProperty(value = "批量商品编号")
    private List<String> goodsIds;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsId;

    /**
     * 客户编号
     */
    @ApiModelProperty(value = "客户编号")
    private String customerId;

    /**
     * 公司信息Id
     */
    @ApiModelProperty(value = "公司信息Id")
    private Long companyInfoId;

    /**
     * 客户等级
     */
    @ApiModelProperty(value = "客户等级")
    private Long customerLevelId;

    /**
     * 客户等级折扣
     */
    @ApiModelProperty(value = "客户等级折扣")
    private BigDecimal customerLevelDiscount;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型",notes = "0:平台自营, 1: 第三方商家")
    private BoolFlag companyType;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String supplierName;

    /**
     * 商品的店铺分类
     */
    @ApiModelProperty(value = "商品的店铺分类")
    private List<Long> storeCateIds;

    /**
     * 运费模板ID
     */
    @ApiModelProperty(value = "运费模板ID")
    private Long freightTempId;
}
