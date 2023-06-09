package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 书组(MetaBookGroup)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookGroupAddReqVO implements Serializable {
    private static final long serialVersionUID = -35191523830675555L;
    
    private Integer id;
    /**
     * 类型：1套系；2不同版本；
     */
    private Integer type;
    /**
     * 名称
     */
    @NotBlank
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
     * 介绍
     */
    private String descr;
    /**
     * 图片列表
     */
    private List<String> imageList = new ArrayList<>();
}

