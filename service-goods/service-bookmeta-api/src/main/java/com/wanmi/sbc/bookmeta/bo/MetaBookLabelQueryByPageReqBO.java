package com.wanmi.sbc.bookmeta.bo;

import java.util.Date;
import java.io.Serializable;
import com.wanmi.sbc.common.base.Page;
import lombok.Data;

/**
 * 标签(MetaBookLabel)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookLabelQueryByPageReqBO implements Serializable {
    private static final long serialVersionUID = -76298405363197271L;
    
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
     * 书名
     */
    private String bookName;
    /**
     * 标签名
     */
    private String labelName;
    /**
     * 删除标识
     */
    private Integer delFlag;
    /**
     * 分页参数
     */
    private Page page = new Page(1, 10);
}

