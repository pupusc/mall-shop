package com.wanmi.sbc.setting.provider.impl.evaluateratio;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.evaluateratio.EvaluateRatioSaveProvider;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioAddRequest;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioDelByIdListRequest;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioDelByIdRequest;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioModifyRequest;
import com.wanmi.sbc.setting.api.response.evaluateratio.EvaluateRatioAddResponse;
import com.wanmi.sbc.setting.api.response.evaluateratio.EvaluateRatioModifyResponse;
import com.wanmi.sbc.setting.bean.vo.EvaluateRatioVO;
import com.wanmi.sbc.setting.evaluateratio.model.root.EvaluateRatio;
import com.wanmi.sbc.setting.evaluateratio.service.EvaluateRatioService;
import com.wanmi.sbc.setting.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>商品评价系数设置保存服务接口实现</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@RestController
@Validated
public class EvaluateRatioSaveController implements EvaluateRatioSaveProvider {
	@Autowired
	private EvaluateRatioService evaluateRatioService;

	@Autowired
	RedisService redisService;

	@Override
	public BaseResponse<EvaluateRatioAddResponse> add(@RequestBody @Valid EvaluateRatioAddRequest evaluateRatioAddRequest) {
		EvaluateRatio evaluateRatio = new EvaluateRatio();
		KsBeanUtil.copyPropertiesThird(evaluateRatioAddRequest, evaluateRatio);
		return BaseResponse.success(new EvaluateRatioAddResponse(
				evaluateRatioService.wrapperVo(evaluateRatioService.add(evaluateRatio))));
	}

	@Override
	public BaseResponse<EvaluateRatioModifyResponse> modify(@RequestBody @Valid EvaluateRatioModifyRequest evaluateRatioModifyRequest) {
		EvaluateRatio evaluateRatio = new EvaluateRatio();
		KsBeanUtil.copyPropertiesThird(evaluateRatioModifyRequest, evaluateRatio);
		EvaluateRatioVO evaluateRatioVO = new EvaluateRatioVO();
		KsBeanUtil.copyPropertiesThird(evaluateRatioModifyRequest, evaluateRatioVO);
		redisService.setObj(CacheKeyConstant.EVALUATE_RATIO,evaluateRatioVO,-1);
		return BaseResponse.success(new EvaluateRatioModifyResponse(
				evaluateRatioService.wrapperVo(evaluateRatioService.modify(evaluateRatio))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid EvaluateRatioDelByIdRequest evaluateRatioDelByIdRequest) {
		evaluateRatioService.deleteById(evaluateRatioDelByIdRequest.getRatioId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid EvaluateRatioDelByIdListRequest evaluateRatioDelByIdListRequest) {
		evaluateRatioService.deleteByIdList(evaluateRatioDelByIdListRequest.getRatioIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

