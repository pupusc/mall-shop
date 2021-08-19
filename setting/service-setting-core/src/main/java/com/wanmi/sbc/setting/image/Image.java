package com.wanmi.sbc.setting.image;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 图片实体类
 * Created by dyt on 2017/4/11.
 */
@Data
@Entity
@Table(name = "system_image")
public class Image implements Serializable {

    /**
     * 图片编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    /**
     * 商品编号
     */
    @Column(name = "cate_id")
    private Long cateId;

    /**
     * 图片名称
     */
    @Column(name = "image_name")
    private String imageName;

    /**
     * 图片KEY
     */
    @Column(name = "image_key")
    private String imageKey;

    /**
     * 原图路径
     */
    @Column(name = "artwork_url")
    private String artworkUrl;

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
     * 删除标记
     */
    @Column(name = "del_flag")
    @NotNull
    private DeleteFlag delFlag;

    /**
     * 图片服务器类型
     * 对应system_config的config_type
     * 参照ConfigType枚举
     */
    @Column(name = "server_type")
    private String serverType;

}
