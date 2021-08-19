package com.wanmi.sbc.job;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoModifyAddedStatusRequest;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoModifyAddedStatusRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoPageRequest;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 定时任务Handler
 * 商品定时上架
 *
 * @author dyt
 */
@JobHandler(value = "goodsAddedTimingJobHandler")
@Component
@Slf4j
public class GoodsAddedTimingJobHandler extends IJobHandler {

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("更新spu定时上架");
        // 每小时执行 0 1 0/1 * * ?
        GoodsInfoPageRequest pageRequest = GoodsInfoPageRequest.builder().addedTimingFlag(Boolean.TRUE)
                .delFlag(DeleteFlag.NO.toValue()).addedFlag(AddedFlag.NO.toValue()).build();
        pageRequest.setPageSize(500);
        //第1页商品
        pageRequest.setPageNum(0);
        MicroServicePage<GoodsInfoVO> goodsPages = goodsInfoQueryProvider.page(pageRequest).getContext().getGoodsInfoPage();
        if (CollectionUtils.isEmpty(goodsPages.getContent())) {
            return SUCCESS;
        }
        this.added(goodsPages.getContent());
        Integer pageCount = goodsPages.getTotalPages();
        if (pageCount <= 1) {
            return SUCCESS;
        }
        //第2页开始
        for (int i = 1; i < pageCount; i++) {
            MicroServicePage<GoodsInfoVO> tmpGoodsPages = goodsInfoQueryProvider.page(pageRequest).getContext().getGoodsInfoPage();
            if (CollectionUtils.isNotEmpty(tmpGoodsPages.getContent())) {
                this.added(tmpGoodsPages.getContent());
            }
        }
        return SUCCESS;
    }


    //上架
    private void added(List<GoodsInfoVO> goodsInfoList){
        List<String> goodsIds = goodsInfoList.stream().map(GoodsInfoVO::getGoodsInfoId).distinct().collect(Collectors.toList());
        goodsInfoProvider.modifyAddedStatus(GoodsInfoModifyAddedStatusRequest.builder().addedFlag(AddedFlag.YES.toValue())
                        .goodsInfoIds(goodsIds)
                        .build());
        esGoodsInfoElasticProvider.updateAddedStatus(EsGoodsInfoModifyAddedStatusRequest.builder().
                        addedFlag(AddedFlag.YES.toValue()).goodsIds(null).goodsInfoIds(goodsIds).build());
    }
}
