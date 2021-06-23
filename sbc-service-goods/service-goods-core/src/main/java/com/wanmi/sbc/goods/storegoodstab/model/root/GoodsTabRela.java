package com.wanmi.sbc.goods.storegoodstab.model.root;

import com.wanmi.sbc.common.annotation.CanEmpty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>商品详情模板关联实体</p>
 * @author: sunkun
 * @Date: 2018-10-16
 */
@Data
@Entity
@Table(name = "goods_tab_rela")
@IdClass(GoodsTabRelaMultiKeys.class)
public class GoodsTabRela implements Serializable {

    private static final long serialVersionUID = 1949474666275166208L;

    /**
     * spu标识
     */
    @Id
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 详情模板id
     */
    @Id
    @Column(name = "tab_id")
    private Long tabId;

    /**
     * 内容详情
     */
    @Column(name = "tab_detail")
    @CanEmpty
    private String tabDetail;
}
