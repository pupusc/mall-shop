package com.soybean.mall.goods.req;

import com.soybean.elastic.api.req.EsKeyWordQueryProviderReq;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 2:39 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListKeyWordQueryReq extends EsKeyWordQueryProviderReq {

    /**
     * 商品数量
     */
    @NotNull
    @Min(1)
    private Integer spuNum;

    /**
     * 书单/榜单类型 1榜单 2书单
     */
    @NotNull
    private Integer bookListBusinessType;
}
