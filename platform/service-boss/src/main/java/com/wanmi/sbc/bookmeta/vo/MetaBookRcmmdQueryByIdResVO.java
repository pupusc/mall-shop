package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 书籍推荐(MetaBookRcmmd)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookRcmmdQueryByIdResVO implements Serializable {
    private static final long serialVersionUID = -44306547516009484L;
    
    private Integer id;
    /**
     * 业务id
     */
    private Integer bizId;
    /**
     * 业务type：1获奖推荐；2编辑推荐；3媒体推荐；4专业机构推荐；5名家推荐；6书中引用推荐；7讲稿引用推荐；
     */
    private Integer bizType;
    /**
     * 业务时间
     */
    private Date bizTime;
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
     * 描述信息
     */
    private String descr;
}

