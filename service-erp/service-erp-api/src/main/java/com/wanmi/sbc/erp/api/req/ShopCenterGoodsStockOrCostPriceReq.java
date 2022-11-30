package com.wanmi.sbc.erp.api.req;

import com.wanmi.sbc.erp.api.enums.ShopCenterEnum;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/11/23 6:25 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ShopCenterGoodsStockOrCostPriceReq {

    private List<String> goodsCodes;

    private Long shopId = ShopCenterEnum.SHOPCENTER.getCode();
}
