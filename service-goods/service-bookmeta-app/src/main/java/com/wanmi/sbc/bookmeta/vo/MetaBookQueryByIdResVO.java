package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 书籍(MetaBook)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookQueryByIdResVO implements Serializable {
    private static final long serialVersionUID = -50994838027752127L;
    
    private Integer id;
    
    private String isbn;
    /**
     * 名称
     */
    private String name;
    /**
     * 副名称
     */
    private String subName;
    /**
     * 原作名称
     */
    private String originName;
    /**
     * 标签列表
     */
    private List<MetaBookLabelVO> labelList;
    /**
     * 作者id列表
     */
    private List<Integer> authorIdList;
    /**
     * 译者id列表
     */
    private List<Integer> translatorIdList;
    /**
     * 绘者id列表
     */
    private List<Integer> painterIdList;

    private static class MetaBookLabelVO {
        private Integer id;
        /**
         * 名称
         */
        private String name;
        /**
         * 说明
         */
        private String descr;
        /**
         * 顺序
         */
        private Integer seq;
        /**
         * 路径，下划线分隔
         */
        private String path;
    }
}

