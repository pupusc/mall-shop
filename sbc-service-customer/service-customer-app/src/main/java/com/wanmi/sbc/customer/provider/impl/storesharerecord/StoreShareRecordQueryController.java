package com.wanmi.sbc.customer.provider.impl.storesharerecord;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storesharerecord.StoreShareRecordQueryProvider;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordPageRequest;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordQueryRequest;
import com.wanmi.sbc.customer.api.response.storesharerecord.StoreShareRecordPageResponse;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordListRequest;
import com.wanmi.sbc.customer.api.response.storesharerecord.StoreShareRecordListResponse;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordByIdRequest;
import com.wanmi.sbc.customer.api.response.storesharerecord.StoreShareRecordByIdResponse;
import com.wanmi.sbc.customer.bean.vo.StoreShareRecordVO;
import com.wanmi.sbc.customer.storesharerecord.service.StoreShareRecordService;
import com.wanmi.sbc.customer.storesharerecord.model.root.StoreShareRecord;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>商城分享查询服务接口实现</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:48:42
 */
@RestController
@Validated
public class StoreShareRecordQueryController implements StoreShareRecordQueryProvider {
	@Autowired
	private StoreShareRecordService storeShareRecordService;

	@Override
	public BaseResponse<StoreShareRecordPageResponse> page(@RequestBody @Valid StoreShareRecordPageRequest storeShareRecordPageReq) {
		StoreShareRecordQueryRequest queryReq = new StoreShareRecordQueryRequest();
		KsBeanUtil.copyPropertiesThird(storeShareRecordPageReq, queryReq);
		Page<StoreShareRecord> storeShareRecordPage = storeShareRecordService.page(queryReq);
		Page<StoreShareRecordVO> newPage = storeShareRecordPage.map(entity -> storeShareRecordService.wrapperVo(entity));
		MicroServicePage<StoreShareRecordVO> microPage = new MicroServicePage<>(newPage, storeShareRecordPageReq.getPageable());
		StoreShareRecordPageResponse finalRes = new StoreShareRecordPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<StoreShareRecordListResponse> list(@RequestBody @Valid StoreShareRecordListRequest storeShareRecordListReq) {
		StoreShareRecordQueryRequest queryReq = new StoreShareRecordQueryRequest();
		KsBeanUtil.copyPropertiesThird(storeShareRecordListReq, queryReq);
		List<StoreShareRecord> storeShareRecordList = storeShareRecordService.list(queryReq);
		List<StoreShareRecordVO> newList = storeShareRecordList.stream().map(entity -> storeShareRecordService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new StoreShareRecordListResponse(newList));
	}

	@Override
	public BaseResponse<StoreShareRecordByIdResponse> getById(@RequestBody @Valid StoreShareRecordByIdRequest storeShareRecordByIdRequest) {
		StoreShareRecord storeShareRecord = storeShareRecordService.getById(storeShareRecordByIdRequest.getShareId());
		return BaseResponse.success(new StoreShareRecordByIdResponse(storeShareRecordService.wrapperVo(storeShareRecord)));
	}

}

