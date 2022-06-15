package com.wanmi.sbc.goods.api.response.collect;

import com.wanmi.sbc.common.enums.TerminalSource;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Description: 采集店铺分类
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/14 2:27 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CollectCommentRelSpuDetailResp {


    /**
     * 商品id(spuId)
     */
    private String goodsId;

    /**
     * 总评数
     */
    private BigDecimal evaluateSum;

    /**
     * 好评数
     */
    private BigDecimal goodEvaluateSum;

    /**
     * 好评率
     */
    private String goodEvaluateRatio;

}



