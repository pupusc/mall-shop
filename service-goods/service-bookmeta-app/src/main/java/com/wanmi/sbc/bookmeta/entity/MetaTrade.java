package com.wanmi.sbc.bookmeta.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/10:03
 * @Description:
 */
@Data
@Table(name = "meta_trade")
public class MetaTrade implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "name")
    private String name;
    @Column(name = "descr")
    private String descr;
    @Column(name = "order_num")
    private Integer orderNum;
    @Column(name = "path")
    private String  path;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "del_flag")
    private Integer delFlag;

}
