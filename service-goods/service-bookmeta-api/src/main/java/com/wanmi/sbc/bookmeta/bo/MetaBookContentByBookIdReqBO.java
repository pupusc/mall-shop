package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 书籍内容描述(MetaBookContent)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookContentByBookIdReqBO implements Serializable {
    private Integer id;
    /**
     * 书籍id
     */
    private Integer bookId;
    /**
     * 1简介；2目录；3摘录；4序言；5前言；
     */
    private Integer type;
    /**
     * 关联的人物
     */
    private Integer figureId;
    /**
     * 内容
     */
    private String content;
}

