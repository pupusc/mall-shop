package com.wanmi.sbc.goods.provider.impl.restrictedrecord;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.restrictedrecord.RestrictedRecordQueryProvider;
import com.wanmi.sbc.goods.api.request.restrictedrecord.RestrictedRecordPageRequest;
import com.wanmi.sbc.goods.api.request.restrictedrecord.RestrictedRecordQueryRequest;
import com.wanmi.sbc.goods.api.response.restrictedrecord.RestrictedRecordPageResponse;
import com.wanmi.sbc.goods.api.request.restrictedrecord.RestrictedRecordListRequest;
import com.wanmi.sbc.goods.api.response.restrictedrecord.RestrictedRecordListResponse;
import com.wanmi.sbc.goods.api.request.restrictedrecord.RestrictedRecordByIdRequest;
import com.wanmi.sbc.goods.api.response.restrictedrecord.RestrictedRecordByIdResponse;
import com.wanmi.sbc.goods.bean.vo.RestrictedRecordVO;
import com.wanmi.sbc.goods.restrictedrecord.service.RestrictedRecordService;
import com.wanmi.sbc.goods.restrictedrecord.model.root.RestrictedRecord;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>限售查询服务接口实现</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
@RestController
@Validated
public class RestrictedRecordQueryController implements RestrictedRecordQueryProvider {
	@Autowired
	private RestrictedRecordService restrictedRecordService;

	@Override
	public BaseResponse<RestrictedRecordPageResponse> page(@RequestBody @Valid RestrictedRecordPageRequest restrictedRecordPageReq) {
		RestrictedRecordQueryRequest queryReq = new RestrictedRecordQueryRequest();
		KsBeanUtil.copyPropertiesThird(restrictedRecordPageReq, queryReq);
		Page<RestrictedRecord> restrictedRecordPage = restrictedRecordService.page(queryReq);
		Page<RestrictedRecordVO> newPage = restrictedRecordPage.map(entity -> restrictedRecordService.wrapperVo(entity));
		MicroServicePage<RestrictedRecordVO> microPage = new MicroServicePage<>(newPage, restrictedRecordPageReq.getPageable());
		RestrictedRecordPageResponse finalRes = new RestrictedRecordPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<RestrictedRecordListResponse> list(@RequestBody @Valid RestrictedRecordListRequest restrictedRecordListReq) {
		RestrictedRecordQueryRequest queryReq = new RestrictedRecordQueryRequest();
		KsBeanUtil.copyPropertiesThird(restrictedRecordListReq, queryReq);
		List<RestrictedRecord> restrictedRecordList = restrictedRecordService.list(queryReq);
		List<RestrictedRecordVO> newList = restrictedRecordList.stream().map(entity -> restrictedRecordService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new RestrictedRecordListResponse(newList));
	}

	@Override
	public BaseResponse<RestrictedRecordByIdResponse> getById(@RequestBody @Valid RestrictedRecordByIdRequest restrictedRecordByIdRequest) {
		RestrictedRecord restrictedRecord = restrictedRecordService.getById(restrictedRecordByIdRequest.getRecordId());
		return BaseResponse.success(new RestrictedRecordByIdResponse(restrictedRecordService.wrapperVo(restrictedRecord)));
	}

}

