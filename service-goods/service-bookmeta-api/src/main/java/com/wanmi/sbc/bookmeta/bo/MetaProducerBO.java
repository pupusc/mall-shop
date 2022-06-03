package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 出品方(MetaProducer)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 13:54:10
 */
@Data
public class MetaProducerBO implements Serializable {
    private static final long serialVersionUID = -79745002617298425L;

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

