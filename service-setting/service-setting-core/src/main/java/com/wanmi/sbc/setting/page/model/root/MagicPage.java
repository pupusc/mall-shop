package com.wanmi.sbc.setting.page.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>magic-page实体类</p>
 *
 * @author lq
 */
@Data
@Entity
@Table(name = "magic_page")
public class MagicPage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 在线客服主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 通过建站生成的首页dom字符串
     */
    @Column(name = "html_string")
    private String htmlString;

    /**
     * 删除标志 默认0：未删除 1：删除
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 操作人
     */
    @Column(name = "operate_person")
    private String operatePerson;

}