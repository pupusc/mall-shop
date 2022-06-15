package com.wanmi.sbc.goods.api.response.collect;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/14 6:25 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CollectSpuPropResp {


    /**
     * 临时id
     */
    private int tmpId;

    private String spuId;

    /**
     * isbn
     */
    private String isbn;

    /**
     * 定价
     */
    private BigDecimal fixPrice;

    /**
     * 评分
     */
    private Double score;

}
