package com.wanmi.sbc.goods.api.response.classify;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/15 2:35 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ClassifyGoodsProviderResponse implements Serializable {

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 店铺分类id
     */
    private Integer classifyId;
}
