package com.soybean.mall.marketing.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/20 11:38 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class TradeItemSimpleRequest {

    /**
     * skuId
     */
    @NotBlank
    private String skuId;

    /**
     * 购买数量
     */
    @Range(min = 1)
    private long num;
}
