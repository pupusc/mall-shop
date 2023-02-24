package com.wanmi.sbc.setting.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description: TODO
 * @Author zh
 * @Date 2023/2/18 11:39
 */
@Data
public class ColumnContentDTO {

    private Integer id;

    private Integer topicStoreySearchId;

    private Integer topicStoreyId;

    private String spuNo;

    private String skuNo;

    private Integer type;

    private String imageUrl;

    private String linkUrl;

    private Integer sorting;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    private Integer deleted;

    private String skuId;

    private String goodsName;

    private String spuId;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
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

    /**
     * 0-未开始 1-进行中 2-已结束
     */
    public Integer state;

    /**
     * 1-未启用 0-启用
     */
    public Integer publishState;

    private BigDecimal marketPrice;

    private Integer goodsStatus;

    private Long imageId;

    private String referrer;

    private String referrerTitle;

    private String recommendName;


    public ColumnContentDTO() {
        LocalDateTime now = LocalDateTime.now();
        publishState = deleted;
        if (createTime != null && now.isBefore(createTime)) {
            //未开始
            state = 0;
        } else if (endTime != null && now.isAfter(endTime)) {
            //已结束
            state = 2;
        } else {
            //进行中
            state = 1;
        }
    }
}
