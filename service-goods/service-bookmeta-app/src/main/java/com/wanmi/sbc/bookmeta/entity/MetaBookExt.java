package com.wanmi.sbc.bookmeta.entity;

import lombok.Data;

/**
 * 书籍(MetaBook)实体类
 * @author Liang Jun
 * @since 2022-05-26 01:25:34
 */
@Data
public class MetaBookExt extends MetaBook {
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
//    private String isbn;
}

