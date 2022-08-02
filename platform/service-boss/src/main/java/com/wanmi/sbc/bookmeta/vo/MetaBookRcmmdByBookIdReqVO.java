package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 书籍推荐(MetaBookRcmmd)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookRcmmdByBookIdReqVO implements Serializable {
    private static final long serialVersionUID = -44306547516009484L;
    /**
     * 书籍id
     */
    @NotNull
    private Integer bookId;
    /**
     * 最小阅读年龄
     */
    private Integer fitAgeMin;
    /**
     * 最大阅读年龄
     */
    private Integer fitAgeMax;
    /**
     * 适读对象
     */
    //private List<Integer> fitTargetIdList = new ArrayList<>();
    private List<FitTarget> fitTargets = new ArrayList<>();
    /**
     * 书中提到人物
     */
    private List<MetaBookRcmmdVO> mentionList = new ArrayList<>();
    /**
     * 奖项推荐
     */
    private List<MetaBookRcmmdVO> awardList = new ArrayList<>();
    /**
     * 编辑推荐
     */
    private List<MetaBookRcmmdVO> editorList = new ArrayList<>();
    /**
     * 媒体推荐
     */
    private List<MetaBookRcmmdVO> mediaList = new ArrayList<>();
    /**
     * 机构推荐
     */
    private List<MetaBookRcmmdVO> organList = new ArrayList<>();
    /**
     * 名家推荐
     */
    private List<MetaBookRcmmdVO> expertList = new ArrayList<>();
    /**
     * 引用推荐
     */
    private List<MetaBookRcmmdVO> quoteList = new ArrayList<>();
    /**
     * 讲稿推荐
     */
    private List<MetaBookRcmmdVO> draftList = new ArrayList<>();

    @Data
    public static class  MetaBookRcmmdVO {
        /**
         * 主键id
         */
        private Integer id;
        /**
         * 名称
         */
        private String name;
        /**
         * 业务id
         */
        private Integer bizId;
        /**
         * 业务type：1获奖推荐；2编辑推荐；3媒体推荐；4专业机构推荐；5名家推荐；6书中引用推荐；7讲稿引用推荐；
         */
        private Integer bizType;
        /**
         * 业务时间
         */
        private Date bizTime;
        /**
         * 描述信息：推荐语、获奖理由
         */
        private String descr;
    }
    @Data
    public static class FitTarget {
        private Integer id;
        private String name;
    }
}

