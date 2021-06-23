package com.wanmi.sbc.setting.bean.vo;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.enums.PopupStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>弹窗管理VO</p>
 * @author weiwenhao
 * @date 2020-04-21
 */
@ApiModel
@Data
public class PopupAdministrationVO implements Serializable {


    private static final long serialVersionUID = 7032990120777222164L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long popupId;

    /**
     * 弹窗名称
     */
    @ApiModelProperty(value = "弹窗名称")
    private String popupName;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 弹窗url
     */
    @ApiModelProperty(value = "弹窗url")
    private String popupUrl;

    /**
     * 应用页面
     */
    @ApiModelProperty(value = "应用页面")
    private String applicationPageName;

    /**
     * 跳转页
     */
    @ApiModelProperty(value = "跳转页")
    private String jumpPage;

    /**
     * 投放频次
     */
    @ApiModelProperty(value = "投放频次")
    private String launchFrequency;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;


    /**
     * 修改时间
      */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 删除人
     */
    @ApiModelProperty(value = "删除人")
    private String deletePerson;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deleteTime;

    /**
     * 弹窗状态
     */
    @ApiModelProperty(value = "是否暂停（1：暂停，0：正常）")
    private BoolFlag isPause;

    /**
     * 查询类型，0：全部，1：进行中，2：暂停中，3：未开始，4：已结束
     */
    @ApiModelProperty(value = "查询类型")
    private PopupStatus popupStatus;

    /**
     * 获取活动状态
     * @return
     */
    public PopupStatus getPopupStatus() {
        if (beginTime != null && endTime != null) {
            if (LocalDateTime.now().isBefore(beginTime)) {
                return PopupStatus.NOT_START;
            } else if (LocalDateTime.now().isAfter(endTime)) {
                return PopupStatus.ENDED;
            } else if (isPause == BoolFlag.YES) {
                return PopupStatus.PAUSED;
            } else {
                return PopupStatus.STARTED;
            }
        }
        return null;
    }
}
