package com.wanmi.sbc.goods.provider.impl.bookingsalegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.bookingsalegoods.BookingSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingGoodsInfoSimplePageRequest;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleQueryRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsPageRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsQueryRequest;
import com.wanmi.sbc.goods.api.response.bookingsalegoods.*;
import com.wanmi.sbc.goods.bean.dto.BookingGoodsInfoSimplePageDTO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.BookingVO;
import com.wanmi.sbc.goods.bookingsale.model.root.BookingSale;
import com.wanmi.sbc.goods.bookingsale.service.BookingSaleService;
import com.wanmi.sbc.goods.bookingsalegoods.model.root.BookingSaleGoods;
import com.wanmi.sbc.goods.bookingsalegoods.service.BookingGoodsInfoSimpleCriterIaBuilder;
import com.wanmi.sbc.goods.bookingsalegoods.service.BookingSaleGoodsService;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.spec.service.GoodsInfoSpecDetailRelService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>预售商品信息查询服务接口实现</p>
 *
 * @author dany
 * @date 2020-06-05 10:51:35
 */
@RestController
@Validated
public class BookingSaleGoodsQueryController implements BookingSaleGoodsQueryProvider {
    @Autowired
    private BookingSaleGoodsService bookingSaleGoodsService;

    @Autowired
    private BookingSaleService bookingSaleService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private GoodsInfoSpecDetailRelService goodsInfoSpecDetailRelService;

