package com.wanmi.sbc.goods.bookingsalegoods.model.root;

import com.wanmi.sbc.common.base.BaseEntity;
import com.wanmi.sbc.goods.bookingsale.model.root.BookingSale;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * <p>预售商品信息实体类</p>
 *
 * @author dany
 * @date 2020-06-05 10:51:35
 */
@Data
@Entity
@Table(name = "booking_sale_goods")
public class BookingSaleGoods extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 预售id
     */
    @Column(name = "booking_sale_id")
    private Long bookingSaleId;

    /**
     * 商户id
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * skuID
     */
    @Column(name = "goods_info_id")
    private String goodsInfoId;

    /**
     * spuID
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 定金
     */
    @Column(name = "hand_sel_price")
    private BigDecimal handSelPrice;

    /**
     * 膨胀价格
     */
    @Column(name = "inflation_price")
    private BigDecimal inflationPrice;

    /**
     * 预售价
     */
    @Column(name = "booking_price")
    private BigDecimal bookingPrice;

    /**
     * 预售数量
     */
    @Column(name = "booking_count")
    private Integer bookingCount;

    /**
     * 实际可售数量
     */
    @Column(name = "can_booking_count")
    private Integer canBookingCount;

    /**
     * 定金支付数量
     */
    @Column(name = "hand_sel_count")
    private Integer handSelCount;

    /**
     * 尾款支付数量
     */
    @Column(name = "tail_count")
    private Integer tailCount;

    /**
     * 全款支付数量
     */
    @Column(name = "pay_count")
    private Integer payCount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_sale_id", insertable = false, updatable = false)
    private BookingSale bookingSale;

}