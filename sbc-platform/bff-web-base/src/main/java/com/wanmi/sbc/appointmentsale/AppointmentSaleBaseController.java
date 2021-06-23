package com.wanmi.sbc.appointmentsale;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.appointmentsale.service.AppointmentSaleService;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.appointmentrecord.AppointmentRecordQueryProvider;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordPageCriteriaRequest;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordPageRequest;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordQueryRequest;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleIsInProgressRequest;
import com.wanmi.sbc.goods.api.request.appointmentsale.RushToAppointmentSaleGoodsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.order.api.response.appointmentrecord.AppointmentRecordPageCriteriaResponse;
import com.wanmi.sbc.order.api.response.appointmentrecord.AppointmentRecordResponse;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleIsInProcessResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.order.bean.dto.AppointmentQueryDTO;
import com.wanmi.sbc.goods.bean.vo.AppointmentRecordVO;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.mq.appointment.RushToAppointmentSaleGoodsMqService;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Api(description = "预约抢购webAPI", tags = "AppointmentSaleBaseController")
@RestController
@RequestMapping(value = "/appointmentsale")
public class AppointmentSaleBaseController {

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    FlashSaleGoodsQueryProvider flashSaleGoodsQueryProvider;

    @Autowired
    private AppointmentSaleService appointmentSaleService;

    @Autowired
    private AppointmentRecordQueryProvider appointmentRecordQueryProvider;

    @Autowired
    private RushToAppointmentSaleGoodsMqService rushToAppointmentSaleGoodsMqService;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    /**
     * @param goodsInfoId
     * @Description: 商品是否正在预购活动中
     */
    @ApiOperation(value = "商品是否正在预购活动中")
    @GetMapping("/{goodsInfoId}/isInProgress")
    public BaseResponse<AppointmentSaleIsInProcessResponse> isInProgress(@PathVariable String goodsInfoId) {
        AppointmentSaleIsInProcessResponse response = appointmentSaleQueryProvider.isInProgress(AppointmentSaleIsInProgressRequest.builder().goodsInfoId(goodsInfoId).build()).getContext();
        return BaseResponse.success(response);
    }

    /**
     * @param goodsInfoId
     * @Description: 判断用户是否预约
     */
    @ApiOperation(value = "判断用户是否预约")
    @GetMapping("/{goodsInfoId}/isSubscriptionFlag")
    public BaseResponse<Boolean> isSubscriptionFlag(@PathVariable String goodsInfoId) {
        AppointmentSaleIsInProcessResponse response = appointmentSaleQueryProvider.isInProgress(AppointmentSaleIsInProgressRequest.builder().goodsInfoId(goodsInfoId).build()).getContext();

        if (Objects.isNull(response) || Objects.isNull(response.getAppointmentSaleVO())) {
            return BaseResponse.success(false);
        }
        AppointmentSaleVO appointmentSaleVO = response.getAppointmentSaleVO();
        AppointmentRecordResponse recordResponse = appointmentRecordQueryProvider.getAppointmentInfo(AppointmentRecordQueryRequest.builder().buyerId(commonUtil.getCustomer().getCustomerId())
                .goodsInfoId(appointmentSaleVO.getAppointmentSaleGood().getGoodsInfoId()).appointmentSaleId(appointmentSaleVO.getId()).build()).getContext();
        if (Objects.nonNull(recordResponse) && Objects.nonNull(recordResponse.getAppointmentRecordVO())) {
            return BaseResponse.success(true);
        } else {
            return BaseResponse.success(false);
        }
    }


    @ApiOperation(value = "立即预约")
    @PostMapping("/rushToAppointmentGoods")
    @MultiSubmit
    public BaseResponse rushToAppointmentGoods(@RequestBody @Valid RushToAppointmentSaleGoodsRequest request) {
        request.setCustomerId(commonUtil.getCustomer().getCustomerId());
        //预约商品对应限制条件判断
        appointmentSaleService.judgeAppointmentGoodsCondition(request);
        //发送mq消息异步处理同步资源
        rushToAppointmentSaleGoodsMqService.rushToAppointmentSaleGoodsMq(JSONObject.toJSONString(request));
        return BaseResponse.SUCCESSFUL();
    }


