package com.wanmi.sbc.bookingsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleIsInProgressRequest;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSaleByIdResponse;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSaleIsInProgressResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "预售webAPI", tags = "BookingSaleBaseController")
@RestController
@RequestMapping(value = "/bookingsale")
public class BookingSaleBaseController {

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;


    /**
     * @param goodsInfoId
     * @Description: 商品是否正在预售活动中
     */
    @ApiOperation(value = "商品是否正在预售活动中")
    @GetMapping("/{goodsInfoId}/isInProgress")
    public BaseResponse<BookingSaleIsInProgressResponse> isInProgress(@PathVariable String goodsInfoId) {

        return bookingSaleQueryProvider.isInProgress(BookingSaleIsInProgressRequest.builder().goodsInfoId(goodsInfoId).build());
    }

}
