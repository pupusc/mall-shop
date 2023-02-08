package com.wanmi.sbc.setting.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel
public class TopicStoreyColumnGoodsDTO implements Serializable {

    private static final long serialVersionUID = 1957886388271489221L;

    @NotNull
    @ApiModelProperty("专栏id")
    private Integer id;

    private String skuNo;

    @ApiModelProperty("专栏名称")
    private String goodsName;

    private String imageUrl;

    @ApiModelProperty("排序")
    private Integer sorting;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer deleted;

    /**
     * 0-未开始 1-进行中 2-已结束
     */
    public Integer state;

    /**
     * 1-未启用 0-启用
     */
    public Integer publishState;

    private Integer topicStoreyId;
}