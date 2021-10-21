package com.wanmi.sbc.goods.api.response.image;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/22 2:11 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ImageProviderResponse implements Serializable {

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
     * 启用状态 0未启用 1启用
     */
    private Integer publishState;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     *  0未开始 1进行中 2 已结束
     */
    private Integer status;

    /**
     * 图片类型 1首页轮播
     */
    private Integer imageType;
}
