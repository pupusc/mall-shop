package com.wanmi.sbc.job;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsAtmosphereRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsCateSyncVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSyncVO;
import com.wanmi.sbc.setting.api.provider.AtmosphereProvider;
import com.wanmi.sbc.setting.api.request.AtmosphereQueryRequest;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@JobHandler(value = "GoodsAtmosphereInitJobHandler")
@Component
@Slf4j
public class GoodsAtmosphereInitJobHandler extends IJobHandler {

    @Autowired
    private AtmosphereProvider atmosphereProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Override
    public ReturnT<String> execute(String params) throws Exception {
        log.info("=====氛围同步start======");
        AtmosphereQueryRequest request = new AtmosphereQueryRequest();
        BaseResponse<MicroServicePage<AtmosphereDTO>> atmosList = atmosphereProvider.page(request);
        if(atmosList == null || atmosList.getContext() ==null || CollectionUtils.isEmpty(atmosList.getContext().getContent())){
            log.info("没有未同步的氛围信息");
            return SUCCESS;
        }
        List<String> skuIds = atmosList.getContext().getContent().stream().map(AtmosphereDTO::getSkuId).collect(Collectors.toList());
        EsGoodsAtmosphereRequest esGoodsAtmosphereRequest = new EsGoodsAtmosphereRequest();
        esGoodsAtmosphereRequest.setGoodsInfoIds(skuIds);
        esGoodsInfoElasticProvider.adjustAtmosphere(esGoodsAtmosphereRequest);
        //更新状态
        log.info("=====氛围同步end======");
        return SUCCESS;
    }
}
