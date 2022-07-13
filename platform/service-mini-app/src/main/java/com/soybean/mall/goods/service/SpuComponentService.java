package com.soybean.mall.goods.service;

import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description: 组件服务信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/8 1:43 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class SpuComponentService {

    @Autowired
    private GoodsBlackListProvider goodsBlackListProvider;

    /**
     * 获取搜索黑名单
     * @param blackListCategorys
     * @return
     */
    public List<String> listSearchBlackList(List<Integer> blackListCategorys) {
        List<String> unSpuIds = new ArrayList<>();
        //获取搜索黑名单
        GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest = new GoodsBlackListPageProviderRequest();
        goodsBlackListPageProviderRequest.setBusinessCategoryColl(blackListCategorys);
        GoodsBlackListPageProviderResponse goodsBlackListResponse = goodsBlackListProvider.listNoPage(goodsBlackListPageProviderRequest).getContext();
        if (goodsBlackListResponse != null) {
            List<String> tmpUnSpuIds = goodsBlackListResponse.getGoodsSearchH5AtIndexBlackListModel().getGoodsIdList();
            if (CollectionUtils.isNotEmpty(tmpUnSpuIds)) {
                unSpuIds.addAll(tmpUnSpuIds);
            }
            List<String> tmpGoodsIdList = goodsBlackListResponse.getGoodsSearchAtIndexBlackListModel().getGoodsIdList();
            if (CollectionUtils.isNotEmpty(tmpGoodsIdList)) {
                unSpuIds.addAll(tmpGoodsIdList);
            }
        }
        return unSpuIds;
    }

    
}
