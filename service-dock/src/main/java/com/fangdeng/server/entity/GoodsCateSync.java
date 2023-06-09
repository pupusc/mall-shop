package com.fangdeng.server.entity;

import lombok.Data;

import java.util.Date;

@Data
public class GoodsCateSync {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_cate_sync.id
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_cate_sync.parent_id
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    private Integer parentId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_cate_sync.name
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_cate_sync.deleted
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    private Byte deleted;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_cate_sync.create_time
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_cate_sync.update_time
     *
     * @mbggenerated Mon Oct 18 09:23:12 CST 2021
     */
    private Date updateTime;

    private String labelIds;
}