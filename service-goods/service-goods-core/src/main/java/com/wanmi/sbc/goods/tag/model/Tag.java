package com.wanmi.sbc.goods.tag.model;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;
}
