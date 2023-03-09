package com.wanmi.sbc.setting.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel
public class TopicStoreyColumnDTO implements Serializable {

    private static final long serialVersionUID = 1957886388271489221L;

    @NotNull
    @ApiModelProperty("专栏id")
    private Integer id;

    private Integer topicStoreyId;

    @ApiModelProperty("专栏名称")
    private String Name;

    @ApiModelProperty("排序")
    private Integer sorting;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime beginTime;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    private Integer deleted;

    @ApiModelProperty("1动态，2静态")
    private Integer showType;

    /**
     * 0-未开始 1-进行中 2-已结束
     */
    public Integer state;

    /**
     * 1-未启用 0-启用
     */
    public Integer publishState;

}
