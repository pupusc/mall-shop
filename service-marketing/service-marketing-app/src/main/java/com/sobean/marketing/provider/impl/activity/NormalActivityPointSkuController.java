package com.sobean.marketing.provider.impl.activity;

import com.soybean.common.resp.CommonPageResp;
import com.soybean.marketing.activity.service.NormalActivityPointSkuService;
import com.soybean.marketing.activity.service.NormalActivityService;
import com.soybean.marketing.api.provider.activity.NormalActivityPointSkuProvider;
import com.soybean.marketing.api.req.NormalActivityPointSkuReq;
import com.soybean.marketing.api.req.NormalActivitySearchReq;
import com.soybean.marketing.api.resp.NormalActivityResp;
import com.soybean.marketing.api.resp.NormalActivitySkuResp;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 8:04 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Validated
@RestController
public class NormalActivityPointSkuController implements NormalActivityPointSkuProvider {

    @Autowired
    private NormalActivityPointSkuService normalActivityPointSkuService;


    @Override
    public BaseResponse add(NormalActivityPointSkuReq normalActivityPointSkuReq) {
        normalActivityPointSkuService.add(normalActivityPointSkuReq);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse update(NormalActivityPointSkuReq normalActivityPointSkuReq) {
        normalActivityPointSkuService.update(normalActivityPointSkuReq);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<CommonPageResp<List<NormalActivityResp>>> list(NormalActivitySearchReq normalActivitySearchReq) {
        return BaseResponse.success(normalActivityPointSkuService.list(normalActivitySearchReq));
    }

    @Override
    public BaseResponse publish(Integer activityId, Boolean isOpen) {
        normalActivityPointSkuService.publish(activityId, isOpen);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<List<NormalActivitySkuResp>> listActivityPointSku(Integer activityId) {
        return BaseResponse.success(normalActivityPointSkuService.listActivityPointSku(activityId));
    }
}
