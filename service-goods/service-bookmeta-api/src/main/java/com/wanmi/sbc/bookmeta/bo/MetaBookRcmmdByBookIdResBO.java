package com.wanmi.sbc.bookmeta.bo;

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
public class MetaBookRcmmdByBookIdResBO implements Serializable {
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
     * 适读对象id
     */
    //private List<Integer> fitTargetIds = new ArrayList<>();

    private List<FitTarget> fitTargets = new ArrayList<>();
    /**
     * 推荐列表
     */
    private List<MetaBookRcmmdBO> bookRcmmds = new ArrayList<>();

    @Data
    public static class FitTarget {
        private Integer id;
        private String name;
    }

    @Data
    public static class MetaBookRcmmdBO {
        /**
         * 主键id
         */
        private Integer id;
        /**
         * 名称
         */
        private String name;
        /**
         * 书籍id
         */
        private Integer bookId;
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

        /**
         * 是否选中
         */
        private Integer isSelected;
    }
}

