package com.wanmi.sbc.goods.vo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 数据字典(MetaDataDict)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaDataDictQueryByIdResVO implements Serializable {
    private static final long serialVersionUID = 855156963880640317L;
    
    private Integer id;
    /**
     * 分组（book_cate、book_language、figure_cate、book_group_cate、book_bind，book_paper）
     */
    private String key;
    
    private String value;
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
     * 名称（书籍分类/书籍语言/人物分类/书组分类）
     */
    private String descr;
}

