package com.wanmi.sbc.customer.api.request.points;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.OperateType;
import com.wanmi.sbc.customer.bean.enums.PointsServiceType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>会员积分明细通用查询请求参数</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPointsDetailQueryRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String customerId;

    /**
     * 用户id列表
     */
    private List<String> customerIdList;

    /**
     * 用户账号
     */
    private String customerAccount;

    /**
     * 用户名
     */
    private String customerName;

    /**
     * 操作类型 0:扣除 1:增长
     */
    private OperateType type;

    /**
     * 会员积分业务类型 0签到 1注册 2分享商品 3分享注册 4分享购买  5评论商品 6晒单 7上传头像/完善个人信息 8绑定微信
     * 9添加收货地址 10关注店铺 11订单完成 12订单抵扣 13优惠券兑换 14积分兑换 15退单返还 16订单取消返还 17过期扣除
     */
    private PointsServiceType serviceType;

    /**
     * 内容备注
     */
    private String content;

    /**
     * 搜索条件:操作时间开始
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime opTimeBegin;

    /**
     * 搜索条件:操作时间截止
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime opTimeEnd;

    /**
     * 负责业务员
     */
    private String employeeId;

    /**
     * 负责业务员ID集合
     */
    private List<String> employeeIds;
}