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
public class ColumnDTO implements Serializable {

    private Integer id;

    private Integer topicStoreyId;

    private String name;

    private String subName;

    private String cateId;

    private String brandId;

    private String labelId;

    private Integer num;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    private LocalDateTime endTime;

    private Integer deleted;

    private Integer orderNum;

    private Integer showType;

    private Integer bookType;

    private Integer pId;

    private Integer level;

    private String color;

    private String image;

    private Integer relationStroeId;

    private String recommend;

    private String remark;

    private Integer orderType;

    private Integer orderWay;

    private String dropName;

    private String attributeInfo;

    /**
     * 0-未开始 1-进行中 2-已结束
     */
    public Integer state;

    /**
     * 1-未启用 0-启用
     */
    public Integer publishState;


    public ColumnDTO() {
    }

    public ColumnDTO(LocalDateTime createTime, LocalDateTime endTime, Integer deleted) {
        LocalDateTime now = LocalDateTime.now();
        this.publishState = deleted;
        if (createTime != null && now.isBefore(createTime)) {
            //未开始
            this.state = 0;
        } else if (endTime != null && now.isAfter(endTime)) {
            //已结束
            this.state = 2;
        } else {
            //进行中
            this.state = 1;
        }
    }

}
