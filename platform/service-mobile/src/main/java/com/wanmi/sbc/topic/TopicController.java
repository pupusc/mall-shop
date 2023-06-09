package com.wanmi.sbc.topic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.bookmeta.bo.GoodsNameBySpuIdBO;
import com.wanmi.sbc.bookmeta.provider.GoodsSearchKeyProvider;
import com.wanmi.sbc.collectFactory.CollectSkuIdFactory;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.index.RefreshConfig;
import com.wanmi.sbc.order.api.request.stockAppointment.AppointmentRequest;
import com.wanmi.sbc.order.request.AppointmentStockRequest;
import com.wanmi.sbc.setting.api.request.RankPageRequest;
import com.wanmi.sbc.setting.api.request.RankStoreyRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.MixedComponentContentRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyColumnQueryRequest;
import com.wanmi.sbc.setting.bean.dto.MixedComponentDto;
import com.wanmi.sbc.task.*;
import com.wanmi.sbc.topic.request.GoodsSearchBySpuIdRequest;
import com.wanmi.sbc.topic.response.GoodsSearchBySpuIdResponse;
import com.wanmi.sbc.topic.response.TopicResponse;
import com.wanmi.sbc.topic.service.TopicService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.DitaUtil;
import com.wanmi.sbc.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @menu 专题
 * @tag topic
 * @status undone
 */

