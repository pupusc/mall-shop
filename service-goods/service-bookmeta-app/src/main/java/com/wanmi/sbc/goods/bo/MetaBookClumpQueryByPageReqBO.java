package com.wanmi.sbc.goods.bo;

import java.util.Date;
import java.io.Serializable;
import com.wanmi.sbc.common.base.Page;
import lombok.Data;

/**
 * 丛书(MetaBookClump)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookClumpQueryByPageReqBO implements Serializable {
    private static final long serialVersionUID = -13165759169038849L;
    
    private Integer id;
    /**
     * 出版社id
     */
    private Integer publisherId;
    /**
     * 名称
     */
    private String name;
    /**
     * 图片
     */
    private String image;
    /**
     * 册数
     */
    private Integer volumeCount;
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
     * 分页参数
     */
    private Page page = new Page(1, 10);
}

