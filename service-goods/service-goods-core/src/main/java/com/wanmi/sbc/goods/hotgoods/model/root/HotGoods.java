package com.wanmi.sbc.goods.hotgoods.model.root;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * t_hot_goods
 * @author 
 */
@Table(name = "t_hot_goods")
@Data
@Entity
public class HotGoods implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private String id;
    
    @Column(name = "spu_id")
    private String spuId;

    /**
     * 1-商品  2-书单
     */
    private Integer type;

    private Integer sort;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}