package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import java.io.Serializable;
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
    private Integer tradeId;
    private String tradeName;
    private Double price;
    /**
     * 作者列表
     */
    private List<Figure> authorList;
    /**
     * 译者列表
     */
    private List<Figure> translatorList;
    /**
     * 绘者列表
     */
    private List<Figure> painterList;

    /**
     * 标签分类列表
     */
    private List<LabelCate> labelCateList;

    @Data
    public static class Figure {
        private Integer id;
        private String name;
    }

    @Data
    public static class LabelCate {
        /**
         * 一级分类id
         */
        private Integer firstCateId;
        /**
         * 一级分类name
         */
        private String firstCateName;
        /**
         * 二级分类id
         */
        private Integer secondCateId;
        /**
         * 二级分类name
         */
        private String secondCateName;
        /**
         * 多选标签
         */
        private List<Label> labelList;
    }

    @Data
    public static class Label {
        /**
         * 主键
         */
        private Integer id;
        /**
         * 名称
         */
        private String name;
        /**
         * 路径
         */
        private String path;
    }
}

