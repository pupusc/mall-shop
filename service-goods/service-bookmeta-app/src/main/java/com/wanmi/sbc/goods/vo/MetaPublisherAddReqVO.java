package com.wanmi.sbc.goods.vo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 出版社(MetaPublisher)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaPublisherAddReqVO implements Serializable {
    private static final long serialVersionUID = -37930612708072436L;
    
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
     * 成立时间
     */
    private Date buildTime;
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

