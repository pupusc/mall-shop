package com.wanmi.sbc.setting.defaultsearchterms.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "default_search_terms")
public class DefaultSearchTerms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "default_search_keyword")
    private String defaultSearchKeyword;
    /**
     * 搜索词类型 0-H5  1-小程序
     */
    @Column(name = "default_channel")
    private Integer defaultChannel ;

    /**
     * 落地页
     */
    @Column(name = "related_landing_page")
    private String relatedLandingPage;


    /**
     * 父类id
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 是否为父节点 (0-否，1-是)
     */
    @Column(name = "is_parent")
    private Boolean isParent;

    /**
     * 前图片url
     */
    @Column(name = "img_before")
    private String imgBefore;

    /**
     * 后图片url
     */
    @Column(name = "img_after")
    private String imgAfter;

    @Column(name = "sort_number")
    private Long sortNumber;

    /**
     * 是否删除 0 否  1 是
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 创建人
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @Column(name = "update_person")
    private String updatePerson;

}
