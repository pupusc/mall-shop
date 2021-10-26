package com.wanmi.sbc.goods.api.request.index;

import com.wanmi.sbc.goods.bean.enums.PublishState;
import lombok.Data;

@Data
public class CmsSpecialTopicAddRequest {

    public Integer id;
    /**
     * 名称
     */
    public String name;

    /**
     * 栏目名称
     */
    public String title;

    /**
     * 副标题
     */
    public String subTitle;

    /**
     * 图片地址
     */
    public String imgUrl;

    /**
     * 图片链接
     */
    public String imgHref;

    /**
     * 开始时间
     */
    public String beginTime;

    /**
     * 结束时间
     */
    public String endTime;

    /**
     * 顺序
     */
    public Integer orderNum;

    /**
     * 发布状态 0-未启用 1-启用
     */
    public PublishState publishState;

}
