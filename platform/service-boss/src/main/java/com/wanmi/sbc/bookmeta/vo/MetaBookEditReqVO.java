package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 书籍(MetaBook)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookEditReqVO implements Serializable {
    @NotNull
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
    private List<Integer> labelIdList = new ArrayList<>();
    /**
     * 作者id列表
     */
    private List<Integer> authorIdList = new ArrayList<>();
    /**
     * 译者id列表
     */
    private List<Integer> translatorIdList = new ArrayList<>();
    /**
     * 绘者id列表
     */
    private List<Integer> painterIdList = new ArrayList<>();
}

