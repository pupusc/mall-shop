package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

/**
 * @author Liang Jun
 * @date 2022-07-27 18:29:00
 */
@Data
public class MetaZoneQueryByIdResVO$Book {
    /**
     * 图书id
     */
    private Integer id;
    /**
     * 书籍名称
     */
    private String name;
    /**
     * 书籍isbn
     */
    private String isbn;
    /**
     * 作者名称
     */
    private String authorName;
}
