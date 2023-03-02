package com.wanmi.sbc.bookmeta.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;

import java.io.Serializable;
import java.util.Date;

@Data
public class MetaBookRelationKey implements Serializable {

    private static final long serialVersionUID = -3440501815150962728L;
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     *
     */
    @Column(name = "relation_id")
    private Integer relationId;
    /**
     * 名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 排序
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;
    /**
     * 删除标识
     */
    @Column(name = "del_flag")
    private Integer delFlag;
}
