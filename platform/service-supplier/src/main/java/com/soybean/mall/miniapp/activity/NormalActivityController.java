package com.soybean.mall.miniapp.activity;

import com.soybean.common.resp.CommonPageResp;
import com.soybean.mall.miniapp.activity.resp.NormalActivityDetailResp;
import com.soybean.mall.miniapp.activity.resp.NormalActivitySkuDetailResp;
import com.soybean.marketing.api.provider.activity.NormalActivityPointSkuProvider;
import com.soybean.marketing.api.req.NormalActivityPointSkuReq;
import com.soybean.marketing.api.req.NormalActivitySearchReq;
import com.soybean.marketing.api.resp.NormalActivityResp;
import com.soybean.marketing.api.resp.NormalActivitySkuResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 8:20 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/normal-activity")
@RestController
public class NormalActivityController {

    @Autowired
    private NormalActivityPointSkuProvider normalActivityPointSkuProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    /**
     * 新增活动
     * @menu 返积分活动
     * @param normalActivityPointSkuReq
     * @return
     */
    @PostMapping("/add")
    public BaseResponse add(@RequestBody @Validated NormalActivityPointSkuReq normalActivityPointSkuReq) {
        return normalActivityPointSkuProvider.add(normalActivityPointSkuReq);
    }


    /**
     * 修改活动
     * @menu 返积分活动
     * @param normalActivityPointSkuReq
     * @return
     */
    @PostMapping("/update")
    public BaseResponse update(@RequestBody @Validated NormalActivityPointSkuReq normalActivityPointSkuReq) {
        return normalActivityPointSkuProvider.update(normalActivityPointSkuReq);
    }

    /**
     * 活动列表
     * @menu 返积分活动
     * @param searchReq
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<CommonPageResp<List<NormalActivityResp>>> list(@RequestBody @Validated NormalActivitySearchReq searchReq) {
        return normalActivityPointSkuProvider.list(searchReq);
    }


    /**
     * 启用停用 true 启用 false停用
     * @menu 返积分活动
     * @param activityId
     * @param isOpen
     * @return
     */
    @GetMapping("/publish/{activityId}/{isOpen}")
    public BaseResponse publish(@PathVariable("activityId") Integer activityId, @PathVariable("isOpen") Boolean isOpen) {
        return normalActivityPointSkuProvider.publish(activityId, isOpen);
    }


    /**
     * 获取活动详细信息
     * @menu 返积分活动
     * @param activityId
     * @return
     */
    @GetMapping("/findById/{activityId}")
    public BaseResponse<NormalActivityDetailResp> findById(@PathVariable("activityId") Integer activityId) {
        NormalActivitySearchReq normalActivitySearchReq = new NormalActivitySearchReq();
        normalActivitySearchReq.setId(activityId);
        CommonPageResp<List<NormalActivityResp>> context = normalActivityPointSkuProvider.list(normalActivitySearchReq).getContext();
        if (CollectionUtils.isEmpty(context.getContent())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "活动不存在");
        }


        List<NormalActivitySkuResp> normalActivitySkuResps = normalActivityPointSkuProvider.listActivityPointSku(activityId).getContext();
        //获取商品信息
        if (CollectionUtils.isEmpty(normalActivitySkuResps)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品不存在");
        }

        NormalActivityDetailResp result = new NormalActivityDetailResp();
        NormalActivityResp normalActivityResp = context.getContent().get(0);
        BeanUtils.copyProperties(normalActivityResp, result);

        List<String> skuIdList = normalActivitySkuResps.stream().map(NormalActivitySkuResp::getSkuId).collect(Collectors.toList());
        //获取商品详细信息
        GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest = GoodsInfoViewByIdsRequest.builder().goodsInfoIds(skuIdList).deleteFlag(DeleteFlag.NO).build();
        GoodsInfoViewByIdsResponse goodsInfoViewByIdsResponse = goodsInfoQueryProvider.listSimpleView(goodsInfoViewByIdsRequest).getContext();
        List<GoodsInfoVO> goodsInfos = goodsInfoViewByIdsResponse.getGoodsInfos();
        Map<String, GoodsInfoVO> skuId2GoodsInfoVoMap =
                goodsInfos.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity(), (k1, k2) -> k1));

        List<NormalActivitySkuDetailResp> resultSkus = new ArrayList<>();
        for (NormalActivitySkuResp normalActivitySkuResp : normalActivitySkuResps) {

            NormalActivitySkuDetailResp normalActivitySkuDetailResp = new NormalActivitySkuDetailResp();
            BeanUtils.copyProperties(normalActivitySkuResp, normalActivitySkuDetailResp);
            GoodsInfoVO goodsInfoVO = skuId2GoodsInfoVoMap.get(normalActivitySkuResp.getSkuId());
            if (goodsInfoVO == null) {
                continue;
            }
            normalActivitySkuDetailResp.setSkuName(goodsInfoVO.getGoodsInfoName());
            normalActivitySkuDetailResp.setChannelTypes(goodsInfoVO.getGoodsChannelTypeSet().stream().map(Integer::parseInt).collect(Collectors.toList()));
            resultSkus.add(normalActivitySkuDetailResp);
        }
        result.setNormalActivitySkus(resultSkus);
        return BaseResponse.success(result);
    }
}
