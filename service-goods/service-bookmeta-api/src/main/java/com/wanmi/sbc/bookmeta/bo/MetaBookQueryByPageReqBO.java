package com.wanmi.sbc.bookmeta.bo;

import com.wanmi.sbc.common.base.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * 书籍(MetaBook)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookQueryByPageReqBO implements Serializable {
    private static final long serialVersionUID = 441829777766613142L;
    
    private String isbn;
    /**
     * 名称
     */
    private String nameLike;
    /**
     * 作者
     */
    private String authorLike;
    /**
     * 出版社
     */
    private String publisherLike;

    private Boolean fillFigureName = false;

    private Boolean fillPublisherName = false;
    /**
     * 分页参数
     */
    private Page page = new Page(1, 10);
}

