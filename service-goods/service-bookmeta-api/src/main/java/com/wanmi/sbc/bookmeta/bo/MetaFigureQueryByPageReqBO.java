package com.wanmi.sbc.bookmeta.bo;

import com.wanmi.sbc.common.base.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 人物(MetaFigure)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaFigureQueryByPageReqBO implements Serializable {
    private static final long serialVersionUID = 417025162416253059L;
    
    private Integer id;
    /**
     * 类型：1作者/译者/绘画人/作序人；2编辑；3名家；4专业机构；5媒体；
     */
    private Integer type;

    /**
     * 类型查询
     */
    private List<Integer> typeIn;

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
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除标识
     */
    private Integer delFlag;
    /**
     * 分页参数
     */
    private Page page = new Page(1, 10);
}

