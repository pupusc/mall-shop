package com.wanmi.sbc.goods.storecate.model.root;

import com.wanmi.sbc.goods.storecate.model.pk.StoreCateGoodsRelaPK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 商品-店铺分类关联实体类
 * Created by bail on 2017/11/13.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(StoreCateGoodsRelaPK.class)
@Table(name = "store_cate_goods_rela")
public class StoreCateGoodsRela implements Serializable {

    /**
     * 商品标识
     */
    @Id
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 店铺分类标识
     */
    @Id
    @Column(name = "store_cate_id")
    private Long storeCateId;

}

