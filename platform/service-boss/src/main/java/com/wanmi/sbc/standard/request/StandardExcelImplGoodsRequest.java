package com.wanmi.sbc.standard.request;

import com.wanmi.sbc.common.enums.BoolFlag;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-07
 */
@Data
public class StandardExcelImplGoodsRequest implements Serializable {

    private static final long serialVersionUID = -9015124894584238109L;

    @NotBlank
    private String ext;

    /**
     * 批量商品编号
     */
    private List<String> goodsIds;

    /**
     * 商品编号
     */
    private String goodsId;

    /**
     * 客户编号
     */
    private String customerId;

    /**
     * 公司信息Id
     */
    private Long companyInfoId;

    /**
     * 客户等级
     */
    private Long customerLevelId;

    /**
     * 客户等级折扣
     */
    private BigDecimal customerLevelDiscount;

    /**
     * 店铺Id
     */
    private Long storeId;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    private BoolFlag companyType;

    /**
     * 公司名称
     */
    private String supplierName;

    /**
     * 商品的店铺分类
     */
    private List<Long> storeCateIds;

    /**
     * 运费模板ID
     */
    private Long freightTempId;

    /**
     * 操作员id
     */
    private String userId;
}
