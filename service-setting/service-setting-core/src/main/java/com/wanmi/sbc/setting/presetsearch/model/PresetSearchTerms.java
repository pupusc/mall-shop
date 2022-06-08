package com.wanmi.sbc.setting.presetsearch.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name="preset_search_terms")
@Entity
public class PresetSearchTerms implements Serializable {


    private static final long serialVersionUID = 4011602773649061701L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 预置搜索词字
     */
    @Column(name = "preset_search_keyword")
    private String presetSearchKeyword;

    /**
     * 预置搜索词类型 0-H5  1-小程序
     */
    @Column(name = "preset_channel")
    private Integer presetChannel = 0;
}
