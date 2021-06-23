package com.wanmi.sbc.login;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ms.util.CustomLocalDateTimeDeserializer;
import com.wanmi.ms.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录返回
 * Created by chenli on 2017/11/29
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginStoreResponse implements Serializable{

    /**
     * 店铺状态 0、开启 1、关店 2、过期
     */
    @ApiModelProperty(value = "店铺状态")
    private StoreState storeState;

    /**
     * 店铺关店原因
     */
    @ApiModelProperty(value = "店铺关店原因")
    private String storeClosedReason;

    /**
     * 店铺有效期还剩10天时，开始提醒，每天倒计时提醒
     */
    @ApiModelProperty(value = "店铺有效期还剩10天时，开始提醒，每天倒计时提醒")
    private int overDueDay;

    /**
     * 签约结束日期
     */
    @ApiModelProperty(value = "签约结束日期")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractEndDate;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;

}
