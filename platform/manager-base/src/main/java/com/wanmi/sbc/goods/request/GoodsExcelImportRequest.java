package com.wanmi.sbc.goods.request;

import com.wanmi.sbc.common.enums.BoolFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.goodsexcel.GoodsExcelImportGoodsRequest
 * 导入商品信息请求对象
 * @author lipeng
 * @dateTime 2018/11/2 上午9:54
 */
@ApiModel
@Data
public class GoodsExcelImportRequest implements Serializable {

    private static final long serialVersionUID = 1920097575433169925L;

    @ApiModelProperty(value = "用户Id")
    private String userId;

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
    @ApiModelProperty(value = "商家类型")
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

    /**
     * 文件后缀
     */
    @ApiModelProperty(value = "文件后缀")
    private String ext;
}
