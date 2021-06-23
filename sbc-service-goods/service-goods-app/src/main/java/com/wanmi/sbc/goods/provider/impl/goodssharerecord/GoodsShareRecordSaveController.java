package com.wanmi.sbc.goods.provider.impl.goodssharerecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodssharerecord.GoodsShareRecordSaveProvider;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordAddRequest;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordDelByIdListRequest;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordDelByIdRequest;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordModifyRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoRequest;
import com.wanmi.sbc.goods.api.response.goodssharerecord.GoodsShareRecordAddResponse;
import com.wanmi.sbc.goods.api.response.goodssharerecord.GoodsShareRecordModifyResponse;
import com.wanmi.sbc.goods.goodssharerecord.model.root.GoodsShareRecord;
import com.wanmi.sbc.goods.goodssharerecord.service.GoodsShareRecordService;
import com.wanmi.sbc.goods.info.reponse.GoodsInfoDetailResponse;
import com.wanmi.sbc.goods.info.service.GoodsInfoSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * <p>商品分享保存服务接口实现</p>
 *
 * @author zhangwenchang
 * @date 2020-03-06 13:46:24
 */
@RestController
@Validated
public class GoodsShareRecordSaveController implements GoodsShareRecordSaveProvider {
    @Autowired
    private GoodsShareRecordService goodsShareRecordService;

    @Autowired
    private GoodsInfoSiteService goodsInfoSiteService;

    @Override
    public BaseResponse<GoodsShareRecordAddResponse> add(@RequestBody @Valid GoodsShareRecordAddRequest goodsShareRecordAddRequest) {
        GoodsShareRecord goodsShareRecord = new GoodsShareRecord();
        KsBeanUtil.copyPropertiesThird(goodsShareRecordAddRequest, goodsShareRecord);
        if (Objects.isNull(goodsShareRecordAddRequest.getGoodsId())
                || Objects.isNull(goodsShareRecordAddRequest.getStoreId())
                || Objects.isNull(goodsShareRecordAddRequest.getCompanyInfoId())) {
            GoodsInfoRequest goodsInfoRequest = new GoodsInfoRequest();
            goodsInfoRequest.setGoodsInfoId(goodsShareRecordAddRequest.getGoodsInfoId());
            GoodsInfoDetailResponse goodsInfoDetailResponse = goodsInfoSiteService.detail(goodsInfoRequest);
            goodsShareRecord.setCompanyInfoId(goodsInfoDetailResponse.getGoodsInfo().getCompanyInfoId());
            goodsShareRecord.setStoreId(goodsInfoDetailResponse.getGoodsInfo().getStoreId());
            goodsShareRecord.setGoodsId(goodsInfoDetailResponse.getGoodsInfo().getGoodsId());
        }
        return BaseResponse.success(new GoodsShareRecordAddResponse(
                goodsShareRecordService.wrapperVo(goodsShareRecordService.add(goodsShareRecord))));
    }

    @Override
    public BaseResponse<GoodsShareRecordModifyResponse> modify(@RequestBody @Valid GoodsShareRecordModifyRequest goodsShareRecordModifyRequest) {
        GoodsShareRecord goodsShareRecord = new GoodsShareRecord();
        KsBeanUtil.copyPropertiesThird(goodsShareRecordModifyRequest, goodsShareRecord);
        return BaseResponse.success(new GoodsShareRecordModifyResponse(
                goodsShareRecordService.wrapperVo(goodsShareRecordService.modify(goodsShareRecord))));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid GoodsShareRecordDelByIdRequest goodsShareRecordDelByIdRequest) {
        goodsShareRecordService.deleteById(goodsShareRecordDelByIdRequest.getShareId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid GoodsShareRecordDelByIdListRequest goodsShareRecordDelByIdListRequest) {
        goodsShareRecordService.deleteByIdList(goodsShareRecordDelByIdListRequest.getShareIdList());
        return BaseResponse.SUCCESSFUL();
    }

}

