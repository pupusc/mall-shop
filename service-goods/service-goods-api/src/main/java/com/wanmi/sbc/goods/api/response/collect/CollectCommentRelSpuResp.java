package com.wanmi.sbc.goods.api.response.collect;

import lombok.Data;

import java.util.Date;

/**
 * Description: 采集店铺分类
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/14 2:27 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CollectCommentRelSpuResp {

    /**
     * 评论ID
     */
    private String commentId;

    /**
     * 采集店铺分类的商品信息
     */
    private String spuId;

    /**
     * 更新时间
     */
    private Date updateTime;
}
