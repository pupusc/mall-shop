package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/15/16:32
 * @Description:
 */
@Data
public class MetaBookLabelReqBO implements Serializable {
    private static final long serialVersionUID = -77404488365708337L;

    private Integer id;
    /**
     * 书籍id
     */
    private Integer bookId;
    private String bookName;
    /**
     * 目录id
     */
    private Integer labelId;
    private String labelName;
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
