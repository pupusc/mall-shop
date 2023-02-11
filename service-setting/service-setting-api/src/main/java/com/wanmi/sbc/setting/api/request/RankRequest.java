package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.setting.api.response.TopicStoreySearchContentRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
public class RankRequest implements Serializable {
    private static final long serialVersionUID = -5138062021719883495L;

    @ApiModelProperty("榜单名称")
    private String rankName;

    @ApiModelProperty("榜单id")
    private Integer id;

    @ApiModelProperty("层级")
    private Integer level;

    @ApiModelProperty("父级id")
    private Integer p_id;

    @ApiModelProperty("榜单")
    private Collection rankList;
}
