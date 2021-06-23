package com.wanmi.sbc.goods.brand.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 待审核品牌
 * Created by sunkun on 2017/11/1.
 */
@Data
@Entity
@Table(name = "check_brand")
public class CheckBrand implements Serializable {

    private static final long serialVersionUID = -6801651486403631164L;

    /**
     * 待审核品牌分类
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_brand_id")
    private Long checkBrandId;

    /**
     * 品牌名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 品牌昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 品牌logo
     */
    @Column(name = "logo")
    private String logo;

    /**
     * 店铺主键
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 审核状态(0:未审核,1:通过,2:驳回)
     */
    @Column(name = "status")
    private Integer status = 0;

}
