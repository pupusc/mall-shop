package com.wanmi.sbc.goods.vo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 标签(MetaBookLabel)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookLabelQueryByPageResVO implements Serializable {
    private static final long serialVersionUID = -57628148955616010L;
    
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

