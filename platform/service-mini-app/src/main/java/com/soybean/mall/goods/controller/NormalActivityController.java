package com.soybean.mall.goods.controller;

import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.goods.response.activity.PayAfterActivityResp;
import com.soybean.marketing.api.provider.activity.NormalActivityPointSkuProvider;
import com.soybean.marketing.api.req.SpuNormalActivityReq;
import com.soybean.marketing.api.resp.SkuNormalActivityResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.enums.StateEnum;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/22 2:15 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/normal-activity")
@RestController
@Slf4j
public class NormalActivityController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private NormalActivityPointSkuProvider normalActivityPointSkuProvider;

    /**
     * 获取商品进行中的活动
     *
     * @menu 返积分活动
     * @param skuId
     * @return
     */
    @PostMapping("/list/{skuId}")
    public BaseResponse listNormalActivity(@PathVariable("skuId") String skuId) {
        String customerId = StringUtils.isEmpty(commonUtil.getOperatorId()) ? "" : commonUtil.getOperatorId();
        SpuNormalActivityReq searchReq = new SpuNormalActivityReq();
        searchReq.setChannelTypes(Collections.singletonList(commonUtil.getTerminal().getCode()));
        searchReq.setSkuIds(Collections.singletonList(skuId));
        searchReq.setStatus(StateEnum.RUNNING.getCode());
        searchReq.setPublishState(PublishState.ENABLE.toValue());
        searchReq.setCustomerId(customerId);
        List<SkuNormalActivityResp> context = normalActivityPointSkuProvider.listSpuRunningNormalActivity(searchReq).getContext();

        //进行中的订单
        boolean isPrepare = false;
        if (CollectionUtils.isEmpty(context)){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endTime = now.plusDays(3);
            searchReq = new SpuNormalActivityReq();
            searchReq.setChannelTypes(Collections.singletonList(commonUtil.getTerminal().getCode()));
            searchReq.setSkuIds(Collections.singletonList(skuId));
            searchReq.setPublishState(PublishState.ENABLE.toValue());
            searchReq.setBeginTime(now);
            searchReq.setEndTime(endTime);
            searchReq.setCustomerId(customerId);
            context = normalActivityPointSkuProvider.listSpuRunningNormalActivity(searchReq).getContext();
            isPrepare = true;
        }

        List<PayAfterActivityResp> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(context)) {
            return BaseResponse.success(result);
        }

        for (SkuNormalActivityResp skuNormalActivityResp : context) {
            PayAfterActivityResp payAfterActivityResp = new PayAfterActivityResp();
            if (isPrepare) {
                String date = skuNormalActivityResp.getBeginTime().getMonthValue() + "月"
                        + skuNormalActivityResp.getBeginTime().getDayOfMonth() + "日 "
                        + skuNormalActivityResp.getBeginTime().getHour() + "点";
                payAfterActivityResp.setShowTitle(String.format("返积分预告：%s 下单返%s积分", date, skuNormalActivityResp.getNum()));
            } else {
                String beginTime = skuNormalActivityResp.getBeginTime().getMonthValue() + "."
                        + skuNormalActivityResp.getBeginTime().getDayOfMonth() + " "
                        + skuNormalActivityResp.getBeginTime().getHour() + "点";
                String endTime = skuNormalActivityResp.getEndTime().getMonthValue() + "."
                        + skuNormalActivityResp.getEndTime().getDayOfMonth() + " "
                        + skuNormalActivityResp.getEndTime().getHour() + "点";
                payAfterActivityResp.setShowTitle(String.format("下单返%s积分 %s-%s", skuNormalActivityResp.getNum(),beginTime, endTime));
            }
            payAfterActivityResp.setNormalActivityId(skuNormalActivityResp.getNormalActivityId());
            payAfterActivityResp.setNormalActivityName(skuNormalActivityResp.getName());

            result.add(payAfterActivityResp);
        }
        return BaseResponse.success(result);
    }
}
