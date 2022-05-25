package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 书籍内容描述(MetaBookContent)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookContentByBookIdVO implements Serializable {
    private static final long serialVersionUID = 636367289017261664L;
    /**
     * 书籍id
     */
    @NotNull
    private Integer bookId;
    /**
     * 简介
     */
    private MetaBookContentVO introduce;
    /**
     * 目录
     */
    private MetaBookContentVO catalogue;
    /**
     * 摘录
     */
    private List<MetaBookContentVO> extractList = new ArrayList<>();
    /**
     * 序言
     */
    private List<MetaBookContentVO> preludeList = new ArrayList<>();
    /**
     * 前言
     */
    private MetaBookContentVO preface;

    @Data
    public static class MetaBookContentVO {
        private Integer id;
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
}

