package com.fangdeng.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsStockSync {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_stock_sync.id
     *
     * @mbggenerated Fri Sep 17 14:44:01 CST 2021
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_stock_sync.goods_no
     *
     * @mbggenerated Fri Sep 17 14:44:01 CST 2021
     */
    private String goodsNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_stock_sync.stock_change_time
     *
     * @mbggenerated Fri Sep 17 14:44:01 CST 2021
     */
    private LocalDateTime stockChangeTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_stock_sync.stock
     *
     * @mbggenerated Fri Sep 17 14:44:01 CST 2021
     */
    private Integer stock;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_stock_sync.create_time
     *
     * @mbggenerated Fri Sep 17 14:44:01 CST 2021
     */
    private LocalDateTime createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_stock_sync.update_time
     *
     * @mbggenerated Fri Sep 17 14:44:01 CST 2021
     */
    private LocalDateTime updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_stock_sync.status
     *
     * @mbggenerated Fri Sep 17 14:44:01 CST 2021
     */
    private Integer status;

}