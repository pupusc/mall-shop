package com.wanmi.sbc.setting.provider.impl.evaluateratio;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.evaluateratio.EvaluateRatioQueryProvider;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioPageRequest;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioQueryRequest;
import com.wanmi.sbc.setting.api.response.evaluateratio.EvaluateRatioPageResponse;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioListRequest;
import com.wanmi.sbc.setting.api.response.evaluateratio.EvaluateRatioListResponse;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioByIdRequest;
import com.wanmi.sbc.setting.api.response.evaluateratio.EvaluateRatioByIdResponse;
import com.wanmi.sbc.setting.bean.vo.EvaluateRatioVO;
import com.wanmi.sbc.setting.evaluateratio.service.EvaluateRatioService;
import com.wanmi.sbc.setting.evaluateratio.model.root.EvaluateRatio;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>商品评价系数设置查询服务接口实现</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@RestController
@Validated
public class EvaluateRatioQueryController implements EvaluateRatioQueryProvider {
	@Autowired
	private EvaluateRatioService evaluateRatioService;

	@Override
	public BaseResponse<EvaluateRatioPageResponse> page(@RequestBody @Valid EvaluateRatioPageRequest evaluateRatioPageReq) {
		EvaluateRatioQueryRequest queryReq = new EvaluateRatioQueryRequest();
		KsBeanUtil.copyPropertiesThird(evaluateRatioPageReq, queryReq);
		Page<EvaluateRatio> evaluateRatioPage = evaluateRatioService.page(queryReq);
		Page<EvaluateRatioVO> newPage = evaluateRatioPage.map(entity -> evaluateRatioService.wrapperVo(entity));
		MicroServicePage<EvaluateRatioVO> microPage = new MicroServicePage<>(newPage, evaluateRatioPageReq.getPageable());
		EvaluateRatioPageResponse finalRes = new EvaluateRatioPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<EvaluateRatioListResponse> list(@RequestBody @Valid EvaluateRatioListRequest evaluateRatioListReq) {
		EvaluateRatioQueryRequest queryReq = new EvaluateRatioQueryRequest();
		KsBeanUtil.copyPropertiesThird(evaluateRatioListReq, queryReq);
		List<EvaluateRatio> evaluateRatioList = evaluateRatioService.list(queryReq);
		List<EvaluateRatioVO> newList = evaluateRatioList.stream().map(entity -> evaluateRatioService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new EvaluateRatioListResponse(newList));
	}

	@Override
	public BaseResponse<EvaluateRatioByIdResponse> getById(@RequestBody @Valid EvaluateRatioByIdRequest evaluateRatioByIdRequest) {
		EvaluateRatio evaluateRatio = evaluateRatioService.getById(evaluateRatioByIdRequest.getRatioId());
		return BaseResponse.success(new EvaluateRatioByIdResponse(evaluateRatioService.wrapperVo(evaluateRatio)));
	}

	/**
	 * 获取一条数据
	 *
	 * @return
	 */
	@Override
	public BaseResponse<EvaluateRatioByIdResponse> findOne() {
		EvaluateRatio evaluateRatio = evaluateRatioService.findOne();
		return BaseResponse.success(new EvaluateRatioByIdResponse(evaluateRatioService.wrapperVo(evaluateRatio)));
	}
}

