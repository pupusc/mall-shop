package com.wanmi.sbc.goods.marketing.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 采购单商品选择的营销
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "goods_marketing")
public class GoodsMarketing {
    /**
     * 唯一编号
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    /**
     * sku编号
     */
    @Column(name = "goods_info_id")
    private String goodsInfoId;

    /**
     * 客户编号
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 营销编号
     */
    @Column(name = "marketing_id")
    private Long marketingId;
}
