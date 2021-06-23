package com.wanmi.sbc.goods.cate.model.root;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 签约分类
 * Created by sunkun on 2017/10/30.
 */
@Data
@Entity
@Table(name = "contract_cate")
public class ContractCate implements Serializable {

    private static final long serialVersionUID = -4228807194628440375L;

    /**
     * 签约分类主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_cate_id")
    private Long contractCateId;

    /**
     * 店铺主键
     */
    @Column(name = "store_id")
    private Long storeId;


    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="cate_id")
    @JsonManagedReference
    private GoodsCate goodsCate;

    /**
     * 分类扣率
     */
    @Column(name = "cate_rate")
    private BigDecimal cateRate;

    /**
     * 资质图片路径
     */
    @Column(name = "qualification_pics")
    private String qualificationPics;


}
