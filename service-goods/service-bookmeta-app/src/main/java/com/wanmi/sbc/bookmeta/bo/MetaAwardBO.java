package com.wanmi.sbc.bookmeta.bo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 奖项(MetaAward)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 13:54:01
 */
@Data
public class MetaAwardBO implements Serializable {
    private static final long serialVersionUID = 280630643071795080L;
         
    private Integer id;
    /**
     * 名称
     */     
    private String name;
    /**
     * 图片
     */     
    private String image;
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
     * 描述
     */     
    private String descr;
}

