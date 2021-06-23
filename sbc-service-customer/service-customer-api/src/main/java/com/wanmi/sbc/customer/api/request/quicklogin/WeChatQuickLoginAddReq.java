package com.wanmi.sbc.customer.api.request.quicklogin;

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
 * @description:
 * @create: 2020-05-22 16:20
 **/
@Data
@ApiModel
public class WeChatQuickLoginAddReq implements Serializable {

    private static final long serialVersionUID = 5991251223731043214L;

    @ApiModelProperty(value = "应用内用户唯一标示")
    private String openId;

    @ApiModelProperty(value = "同一开放平台下唯一")
    private String unionId;

    @ApiModelProperty(value = "删除标示")
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
}