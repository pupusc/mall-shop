package com.wanmi.sbc.goods.api.response.index;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IndexFeatureDto {

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
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 启用状态
     */
    private Integer publishState;

    /**
     * 状态
     */
    private Integer state;



}
