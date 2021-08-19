package com.wanmi.sbc.customer.api.request.points;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.OperateType;
import com.wanmi.sbc.customer.bean.enums.PointsServiceType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>会员积分明细新增参数</p>
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPointsDetailBatchAddRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -6727896651743144619L;
    /**
     * 用户id
     */
    private List<String> customerIdList;


    /**
     * 操作类型 0:扣除 1:增长
     */
    @NotNull
    private OperateType type;

    /**
     * 会员积分业务类型 0签到 1注册 2分享商品 3分享注册 4分享购买  5评论商品 6晒单 7上传头像/完善个人信息 8绑定微信
     * 9添加收货地址 10关注店铺 11订单完成 12订单抵扣 13优惠券兑换 14积分兑换 15退单返还 16订单取消返还 17过期扣除
     */
    @NotNull
    private PointsServiceType serviceType;

    /**
     * 积分数量
     */
    private Long points;

    /**
     * 内容备注
     */
    @Length(max = 255)
    private String content;



}