@Api(tags = "TopicController", description = "专题")
@RestController
@RequestMapping("/topic")
@Slf4j
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private RefreshConfig refreshConfig;

    @Autowired
    private CommonUtil commonUtil;


    /**
     * @description 根据JWT TOKEN跳转新老版本首页
     * @menu 专题
     * @param
     * @status undone
     */
    @ApiOperation(value = "首页路由")
    @PostMapping(value = "/v2/page")
    public BaseResponse pageV2(){
        BaseResponse resp = BaseResponse.SUCCESSFUL();
        resp.setContext(topicService.routeIndexTemp());
        return resp;
    }

    /**
     * @description 根据专题id返回页面数据
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation(value = "根据专题id返回页面数据，")
    @PostMapping(value = "detail")
    public BaseResponse<TopicResponse> detail(@RequestBody TopicQueryRequest request) {
        return topicService.detail(request,true);
    }

    @ApiOperation(value = "根据专题id返回数据，第一次加载只返回1，2楼层数据信息")
    @PostMapping(value = "/headTopic")
    public BaseResponse<TopicResponse> storey(@RequestBody TopicQueryRequest request) {
        return topicService.detail(request,false);
    }

    /**
     * @首页~刷新redis
     */
    @PostMapping(value = "/v2/refresRedis")
    public BaseResponse refresRedis() {

        new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println("redis~begin:" + DitaUtil.getCurrentAllDate());
                    topicService.refresRedis();
                    System.out.println("redis~end  :" + DitaUtil.getCurrentAllDate());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @description 根据专题id返回页面数据_V2
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation(value = "根据专题id返回页面数据，")
    @PostMapping(value = "/v2/detail")
    public BaseResponse<TopicResponse> detailV2(@RequestBody TopicQueryRequest request) {
        System.out.println("detailV2~begin:" + DitaUtil.getCurrentAllDate());
        BaseResponse<TopicResponse> response = topicService.detailV2(request,true);
        System.out.println("detailV2~  end:" + DitaUtil.getCurrentAllDate());
        return response;
    }

    @ApiOperation(value = "根据专题id返回数据，第一次加载只返回1，2楼层数据信息")
    @PostMapping(value = "/v2/headTopic")
    public BaseResponse<TopicResponse> storeyV2(@RequestBody TopicQueryRequest request) {
        BaseResponse<TopicResponse> response = topicService.detailV2(request,false);
        return response;
    }

    @Autowired
    private CollectSkuIdFactory collectSkuIdFactory;

    @Autowired
    private CollectSkuIdJobHandler collectSkuIdJobHandler;

    @Autowired
    private RankPageJobHandler rankPageJobHandler;
    @Autowired
    private NewRankJobHandler rankJobHandler;

    @ApiOperation(value = "榜单聚合页")
    @PostMapping(value = "/v2/rankPage")
    public BaseResponse<RankPageRequest> rankPage(@RequestBody RankStoreyRequest request) throws Exception {
//        BaseResponse<RankPageRequest> response = topicService.rankPage(request);

//        homeIndexGoodsJobHandler.execute("H5,MINIPROGRAM");
        rankJobHandler.execute("618");
//        rankPageJobHandler.execute("7ffffe79444084fa6c7ce890988eb95b");
//        newBookPointJobHandler.execute(null);

//        collectSkuIdFactory.collectId();
//        collectSkuIdJobHandler.execute(null);
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "榜单聚合页")
    @PostMapping(value = "/v2/refreshRankPageRedis")
    public BaseResponse refreshRankPageRedis(@RequestParam(value = "topicKey") String topicKey) throws Exception {
        rankPageJobHandler.execute(topicKey);
        return BaseResponse.SUCCESSFUL();
    }

    @PostMapping(value = "/v2/getMixedComponentContent")
    public BaseResponse<List<MixedComponentDto>> getMixedComponentContent(@RequestBody MixedComponentContentRequest request) {
        return topicService.getMixedComponentContent(request.getTopicStoreyId(), request.getTabId(), request.getKeyWord(), null, request.getPageNum(), request.getPageSize());
    }

    @PostMapping(value = "/v2/saveMixedComponentContent")
    public BaseResponse<List<MixedComponentDto>> saveMixedComponentContent(@RequestBody MixedComponentContentRequest request) {
        topicService.saveMixedComponentContent(request);
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "榜单聚合页")
    @PostMapping(value = "/v2/rankPageV2")
    public BaseResponse<RankPageRequest> rankPageV2(@RequestBody RankStoreyRequest request) {
        BaseResponse<RankPageRequest> response = BaseResponse.success(topicService.rankPageByBookList(request));
        return response;
    }

    @Resource
    private GoodsSearchKeyProvider goodsSearchKeyProvider;
    @PostMapping("/v2/queryGoodsSearchKey")
    public BusinessResponse<List<GoodsSearchBySpuIdResponse>> getGoodsSearch(@RequestBody GoodsSearchBySpuIdRequest pageRequest) {
        List<GoodsNameBySpuIdBO> goodsNameBySpuId = goodsSearchKeyProvider.getGoodsNameBySpuId(pageRequest.getName());
        List<GoodsSearchBySpuIdResponse> goodsSearchBySpuIdRespVOS = KsBeanUtil.convertList(goodsNameBySpuId, GoodsSearchBySpuIdResponse.class);
        return BusinessResponse.success(goodsSearchBySpuIdRespVOS);
    }

    /**
     * 添加到货提醒
     * @param request
     * @return
     */
    @PostMapping(value = "/addAppointment")
    public BaseResponse addAppointment(@RequestBody AppointmentStockRequest request){
        return topicService.addAppointment(request);
    }

    /**
     * 取消到货提醒
     * @param request
     * @return
     */
    @PostMapping(value = "/deleteAppointment")
    public BaseResponse deleteAppointment(@RequestBody AppointmentStockRequest request){
        return topicService.deleteAppointment(request);
    }

    /**
     * 获取用户所有到货提醒
     * @return
     */
    @PostMapping(value = "/findAppointment")
    public BaseResponse<AppointmentRequest> findAppointment(){
        return topicService.findAppointment();
    }


    /**
     * 日志处理规范
     * @return
     */
    @PostMapping(value = "/testCommonLog")
    public BaseResponse<AppointmentRequest> testLog(@RequestBody AppointmentStockRequest request){
        return topicService.testLog(request);
    }


}
