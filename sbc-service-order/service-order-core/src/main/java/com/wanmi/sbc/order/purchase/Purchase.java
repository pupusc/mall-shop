package com.wanmi.sbc.order.purchase;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 采购单实体
 * Created by sunkun on 2017/11/27.
 */
@Data
@Entity
@Table(name = "Purchase")
public class Purchase {
    /**
     * 采购单编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long purchaseId;

    /**
     * 客户编号
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 默认为0、店铺精选时对应邀请人id-会员id
     */
    @ApiModelProperty(value = "邀请人id")
    String inviteeId;

    /**
     * 商品编号
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * SKU编号
     */
    @Column(name = "goods_info_id")
    private String goodsInfoId;

    /**
     * 全局购买数
     */
    @Column(name = "goods_num")
    private Long goodsNum;

    /**
     * 公司信息ID
     */
    @Column(name = "company_info_id")
    private Long companyInfoId;

    /**
     * 采购创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 店铺id
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 商品一级类目
     */
    @Column(name = "cate_top_id")
    private Long cateTopId;

    /**
     * 商品类目
     */
    @Column(name = "cate_id")
    private Long cateId;

    /**
     * 商品品牌
     */
    @Column(name = "brand_id")
    private Long brandId;

    /**
     * 终端来源
     */
    @Column(name = "terminal_source")
    private TerminalSource terminalSource;
    /**
     * 更新时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

}
