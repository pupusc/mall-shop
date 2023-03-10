package com.wanmi.sbc.setting.api.request.topicconfig;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.dto.KeyWordsDto;
import com.wanmi.sbc.setting.bean.dto.SelectDto;
import com.wanmi.sbc.setting.bean.enums.MixedComponentLevel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel
public class MixedComponentTabAddRequest implements Serializable {

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

    private String dropName;

    private String name;

    private Integer level;
    private SelectDto color;

    private SelectDto image;

    private String subName;

    private Integer sorting;

    private Integer topicStoreyId;

    private List<KeyWordsDto> keyWords;

    private Integer pId;

    private Integer id;

    public ColumnAddRequest getColumnAddRequest() {
        ColumnAddRequest columnAddRequest = new ColumnAddRequest();
        columnAddRequest.setLevel(level);
        columnAddRequest.setEndTime(endTime);
        columnAddRequest.setPId(pId);
        columnAddRequest.setBeginTime(startTime);
        columnAddRequest.setCreateTime(LocalDateTime.now());
        columnAddRequest.setDropName(dropName);
        columnAddRequest.setName(name);
        columnAddRequest.setColor(color);
        columnAddRequest.setImage(image);
        columnAddRequest.setSubName(subName);
        columnAddRequest.setOrderNum(sorting);
        columnAddRequest.setTopicStoreyId(topicStoreyId);
        //columnAddRequest.setPId(id != null ? id : null);
        columnAddRequest.setAttributeInfo(keyWords != null ? JSON.toJSONString(keyWords) : null);
        return columnAddRequest;
    }
}
