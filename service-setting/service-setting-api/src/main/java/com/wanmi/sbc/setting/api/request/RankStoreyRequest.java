package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankStoreyRequest extends RankPageRequest implements Serializable {
    private static final long serialVersionUID = -5138062021719883495L;

    @ApiModelProperty("topicStoreyId")
    private Integer topicStoreyId;

    private Integer topicId;

    private Integer topicStoreySearchId;

    private Boolean isRankDetail;

    private Integer rankId;
}
