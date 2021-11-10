package com.wanmi.sbc.points;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.provider.sku.EsSkuQueryProvider;
import com.wanmi.sbc.elastic.api.request.sku.EsSkuPageRequest;
import com.wanmi.sbc.elastic.api.response.sku.EsSkuPageResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.points.PointsExchangeProvider;
import com.wanmi.sbc.marketing.api.request.points.PointsExchangeAcitvityAddRequest;
import com.wanmi.sbc.marketing.api.request.points.PointsExchangePageRequest;
import com.wanmi.sbc.marketing.bean.vo.PointsExchangeActivityGoodsVO;
import com.wanmi.sbc.marketing.bean.vo.PointsExchangeActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = "PointsController", description = "积分兑换服务API")
@RestController
@RequestMapping("/point/exchange")

public class PointsExchangeController {

    @Autowired
    private PointsExchangeProvider pointsExchangeProvider;

    @Autowired
    private EsSkuQueryProvider esSkuQueryProvider;

    /**
     * @description 新增积分兑换活动
     * @menu 积分兑换
     * @param request
     * @status undone
     */

    @ApiOperation(value = "新增积分兑换活动")
    @PostMapping(value = "/add")
    public  BaseResponse add(@RequestBody @Valid PointsExchangeAcitvityAddRequest request){
        return pointsExchangeProvider.add(request);
    }

    /**
     * @description 积分兑换活动列表
     * @menu 积分兑换
     * @param request
     * @status undone
     */
    @ApiOperation(value = "积分兑换活动列表")
    @PostMapping(value = "/list")
    public BaseResponse<MicroServicePage<PointsExchangeActivityVO>> page(@RequestBody PointsExchangePageRequest request){
        return pointsExchangeProvider.page(request);
    }

    /**
     * @description 积分兑换活动详情
     * @menu 积分兑换
     * @param id
     * @status undone
     */
    @ApiOperation(value = "详情")
    @GetMapping(value = "/detail")
    public BaseResponse<PointsExchangeActivityVO> detail(@RequestParam("id")Integer id){
        BaseResponse<PointsExchangeActivityVO> activityVO =  pointsExchangeProvider.detail(id);
        if(activityVO == null || activityVO.getContext() ==null || CollectionUtils.isEmpty(activityVO.getContext().getSkus())){
            return activityVO;
        }
        List<String> skuNos = activityVO.getContext().getSkus().stream().map(PointsExchangeActivityGoodsVO::getSkuNo).collect(Collectors.toList());
        EsSkuPageRequest esSkuPageRequest = new EsSkuPageRequest();
        esSkuPageRequest.setPageSize(1000);
        esSkuPageRequest.setGoodsInfoNos(skuNos);
        EsSkuPageResponse response = esSkuQueryProvider.page(esSkuPageRequest).getContext();
        if(response !=null && CollectionUtils.isNotEmpty(response.getGoodsInfoPage().getContent())){
            activityVO.getContext().getSkus().forEach(p->{
                Optional<GoodsInfoVO> goodsInfoVO = response.getGoodsInfoPage().getContent().stream().filter(s->s.getGoodsInfoNo().equals(p.getSkuNo())).findFirst();
                if(goodsInfoVO.isPresent()){
                    p.setSkuName(goodsInfoVO.get().getGoodsInfoName());
                }
            });
        }
        return  activityVO;
    }


    /**
     * @description 积分兑换活动修改
     * @menu 积分兑换
     * @param request
     * @status undone
     */
    @ApiOperation(value = "修改")
    @PostMapping(value = "/modify")
    BaseResponse modify(@RequestBody @Valid PointsExchangeAcitvityAddRequest request){
        return pointsExchangeProvider.modify(request);
    }


    /**
     * @description 积分兑换活动暂停
     * @menu 积分兑换
     * @param id
     * @status undone
     */
    @ApiOperation(value = "暂停活动")
    @PostMapping(value = "/pause")
    BaseResponse pause(@RequestParam("id")Integer id){
        return pointsExchangeProvider.pause(id);
    }


}
