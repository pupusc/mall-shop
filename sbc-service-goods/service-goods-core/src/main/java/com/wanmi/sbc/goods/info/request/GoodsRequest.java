package com.wanmi.sbc.goods.info.request;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.BoolFlag;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by keith on 17/5/6.
 * spu删除,上下架请求对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsRequest extends BaseRequest {

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
}
