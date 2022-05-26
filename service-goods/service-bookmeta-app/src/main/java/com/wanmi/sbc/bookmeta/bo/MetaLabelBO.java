package com.wanmi.sbc.bookmeta.bo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 标签(MetaLabel)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 13:54:10
 */
@Data
public class MetaLabelBO implements Serializable {
    private static final long serialVersionUID = -96793554573604763L;
         
    private Integer id;
    /**
     * 名称
     */     
    private String name;
    /**
     * 状态：1启用；2停用；
     */     
    private Integer status;
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
     * 说明
     */     
    private String descr;
    /**
     * 顺序
     */     
    private Integer seq;
    /**
     * 父级id
     */     
    private Integer parentId;
    /**
     * 类型：1目录；2标签；
     */     
    private Integer type;
    /**
     * 场景：1适读对象；
     */     
    private Integer scene;
    /**
     * 路径：下划线分隔
     */     
    private String path;
}

