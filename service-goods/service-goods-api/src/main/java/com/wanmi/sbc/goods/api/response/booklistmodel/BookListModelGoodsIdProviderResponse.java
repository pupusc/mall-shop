package com.wanmi.sbc.goods.api.response.booklistmodel;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/18 2:22 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelGoodsIdProviderResponse extends BookListModelProviderResponse{

    /**
     * 商品id
     */
    private String spuId;

    private String spuNo;

    private String skuId;

    private String skuNo;

    private String erpGoodsNo;

    private String erpGoodsInfoNo;

    private Integer orderNum;
}
