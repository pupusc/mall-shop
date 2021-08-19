package com.wanmi.sbc.goods.provider.impl.bookingsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelListByCustomerLevelNameRequest;
import com.wanmi.sbc.customer.bean.dto.MarketingCustomerLevelDTO;
import com.wanmi.sbc.customer.bean.vo.MarketingCustomerLevelVO;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.request.bookingsale.*;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsQueryRequest;
import com.wanmi.sbc.goods.api.response.bookingsale.*;
import com.wanmi.sbc.goods.api.response.bookingsalegoods.BookingSaleInProgressAllGoodsInfoIdsResponse;
import com.wanmi.sbc.goods.appointmentsale.model.root.AppointmentSaleDO;
import com.wanmi.sbc.goods.appointmentsale.service.AppointmentSaleService;
import com.wanmi.sbc.goods.appointmentsalegoods.model.root.AppointmentSaleGoodsDO;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.goods.bookingsale.model.root.BookingSale;
import com.wanmi.sbc.goods.bookingsale.service.BookingSaleService;
import com.wanmi.sbc.goods.bookingsalegoods.model.root.BookingSaleGoods;
import com.wanmi.sbc.goods.bookingsalegoods.service.BookingSaleGoodsService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>预售信息查询服务接口实现</p>
 *
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@RestController
@Validated
public class BookingSaleQueryController implements BookingSaleQueryProvider {
    @Autowired
    private BookingSaleService bookingSaleService;

    @Autowired
    private BookingSaleGoodsService bookingSaleGoodsService;