    /*@ApiOperation(value = "立即抢购")
    @PostMapping("/rushToAppointmentSaleGoods")
    public BaseResponse rushToAppointmentSaleGoods(@RequestBody @Valid RushToAppointmentSaleGoodsRequest request) {
        request.setCustomerId(commonUtil.getCustomer().getCustomerId());
        //抢购商品对应限制条件判断
        appointmentSaleService.judgeAppointmentSaleGoodsCondition(request);
        //发送mq消息异步处理抢购信息
        rushToAppointmentSaleGoodsMqService.rushToSaleGoodsMq(JSONObject.toJSONString(request));
        return BaseResponse.SUCCESSFUL();
    }


    @ApiOperation(value = "获取抢购活动详情")
    @PostMapping("/getAppointmentSaleInfo")
    public BaseResponse<AppointmentSaleByIdResponse> getAppointmentSaleInfo(@RequestBody @Valid RushToAppointmentSaleGoodsRequest request) {
        return BaseResponse.success(appointmentSaleService.getAppointmentSaleInfo(request));
    }


    @ApiOperation(value = "获取是否抢购资格")
    @PostMapping("/getAppointmentSaleGoodsQualifications")
    public BaseResponse getAppointmentSaleGoodsQualifications(@RequestBody @Valid RushToAppointmentSaleGoodsRequest request) {
        request.setCustomerId(commonUtil.getCustomer().getCustomerId());
        return BaseResponse.success(appointmentSaleService.getAppointmentSaleGoodsQualifications(request));
    }
*/

    @ApiOperation(value = "我的预约")
    @PostMapping("/appointmentSalePage")
    public BaseResponse<AppointmentRecordPageCriteriaResponse> appointmentSalePage(@RequestBody @Valid AppointmentRecordPageRequest request) {
        request.setBuyerId(commonUtil.getCustomer().getCustomerId());
        AppointmentQueryDTO appointmentQueryDTO = AppointmentQueryDTO.builder().buyerId(commonUtil.getCustomer().getCustomerId()).build();
        appointmentQueryDTO.setPageSize(request.getPageSize());
        appointmentQueryDTO.setPageNum(request.getPageNum());
        AppointmentRecordPageCriteriaResponse response = appointmentRecordQueryProvider.pageCriteria(AppointmentRecordPageCriteriaRequest.builder().appointmentQueryDTO(appointmentQueryDTO).build()).getContext();
        if (Objects.isNull(response) || Objects.isNull(response.getAppointmentRecordPage()) || CollectionUtils.isEmpty(response.getAppointmentRecordPage().getContent())) {
            return BaseResponse.success(response);
        }
        List<AppointmentRecordVO> appointmentRecordVOS = response.getAppointmentRecordPage().getContent();

        List<String> skuIds = new ArrayList<>(appointmentRecordVOS.stream().collect(Collectors.groupingBy(AppointmentRecordVO::getGoodsInfoId)).keySet());

        GoodsInfoViewByIdsResponse goodsInfoViewByIdsResponse = goodsInfoQueryProvider.listViewByIds(GoodsInfoViewByIdsRequest.builder().goodsInfoIds(skuIds).isHavSpecText(1).build()).getContext();
        Map<String, GoodsInfoVO> goodsInfoVOMap = goodsInfoViewByIdsResponse.getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, v -> v, (v1, v2) -> v1));
        Map<String, GoodsVO> goodsVOMap = goodsInfoViewByIdsResponse.getGoodses().stream().collect(Collectors.toMap(GoodsVO::getGoodsId, v -> v, (v1, v2) -> v1));
        appointmentRecordVOS.forEach(a -> {
            if (goodsInfoVOMap.containsKey(a.getGoodsInfoId())) {
                a.setGoodsInfo(goodsInfoVOMap.get(a.getGoodsInfoId()));
            }
            if (goodsVOMap.containsKey(a.getAppointmentSaleGoodsInfo().getGoodsId())) {
                a.setGoods(goodsVOMap.get(a.getAppointmentSaleGoodsInfo().getGoodsId()));
            }
        });
        return BaseResponse.success(response);
    }


}
