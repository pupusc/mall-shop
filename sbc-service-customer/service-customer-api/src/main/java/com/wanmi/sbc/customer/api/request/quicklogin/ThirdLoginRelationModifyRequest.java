package com.wanmi.sbc.customer.api.request.quicklogin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 第三方关系表更新
 *
 * @Author: songhanlin
 * @Date: Created In 10:01 AM 2018/8/8
 * @Description: 第三方关系表
 */
@ApiModel
@Data
public class ThirdLoginRelationModifyRequest implements Serializable {

    private static final long serialVersionUID = -7552115296458159444L;

    /**
     * 第三方登录主键
     */
    @ApiModelProperty(value = "第三方登录主键")
    @NotNull
    private String thirdLoginId;

    /**
     * 第三方关系关联(union)Id
     */
    @ApiModelProperty(value = "第三方关系关联(union)Id")
    private String thirdLoginUid;

    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id")
    private String customerId;

    /**
     * 第三方类型 0:wechat
     */
    @ApiModelProperty(value = "第三方类型")
    private ThirdLoginType thirdLoginType;

    /**
     * 微信授权openId, 该字段只有微信才有, 由于微信登录使用的是unionId,
     * 但是微信模板消息发送需要使用openId, 所以需要union_id, 所以union_id和open_id单独存放
     */
    @ApiModelProperty(value = "微信授权openI")
    private String thirdLoginOpenId;

    /**
     * 绑定时间
     */
    @ApiModelProperty(value = "绑定时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime bindingTime;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;
}
