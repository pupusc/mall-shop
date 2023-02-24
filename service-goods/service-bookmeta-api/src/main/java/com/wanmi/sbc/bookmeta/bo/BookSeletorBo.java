package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
@Data
public class BookSeletorBo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 名称
     */
    private String name;

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
    /**
     * 书id
     */
    private Integer bookId;

    /**
     *
     */
    private Integer pageIndex=0;

    /**
     *
     */
    private Integer pageSize=10;


    private String rank;
}
