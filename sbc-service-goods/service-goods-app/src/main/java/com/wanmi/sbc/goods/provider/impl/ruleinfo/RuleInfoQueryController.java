package com.wanmi.sbc.goods.provider.impl.ruleinfo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.ruleinfo.RuleInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoPageRequest;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoQueryRequest;
import com.wanmi.sbc.goods.api.response.ruleinfo.RuleInfoPageResponse;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoListRequest;
import com.wanmi.sbc.goods.api.response.ruleinfo.RuleInfoListResponse;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoByIdRequest;
import com.wanmi.sbc.goods.api.response.ruleinfo.RuleInfoByIdResponse;
import com.wanmi.sbc.goods.bean.vo.RuleInfoVO;
import com.wanmi.sbc.goods.ruleinfo.service.RuleInfoService;
import com.wanmi.sbc.goods.ruleinfo.model.root.RuleInfo;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>规则说明查询服务接口实现</p>
 * @author zxd
 * @date 2020-05-25 18:55:56
 */
@RestController
@Validated
public class RuleInfoQueryController implements RuleInfoQueryProvider {
	@Autowired
	private RuleInfoService ruleInfoService;

	@Override
	public BaseResponse<RuleInfoPageResponse> page(@RequestBody @Valid RuleInfoPageRequest ruleInfoPageReq) {
		RuleInfoQueryRequest queryReq = KsBeanUtil.convert(ruleInfoPageReq, RuleInfoQueryRequest.class);
		Page<RuleInfo> ruleInfoPage = ruleInfoService.page(queryReq);
		Page<RuleInfoVO> newPage = ruleInfoPage.map(entity -> ruleInfoService.wrapperVo(entity));
		MicroServicePage<RuleInfoVO> microPage = new MicroServicePage<>(newPage, ruleInfoPageReq.getPageable());
		RuleInfoPageResponse finalRes = new RuleInfoPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<RuleInfoListResponse> list(@RequestBody @Valid RuleInfoListRequest ruleInfoListReq) {
		RuleInfoQueryRequest queryReq = KsBeanUtil.convert(ruleInfoListReq, RuleInfoQueryRequest.class);
		List<RuleInfo> ruleInfoList = ruleInfoService.list(queryReq);
		List<RuleInfoVO> newList = ruleInfoList.stream().map(entity -> ruleInfoService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new RuleInfoListResponse(newList));
	}

	@Override
	public BaseResponse<RuleInfoByIdResponse> getById(@RequestBody @Valid RuleInfoByIdRequest ruleInfoByIdRequest) {
		RuleInfo ruleInfo =
		ruleInfoService.getOne(ruleInfoByIdRequest.getId());
		return BaseResponse.success(new RuleInfoByIdResponse(ruleInfoService.wrapperVo(ruleInfo)));
	}

}

