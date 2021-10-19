package com.wanmi.sbc.goods.cate.model.root;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "goods_cate_sync")
public class GoodsCateSync {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "cate_type")
    private String cateType;

    @Column(name = "name")
    private String name;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "label_ids")
    private String labelIds;
}
