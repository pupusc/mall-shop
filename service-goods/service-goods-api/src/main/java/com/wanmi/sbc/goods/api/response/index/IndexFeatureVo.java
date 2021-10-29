package com.wanmi.sbc.goods.api.response.index;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSecondSerializer;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class IndexFeatureVo {

    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 主标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 图片链接
     */
    private String imgHref;

    /**
     * 开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSecondSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSecondSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 启用状态
     */
    private Integer publishState;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 排序
     */
    private Integer orderNum;

}
