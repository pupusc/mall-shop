package com.wanmi.sbc.job;

import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListCacheProviderRequest;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/23 7:52 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@JobHandler(value = "GoodsBlackListJobHandler")
@Component
@Slf4j
public class GoodsBlackListJobHandler extends IJobHandler{

    @Autowired
    private GoodsBlackListProvider goodsBlackListProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        Set<Integer> businessCateGoryColl = new HashSet<>();
        if (StringUtils.isEmpty(param)) {
            String[] split = param.split(",");
            for (String s : split) {
                if (StringUtils.isEmpty(s)) {
                    continue;
                }
                businessCateGoryColl.add(Integer.parseInt(s));
            }
        }
        GoodsBlackListCacheProviderRequest goodsBlackListCacheProviderRequest = new GoodsBlackListCacheProviderRequest();
        goodsBlackListCacheProviderRequest.setBusinessCategoryColl(businessCateGoryColl);
        goodsBlackListProvider.flushBlackListCache(goodsBlackListCacheProviderRequest);
        return ReturnT.SUCCESS;
    }
}
