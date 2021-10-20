package com.wanmi.sbc.goods.api.request.image;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/20 1:38 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ImageProviderRequest {

    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 图片跳转链接
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
     * 排序
     */
    private Integer orderNum;

    /**
     * 图片类型 1首页轮播
     */
    private Integer imageType;
}
