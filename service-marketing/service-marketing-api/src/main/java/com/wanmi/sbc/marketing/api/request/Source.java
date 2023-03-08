package com.wanmi.sbc.marketing.api.request;

import com.wanmi.sbc.marketing.bean.enums.SourceType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Source {

    @ApiModelProperty(value = "来源id")
    private String sourceId;
    /**
     * @see SourceType
     */
    @ApiModelProperty(value = "来源类型")
    private Integer sourceType;

    public Source(String sourceId, Integer sourceType) {
        this.sourceId = sourceId;
        this.sourceType = sourceType;
    }

    public Source() {
    }
}
