package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 书籍(MetaBook)实体类
 *
 * @author Liang Jun
 * @since 2022-05-23 23:47:42
 */
@Data
public class MetaBookEditReqBO implements Serializable {

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
     * 标签
     */
    private List<Integer> labelIds = new ArrayList<>();
    /**
     * 人物列表
     */
    private List<MetaBookAddReqBO.Figure> figures = new ArrayList<>();

    @Data
    public static class Figure {
        /**
         * 人物id
         */
        private Integer figureId;
        /**
         * 类型：1作者；2译者；3绘画人；
         */
        private Integer figureType;
    }
}

