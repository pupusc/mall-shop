package com.wanmi.sbc.flashsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.flashsale.DetailInfoReq;
import com.wanmi.sbc.goods.api.response.flashsale.DetailListResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: sbc-micro-service
 * @description: 活动商品详情
 * @create: 2019-06-12 11:09
 **/
@RestController
@RequestMapping("/detailBase")
public class DetailBaseController {

    @ApiOperation(value = "活动商品详情列表数据")
    @PostMapping("/pageList/")
    public BaseResponse<DetailListResp> pageList(DetailInfoReq detailInfoReq){
        return BaseResponse.success(new DetailListResp());
    }

}