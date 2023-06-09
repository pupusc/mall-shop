package com.soybean.mall.miniapp.goods.resp;

import com.wanmi.sbc.goods.api.response.index.NormalModuleSkuResp;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 2:13 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@Data
public class NormalModuleSkuDetailResp extends NormalModuleSkuResp {

    private String skuName;

    private List<Integer> channelTypes;


    private String specText;

    private BigDecimal marketPrice;
}
