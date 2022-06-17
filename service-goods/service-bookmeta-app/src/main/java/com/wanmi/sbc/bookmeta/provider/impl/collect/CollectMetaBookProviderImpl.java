package com.wanmi.sbc.bookmeta.provider.impl.collect;

import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaBookResp;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.provider.collect.CollectMetaBookProvider;
import com.wanmi.sbc.bookmeta.service.collect.CollectMetaAwardService;
import com.wanmi.sbc.bookmeta.service.collect.CollectMetaBookClumpService;
import com.wanmi.sbc.bookmeta.service.collect.CollectMetaBookContentService;
import com.wanmi.sbc.bookmeta.service.collect.CollectMetaBookGroupService;
import com.wanmi.sbc.bookmeta.service.collect.CollectMetaBookLabelService;
import com.wanmi.sbc.bookmeta.service.collect.CollectMetaBookProducerService;
import com.wanmi.sbc.bookmeta.service.collect.CollectMetaBookPublisherService;
import com.wanmi.sbc.bookmeta.service.collect.CollectMetaBookService;
import com.wanmi.sbc.bookmeta.service.collect.CollectMetaFigureRelService;
import com.wanmi.sbc.bookmeta.service.collect.CollectMetaFigureService;
import com.wanmi.sbc.bookmeta.service.collect.CollectMetaLabelService;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 采集图书信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/17 3:04 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectMetaBookProviderImpl implements CollectMetaBookProvider {

    @Autowired
    private CollectMetaAwardService collectMetaAwardService;

    @Autowired
    private CollectMetaBookClumpService collectMetaBookClumpService;

    @Autowired
    private CollectMetaBookContentService collectMetaBookContentService;

    @Autowired
    private CollectMetaBookGroupService collectMetaBookGroupService;

    @Autowired
    private CollectMetaBookLabelService collectMetaBookLabelService;

    @Autowired
    private CollectMetaBookProducerService collectMetaBookProducerService;

    @Autowired
    private CollectMetaBookPublisherService collectMetaBookPublisherService;

    @Autowired
    private CollectMetaBookService collectMetaBookService;

    @Autowired
    private CollectMetaFigureService collectMetaFigureService;

    @Autowired
    private CollectMetaFigureRelService collectMetaFigureRelService;

    @Autowired
    private CollectMetaLabelService collectMetaLabelService;

    @Override
    public BaseResponse<CollectMetaResp> collectMetaAwardByTime(CollectMetaReq collectMetaReq) {
        return BaseResponse.success(collectMetaAwardService.collectMetaAwardByTime(collectMetaReq));
    }

    @Override
    public BaseResponse<CollectMetaResp> collectMetaBookClumpByTime(CollectMetaReq collectMetaReq) {
        return BaseResponse.success(collectMetaBookClumpService.collectMetaBookClumpByTime(collectMetaReq));
    }

    @Override
    public BaseResponse<CollectMetaResp> collectMetaBookContentByTime(CollectMetaReq collectMetaReq) {
        return BaseResponse.success(collectMetaBookContentService.collectMetaBookContentByTime(collectMetaReq));
    }

    @Override
    public BaseResponse<CollectMetaResp> collectMetaBookGroupByTime(CollectMetaReq collectMetaReq) {
        return BaseResponse.success(collectMetaBookGroupService.collectMetaBookGroupByTime(collectMetaReq));
    }

    @Override
    public BaseResponse<CollectMetaResp> collectMetaBookLabelByTime(CollectMetaReq collectMetaReq) {
        return BaseResponse.success(collectMetaBookLabelService.collectMetaBookLabelByTime(collectMetaReq));
    }

    @Override
    public BaseResponse<CollectMetaResp> collectMetaBookProducerByTime(CollectMetaReq collectMetaReq) {
        return BaseResponse.success(collectMetaBookProducerService.collectMetaBookProducerByTime(collectMetaReq));
    }

    @Override
    public BaseResponse<CollectMetaResp> collectMetaBookPublisherByTime(CollectMetaReq collectMetaReq) {
        return BaseResponse.success(collectMetaBookPublisherService.collectMetaBookPublisherByTime(collectMetaReq));
    }

    @Override
    public BaseResponse<CollectMetaResp> collectMetaBookByTime(CollectMetaReq collectMetaReq) {
        return BaseResponse.success(collectMetaBookService.collectMetaBookByTime(collectMetaReq));
    }

    @Override
    public BaseResponse<CollectMetaResp> collectMetaFigureRelByTime(CollectMetaReq collectMetaReq) {
        return BaseResponse.success(collectMetaFigureRelService.collectMetaFigureRelByTime(collectMetaReq));
    }

    @Override
    public BaseResponse<CollectMetaResp> collectMetaFigureByTime(CollectMetaReq collectMetaReq) {
        return BaseResponse.success(collectMetaFigureService.collectMetaFigureByTime(collectMetaReq));
    }

    @Override
    public BaseResponse<CollectMetaResp> collectMetaLabelByTime(CollectMetaReq collectMetaReq) {
        return BaseResponse.success(collectMetaLabelService.collectMetaLabelByTime(collectMetaReq));
    }

    @Override
    public BaseResponse<List<CollectMetaBookResp>> collectMetaBook(CollectMetaReq collectMetaReq) {
        return BaseResponse.success(collectMetaBookService.collectMetaBook(collectMetaReq));
    }
}
