package com.wanmi.sbc.bookmeta.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 书籍推荐(MetaBookRcmmd)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 01:28:20
 */
@Table(name = "meta_book_rcmmd")
@Data
public class MetaBookRcmmd implements Serializable {
    private static final long serialVersionUID = 286270472355180346L;
         
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    /**
     * 业务id
     */     
    @Column(name = "biz_id")
    private Integer bizId;
    /**
     * 业务type：1获奖推荐；2编辑推荐；3媒体推荐；4专业机构推荐；5名家推荐；6书中引用推荐；7讲稿引用推荐；
     */     
    @Column(name = "biz_type")
    private Integer bizType;
    /**
     * 业务时间
     */     
    @Column(name = "biz_time")
    private Date bizTime;
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
     * 描述信息
     */     
    @Column(name = "descr")
    private String descr;
    /**
     * 书籍id
     */     
    @Column(name = "book_id")
    private Integer bookId;

    /**
     * 是否选中
     */
    @Column(name = "is_selected")
    private Integer isSelected;
}