    @Override
    public BaseResponse<BookingSaleGoodsPageResponse> page(@RequestBody @Valid BookingSaleGoodsPageRequest bookingSaleGoodsPageReq) {
        BookingSaleGoodsQueryRequest queryReq = KsBeanUtil.convert(bookingSaleGoodsPageReq, BookingSaleGoodsQueryRequest.class);
        Page<BookingSaleGoods> bookingSaleGoodsPage = bookingSaleGoodsService.page(queryReq);
        Page<BookingSaleGoodsVO> newPage = bookingSaleGoodsPage.map(entity -> bookingSaleGoodsService.wrapperVo(entity));
        MicroServicePage<BookingSaleGoodsVO> microPage = new MicroServicePage<>(newPage, bookingSaleGoodsPageReq.getPageable());
        BookingSaleGoodsPageResponse finalRes = new BookingSaleGoodsPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<BookingSaleGoodsListResponse> list(@RequestBody @Valid BookingSaleGoodsListRequest bookingSaleGoodsListReq) {
        BookingSaleGoodsQueryRequest queryReq = KsBeanUtil.convert(bookingSaleGoodsListReq, BookingSaleGoodsQueryRequest.class);
        List<BookingSaleGoods> bookingSaleGoodsList = bookingSaleGoodsService.list(queryReq);
        List<BookingSaleGoodsVO> newList = bookingSaleGoodsList.stream().map(entity -> bookingSaleGoodsService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new BookingSaleGoodsListResponse(newList));
    }

    @Override
    public BaseResponse<BookingSaleGoodsByIdResponse> getById(@RequestBody @Valid BookingSaleGoodsByIdRequest bookingSaleGoodsByIdRequest) {
        BookingSaleGoods bookingSaleGoods =
                bookingSaleGoodsService.getOne(bookingSaleGoodsByIdRequest.getId(), bookingSaleGoodsByIdRequest.getStoreId());
        return BaseResponse.success(new BookingSaleGoodsByIdResponse(bookingSaleGoodsService.wrapperVo(bookingSaleGoods)));
    }

    @Override
    public BaseResponse<BookingResponse> pageBoss(@RequestBody @Valid BookingGoodsInfoSimplePageRequest request) {
        BookingGoodsInfoSimplePageDTO query = KsBeanUtil.convert(request, BookingGoodsInfoSimplePageDTO.class);
        Page<BookingSaleGoods> page = bookingSaleGoodsService.build(query);
        if (CollectionUtils.isEmpty(page.getContent())) {
            return BaseResponse.success(BookingResponse.builder().build());
        }
        List<BookingSale> bookingSales = bookingSaleService.list(BookingSaleQueryRequest.builder().idList
                (page.getContent().stream().map(BookingSaleGoods::getBookingSaleId).collect(Collectors.toList())).build());
        Map<Long, BookingSale> bookingSaleMap = bookingSales.stream().collect(Collectors.toMap(BookingSale::getId, v -> v));

        List<GoodsInfo> goodsInfoList = goodsInfoService.findByIds(page.getContent().stream().map(BookingSaleGoods::getGoodsInfoId).collect(Collectors.toList()));
        Map<String, GoodsInfo> goodsInfoMap = goodsInfoList.stream().collect(Collectors.toMap(GoodsInfo::getGoodsInfoId, v -> v));
        Page<BookingVO> newPage = page.map(entity -> bookingSaleGoodsService.wrapperBookingVO(entity, bookingSaleMap, goodsInfoMap));
        MicroServicePage<BookingVO> microPage = new MicroServicePage<>(newPage, page.getPageable());

        //填充spu主图片以及划线价
        if(CollectionUtils.isNotEmpty(goodsInfoList)) {
            Map<String, Goods> spuMap = goodsService.findAll(GoodsQueryRequest.builder()
                    .goodsIds(goodsInfoList.stream().map(GoodsInfo::getGoodsId).collect(Collectors.toList())).build())
                    .stream().collect(Collectors.toMap(Goods::getGoodsId, g -> g));
            if (MapUtils.isNotEmpty(spuMap)) {
                microPage.getContent().stream().filter(s -> spuMap.containsKey(s.getBookingSaleGoods().getGoodsId()))
                        .forEach(s -> {
                            Goods goods = spuMap.get(s.getBookingSaleGoods().getGoodsId());
                            s.getBookingSaleGoods().setGoodsImg(goods.getGoodsImg());
                            s.getBookingSaleGoods().setLinePrice(goods.getLinePrice());
                        });
            }
        }

        Map<String, String> specMap = goodsInfoSpecDetailRelService.textByGoodsInfoIds(
                microPage.getContent().stream().map(a -> a.getBookingSaleGoods().getGoodsInfoId()).collect(Collectors.toList()));
        if (MapUtils.isNotEmpty(specMap)) {
            microPage.getContent().forEach(s -> s.getBookingSaleGoods().setSpecText(specMap.containsKey(s.getBookingSaleGoods().getGoodsInfoId()) ?
                    specMap.get(s.getBookingSaleGoods().getGoodsInfoId()) : ""));
        }
        return BaseResponse.success(BookingResponse.builder().bookingVOMicroServicePage(microPage).build());
    }

    @Override
    public BaseResponse<BookingGoodsResponse> pageBookingGoodsInfo(@RequestBody @Valid BookingGoodsInfoSimplePageRequest request) {
        BookingGoodsInfoSimpleCriterIaBuilder query = KsBeanUtil.convert(request, BookingGoodsInfoSimpleCriterIaBuilder.class);
        Page<BookingSaleGoodsVO> bookingSaleGoodsVOS = bookingSaleGoodsService.pageBookingGoodsInfo(query);
        MicroServicePage<BookingSaleGoodsVO> microServicePage = KsBeanUtil.convertPage(bookingSaleGoodsVOS, BookingSaleGoodsVO.class);

        //填充spu主图片以及划线价
        if(CollectionUtils.isNotEmpty(microServicePage.getContent())) {
            Map<String, GoodsInfo> skuMap = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder()
                    .goodsInfoIds(microServicePage.getContent().stream().map(BookingSaleGoodsVO::getGoodsInfoId).collect(Collectors.toList())).build())
                    .stream().collect(Collectors.toMap(GoodsInfo::getGoodsInfoId, g -> g));
            if (MapUtils.isNotEmpty(skuMap)) {
                microServicePage.getContent().stream().filter(s -> skuMap.containsKey(s.getGoodsInfoId()))
                        .forEach(s -> {
                            GoodsInfo sku = skuMap.get(s.getGoodsInfoId());
                            s.setGoodsInfoImg(sku.getGoodsInfoImg());
                        });
            }
        }

        //填充规格
        if (Boolean.TRUE.equals(request.getHavSpecTextFlag())
                && CollectionUtils.isNotEmpty(microServicePage.getContent())) {
            Map<String, String> specMap = goodsInfoSpecDetailRelService.textByGoodsInfoIds(
                    microServicePage.getContent().stream().map(BookingSaleGoodsVO::getGoodsInfoId).collect(Collectors.toList()));
            if (MapUtils.isNotEmpty(specMap)) {
                microServicePage.getContent().forEach(s -> s.setSpecText(specMap.get(s.getGoodsInfoId())));
            }
        }
        //填充服务器时间
        if (CollectionUtils.isNotEmpty(microServicePage.getContent())) {
            microServicePage.getContent().forEach(s -> s.setServerTime(LocalDateTime.now()));
        }
        return BaseResponse.success(BookingGoodsResponse.builder().page(microServicePage).build());
    }
}

