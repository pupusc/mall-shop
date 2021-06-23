package com.wanmi.sbc.setting.provider.impl.qqloginset;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.qqloginset.QqLoginSetQueryProvider;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetPageRequest;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetQueryRequest;
import com.wanmi.sbc.setting.api.response.qqloginset.QqLoginSetPageResponse;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetListRequest;
import com.wanmi.sbc.setting.api.response.qqloginset.QqLoginSetListResponse;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetByIdRequest;
import com.wanmi.sbc.setting.api.response.qqloginset.QqLoginSetByIdResponse;
import com.wanmi.sbc.setting.bean.vo.QqLoginSetVO;
import com.wanmi.sbc.setting.qqloginset.service.QqLoginSetService;
import com.wanmi.sbc.setting.qqloginset.model.root.QqLoginSet;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>qq登录信息查询服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@RestController
@Validated
public class QqLoginSetQueryController implements QqLoginSetQueryProvider {
	@Autowired
	private QqLoginSetService qqLoginSetService;

	@Override
	public BaseResponse<QqLoginSetPageResponse> page(@RequestBody @Valid QqLoginSetPageRequest qqLoginSetPageReq) {
		QqLoginSetQueryRequest queryReq = new QqLoginSetQueryRequest();
		KsBeanUtil.copyPropertiesThird(qqLoginSetPageReq, queryReq);
		Page<QqLoginSet> qqLoginSetPage = qqLoginSetService.page(queryReq);
		Page<QqLoginSetVO> newPage = qqLoginSetPage.map(entity -> qqLoginSetService.wrapperVo(entity));
		MicroServicePage<QqLoginSetVO> microPage = new MicroServicePage<>(newPage, qqLoginSetPageReq.getPageable());
		QqLoginSetPageResponse finalRes = new QqLoginSetPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<QqLoginSetListResponse> list(@RequestBody @Valid QqLoginSetListRequest qqLoginSetListReq) {
		QqLoginSetQueryRequest queryReq = new QqLoginSetQueryRequest();
		KsBeanUtil.copyPropertiesThird(qqLoginSetListReq, queryReq);
		List<QqLoginSet> qqLoginSetList = qqLoginSetService.list(queryReq);
		List<QqLoginSetVO> newList = qqLoginSetList.stream().map(entity -> qqLoginSetService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new QqLoginSetListResponse(newList));
	}

	@Override
	public BaseResponse<QqLoginSetByIdResponse> getById(@RequestBody @Valid QqLoginSetByIdRequest qqLoginSetByIdRequest) {
		QqLoginSet qqLoginSet = qqLoginSetService.getById(qqLoginSetByIdRequest.getQqSetId());
		return BaseResponse.success(new QqLoginSetByIdResponse(qqLoginSetService.wrapperVo(qqLoginSet)));
	}

}

