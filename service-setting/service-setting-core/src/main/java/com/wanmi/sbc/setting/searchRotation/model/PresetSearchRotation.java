package com.wanmi.sbc.setting.searchRotation.model;

import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
//@Table(name="preset_search_rotation")
//@Entity
public class PresetSearchRotation implements Serializable {


    private static final long serialVersionUID = 4011602773649061701L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private int type = 1;

    @Column(name = "name")
    private String name;

    @Column(name = "page_url")
    private String page_url;

    @Column(name = "spu_id")
    private String spu_id;

    @Column(name = "sku_id")
    private String sku_id;

    @Column(name = "goods_name")
    private String goods_name;

    @Column(name = "spu_no")
    private String spu_no;

    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "begin_time")
    private LocalDateTime beginTime;

    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "end_time")
    private LocalDateTime end_time;

    @Column(name = "order_num")
    private int order_num;

}
