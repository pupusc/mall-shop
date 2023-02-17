package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 标签(MetaLabel)实体类
 *
 * @author Liang Jun
 * @since 2022-05-17 11:38:03
 */
@Data
public class MetaLabelQueryByIdResVO implements Serializable {
    private static final long serialVersionUID = 400285051254298424L;
    
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
     * 路径：下划线分隔（1_2）
     */
    private String path;
    /**
     * 路径：下划线拆开 [1, 2]
     */
    private List<String> pathList = new ArrayList<>();

    private Integer isStatic;

    private Integer isRun;

    private Date runFromTime;

    private Date runToTime;

    private Integer showStatus;

    private String showImg;

    private String showText;

    private String remark;

    private Integer isShow;
}

