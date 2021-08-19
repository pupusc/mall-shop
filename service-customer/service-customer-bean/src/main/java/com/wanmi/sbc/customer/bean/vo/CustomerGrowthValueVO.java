package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.GrowthValueServiceType;
import com.wanmi.sbc.customer.bean.enums.OperateType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yang
 * @since 2019/2/23
 */
@Data
public class CustomerGrowthValueVO implements Serializable {

    private static final long serialVersionUID = -9123205282362107694L;

    /**
     * 用户id
     */
    private String customerId;

    /**
     * 操作类型 0:扣除 1:增长
     */
    private OperateType type;

    /**
     * 业务类型 0签到 1注册 2分享商品 3分享注册 4分享购买 5评论商品 6晒单 7上传头像/完善个人信息 8绑定微信 9添加收货地址 10关注店铺 11订单完成'
     */
    private GrowthValueServiceType serviceType;

    /**
     * 成长值
     */
    private Long growthValue;

    /**
     * 相关单号
     */
    private String tradeNo;

    /**
     * 操作时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime opTime;
}
