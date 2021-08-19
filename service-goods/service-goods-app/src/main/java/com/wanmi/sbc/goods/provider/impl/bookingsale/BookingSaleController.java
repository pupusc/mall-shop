package com.wanmi.sbc.goods.provider.impl.bookingsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleProvider;
import com.wanmi.sbc.goods.api.request.bookingsale.*;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSaleAddResponse;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSaleModifyResponse;
import com.wanmi.sbc.goods.bookingsale.model.root.BookingSale;
import com.wanmi.sbc.goods.bookingsale.service.BookingSaleService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>预售信息保存服务接口实现</p>
 *
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@RestController
@Validated
public class BookingSaleController implements BookingSaleProvider {
    @Autowired
    private BookingSaleService bookingSaleService;

    @Override
    public BaseResponse<BookingSaleAddResponse> add(@RequestBody @Valid BookingSaleAddRequest request) {
        BookingSale bookingSale = KsBeanUtil.convert(request, BookingSale.class);
        return BaseResponse.success(new BookingSaleAddResponse(
                bookingSaleService.wrapperVo(bookingSaleService.add(bookingSale))));
    }

    @Override
    public BaseResponse<BookingSaleModifyResponse> modify(@RequestBody @Valid BookingSaleModifyRequest bookingSaleModifyRequest) {
        BookingSale bookingSale = KsBeanUtil.convert(bookingSaleModifyRequest, BookingSale.class);
        return BaseResponse.success(new BookingSaleModifyResponse(
                bookingSaleService.wrapperVo(bookingSaleService.modify(bookingSale))));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid BookingSaleDelByIdRequest request) {
        BookingSale sale = bookingSaleService.getOne(request.getId(), request.getStoreId());
        if (Objects.isNull(sale)) {
            throw new SbcRuntimeException("K-000001");
        }
        if (sale.getBookingType().equals(NumberUtils.INTEGER_ONE) && sale.getHandSelStartTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException("K-000001");
        }
        if (sale.getBookingType().equals(NumberUtils.INTEGER_ZERO) && sale.getBookingStartTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException("K-000001");
        }
        sale.setDelFlag(DeleteFlag.YES);
        bookingSaleService.deleteById(sale);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid BookingSaleDelByIdListRequest bookingSaleDelByIdListRequest) {
        List<BookingSale> bookingSaleList = bookingSaleDelByIdListRequest.getIdList().stream()
                .map(Id -> {
                    BookingSale bookingSale = KsBeanUtil.convert(Id, BookingSale.class);
                    bookingSale.setDelFlag(DeleteFlag.YES);
                    return bookingSale;
                }).collect(Collectors.toList());
        bookingSaleService.deleteByIdList(bookingSaleList);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyStatus(@Valid BookingSaleStatusRequest request) {
        BookingSale sale = bookingSaleService.getOne(request.getId(), request.getStoreId());
        sale.setPauseFlag(request.getPauseFlag());
        bookingSaleService.modifyStatus(sale);
        return BaseResponse.SUCCESSFUL();
    }
}

