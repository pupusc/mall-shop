package com.wanmi.sbc.setting.provider.impl.weibologinset;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.weibologinset.WeiboLoginSetQueryProvider;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetPageRequest;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetQueryRequest;
import com.wanmi.sbc.setting.api.response.weibologinset.WeiboLoginSetPageResponse;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetListRequest;
import com.wanmi.sbc.setting.api.response.weibologinset.WeiboLoginSetListResponse;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetByIdRequest;
import com.wanmi.sbc.setting.api.response.weibologinset.WeiboLoginSetByIdResponse;
import com.wanmi.sbc.setting.bean.vo.WeiboLoginSetVO;
import com.wanmi.sbc.setting.weibologinset.service.WeiboLoginSetService;
import com.wanmi.sbc.setting.weibologinset.model.root.WeiboLoginSet;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>微信登录配置查询服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:17:06
 */
@RestController
@Validated
public class WeiboLoginSetQueryController implements WeiboLoginSetQueryProvider {
	@Autowired
	private WeiboLoginSetService weiboLoginSetService;

	@Override
	public BaseResponse<WeiboLoginSetPageResponse> page(@RequestBody @Valid WeiboLoginSetPageRequest weiboLoginSetPageReq) {
		WeiboLoginSetQueryRequest queryReq = new WeiboLoginSetQueryRequest();
		KsBeanUtil.copyPropertiesThird(weiboLoginSetPageReq, queryReq);
		Page<WeiboLoginSet> weiboLoginSetPage = weiboLoginSetService.page(queryReq);
		Page<WeiboLoginSetVO> newPage = weiboLoginSetPage.map(entity -> weiboLoginSetService.wrapperVo(entity));
		MicroServicePage<WeiboLoginSetVO> microPage = new MicroServicePage<>(newPage, weiboLoginSetPageReq.getPageable());
		WeiboLoginSetPageResponse finalRes = new WeiboLoginSetPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<WeiboLoginSetListResponse> list(@RequestBody @Valid WeiboLoginSetListRequest weiboLoginSetListReq) {
		WeiboLoginSetQueryRequest queryReq = new WeiboLoginSetQueryRequest();
		KsBeanUtil.copyPropertiesThird(weiboLoginSetListReq, queryReq);
		List<WeiboLoginSet> weiboLoginSetList = weiboLoginSetService.list(queryReq);
		List<WeiboLoginSetVO> newList = weiboLoginSetList.stream().map(entity -> weiboLoginSetService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new WeiboLoginSetListResponse(newList));
	}

	@Override
	public BaseResponse<WeiboLoginSetByIdResponse> getById(@RequestBody @Valid WeiboLoginSetByIdRequest weiboLoginSetByIdRequest) {
		WeiboLoginSet weiboLoginSet = weiboLoginSetService.getById(weiboLoginSetByIdRequest.getWeiboSetId());
		return BaseResponse.success(new WeiboLoginSetByIdResponse(weiboLoginSetService.wrapperVo(weiboLoginSet)));
	}

}

