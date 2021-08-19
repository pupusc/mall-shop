package com.wanmi.sbc.customer.storesharerecord.model.root;

import com.wanmi.sbc.common.enums.IndexType;
import com.wanmi.sbc.common.enums.ShareChannel;
import com.wanmi.sbc.common.enums.TerminalSource;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>商城分享实体类</p>
 *
 * @author zhangwenchang
 * @date 2020-03-06 13:48:42
 */
@Data
@Entity
@Table(name = "store_share_record")
public class StoreShareRecord{
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
     * 0分享首页，1分享店铺首页
     */
    @Column(name = "index_type")
    private IndexType indexType;

    /**
     * 终端：1 H5，2pc，3APP，4小程序
     */
    @Column(name = "terminal_source")
    private TerminalSource terminalSource;

    /**
     * 分享渠道：0微信，1朋友圈，2QQ，3QQ空间，4微博，5复制链接，6保存图片
     */
    @Column(name = "share_channel")
    private ShareChannel shareChannel;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

}