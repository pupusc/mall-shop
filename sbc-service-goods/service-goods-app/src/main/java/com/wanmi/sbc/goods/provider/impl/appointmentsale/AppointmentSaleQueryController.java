package com.wanmi.sbc.goods.provider.impl.appointmentsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.*;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsQueryRequest;
import com.wanmi.sbc.goods.api.response.appointmentsale.*;
import com.wanmi.sbc.goods.appointmentsale.model.root.AppointmentSale;
import com.wanmi.sbc.goods.appointmentsale.model.root.AppointmentSaleDO;
import com.wanmi.sbc.goods.appointmentsale.service.AppointmentSaleService;
import com.wanmi.sbc.goods.appointmentsalegoods.model.root.AppointmentSaleGoods;
import com.wanmi.sbc.goods.appointmentsalegoods.service.AppointmentSaleGoodsService;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.goods.bookingsale.model.root.BookingSale;
import com.wanmi.sbc.goods.bookingsale.service.BookingSaleService;
import com.wanmi.sbc.goods.goodsrestrictedsale.service.GoodsRestrictedSaleService;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>预约抢购查询服务接口实现</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@RestController
@Validated
public class AppointmentSaleQueryController implements AppointmentSaleQueryProvider {
    @Autowired
    private AppointmentSaleService appointmentSaleService;

    @Autowired
    private BookingSaleService bookingSaleService;

    @Autowired
    private AppointmentSaleGoodsService appointmentSaleGoodsService;

    @Autowired
    private GoodsInfoService goodsInfoService;
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsRestrictedSaleService goodsRestrictedSaleService;

