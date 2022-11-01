package com.wanmi.sbc.goods.info.model.root;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "t_goods_freight_history")
public class GoodsFreightHistory implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "his_freight_id")
    private Integer hisFreightId;

    @Column(name = "goods_id")
    private String goodsId;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 0 正常 1 已删除
     */
    @Column(name = "del_flag")
    private Integer delFlag;

}
