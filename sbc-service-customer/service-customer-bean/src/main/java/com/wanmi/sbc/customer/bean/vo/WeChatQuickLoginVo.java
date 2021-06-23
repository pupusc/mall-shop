package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: sbc-micro-service
 * @description: 微信小程序用户信息
 * @create: 2020-05-22 15:44
 **/
@Data
@ApiModel
public class WeChatQuickLoginVo implements Serializable {


    private static final long serialVersionUID = 4509189504634741230L;

    @ApiModelProperty(value = "id")
    private String Id;

    @ApiModelProperty(value = "应用内用户唯一标示")
    private String openId;

    @ApiModelProperty(value = "同一开放平台下唯一")
    private String unionId;

    @ApiModelProperty(value = "删除标示")
    private DeleteFlag delFlag;

    @ApiModelProperty(value = "绑定时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
}