package com.wanmi.sbc.setting.api.request.topicconfig;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.enums.MixedComponentLevel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel
public class TopicStoreyColumnAddRequest implements Serializable {

    /**
     * 开始时间
     */
    @NotNull
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @NotNull
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    private String name;

    private Integer sorting;

    private Integer topicStoreyId;

    /**
     * 父级id
     * */
    private Integer parentId;


    private Integer level = MixedComponentLevel.FOUR.toValue();

    private Integer bookType;

    private String dropName;

    private Integer orderWay;

    private String image;

    private String titleImage;

    private String url;

    private String recommend;

    private String labelId;

    private String attributeInfo;
}
