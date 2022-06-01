package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 人物(MetaFigure)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaFigureAddReqBO implements Serializable {
    private static final long serialVersionUID = -39988259016624691L;
    
    private Integer id;
    /**
     * 类型：1作者/译者/绘画人/作序人；2编辑；3名家；4专业机构；5媒体；
     */
    private Integer type;
    /**
     * 名称
     */
    private String name;
    /**
     * 外文名
     */
    private String englishName;
    /**
     * 曾用名
     */
    private String formerName;
    /**
     * 图片
     */
    private String image;
    /**
     * 国籍
     */
    private String country;
    /**
     * 朝代
     */
    private Integer dynastyId;
    /**
     * 省份编码
     */
    private String provinceCode;
    /**
     * 城市编码
     */
    private String cityCode;
    /**
     * 区县编码
     */
    private String districtCode;
    /**
     * 毕业学校
     */
    private String graduateSchool;
    /**
     * 工作岗位
     */
    private String jobPost;
    /**
     * 职称头衔
     */
    private String jobTitle;
    /**
     * 学位文凭
     */
    private String diploma;
    /**
     * 研究领域
     */
    private String researchField;
    /**
     * 介绍
     */
    private String introduce;
    /**
     * 出生/成立日期
     */
    private Date birthTime;
    /**
     * 图片列表
     */
    private List<String> imageList = new ArrayList<>();
    /**
     * 奖项列表
     */
    private List<FigureAward> awardList = new ArrayList<>();

    @Data
    public static class FigureAward {
        /**
         * 奖项id
         */
        private Integer awardId;
        /**
         * 获奖时间
         */
        private Date awardTime;
    }
}

