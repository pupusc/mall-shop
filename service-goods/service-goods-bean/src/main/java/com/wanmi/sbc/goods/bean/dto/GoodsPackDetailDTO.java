package com.wanmi.sbc.goods.bean.dto;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品打包
 */
@DynamicInsert
@Table(name = "goods_pack_detail")
@Entity
@Data
public class GoodsPackDetailDTO implements Serializable {
    /**
     * 主键
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * spu主键
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * sku主键
     */
    @Column(name = "goods_info_id")
    private String goodsInfoId;

    /**
     * 商品包主键
     */
    @Column(name = "pack_id")
    private String packId;

    /**
     * 商品数量
     */
    @Column(name = "count")
    private Integer count;

    /**
     * 分摊比例
     */
    @Column(name = "share_rate")
    private BigDecimal shareRate;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 删除标志
     */
    @Column(name = "del_flag")
    private Integer delFlag;
}