    @Autowired
    private AppointmentSaleService appointmentSaleService;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Override
    public BaseResponse<BookingSalePageResponse> page(@RequestBody @Valid BookingSalePageRequest request) {
        BookingSaleQueryRequest queryReq = KsBeanUtil.convert(request, BookingSaleQueryRequest.class);
        Page<BookingSale> bookingSalePage = bookingSaleService.page(queryReq);
        Page<BookingSaleVO> newPage = bookingSalePage.map(entity -> bookingSaleService.wrapperVo(entity));

        List<BookingSaleVO> bookingSaleVos = newPage.getContent();
        Map<Long, List<BookingSaleGoods>> saleGoodsMap = bookingSaleGoodsService.list(BookingSaleGoodsQueryRequest.builder().
                bookingSaleIdList(bookingSaleVos.stream().map(BookingSaleVO::getId).
                        collect(Collectors.toList())).build()).stream().collect(Collectors.groupingBy(BookingSaleGoods::getBookingSaleId));
        bookingSaleVos.forEach(s -> {
            if (saleGoodsMap.containsKey(s.getId())) {
                if (s.getBookingType().equals(NumberUtils.INTEGER_ONE)) {
                    s.setTailCount(saleGoodsMap.get(s.getId()).stream().mapToInt(BookingSaleGoods::getTailCount).sum());
                    s.setHandSelCount(saleGoodsMap.get(s.getId()).stream().mapToInt(BookingSaleGoods::getHandSelCount).sum());
                } else if (s.getBookingType().equals(NumberUtils.INTEGER_ZERO)) {
                    s.setPayCount(saleGoodsMap.get(s.getId()).stream().mapToInt(BookingSaleGoods::getPayCount).sum());
                }
            }
            s.buildStatus();
        });
        MicroServicePage<BookingSaleVO> microPage = new MicroServicePage<>(newPage, request.getPageable());
        BookingSalePageResponse finalRes = new BookingSalePageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<BookingSaleListResponse> list(@RequestBody @Valid BookingSaleListRequest bookingSaleListReq) {
        BookingSaleQueryRequest queryReq = KsBeanUtil.convert(bookingSaleListReq, BookingSaleQueryRequest.class);
        List<BookingSale> bookingSaleList = bookingSaleService.list(queryReq);
        List<BookingSaleVO> newList = bookingSaleList.stream().map(entity -> bookingSaleService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new BookingSaleListResponse(newList));
    }

    @Override
    public BaseResponse<BookingSaleByIdResponse> getById(@RequestBody @Valid BookingSaleByIdRequest bookingSaleByIdRequest) {
        BookingSale bookingSale =
                bookingSaleService.getOne(bookingSaleByIdRequest.getId(), bookingSaleByIdRequest.getStoreId());
        return BaseResponse.success(new BookingSaleByIdResponse(bookingSaleService.wrapperVo(bookingSale)));
    }


    @Override
    public BaseResponse<BookingSaleByIdResponse> getOne(@RequestBody @Valid BookingSaleByIdRequest bookingSaleByIdRequest) {
        BookingSale bookingSale =
                bookingSaleService.getById(bookingSaleByIdRequest.getId());
        return BaseResponse.success(new BookingSaleByIdResponse(bookingSaleService.wrapperVo(bookingSale)));
    }

    @Override
    public BaseResponse<BookingSaleListResponse> inProgressBookingSaleInfoByGoodsInfoIdList(@RequestBody @Valid BookingSaleInProgressRequest request) {
        List<BookingSale> bookingSaleList = bookingSaleService.inProgressBookingSaleInfoByGoodsInfoIdList(request.getGoodsInfoIdList());

        return BaseResponse.success(BookingSaleListResponse.builder()
                .bookingSaleVOList(KsBeanUtil.convert(bookingSaleList, BookingSaleVO.class)).build());
    }

    @Override
    public BaseResponse<BookingSaleIsInProgressResponse> isInProgress(@RequestBody @Valid BookingSaleIsInProgressRequest request) {
        BookingSale bookingSale = bookingSaleService.isInProcess(request.getGoodsInfoId());
        return BaseResponse.success(BookingSaleIsInProgressResponse.builder().bookingSaleVO(
                (bookingSaleService.wrapperVo(bookingSale))).serverTime(LocalDateTime.now()).build());
    }

    @Override
    public BaseResponse<BookingSaleNotEndResponse> getNotEndActivity(@RequestBody @Valid BookingSaleByGoodsIdRequest request) {
        List<BookingSale> bookingSales = bookingSaleService.getNotEndActivity(request.getGoodsId());

        return BaseResponse.success(BookingSaleNotEndResponse.builder()
                .bookingSaleVOList(KsBeanUtil.convert(bookingSales, BookingSaleVO.class)).build());
    }

    @Override
    public BaseResponse<BookingSaleInProgressAllGoodsInfoIdsResponse> inProgressAllByGoodsInfoIds(@RequestBody @Valid BookingSaleInProgressAllGoodsInfoIdsRequest request) {
        List<AppointmentSaleDO> appointmentSaleDOS = appointmentSaleService.inProgressAppointmentSaleInfoByGoodsInfoIdList(request.getGoodsInfoIdList());
        List<BookingSale> bookingSaleList = bookingSaleService.inProgressBookingSaleInfoByGoodsInfoIdList(request.getGoodsInfoIdList());
        List<AppointmentSaleSimplifyVO> appointmentSaleSimplifyVOList = new ArrayList<>();
        List<BookingSaleSimplifyVO> bookingSaleSimplifyVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(appointmentSaleDOS)){
            appointmentSaleSimplifyVOList = appointmentSaleDOS.stream().map(appointmentSaleDO -> {
                AppointmentSaleGoodsDO appointmentSaleGood = appointmentSaleDO.getAppointmentSaleGood();
                AppointmentSaleSimplifyVO appointmentSaleSimplifyVO = new AppointmentSaleSimplifyVO();
                AppointmentSaleGoodsSimplifyVO simplifyVO = new AppointmentSaleGoodsSimplifyVO();
                simplifyVO.setGoodsInfoId(appointmentSaleGood.getGoodsInfoId());
                simplifyVO.setPrice(appointmentSaleGood.getPrice());
                appointmentSaleSimplifyVO.setAppointmentType(appointmentSaleDO.getAppointmentType());
                appointmentSaleSimplifyVO.setSnapUpStartTime(appointmentSaleDO.getSnapUpStartTime());
                appointmentSaleSimplifyVO.setSnapUpEndTime(appointmentSaleDO.getSnapUpEndTime());
                appointmentSaleSimplifyVO.setAppointmentStartTime(appointmentSaleDO.getAppointmentStartTime());
                appointmentSaleSimplifyVO.setAppointmentEndTime(appointmentSaleDO.getAppointmentEndTime());
                appointmentSaleSimplifyVO.setAppointmentSaleGood(simplifyVO);
                return appointmentSaleSimplifyVO;
            }).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(bookingSaleList)){
            bookingSaleSimplifyVOList = bookingSaleList.stream().map(bookingSale -> {
                BookingSaleGoods bookingSaleGoods = bookingSale.getBookingSaleGoods();
                BookingSaleSimplifyVO bookingSaleSimplifyVO = new BookingSaleSimplifyVO();
                BookingSaleGoodsSimplifyVO simplifyVO = new BookingSaleGoodsSimplifyVO();
                simplifyVO.setGoodsInfoId(bookingSaleGoods.getGoodsInfoId());
                simplifyVO.setBookingPrice(bookingSaleGoods.getBookingPrice());
                bookingSaleSimplifyVO.setBookingType(bookingSale.getBookingType());
                bookingSaleSimplifyVO.setHandSelStartTime(bookingSale.getHandSelStartTime());
                bookingSaleSimplifyVO.setHandSelEndTime(bookingSale.getHandSelEndTime());
                bookingSaleSimplifyVO.setBookingStartTime(bookingSale.getBookingStartTime());
                bookingSaleSimplifyVO.setBookingEndTime(bookingSale.getBookingEndTime());
                bookingSaleSimplifyVO.setBookingSaleGoods(simplifyVO);
                return bookingSaleSimplifyVO;
            }).collect(Collectors.toList());
        }
        return BaseResponse.success(new BookingSaleInProgressAllGoodsInfoIdsResponse(appointmentSaleSimplifyVOList,bookingSaleSimplifyVOList));
    }

