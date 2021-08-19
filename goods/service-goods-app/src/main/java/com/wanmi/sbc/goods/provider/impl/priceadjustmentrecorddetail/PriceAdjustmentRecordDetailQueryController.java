package com.wanmi.sbc.goods.provider.impl.priceadjustmentrecorddetail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailQueryProvider;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailByIdRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailListRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailPageRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailQueryRequest;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailByIdResponse;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailListResponse;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailPageByNoResponse;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailPageResponse;
import com.wanmi.sbc.goods.bean.vo.PriceAdjustmentRecordDetailVO;
import com.wanmi.sbc.goods.priceadjustmentrecord.model.root.PriceAdjustmentRecord;
import com.wanmi.sbc.goods.priceadjustmentrecord.service.PriceAdjustmentRecordService;
import com.wanmi.sbc.goods.priceadjustmentrecorddetail.model.root.PriceAdjustmentRecordDetail;
import com.wanmi.sbc.goods.priceadjustmentrecorddetail.service.PriceAdjustmentRecordDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>调价单详情表查询服务接口实现</p>
 *
 * @author chenli
 * @date 2020-12-09 19:55:41
 */
@RestController
@Validated
public class PriceAdjustmentRecordDetailQueryController implements PriceAdjustmentRecordDetailQueryProvider {
    @Autowired
    private PriceAdjustmentRecordDetailService priceAdjustmentRecordDetailService;

	@Autowired
	private PriceAdjustmentRecordService priceAdjustmentRecordService;

	@Override
	public BaseResponse<PriceAdjustmentRecordDetailPageByNoResponse> page(@RequestBody @Valid PriceAdjustmentRecordDetailPageRequest priceAdjustmentRecordDetailPageReq) {
		PriceAdjustmentRecordDetailPageByNoResponse response = new PriceAdjustmentRecordDetailPageByNoResponse();
		PriceAdjustmentRecordDetailQueryRequest queryReq = KsBeanUtil.convert(priceAdjustmentRecordDetailPageReq, PriceAdjustmentRecordDetailQueryRequest.class);
		// 根据调价单号分页查询调价详情
		Page<PriceAdjustmentRecordDetail> priceAdjustmentRecordDetailPage = priceAdjustmentRecordDetailService.page(queryReq);
		Page<PriceAdjustmentRecordDetailVO> newPage = priceAdjustmentRecordDetailPage.map(entity -> priceAdjustmentRecordDetailService.wrapperVo(entity));
		MicroServicePage<PriceAdjustmentRecordDetailVO> microPage = new MicroServicePage<>(newPage, priceAdjustmentRecordDetailPageReq.getPageable());

		// 查询调价记录
		PriceAdjustmentRecord priceAdjustmentRecord = priceAdjustmentRecordService.getOne(priceAdjustmentRecordDetailPageReq.getPriceAdjustmentNo(), priceAdjustmentRecordDetailPageReq.getBaseStoreId());
		if (Objects.nonNull(priceAdjustmentRecord)){
            response.setPriceAdjustmentRecordVO(priceAdjustmentRecordService.wrapperVoForPage(priceAdjustmentRecord));
        }

		response.setRecordDetailPage(microPage);
		return BaseResponse.success(response);
	}

//	@Override
//	public BaseResponse<PriceAdjustmentRecordDetailPageByTypeResponse> pageByType(@Valid PriceAdjustmentDetailPageByTypeRequest request) {
//		Page<PriceAdjustmentRecordDetail> pageList = priceAdjustmentRecordDetailService.page(request
//				.getAdjustNo(), request.getType(), request.getPageRequest());
//		Page<PriceAdjustmentRecordDetailVO> newPage = pageList.map(entity -> priceAdjustmentRecordDetailService.wrapperVo(entity));
//		MicroServicePage<PriceAdjustmentRecordDetailVO> microPage = new MicroServicePage<>(newPage, request.getPageable());
//		return BaseResponse.success(new PriceAdjustmentRecordDetailPageByTypeResponse(microPage));
//	}

	@Override
	public BaseResponse<PriceAdjustmentRecordDetailPageResponse> pageForConfirm(@RequestBody  @Valid PriceAdjustmentRecordDetailPageRequest priceAdjustmentRecordDetailPageReq) {
		PriceAdjustmentRecordDetailQueryRequest queryReq = KsBeanUtil.convert(priceAdjustmentRecordDetailPageReq, PriceAdjustmentRecordDetailQueryRequest.class);
		Page<PriceAdjustmentRecordDetail> priceAdjustmentRecordDetailPage = priceAdjustmentRecordDetailService.pageForConfirm(queryReq);
		Page<PriceAdjustmentRecordDetailVO> newPage = priceAdjustmentRecordDetailPage.map(entity -> priceAdjustmentRecordDetailService.wrapperVo(entity));
		MicroServicePage<PriceAdjustmentRecordDetailVO> microPage = new MicroServicePage<>(newPage, priceAdjustmentRecordDetailPageReq.getPageable());
		PriceAdjustmentRecordDetailPageResponse finalRes = new PriceAdjustmentRecordDetailPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

    @Override
    public BaseResponse<PriceAdjustmentRecordDetailListResponse> list(@RequestBody @Valid PriceAdjustmentRecordDetailListRequest priceAdjustmentRecordDetailListReq) {
        PriceAdjustmentRecordDetailQueryRequest queryReq = KsBeanUtil.convert(priceAdjustmentRecordDetailListReq, PriceAdjustmentRecordDetailQueryRequest.class);
        List<PriceAdjustmentRecordDetail> priceAdjustmentRecordDetailList = priceAdjustmentRecordDetailService.list(queryReq);
        List<PriceAdjustmentRecordDetailVO> newList = priceAdjustmentRecordDetailList.stream().map(entity -> priceAdjustmentRecordDetailService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new PriceAdjustmentRecordDetailListResponse(newList));
    }

    @Override
    public BaseResponse<PriceAdjustmentRecordDetailByIdResponse> getById(@RequestBody @Valid PriceAdjustmentRecordDetailByIdRequest priceAdjustmentRecordDetailByIdRequest) {
        PriceAdjustmentRecordDetail priceAdjustmentRecordDetail =
                priceAdjustmentRecordDetailService.getOne(priceAdjustmentRecordDetailByIdRequest.getId());
        return BaseResponse.success(new PriceAdjustmentRecordDetailByIdResponse(priceAdjustmentRecordDetailService.wrapperVo(priceAdjustmentRecordDetail)));
    }

}

