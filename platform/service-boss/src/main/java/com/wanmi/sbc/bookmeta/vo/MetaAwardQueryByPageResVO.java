package com.wanmi.sbc.bookmeta.vo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 奖项(MetaAward)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaAwardQueryByPageResVO implements Serializable {
    private static final long serialVersionUID = -64507727871389449L;
    
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

