package com.wanmi.sbc.bookmeta.vo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 书籍扩展属性(MetaBookExtend)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookExtendQueryByPageResVO implements Serializable {
    private static final long serialVersionUID = -46504468496198956L;
    
    private Integer id;
    /**
     * 书籍id
     */
    private Integer bookId;
    /**
     * 属性名称
     */
    private String propName;
    /**
     * 属性值
     */
    private String propValue;
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

