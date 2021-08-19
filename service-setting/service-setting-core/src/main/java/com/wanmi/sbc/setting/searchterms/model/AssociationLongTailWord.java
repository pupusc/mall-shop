package com.wanmi.sbc.setting.searchterms.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>联想词结果</p>
 * @author weiwenhao
 * @date 2020-04-16
 */
@Data
@Entity
@Table(name = "association_long_tail_word")
public class AssociationLongTailWord implements Serializable {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "association_long_tail_word_id")
    private Long associationLongTailWordId;

    /**
     * 联想词
     */
    @Column(name = "associational_word")
    private String associationalWord;

    /**
     * 长尾词
     */
    @Column(name = "long_tail_word")
    private String longTailWord;

    /**
     * 关联搜索词id
     */
    @Column(name = "search_associational_word_Id")
    private Long searchAssociationalWordId;

    /**
     * 排序号
     */
    @Column(name = "sort_number")
    private Long sortNumber;

    /**
     * 是否删除 0 否  1 是
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;



    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @Column(name = "create_person")
    private String createPerson;

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


    /**
     * 删除时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @Column(name = "delete_person")
    private String deletePerson;

}