    @Override
    public BaseResponse<BookingSalePageResponse> pageNew(@RequestBody @Valid BookingSalePageRequest request) {
        BookingSaleQueryRequest queryReq = KsBeanUtil.convert(request, BookingSaleQueryRequest.class);
        Page<BookingSale> bookingSalePage = bookingSaleService.page(queryReq);
        MicroServicePage<BookingSaleVO> newPage = KsBeanUtil.convertPage(bookingSalePage,BookingSaleVO.class);

        List<BookingSaleVO> bookingSaleVos = newPage.getContent();

        List<MarketingCustomerLevelDTO> customerLevelDTOList = bookingSaleVos.stream().map(bookingSaleVO -> {
            MarketingCustomerLevelDTO dto = new MarketingCustomerLevelDTO();
            dto.setId(bookingSaleVO.getId());
            dto.setStoreId(bookingSaleVO.getStoreId());
            dto.setJoinLevel(bookingSaleVO.getJoinLevel());
            return dto;
        }).collect(Collectors.toList());

        List<MarketingCustomerLevelVO> marketingCustomerLevelVOList = customerLevelQueryProvider.listByCustomerLevelName(new CustomerLevelListByCustomerLevelNameRequest(customerLevelDTOList)).getContext().getCustomerLevelVOList();
        Map<Long,MarketingCustomerLevelVO> levelVOMap = marketingCustomerLevelVOList.stream().collect(Collectors.toMap(MarketingCustomerLevelVO::getId, Function.identity()));

        Map<Long, List<BookingSaleGoods>> saleGoodsMap = bookingSaleGoodsService.list(BookingSaleGoodsQueryRequest.builder().
                bookingSaleIdList(bookingSaleVos.stream().map(BookingSaleVO::getId).
                        collect(Collectors.toList())).build()).stream().collect(Collectors.groupingBy(BookingSaleGoods::getBookingSaleId));
        bookingSaleVos.stream().forEach(s -> {
            MarketingCustomerLevelVO levelVO = levelVOMap.get(s.getId());
            s.setLevelName(levelVO.getLevelName());
            s.setStoreName(levelVO.getStoreName());
            if (saleGoodsMap.containsKey(s.getId())) {
                if (s.getBookingType().equals(NumberUtils.INTEGER_ONE)) {
                    s.setTailCount(saleGoodsMap.get(s.getId()).stream().mapToInt(BookingSaleGoods::getTailCount).sum());
                    s.setHandSelCount(saleGoodsMap.get(s.getId()).stream().mapToInt(BookingSaleGoods::getHandSelCount).sum());
                } else if (s.getBookingType().equals(NumberUtils.INTEGER_ZERO)) {
                    s.setPayCount(saleGoodsMap.get(s.getId()).stream().mapToInt(BookingSaleGoods::getPayCount).sum());
                }
            }
            s.buildStatus();
        });
        BookingSalePageResponse finalRes = new BookingSalePageResponse(newPage);
        return BaseResponse.success(finalRes);
    }
}

