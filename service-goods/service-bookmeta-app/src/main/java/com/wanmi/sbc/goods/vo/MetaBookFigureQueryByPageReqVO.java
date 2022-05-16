package com.wanmi.sbc.goods.vo;

import java.util.Date;
import java.io.Serializable;
import com.wanmi.sbc.common.base.Page;
import lombok.Data;

/**
 * 书籍人物(MetaBookFigure)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookFigureQueryByPageReqVO implements Serializable {
    private static final long serialVersionUID = -67640062232476451L;
    
    private Integer id;
    /**
     * 书籍id
     */
    private Integer bookId;
    /**
     * 作者id
     */
    private Integer figureId;
    /**
     * 类型：1作者；2译者；3绘画人；
     */
    private Integer figureType;
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
     * 分页参数
     */
    private Page page = new Page(1, 10);
}

