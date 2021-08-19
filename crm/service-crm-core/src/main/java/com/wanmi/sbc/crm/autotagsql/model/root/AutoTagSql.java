package com.wanmi.sbc.crm.autotagsql.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.crm.bean.enums.TagType;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @program: sbc-micro-service-A
 * @description: 自动标签SQL
 * @create: 2020-08-29 11:21
 **/
@Data
@Entity
@ToString
@Table(name = "auto_tag_sql")
public class AutoTagSql {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 自动标签表ID
     */
    @Column(name = "auto_tag_id")
    private Long autoTagId;

    /**
     * 标签类型，0：偏好标签组，1：指标值标签，2：指标值范围标签，3、综合类标签
     */
    @Column(name = "type")
    private TagType type;

    /**
     * SQL语句
     */
    @Column(name = "sql_str")
    private String sqlStr;

    /**
     * SQL语句
     */
    @Column(name = "big_data_sql_str")
    private String bigDataSqlStr;

    /**
     * 删除标识
     */
    @Column(name = "del_flag")
    private DeleteFlag delFlag;

    /**
     * 创建人
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @CreatedDate
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @Column(name = "update_person")
    private String updatePerson;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @LastModifiedDate
    private LocalDateTime updateTime;
}