package com.wanmi.sbc.goods.standard.model.root;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;

import javax.persistence.*;

/**
 * ${DESCRIPTION}
 *
 * @auther ruilinxin
 * @create 2018/03/20 10:04
 */
@Data
@Entity
@Table(name = "standard_goods_rel")
public class StandardGoodsRel {

    /**
     * 编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rel_id")
    private Long relId;

    /**
     * SPU标识
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     *商品库SPU编号
     */
    @Column(name = "standard_id")
    private String standardId;

    /**
     *店铺id
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 删除标记  二次导入可能用到
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 是否需要同步 0：不需要同步 1：需要同步
     */
    @Column(name = "need_synchronize")
    @Enumerated
    private BoolFlag needSynchronize;

}
