package com.wanmi.sbc.bookmeta.vo;

import java.util.Date;
import java.io.Serializable;
import com.wanmi.sbc.common.base.Page;
import lombok.Data;

/**
 * 数据字典(MetaDataDict)实体类
 *
 * @author Liang Jun
 * @since 2022-05-24 00:37:02
 */
@Data
public class MetaDataDictQueryByPageReqVO implements Serializable {
    private static final long serialVersionUID = 301606262147798056L;
    
    /**
     * 分组：
    book_cate=书籍分类；
    book_language=书籍语言；
    figure_cate=人物分类；
    book_group_cate=书组分类；
    book_bind=书籍装帧；
    book_paper=书籍纸张；
    figure_country=人物国籍；
    label_scene=标签属性；
     */
    private String cate;
    /**
     * 分页参数
     */
    private Page page = new Page(1, 10);
}

