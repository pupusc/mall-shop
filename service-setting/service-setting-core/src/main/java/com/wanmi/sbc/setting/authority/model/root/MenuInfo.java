package com.wanmi.sbc.setting.authority.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统菜单信息
 * Created by bail on 2017-12-28
 */
@Data
@Entity
@Table(name = "menu_info")
public class MenuInfo implements Serializable {

    private static final long serialVersionUID = -3575489933840114170L;

    /**
     * 菜单标识
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "menu_id")
    private String menuId;

    /**
     * 系统类别(如:s2b平台,s2b商家)
     */
    @Column(name = "system_type_cd")
    @NotNull
    private Platform systemTypeCd;

    /**
     * 父菜单id
     */
    @Column(name = "parent_menu_id")
    private String parentMenuId;

    /**
     * 菜单级别
     */
    @Column(name = "menu_grade")
    private Integer menuGrade;

    /**
     * 菜单名称
     */
    @Column(name = "menu_name")
    private String menuName;

    /**
     * 菜单路径
     */
    @Column(name = "menu_url")
    private String menuUrl;

    /**
     * 菜单图标
     */
    @Column(name = "menu_icon")
    private String menuIcon;

    /**
     * 排序号
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 删除标识,0:未删除1:已删除
     */
    @Column(name = "del_flag")
    @NotNull
    private DeleteFlag delFlag;

}
