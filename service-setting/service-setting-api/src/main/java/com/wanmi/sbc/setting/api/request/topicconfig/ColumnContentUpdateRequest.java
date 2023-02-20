package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class ColumnContentUpdateRequest extends ColumnAddRequest implements Serializable {

    private Integer id;
}
