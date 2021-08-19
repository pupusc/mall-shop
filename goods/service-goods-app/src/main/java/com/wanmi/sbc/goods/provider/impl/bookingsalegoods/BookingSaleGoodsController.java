package com.wanmi.sbc.goods.provider.impl.bookingsalegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.bookingsalegoods.BookingSaleGoodsProvider;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleGoodsCountRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsAddRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsDelByIdListRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsDelByIdRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsModifyRequest;
import com.wanmi.sbc.goods.api.response.bookingsalegoods.BookingSaleGoodsAddResponse;
import com.wanmi.sbc.goods.api.response.bookingsalegoods.BookingSaleGoodsModifyResponse;
import com.wanmi.sbc.goods.bean.dto.BookingSaleGoodsDTO;
import com.wanmi.sbc.goods.bookingsalegoods.model.root.BookingSaleGoods;
import com.wanmi.sbc.goods.bookingsalegoods.service.BookingSaleGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>预售商品信息保存服务接口实现</p>
 *
 * @author dany
 * @date 2020-06-05 10:51:35
 */
@RestController
@Validated
public class BookingSaleGoodsController implements BookingSaleGoodsProvider {
    @Autowired
    private BookingSaleGoodsService bookingSaleGoodsService;

    @Override
    public BaseResponse<BookingSaleGoodsAddResponse> add(@RequestBody @Valid BookingSaleGoodsAddRequest bookingSaleGoodsAddRequest) {
        BookingSaleGoods bookingSaleGoods = KsBeanUtil.convert(bookingSaleGoodsAddRequest, BookingSaleGoods.class);
        return BaseResponse.success(new BookingSaleGoodsAddResponse(
                bookingSaleGoodsService.wrapperVo(bookingSaleGoodsService.add(bookingSaleGoods))));
    }

    @Override
    public BaseResponse<BookingSaleGoodsModifyResponse> modify(@RequestBody @Valid BookingSaleGoodsModifyRequest bookingSaleGoodsModifyRequest) {
        BookingSaleGoods bookingSaleGoods = KsBeanUtil.convert(bookingSaleGoodsModifyRequest, BookingSaleGoods.class);
        return BaseResponse.success(new BookingSaleGoodsModifyResponse(
                bookingSaleGoodsService.wrapperVo(bookingSaleGoodsService.modify(bookingSaleGoods))));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid BookingSaleGoodsDelByIdRequest bookingSaleGoodsDelByIdRequest) {
        bookingSaleGoodsService.deleteById(bookingSaleGoodsDelByIdRequest.getId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid BookingSaleGoodsDelByIdListRequest bookingSaleGoodsDelByIdListRequest) {
        bookingSaleGoodsService.deleteByIdList(bookingSaleGoodsDelByIdListRequest.getIdList());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse subCanBookingCount(@RequestBody @Valid BookingSaleGoodsCountRequest request) {
        bookingSaleGoodsService.subCanBookingCount(BookingSaleGoodsDTO.builder().goodsInfoId(request.getGoodsInfoId()).
                bookingSaleId(request.getBookingSaleId()).stock(request.getStock().intValue()).build());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse addCanBookingCount(@RequestBody @Valid BookingSaleGoodsCountRequest request) {
        bookingSaleGoodsService.addCanBookingCount(BookingSaleGoodsDTO.builder().goodsInfoId(request.getGoodsInfoId()).
                bookingSaleId(request.getBookingSaleId()).stock(request.getStock().intValue()).build());
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse addBookingPayCount(@RequestBody @Valid BookingSaleGoodsCountRequest request) {
        bookingSaleGoodsService.addBookingPayCount(BookingSaleGoodsDTO.builder().goodsInfoId(request.getGoodsInfoId()).
                bookingSaleId(request.getBookingSaleId()).stock(request.getStock().intValue()).build());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse addBookinghandSelCount(@RequestBody @Valid BookingSaleGoodsCountRequest request) {
        bookingSaleGoodsService.addBookinghandSelCount(BookingSaleGoodsDTO.builder().goodsInfoId(request.getGoodsInfoId()).
                bookingSaleId(request.getBookingSaleId()).stock(request.getStock().intValue()).build());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse addBookingTailCount(@RequestBody @Valid BookingSaleGoodsCountRequest request) {
        bookingSaleGoodsService.addBookingTailCount(BookingSaleGoodsDTO.builder().goodsInfoId(request.getGoodsInfoId()).
                bookingSaleId(request.getBookingSaleId()).stock(request.getStock().intValue()).build());
        return BaseResponse.SUCCESSFUL();
    }
}

