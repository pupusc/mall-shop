package com.wanmi.sbc.goods.api.request.goods;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/2/23 1:36 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsUpdateProviderRequest implements Serializable {

    private String goodsId;

    private String erpGoodsNoNew;

    private String goodsInfoId;

    private String erpGoodsInfoNoNew;
}
