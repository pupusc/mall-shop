package com.wanmi.sbc.setting.api.request.topicconfig;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.dto.SelectDto;
import io.swagger.annotations.ApiModel;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel
public class MixedComponentTabQueryRequest implements Serializable {

    private Integer pageNum = 0;

    private Integer pageSize = 10;

    /**
     * 1-未启用 0-启用
     */
    public Integer publishState;

    public Integer id;

    private Integer topicStoreyId;

    private String name;

    private String dropName;

    private String keyWord;

    private Integer level;
}
