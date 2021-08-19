package com.wanmi.sbc.job;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreListByStoreIdsRequest;
import com.wanmi.sbc.customer.api.response.store.StoreListByStoreIdsResponse;
import com.wanmi.sbc.goods.api.provider.distributor.goods.DistributorGoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.distributor.goods.DistributorGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoDeleteByStoreIdsRequest;
import com.wanmi.sbc.goods.api.response.distributor.goods.DistributorGoodsInfoListAllStoreIdResponse;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务Handler
 * 根据分销设置的奖励规则补发邀请奖励
 */
@JobHandler(value = "distributorGoodsJobHandler")
@Component
@Slf4j
public class DistributorGoodsJobHandler extends IJobHandler {

	@Autowired
	private DistributorGoodsInfoProvider distributorGoodsInfoProvider;

	@Autowired
	private DistributorGoodsInfoQueryProvider distributorGoodsInfoQueryProvider;

	@Autowired
	private StoreQueryProvider storeQueryProvider;

    @Override
    @Transactional
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("DistributorGoodsTask定时任务执行开始： " + LocalDateTime.now());
        BaseResponse<DistributorGoodsInfoListAllStoreIdResponse>  baseResponse = distributorGoodsInfoQueryProvider.findAllStoreId();
        List<Long> list = baseResponse.getContext().getList();
        if (CollectionUtils.isEmpty(list)){
            XxlJobLogger.log("DistributorGoodsTask定时任务执行结束,分销员商品表------店铺ID集合大小为0 " + LocalDateTime.now());
            return SUCCESS;
        }
        BaseResponse<StoreListByStoreIdsResponse> storeIdsResponseBaseResponse = storeQueryProvider.listByStoreIds(new StoreListByStoreIdsRequest(list));
        list = storeIdsResponseBaseResponse.getContext().getLongList();
        if (CollectionUtils.isEmpty(list)){
            XxlJobLogger.log("DistributorGoodsTask定时任务执行结束,分销员商品表------已过期的店铺ID集合大小为0 " + LocalDateTime.now());
            return SUCCESS;
        }
        BaseResponse response = distributorGoodsInfoProvider.deleteByStoreIds(new DistributorGoodsInfoDeleteByStoreIdsRequest(list));
        XxlJobLogger.log("DistributorGoodsTask定时任务执行结束： " + LocalDateTime.now() + ",处理总数为：" + response.getContext());
        return SUCCESS;
    }


}
