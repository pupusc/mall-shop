package com.wanmi.sbc.classify.request;

import lombok.Data;

/**
 * Description: 获取店铺分类列表中的商品信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/30 10:39 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ClassifyGoodsAndBookListModelPageRequest {

    private int pageNum = 0;

    private int pageSize = 10;

    //@NotNull
    private Integer classifyId;

    /**
     * 店铺类型 0 推荐 1评分 2 上新 3 销售额
     */
    private Integer classifySelectType = 0;

    /**
     * 主播推荐 1樊登解读,2非凡精读,3樊登直播,4热销，5上新
     */
    private String anchorPushs;
}
