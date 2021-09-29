package com.wanmi.sbc.goods.tag.model;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_tag_rel")
public class TagRel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "goods_id")
    private String goodsId;

    @Column(name = "category")
    private Integer category;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;
}
