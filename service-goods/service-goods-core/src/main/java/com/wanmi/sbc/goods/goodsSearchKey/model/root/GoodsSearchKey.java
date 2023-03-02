package com.wanmi.sbc.goods.goodsSearchKey.model.root;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "goods_restricted_sale")
public class GoodsSearchKey implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "spu_id")
    private String spuId;

    @Column(name = "del_flag")
    private Integer delFlag;

    @Column(name = "creat_time")
    private LocalDateTime creatTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
