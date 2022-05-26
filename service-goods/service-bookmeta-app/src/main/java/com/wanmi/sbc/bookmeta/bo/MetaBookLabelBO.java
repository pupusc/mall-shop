package com.wanmi.sbc.bookmeta.bo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 标签(MetaBookLabel)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 13:54:08
 */
@Data
public class MetaBookLabelBO implements Serializable {
    private static final long serialVersionUID = -77404488365708337L;
         
    private Integer id;
    /**
     * 书籍id
     */     
    private Integer bookId;
    /**
     * 目录id
     */     
    private Integer labelId;
    /**
     * 创建时间
     */     
    private Date createTime;
    /**
     * 更新时间
     */     
    private Date updateTime;
    /**
     * 删除标识
     */     
    private Integer delFlag;
}

