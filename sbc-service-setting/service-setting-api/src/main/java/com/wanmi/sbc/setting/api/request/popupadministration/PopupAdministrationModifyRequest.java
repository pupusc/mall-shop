package com.wanmi.sbc.setting.api.request.popupadministration;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>弹窗管理新增参数</p>
 * @author weiwenhao
 * @date 2020-04-21
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupAdministrationModifyRequest extends SettingBaseRequest {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @NotNull
    private Long popupId;

    /**
     * 弹窗名称
     */
    @ApiModelProperty(value = "弹窗名称")
    @Length(max=20)
    @NotNull
    private String popupName;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @NotNull
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @NotNull
    private LocalDateTime endTime;

    /**
     * 弹窗url
     */
    @ApiModelProperty(value = "弹窗url")
    @NotNull
    private String popupUrl;

    /**
     * 应用页面
     */
    @ApiModelProperty(value = "应用页面")
    @NotNull
    private List<String> applicationPageName;

    /**
     * 跳转页
     */
    @ApiModelProperty(value = "跳转页")
    @NotNull
    private String jumpPage;

    /**
     * 投放频次
     */
    @ApiModelProperty(value = "投放频次")
    @NotNull
    private String launchFrequency;


    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updatePerson;

    /**
     * 弹窗状态
     */
    @ApiModelProperty(value = "是否暂停（1：暂停，0：正常）")
    private BoolFlag isPause;
}
