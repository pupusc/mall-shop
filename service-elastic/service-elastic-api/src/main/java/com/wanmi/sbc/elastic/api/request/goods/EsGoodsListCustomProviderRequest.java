package com.wanmi.sbc.elastic.api.request.goods;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 定制商品列表请求对象
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/10 7:32 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsGoodsListCustomProviderRequest implements Serializable {


    private List<String> goodsIdList;
}
