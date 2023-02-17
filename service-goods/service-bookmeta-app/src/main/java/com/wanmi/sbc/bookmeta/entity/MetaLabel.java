package com.wanmi.sbc.bookmeta.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 标签(MetaLabel)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 01:28:20
 */
@Table(name = "meta_label")
@Data
public class MetaLabel implements Serializable {
    private static final long serialVersionUID = 262954670573753788L;
         
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    /**
     * 名称
     */     
    @Column(name = "name")
    private String name;
    /**
     * 状态：1启用；2停用；
     */     
    @Column(name = "status")
    private Integer status;
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
    /**
     * 说明
     */     
    @Column(name = "descr")
    private String descr;
    /**
     * 顺序
     */     
    @Column(name = "seq")
    private Integer seq;
    /**
     * 父级id
     */     
    @Column(name = "parent_id")
    private Integer parentId;
    /**
     * 类型：1目录；2标签；
     */     
    @Column(name = "type")
    private Integer type;
    /**
     * 场景：1适读对象；
     */     
    @Column(name = "scene")
    private Integer scene;
    /**
     * 路径：下划线分隔
     */     
    @Column(name = "path")
    private String path;

    @Column(name = "is_static")
    private Integer isStatic;

    @Column(name = "is_run")
    private Integer isRun;

    @Column(name = "run_from_time")
    private Date runFromTime;

    @Column(name = "run_to_time")
    private Date runToTime;

    @Column(name = "show_status")
    private Integer showStatus;

    @Column(name = "show_img")
    private String showImg;

    @Column(name = "show_text")
    private String showText;

    @Column(name = "remark")
    private String remark;

    @Column(name = "is_show")
    private Integer isShow;

}

