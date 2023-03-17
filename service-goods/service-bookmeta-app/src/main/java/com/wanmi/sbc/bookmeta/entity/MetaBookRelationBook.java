package com.wanmi.sbc.bookmeta.entity;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/06/16:54
 * @Description:
 */
@Data
public class MetaBookRelationBook implements Serializable {

    @Column(name = "id")
    private int id;
    @Column(name = "relation_id")
    private int relationId;
    @Column(name = "isbn")
    private String isbn;
    @Column(name = "name")
    private String name;
    @Column(name = "order_num")
    private int orderNum;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "del_flag")
    private int delFlag;
}
