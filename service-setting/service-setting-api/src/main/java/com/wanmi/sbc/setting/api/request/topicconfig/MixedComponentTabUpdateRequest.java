package com.wanmi.sbc.setting.api.request.topicconfig;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.dto.KeyWordsDto;
import com.wanmi.sbc.setting.bean.dto.SelectDto;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel
public class MixedComponentTabUpdateRequest implements Serializable {

    private Integer id;

    /**
     * 开始时间
     */
    @NotNull
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @NotNull
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    private String name;

    private SelectDto color;

    private SelectDto image;

    private String subName;

    private Integer sorting;

    private Integer topicStoreyId;

    private String dropName;

    private Integer pId;
    private List<KeyWordsDto> keywords;

    public ColumnUpdateRequest getColumnUpdateRequest() {
        ColumnUpdateRequest columnUpdateRequest = new ColumnUpdateRequest();
        columnUpdateRequest.setId(id);
        columnUpdateRequest.setEndTime(endTime);
        columnUpdateRequest.setBeginTime(startTime);
        columnUpdateRequest.setDropName(dropName);
        columnUpdateRequest.setName(name);
        columnUpdateRequest.setColor(color);
        columnUpdateRequest.setImage(image);
        columnUpdateRequest.setSubName(subName);
        columnUpdateRequest.setOrderNum(sorting);
        columnUpdateRequest.setTopicStoreyId(topicStoreyId);
        columnUpdateRequest.setPId(pId);
        columnUpdateRequest.setAttributeInfo(keywords != null ? JSON.toJSONString(keywords) : null);
        return columnUpdateRequest;
    }

}
