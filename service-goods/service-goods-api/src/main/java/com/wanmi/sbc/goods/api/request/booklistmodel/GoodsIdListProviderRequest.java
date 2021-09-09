package com.wanmi.sbc.goods.api.request.booklistmodel;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/2 2:49 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsIdListProviderRequest implements Serializable {
    /**
     *
     */
    private String spuId;

    private String spuNo;

    private String skuId;

    private String skuNo;

    private String erpGoodsNo;

    private String erpGoodsInfoNo;
}
