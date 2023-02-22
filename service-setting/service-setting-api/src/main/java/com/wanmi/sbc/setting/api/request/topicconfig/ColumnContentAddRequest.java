package com.wanmi.sbc.setting.api.request.topicconfig;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.dto.SelectDto;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel
public class ColumnContentAddRequest implements Serializable {

    private Integer topicStoreySearchId;

    private Integer topicStoreyId;

    private String spuNo;

    private String skuNo;

    private Integer type;

    private String imageUrl;

    private String linkUrl;

    private Integer sorting;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    private Integer deleted;

    private String skuId;

    private String goodsName;

    private String spuId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endTime;

    private String title;

    private String recommend;

    private Integer orderNum;

    private String isbn;

    private String name;

    private Integer publisherId;

    private Integer publisherName;

    private String numTxt;

    private String showLabeTxt;

    private BigDecimal marketPrice;

    private Integer goodsStatus;

    private Long imageId;


    /**
     * 0-未开始 1-进行中 2-已结束
     */
    public Integer state;

    /**
     * 1-未启用 0-启用
     */
    public Integer publishState;

    public ColumnContentAddRequest() {
        deleted = publishState;
    }
}
