package com.wanmi.sbc.job;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.thirdgoodscate.ThirdGoodsCateProvider;
import com.wanmi.sbc.goods.api.request.thirdgoodscate.UpdateAllRequest;
import com.wanmi.sbc.goods.bean.dto.ThirdGoodsCateDTO;
import com.wanmi.sbc.linkedmall.api.provider.cate.LinkedMallCateQueryProvider;
import com.wanmi.sbc.linkedmall.bean.vo.LinkedMallGoodsCateVO;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 全量同步linkedmall商品类目
 */
@Component
@Slf4j
@JobHandler(value = "LinkedMallGoodsCateSyncHandler")
public class LinkedMallGoodsCateSyncHandler extends IJobHandler {
    @Autowired
    private LinkedMallCateQueryProvider linkedMallCateQueryProvider;

    @Autowired
    private ThirdGoodsCateProvider thirdGoodsCateProvider;

    @Autowired
    private ThirdPlatformConfigQueryProvider thirdPlatformConfigQueryProvider;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
            List<LinkedMallGoodsCateVO> linkedMallGoodsCateVOS = linkedMallCateQueryProvider.getAllLinkedMallCate().getContext().getLinkedMallGoodsCateVOS();
            List<ThirdGoodsCateDTO> thirdGoodsCateDTOS = KsBeanUtil.convert(linkedMallGoodsCateVOS, ThirdGoodsCateDTO.class);
            UpdateAllRequest updateAllRequest = new UpdateAllRequest();
            updateAllRequest.setThirdGoodsCateDTOS(thirdGoodsCateDTOS);
            thirdGoodsCateProvider.updateAll(updateAllRequest);
            return ReturnT.SUCCESS;
    }
}
