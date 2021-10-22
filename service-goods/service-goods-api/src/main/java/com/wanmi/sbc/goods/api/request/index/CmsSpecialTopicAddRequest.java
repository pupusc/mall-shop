package com.wanmi.sbc.goods.api.request.index;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CmsSpecialTopicAddRequest {

    /**
     * 名称
     */
    private String name;

    /**
     * 栏目名称
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
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

}
