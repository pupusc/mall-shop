package com.wanmi.sbc.bookmeta.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 人物(MetaFigure)实体类
 *
 * @author Liang Jun
 * @since 2022-06-01 15:02:59
 */
@Table(name = "meta_figure")
public class MetaFigure implements Serializable {
    private static final long serialVersionUID = -83395116140008348L;

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    /**
     * 类型：1作者/译者/绘画人/作序人；2编辑；3名家；4专业机构；5媒体；
     */     
    @Column(name = "type")
    private Integer type;
    /**
     * 名称
     */     
    @Column(name = "name")
    private String name;
    /**
     * 外文名
     */     
    @Column(name = "english_name")
    private String englishName;
    /**
     * 曾用名
     */     
    @Column(name = "former_name")
    private String formerName;
    /**
     * 图片
     */     
    @Column(name = "image")
    private String image;
    /**
     * 所属国家
     */     
    @Column(name = "country")
    private String country;
    /**
     * 省份编码
     */     
    @Column(name = "province_code")
    private String provinceCode;
    /**
     * 城市编码
     */     
    @Column(name = "city_code")
    private String cityCode;
    /**
     * 区县编码
     */     
    @Column(name = "district_code")
    private String districtCode;
    /**
     * 毕业学校
     */     
    @Column(name = "graduate_school")
    private String graduateSchool;
    /**
     * 工作岗位
     */     
    @Column(name = "job_post")
    private String jobPost;
    /**
     * 职称头衔
     */     
    @Column(name = "job_title")
    private String jobTitle;
    /**
     * 学位文凭
     */     
    @Column(name = "diploma")
    private String diploma;
    /**
     * 研究领域
     */     
    @Column(name = "research_field")
    private String researchField;
    /**
     * 介绍
     */     
    @Column(name = "introduce")
    private String introduce;
    /**
     * 出生/成立日期
     */     
    @Column(name = "birth_time")
    private Date birthTime;
    /**
     * 创建时间
     */     
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 更新时间
     */     
    @Column(name = "update_time")
    private Date updateTime;
    /**
     * 删除标识
     */     
    @Column(name = "del_flag")
    private Integer delFlag;
    /**
     * 所属朝代
     */     
    @Column(name = "dynasty_id")
    private Integer dynastyId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getFormerName() {
        return formerName;
    }

    public void setFormerName(String formerName) {
        this.formerName = formerName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    public String getJobPost() {
        return jobPost;
    }

    public void setJobPost(String jobPost) {
        this.jobPost = jobPost;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDiploma() {
        return diploma;
    }

    public void setDiploma(String diploma) {
        this.diploma = diploma;
    }

    public String getResearchField() {
        return researchField;
    }

    public void setResearchField(String researchField) {
        this.researchField = researchField;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Date getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(Date birthTime) {
        this.birthTime = birthTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getDynastyId() {
        return dynastyId;
    }

    public void setDynastyId(Integer dynastyId) {
        this.dynastyId = dynastyId;
    }

}

