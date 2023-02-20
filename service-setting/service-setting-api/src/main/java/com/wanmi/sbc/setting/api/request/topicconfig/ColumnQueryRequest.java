package com.wanmi.sbc.setting.api.request.topicconfig;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel
public class ColumnQueryRequest implements Serializable {

    private Integer pageNum = 0;

    private Integer pageSize = 10;

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

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
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

    /**
     * 0-未开始 1-进行中 2-已结束
     */
    public Integer state;

    /**
     * 1-未启用 0-启用
     */
    public Integer publishState;

    public ColumnQueryRequest() {
        deleted = publishState;
    }
}
