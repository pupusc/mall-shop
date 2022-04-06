package com.wanmi.sbc.goods.api.response.goods;

import lombok.Data;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Liang Jun
 * @date 2022-04-06 21:32:00
 */
@Data
public class GoodsPackDetailResponse implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * spu主键
     */
    private String goodsId;

    /**
     * sku主键
     */
    private String goodsInfoId;

    /**
     * 商品包主键
     */
    private String packId;

    /**
     * 商品数量
     */
    private Integer count;

    /**
     * 分摊比例
     */
    private BigDecimal shareRate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标志
     */
    private Integer delFlag;
}
