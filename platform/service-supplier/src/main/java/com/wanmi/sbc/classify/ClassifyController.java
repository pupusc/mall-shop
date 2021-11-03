package com.wanmi.sbc.classify;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.classify.request.TradeRequest;
import com.wanmi.sbc.classify.response.ClassifyResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsDeleteStoreCateRequest;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.request.classify.ClassifyProviderRequest;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.wanmi.sbc.order.api.request.trade.TradePushRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/7 7:10 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/classify")
@RestController
public class ClassifyController {

    @Autowired
    private ClassifyProvider classifyProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private CommonUtil commonUtil;


    @PostMapping("/addProviderTrade")
    public BaseResponse addProviderTrade(@RequestBody TradeRequest request) {
        tradeProvider.addProviderTrade(request.getOid(), request.getUserId());
        return BaseResponse.SUCCESSFUL();
    }


    @PostMapping("/pushOrderToERP")
    public BaseResponse pushOrderToERP(@RequestBody TradePushRequest request) {
        tradeProvider.pushOrderToERP(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 新增分类
     * @menu 后台CMS2.0
     * @param classifyProviderRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse add(@RequestBody ClassifyProviderRequest classifyProviderRequest) {
        classifyProvider.add(classifyProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 修改店铺分类
     * @menu 后台CMS2.0
     * @param classifyProviderRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse update(@RequestBody ClassifyProviderRequest classifyProviderRequest) {
        classifyProvider.update(classifyProviderRequest);
        if(classifyProviderRequest.getClassifyName() != null){
            esGoodsInfoElasticProvider.updateClassify(classifyProviderRequest.getId(), classifyProviderRequest.getClassifyName());
        }
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 删除店铺分类
     * @menu 后台CMS2.0
     * @return
     */
    @GetMapping("/delete/{classifyId}")
    public BaseResponse delete(@PathVariable("classifyId") Integer classifyId) {
        ClassifyProviderRequest classifyProviderRequest = new ClassifyProviderRequest();
        classifyProviderRequest.setId(classifyId);
        classifyProvider.delete(classifyProviderRequest);
        esGoodsInfoElasticProvider.delClassify(classifyId);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     *
     * 类目列表
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @GetMapping("/listClassify")
    public BaseResponse<List<ClassifyResponse>> listClassify() {
        BaseResponse<List<ClassifyProviderResponse>> listClassifyResponse = classifyProvider.listClassify();
        String listClassifyJsonStr = JSON.toJSONString(listClassifyResponse.getContext());
        List<ClassifyResponse> result = JSON.parseArray(listClassifyJsonStr, ClassifyResponse.class);
        return BaseResponse.success(result);
    }
}
