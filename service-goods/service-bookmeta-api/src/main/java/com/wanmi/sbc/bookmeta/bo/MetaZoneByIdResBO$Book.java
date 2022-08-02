package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 图书分区关联图书(MetaZoneBook)实体类
 *
 * @author Liang Jun
 * @since 2022-07-27 10:45:37
 */
@Data
public class MetaZoneByIdResBO$Book implements Serializable {
    private static final long serialVersionUID = 250693242518938663L;
    /**
     * 关联图书
     */     
    private Integer id;
    /**
     * isbn
     */
    private String isbn;
    /**
     * 图书名称
     */
    private String name;
    /**
     * 作者名称
     */
    private String authorName;
}

