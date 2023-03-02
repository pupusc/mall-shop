package com.wanmi.sbc.bookmeta.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/10:03
 * @Description:
 */
@Data
@Table(name = "meta_trade")
public class MetaTrade {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "parent_id")
    private int parentId;
    @Column(name = "name")
    private String name;
    @Column(name = "descr")
    private String descr;
    @Column(name = "order_num")
    private int orderNum;
    @Column(name = "path")
    private String  path;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "del_flag")
    private int delFlag;

}
