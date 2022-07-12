package com.soybean.marketing.api.provider.activity;

import com.soybean.common.resp.CommonPageResp;
import com.soybean.marketing.api.req.NormalActivityPointSkuReq;
import com.soybean.marketing.api.req.NormalActivitySearchReq;
import com.soybean.marketing.api.resp.NormalActivityResp;
import com.soybean.marketing.api.resp.NormalActivitySkuResp;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 2:34 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@FeignClient(value = "${application.marketing.name}", contextId = "NormalActivityPointSkuProvider")
public interface NormalActivityPointSkuProvider {


    /**
     * 新增活动
     * @param normalActivityPointSkuReq
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/normal-activity/point-sku/add")
    BaseResponse add(@RequestBody @Validated NormalActivityPointSkuReq normalActivityPointSkuReq);

    /**
     * 修改活动
     * @param normalActivityPointSkuReq
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/normal-activity/point-sku/update")
    BaseResponse update(@RequestBody @Validated NormalActivityPointSkuReq normalActivityPointSkuReq);


    /**
     * 获取活动列表
     * @param normalActivitySearchReq
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/normal-activity/point-sku/list")
    BaseResponse<CommonPageResp<List<NormalActivityResp>>> list(@RequestBody NormalActivitySearchReq normalActivitySearchReq);


    /**
     * 开启/关闭
     * @param activityId
     * @param isOpen
     * @return
     */
    @GetMapping("/marketing/${application.marketing.version}/normal-activity/point-sku/publish/{activityId}/{isOpen}")
    BaseResponse publish(@PathVariable("activityId") Integer activityId, @PathVariable("isOpen") Boolean isOpen);

    /**
     * 获取活动下的积分活动商品列表
     * @param activityId
     * @return
     */
    @GetMapping("/marketing/${application.marketing.version}/normal-activity/point-sku/listActivityPointSku/{activityId}")
    BaseResponse<List<NormalActivitySkuResp>> listActivityPointSku(@PathVariable("activityId") Integer activityId);
}
