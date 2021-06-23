package com.wanmi.sbc.customer.provider.impl.paidcardbuyrecord;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.paidcardbuyrecord.PaidCardBuyRecordQueryProvider;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordPageRequest;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordQueryRequest;
import com.wanmi.sbc.customer.api.response.paidcardbuyrecord.PaidCardBuyRecordPageResponse;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordListRequest;
import com.wanmi.sbc.customer.api.response.paidcardbuyrecord.PaidCardBuyRecordListResponse;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordByIdRequest;
import com.wanmi.sbc.customer.api.response.paidcardbuyrecord.PaidCardBuyRecordByIdResponse;
import com.wanmi.sbc.customer.bean.vo.PaidCardBuyRecordVO;
import com.wanmi.sbc.customer.paidcardbuyrecord.service.PaidCardBuyRecordService;
import com.wanmi.sbc.customer.paidcardbuyrecord.model.root.PaidCardBuyRecord;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>付费会员查询服务接口实现</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
@RestController
@Validated
public class PaidCardBuyRecordQueryController implements PaidCardBuyRecordQueryProvider {
	@Autowired
	private PaidCardBuyRecordService paidCardBuyRecordService;

	@Override
	public BaseResponse<PaidCardBuyRecordPageResponse> page(@RequestBody @Valid PaidCardBuyRecordPageRequest paidCardBuyRecordPageReq) {
		PaidCardBuyRecordQueryRequest queryReq = new PaidCardBuyRecordQueryRequest();
		KsBeanUtil.copyPropertiesThird(paidCardBuyRecordPageReq, queryReq);
		Page<PaidCardBuyRecord> paidCardBuyRecordPage = paidCardBuyRecordService.page(queryReq);
		Page<PaidCardBuyRecordVO> newPage = paidCardBuyRecordPage.map(entity -> paidCardBuyRecordService.wrapperVo(entity));
		MicroServicePage<PaidCardBuyRecordVO> microPage = new MicroServicePage<>(newPage, paidCardBuyRecordPageReq.getPageable());
		PaidCardBuyRecordPageResponse finalRes = new PaidCardBuyRecordPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<PaidCardBuyRecordListResponse> list(@RequestBody @Valid PaidCardBuyRecordListRequest paidCardBuyRecordListReq) {
		PaidCardBuyRecordQueryRequest queryReq = new PaidCardBuyRecordQueryRequest();
		KsBeanUtil.copyPropertiesThird(paidCardBuyRecordListReq, queryReq);
		List<PaidCardBuyRecord> paidCardBuyRecordList = paidCardBuyRecordService.list(queryReq);
		List<PaidCardBuyRecordVO> newList = paidCardBuyRecordList.stream().map(entity -> paidCardBuyRecordService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new PaidCardBuyRecordListResponse(newList));
	}

	@Override
	public BaseResponse<PaidCardBuyRecordByIdResponse> getById(@RequestBody @Valid PaidCardBuyRecordByIdRequest paidCardBuyRecordByIdRequest) {
		PaidCardBuyRecord paidCardBuyRecord = paidCardBuyRecordService.getById(paidCardBuyRecordByIdRequest.getPayCode());
		return BaseResponse.success(new PaidCardBuyRecordByIdResponse(paidCardBuyRecordService.wrapperVo(paidCardBuyRecord)));
	}

}

