package com.wanmi.sbc.setting.api.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description 楼层商品
 * @Date  2023/2/7 14:47
 * @param: null
 * @return: null
 */
@Data
public class TopicStoreySearchContentRequest implements Serializable {

    private static final long serialVersionUID = 7169124215109467084L;

    private Integer id;

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
    private LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

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

    private String numTxt;

    private String showLabeTxt;

    private String recommend;

    private Integer publisherId;

    private String isbn;

    private Integer orderNum;
}
