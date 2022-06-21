package com.wanmi.sbc.goods.api.response.collect;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description: 采集店铺分类
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/14 2:27 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CollectCommentRelSpuDetailResp implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品id(spuId)
     */
    @Column(name = "goodsId")
    private String goodsId;

    /**
     * 总评数
     */
    @Column(name = "evaluateSum")
    private BigDecimal evaluateSum;

    /**
     * 好评数
     */
    @Column(name = "goodEvaluateSum")
    private BigDecimal goodEvaluateSum;

    /**
     * 好评率
     */
    private String goodEvaluateRatio;

}



