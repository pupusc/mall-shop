package com.wanmi.sbc.goods.brand.model.root;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 签约品牌实体类
 * Created by sunkun on 2017/10/31.
 */
@Data
@Entity
@Table(name = "contract_brand")
public class ContractBrand implements Serializable {

    private static final long serialVersionUID = -1889721118970868784L;

    /**
     * 签约品牌分类
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_brand_id")
    private Long contractBrandId;


    /**
     * 店铺主键
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 品牌编号
     */
    @Column(name = "brand_id",insertable=false,updatable=false)
    private Long brandId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    @JsonManagedReference
    private GoodsBrand goodsBrand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "check_brand_id")
    @JsonManagedReference
    private CheckBrand checkBrand;


    /**
     * 授权图片路径
     */
    @Column(name = "authorize_pic")
    private String authorizePic;
}
