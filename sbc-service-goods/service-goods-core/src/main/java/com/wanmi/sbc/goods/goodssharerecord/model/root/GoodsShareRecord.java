package com.wanmi.sbc.goods.goodssharerecord.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.ShareChannel;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * <p>商品分享实体类</p>
 *
 * @author zhangwenchang
 * @date 2020-03-06 13:46:24
 */
@Data
@Entity
@Table(name = "goods_share_record")
public class GoodsShareRecord {
    /**
     * shareId
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "share_id")
    private Long shareId;

    /**
     * 会员Id
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * SPU id
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * SKU id
     */
    @Column(name = "goods_info_id")
    private String goodsInfoId;

    /**
     * 店铺ID
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 公司信息ID
     */
    @Column(name = "company_info_id")
    private Long companyInfoId;

    /**
     * 终端：1 H5，2pc，3APP，4小程序
     */
    @Column(name = "terminal_source")
    private TerminalSource terminalSource;

    /**
     * 分享渠道：0微信，1朋友圈，2QQ，3QQ空间，4微博，5复制链接，6保存图片
     */
    @ApiModelProperty(value = "分享渠道：0微信，1朋友圈，2QQ，3QQ空间，4微博，5复制链接，6保存图片")
    private ShareChannel shareChannel;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

}