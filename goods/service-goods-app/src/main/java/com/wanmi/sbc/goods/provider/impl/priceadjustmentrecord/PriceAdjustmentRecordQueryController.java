package com.wanmi.sbc.goods.provider.impl.priceadjustmentrecord;

import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.*;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailQueryRequest;
import com.wanmi.sbc.goods.priceadjustmentrecorddetail.model.root.PriceAdjustmentRecordDetail;
import com.wanmi.sbc.goods.priceadjustmentrecorddetail.service.PriceAdjustmentRecordDetailService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecord.PriceAdjustmentRecordQueryProvider;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordPageResponse;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordListResponse;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordByIdResponse;
import com.wanmi.sbc.goods.bean.vo.PriceAdjustmentRecordVO;
import com.wanmi.sbc.goods.priceadjustmentrecord.service.PriceAdjustmentRecordService;
import com.wanmi.sbc.goods.priceadjustmentrecord.model.root.PriceAdjustmentRecord;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>调价记录表查询服务接口实现</p>
 *
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@RestController
@Validated
public class PriceAdjustmentRecordQueryController implements PriceAdjustmentRecordQueryProvider {
    @Autowired
    private PriceAdjustmentRecordService priceAdjustmentRecordService;

    @Autowired
    private PriceAdjustmentRecordDetailService priceAdjustmentRecordDetailService;

    @Override
    public BaseResponse<PriceAdjustmentRecordPageResponse> page(@RequestBody @Valid PriceAdjustmentRecordPageRequest priceAdjustmentRecordPageReq) {
        PriceAdjustmentRecordPageResponse finalRes = new PriceAdjustmentRecordPageResponse();

        PriceAdjustmentRecordQueryRequest queryReq = KsBeanUtil.convert(priceAdjustmentRecordPageReq,
                PriceAdjustmentRecordQueryRequest.class);
        // 商品名称和商品编码是否不为空
        if (StringUtils.isNotBlank(queryReq.getGoodsInfoName()) || StringUtils.isNotBlank(queryReq.getGoodsInfoNo())) {
            // 根据商品名称或者商品编码模糊查询调价详情表
            List<PriceAdjustmentRecordDetail> recordDetails =
                    priceAdjustmentRecordDetailService.list(PriceAdjustmentRecordDetailQueryRequest.builder().goodsInfoName(queryReq.getGoodsInfoName()).goodsInfoNo(queryReq.getGoodsInfoNo()).build());
            if (CollectionUtils.isNotEmpty(recordDetails)){
                List<String> recordNos =
                        recordDetails.stream().map(PriceAdjustmentRecordDetail::getPriceAdjustmentNo).distinct().collect(Collectors.toList());
                // 调价单号id集合
                queryReq.setIdList(recordNos);
            } else {
                finalRes.setPriceAdjustmentRecordVOPage(new MicroServicePage<>());
                return BaseResponse.success(finalRes);
            }
        }
        Page<PriceAdjustmentRecord> priceAdjustmentRecordPage = priceAdjustmentRecordService.page(queryReq);
        Page<PriceAdjustmentRecordVO> newPage =
                priceAdjustmentRecordPage.map(entity -> priceAdjustmentRecordService.wrapperVoForPage(entity));
        MicroServicePage<PriceAdjustmentRecordVO> microPage = new MicroServicePage<>(newPage,
                priceAdjustmentRecordPageReq.getPageable());
        finalRes.setPriceAdjustmentRecordVOPage(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<PriceAdjustmentRecordListResponse> list(@RequestBody @Valid PriceAdjustmentRecordListRequest priceAdjustmentRecordListReq) {
        PriceAdjustmentRecordQueryRequest queryReq = KsBeanUtil.convert(priceAdjustmentRecordListReq,
                PriceAdjustmentRecordQueryRequest.class);
        List<PriceAdjustmentRecord> priceAdjustmentRecordList = priceAdjustmentRecordService.list(queryReq);
        List<PriceAdjustmentRecordVO> newList =
                priceAdjustmentRecordList.stream().map(entity -> priceAdjustmentRecordService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new PriceAdjustmentRecordListResponse(newList));
    }

    @Override
    public BaseResponse<PriceAdjustmentRecordByIdResponse> getById(@RequestBody @Valid PriceAdjustmentRecordByIdRequest priceAdjustmentRecordByIdRequest) {
        PriceAdjustmentRecord priceAdjustmentRecord =
                priceAdjustmentRecordService.getOne(priceAdjustmentRecordByIdRequest.getId(),
                        priceAdjustmentRecordByIdRequest.getStoreId());
        return BaseResponse.success(new PriceAdjustmentRecordByIdResponse(priceAdjustmentRecordService.wrapperVo(priceAdjustmentRecord)));
    }

    @Override
    public BaseResponse<PriceAdjustmentRecordByIdResponse> getByAdjustNo(@RequestBody @Valid PriceAdjustmentRecordByAdjustNoRequest request) {
        PriceAdjustmentRecord record = priceAdjustmentRecordService.findById(request.getAdjustNo());
        return BaseResponse.success(new PriceAdjustmentRecordByIdResponse(priceAdjustmentRecordService.wrapperVo(record)));
    }

}

