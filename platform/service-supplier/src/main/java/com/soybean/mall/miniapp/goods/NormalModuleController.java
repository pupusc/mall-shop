package com.soybean.mall.miniapp.goods;

import com.soybean.common.resp.CommonPageResp;
import com.soybean.mall.miniapp.goods.resp.NormalModuleDetailResp;
import com.soybean.mall.miniapp.goods.resp.NormalModuleSkuDetailResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.provider.index.NormalModuleProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.index.NormalModuleReq;
import com.wanmi.sbc.goods.api.request.index.NormalModuleSearchReq;
import com.wanmi.sbc.goods.api.request.index.NormalModuleSkuSearchReq;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.index.NormalModuleResp;
import com.wanmi.sbc.goods.api.response.index.NormalModuleSkuResp;
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
 * Date       : 2022/7/12 1:16 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@RequestMapping("/normal-module")
@RestController
public class NormalModuleController {

    @Autowired
    private NormalModuleProvider normalModuleProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    /**
     * 栏目 新增
     * @menu 返积分活动
     * @param normalModuleReq
     * @return
     */
    @PostMapping("/add")
    public BaseResponse add(@RequestBody @Validated NormalModuleReq normalModuleReq) {
        return normalModuleProvider.add(normalModuleReq);
    }

    /**
     * 栏目 修改
     * @menu 返积分活动
     * @param normalModuleReq
     * @return
     */
    @PostMapping("/update")
    public BaseResponse update(@RequestBody @Validated NormalModuleReq normalModuleReq) {
        return normalModuleProvider.update(normalModuleReq);
    }

    /**
     * 栏目 列表
     * @menu 返积分活动
     * @param normalModuleSearchReq
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<CommonPageResp<List<NormalModuleResp>>> list(@RequestBody NormalModuleSearchReq normalModuleSearchReq) {
        return normalModuleProvider.list(normalModuleSearchReq);
    }


    /**
     *
     * 栏目 发布 true 开启 false 关闭
     * @menu 返积分活动
     */
    @GetMapping("/publish/{id}/{isOpen}")
    public BaseResponse publish(@PathVariable("id") Integer id, @PathVariable("isOpen") Boolean isOpen) {
        return normalModuleProvider.publish(id, isOpen);
    }

    /**
     * 栏目 获取详细信息
     * @menu 返积分活动
     * @param id
     * @return
     */
    @GetMapping("/findById/{id}")
    public BaseResponse<NormalModuleDetailResp> findById(@PathVariable("id") Integer id) {
        NormalModuleSearchReq normalModuleSearchReq = new NormalModuleSearchReq();
        normalModuleSearchReq.setId(id);
        CommonPageResp<List<NormalModuleResp>> context = normalModuleProvider.list(normalModuleSearchReq).getContext();
        if (CollectionUtils.isEmpty(context.getContent())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "信息不存在");
        }
        NormalModuleDetailResp normalModuleDetailResp = new NormalModuleDetailResp();

        NormalModuleResp normalModuleResp = context.getContent().get(0);
        BeanUtils.copyProperties(normalModuleResp, normalModuleDetailResp);

        //获取商品
        NormalModuleSkuSearchReq normalModuleSkuSearchReq = new NormalModuleSkuSearchReq();
        normalModuleSkuSearchReq.setNormalModuleId(normalModuleResp.getId());
        List<NormalModuleSkuResp> normalModuleSkuResps = normalModuleProvider.listNormalModuleSku(normalModuleSkuSearchReq).getContext();

        //获取商品信息
        if (CollectionUtils.isEmpty(normalModuleSkuResps)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品不存在");
        }
        List<String> skuIdList = normalModuleSkuResps.stream().map(NormalModuleSkuResp::getSkuId).collect(Collectors.toList());

        //获取商品详细信息
        GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest = GoodsInfoViewByIdsRequest.builder().goodsInfoIds(skuIdList).deleteFlag(DeleteFlag.NO).build();
        GoodsInfoViewByIdsResponse goodsInfoViewByIdsResponse = goodsInfoQueryProvider.listSimpleView(goodsInfoViewByIdsRequest).getContext();
        List<GoodsInfoVO> goodsInfos = goodsInfoViewByIdsResponse.getGoodsInfos();
        Map<String, GoodsInfoVO> skuId2GoodsInfoVoMap =
                goodsInfos.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity(), (k1, k2) -> k1));

        List<NormalModuleSkuDetailResp> resultSkus = new ArrayList<>();
        for (NormalModuleSkuResp normalModuleSkuResp : normalModuleSkuResps) {
            NormalModuleSkuDetailResp normalModuleSkuDetailResp = new NormalModuleSkuDetailResp();
            BeanUtils.copyProperties(normalModuleSkuResp, normalModuleSkuDetailResp);
            GoodsInfoVO goodsInfoVO = skuId2GoodsInfoVoMap.get(normalModuleSkuResp.getSkuId());
            if (goodsInfoVO == null) {
                continue;
            }
            normalModuleSkuDetailResp.setSkuName(goodsInfoVO.getGoodsInfoName());
            normalModuleSkuDetailResp.setChannelTypes(goodsInfoVO.getGoodsChannelTypeSet().stream().map(Integer::parseInt).collect(Collectors.toList()));
            normalModuleSkuDetailResp.setSpecText(goodsInfoVO.getSpecText());
            normalModuleSkuDetailResp.setMarketPrice(goodsInfoVO.getMarketPrice());
            resultSkus.add(normalModuleSkuDetailResp);
        }
        normalModuleDetailResp.setNormalModuleSkus(resultSkus);
        return BaseResponse.success(normalModuleDetailResp);
    }
}
