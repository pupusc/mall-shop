package com.wanmi.sbc.goods.log;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品审核时间
 * Created by daiyitian on 14/3/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "goods_check_log")
public class GoodsCheckLog implements Serializable {

    /**
     * 订单号
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    /**
     * 商品SpuId
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 审核人
     */
    @Column(name = "checker")
    private String checker;

    /**
     * 审核时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "check_time")
    private LocalDateTime checkTime = LocalDateTime.now();

    /**
     * 审核状态 0：待审核 1：已审核 2：审核失败 3：禁售中
     */
    @Column(name = "audit_status")
    @Enumerated
    private CheckStatus auditStatus;

    /**
     * 审核原因
     */
    @Column(name = "audit_reason")
    private String auditReason;

}
