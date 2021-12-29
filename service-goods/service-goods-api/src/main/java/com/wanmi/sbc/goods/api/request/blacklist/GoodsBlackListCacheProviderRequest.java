package com.wanmi.sbc.goods.api.request.blacklist;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/23 5:19 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsBlackListCacheProviderRequest implements Serializable {

    private Integer id;

    /**
     * 业务分类 1 新品榜 2 畅销排行榜 3 特价书榜 4、不显示会员价的商品
     */
    private Collection<Integer> businessCategoryColl;


    /**
     * 业务类型 1、商品skuId 2、商品spuId 3、一级分类id 4、二级分类id
     */
    private Collection<Integer> businessTypeColl;
}
