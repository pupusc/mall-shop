package com.wanmi.sbc.goods.api.request.blacklist.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GoodsBlackListData {

    private Integer id;

    /**
     * 业务名字
     */
    private String businessName;

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 业务分类 1 新品榜 2 畅销排行榜 3 特价书榜 4、不显示会员价的商品 5、库存编码 6、积分黑名单
     */
    private Integer businessCategory;

    /**
     * 业务类型 1、商品skuId 2、商品spuId 3、一级分类id 4、二级分类id、5、无效
     */
    private Integer businessType;

    /**
     * 编号
     */
    private String itemCode;

    /**
     * 名称
     */
    private String itemName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 0-启用 1-停用
     */
    private Integer delFlag;
}
