package com.wanmi.sbc.bookmeta.vo;

import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 出版社(MetaPublisher)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaPublisherQueryByIdResVO implements Serializable {
    private static final long serialVersionUID = 266338748892586288L;
    
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
    /**
     * 图片列表
     */
    private List<String> imageList = new ArrayList<>();
}