    @Override
    public BaseResponse<AppointmentSalePageResponse> page(@RequestBody @Valid AppointmentSalePageRequest appointmentSalePageReq) {
        AppointmentSaleQueryRequest queryReq = KsBeanUtil.convert(appointmentSalePageReq, AppointmentSaleQueryRequest.class);
        Page<AppointmentSale> appointmentSalePage = appointmentSaleService.page(queryReq);
        Page<AppointmentSaleVO> newPage = appointmentSalePage.map(entity -> appointmentSaleService.wrapperVo(entity));

        List<AppointmentSaleVO> appointmentSaleVOS = newPage.getContent();
        if (CollectionUtils.isEmpty(appointmentSaleVOS)) {
            MicroServicePage<AppointmentSaleVO> microPage = new MicroServicePage<>(newPage, appointmentSalePageReq.getPageable());
            AppointmentSalePageResponse finalRes = new AppointmentSalePageResponse(microPage);
            return BaseResponse.success(finalRes);
        }
        Map<Long, List<AppointmentSaleGoods>> saleGoodsMap = appointmentSaleGoodsService.list(AppointmentSaleGoodsQueryRequest.builder().
                appointmentSaleIdList(appointmentSaleVOS.stream().map(AppointmentSaleVO::getId).collect(Collectors.toList())).build()).stream().collect(Collectors.groupingBy(AppointmentSaleGoods::getAppointmentSaleId));
        appointmentSaleVOS.forEach(s -> {
            if (saleGoodsMap.containsKey(s.getId())) {
                s.setAppointmentCount(saleGoodsMap.get(s.getId()).stream().mapToInt(AppointmentSaleGoods::getAppointmentCount).sum());
                s.setBuyerCount(saleGoodsMap.get(s.getId()).stream().mapToInt(AppointmentSaleGoods::getBuyerCount).sum());
            }
            s.buildStatus();
        });
        MicroServicePage<AppointmentSaleVO> microPage = new MicroServicePage<>(newPage, appointmentSalePageReq.getPageable());
        AppointmentSalePageResponse finalRes = new AppointmentSalePageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<AppointmentSaleListResponse> list(@RequestBody @Valid AppointmentSaleListRequest appointmentSaleListReq) {
        AppointmentSaleQueryRequest queryReq = KsBeanUtil.convert(appointmentSaleListReq, AppointmentSaleQueryRequest.class);
        List<AppointmentSale> appointmentSaleList = appointmentSaleService.list(queryReq);
        List<AppointmentSaleVO> newList = appointmentSaleList.stream().map(entity -> appointmentSaleService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new AppointmentSaleListResponse(newList));
    }

    @Override
    public BaseResponse<AppointmentSaleByIdResponse> getById(@RequestBody @Valid AppointmentSaleByIdRequest appointmentSaleByIdRequest) {
        AppointmentSale appointmentSale =
                appointmentSaleService.getOne(appointmentSaleByIdRequest.getId(), appointmentSaleByIdRequest.getStoreId());
        return BaseResponse.success(new AppointmentSaleByIdResponse(appointmentSaleService.wrapperVo(appointmentSale)));
    }


    @Override
    public BaseResponse<AppointmentSaleIsInProcessResponse> isInProgress(@RequestBody @Valid AppointmentSaleIsInProgressRequest request) {
        AppointmentSale appointmentSale = appointmentSaleService.isInProcess(request.getGoodsInfoId());
        return BaseResponse.success(AppointmentSaleIsInProcessResponse.builder().appointmentSaleVO
                (appointmentSaleService.wrapperVo(appointmentSale)).build());
    }

    @Override
    public BaseResponse<AppointmentSaleInProcessResponse> inProgressAppointmentSaleInfoByGoodsInfoIdList(@RequestBody @Valid AppointmentSaleInProgressRequest request) {
        List<AppointmentSaleDO> appointmentSaleDOS = appointmentSaleService.inProgressAppointmentSaleInfoByGoodsInfoIdList(request.getGoodsInfoIdList());

        return BaseResponse.success(AppointmentSaleInProcessResponse.builder()
                .appointmentSaleVOList(KsBeanUtil.convert(appointmentSaleDOS, AppointmentSaleVO.class)).build());
    }

    @Override
    public BaseResponse<AppointmentSaleByIdResponse> getAppointmentSaleRelaInfo(@RequestBody @Valid RushToAppointmentSaleGoodsRequest request) {
        AppointmentSaleGoods appointmentSaleGoods = appointmentSaleGoodsService.list
                (AppointmentSaleGoodsQueryRequest.builder().goodsInfoId(request.getSkuId()).appointmentSaleId(request.getAppointmentSaleId()).build())
                .get(0);
        AppointmentSale appointmentSale = appointmentSaleService.getOne(request.getAppointmentSaleId(), appointmentSaleGoods.getStoreId());
        AppointmentSaleVO appointmentSaleVO = KsBeanUtil.convert(appointmentSale, AppointmentSaleVO.class);

        GoodsInfo goodsInfo = goodsInfoService.findOne(request.getSkuId());
        Goods goods = goodsService.getGoodsById(goodsInfo.getGoodsId());
        appointmentSaleVO.setAppointmentSaleGood(KsBeanUtil.convert(appointmentSaleGoods, AppointmentSaleGoodsVO.class));
        appointmentSaleVO.getAppointmentSaleGood().setGoodsInfoVO(KsBeanUtil.convert(goodsInfo, GoodsInfoVO.class));
        appointmentSaleVO.getAppointmentSaleGood().setGoodsVO(KsBeanUtil.convert(goods, GoodsVO.class));
        appointmentSaleVO.setStock(goodsInfo.getStock());
        return BaseResponse.success(AppointmentSaleByIdResponse.builder()
                .appointmentSaleVO(appointmentSaleVO).build());
    }

    @Override
    public BaseResponse<AppointmentSaleNotEndResponse> getNotEndActivity(@RequestBody @Valid AppointmentSaleByGoodsIdRequest request) {
        List<AppointmentSaleDO> appointmentSaleDOS = appointmentSaleService.getNotEndActivity(request.getGoodsId());

        return BaseResponse.success(AppointmentSaleNotEndResponse.builder()
                .appointmentSaleVOList(KsBeanUtil.convert(appointmentSaleDOS, AppointmentSaleVO.class)).build());
    }

    @Override
    public BaseResponse containAppointmentSaleAndBookingSale(@Valid @RequestBody AppointmentSaleInProgressRequest request) {
        appointmentSaleService.validParticipateInAppointmentSale(request.getGoodsInfoIdList());
        bookingSaleService.validParticipateInBookingSale(request.getGoodsInfoIdList());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<AppointmentSaleMergeInProcessResponse> mergeVaildAppointmentSaleAndBookingSale(@Valid AppointmentSaleMergeInProgressRequest request) {
        goodsRestrictedSaleService.validateBatchGoodsRestricted(request.getGoodsRestrictedValidateVOS(),request.getCustomerVO());
        List<AppointmentSaleDO> appointmentSaleDOS = appointmentSaleService.inProgressAppointmentSaleInfoByGoodsInfoIdList(request.getAppointSaleGoodsInfoIds());
        bookingSaleService.validateBookingQualification(request.getBookingSaleGoodsInfoIds(),request.getSkuIdAndBookSaleIdMap());
        appointmentSaleService.validParticipateInAppointmentSale(request.getNeedValidSkuIds());
        bookingSaleService.validParticipateInBookingSale(request.getNeedValidSkuIds());
        return BaseResponse.success(AppointmentSaleMergeInProcessResponse.builder()
                .appointmentSaleVOList(KsBeanUtil.convert(appointmentSaleDOS, AppointmentSaleVO.class)).build());
    }

    @Override
    public BaseResponse<AppointmentSaleAndBookingSaleResponse> mergeAppointmentSaleAndBookingSale(@Valid AppointmentSaleAndBookingSaleRequest request) {
        AppointmentSaleAndBookingSaleResponse response = new AppointmentSaleAndBookingSaleResponse();
        List<AppointmentSaleDO> appointmentSaleDOS = appointmentSaleService.inProgressAppointmentSaleInfoByGoodsInfoIdList(request.getGoodsInfoIdList());
        List<BookingSale> bookingSaleList = bookingSaleService.inProgressBookingSaleInfoByGoodsInfoIdList(request.getGoodsInfoIdList());
        response.setAppointmentSaleVOList(KsBeanUtil.convert(appointmentSaleDOS, AppointmentSaleVO.class));
        response.setBookingSaleVOList(KsBeanUtil.convert(bookingSaleList, BookingSaleVO.class));
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<AppointmentSalePageResponse> pageNew(@Valid AppointmentSalePageRequest appointmentSalePageReq) {
        AppointmentSaleQueryRequest queryReq = KsBeanUtil.convert(appointmentSalePageReq, AppointmentSaleQueryRequest.class);
        MicroServicePage<AppointmentSaleVO> newPage = appointmentSaleService.pageNew(queryReq);

        AppointmentSalePageResponse finalRes = new AppointmentSalePageResponse(newPage);
        return BaseResponse.success(finalRes);
    }
}

