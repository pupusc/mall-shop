package com.wanmi.sbc.goods.provider.impl.flashsaleactivity;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.flashsaleactivity.FlashSaleActivityQueryProvider;
import com.wanmi.sbc.goods.api.request.flashsaleactivity.FlashSaleActivityListRequest;
import com.wanmi.sbc.goods.api.request.flashsaleactivity.FlashSaleActivityPageRequest;
import com.wanmi.sbc.goods.api.request.flashsaleactivity.FlashSaleActivityQueryRequest;
import com.wanmi.sbc.goods.api.response.flashsaleactivity.FlashSaleActivityListNewResponse;
import com.wanmi.sbc.goods.api.response.flashsaleactivity.FlashSaleActivityListResponse;
import com.wanmi.sbc.goods.api.response.flashsaleactivity.FlashSaleActivityPageResponse;
import com.wanmi.sbc.goods.api.response.flashsaleactivity.FlashSaleActivityResponse;
import com.wanmi.sbc.goods.bean.vo.FlashSaleActivityVO;
import com.wanmi.sbc.goods.flashsaleactivity.FlashSaleActivityService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>抢购活动查询服务接口实现</p>
 * @author bob
 * @date 2019-06-11 14:54:31
 */
@RestController
@Validated
public class FlashSaleActivityQueryController implements FlashSaleActivityQueryProvider {

	@Autowired
	private FlashSaleActivityService flashSaleActivityService;

	@Override
	public BaseResponse<FlashSaleActivityPageResponse> page(@RequestBody @Valid FlashSaleActivityPageRequest
																		flashSaleActivityPageRequest) {
		FlashSaleActivityQueryRequest queryRequest = new FlashSaleActivityQueryRequest();
		KsBeanUtil.copyPropertiesThird(flashSaleActivityPageRequest, queryRequest);
		Page<FlashSaleActivityResponse> pages = flashSaleActivityService.page(queryRequest);
		List<FlashSaleActivityVO> voList = wraperVos(pages.getContent());
		FlashSaleActivityPageResponse response = FlashSaleActivityPageResponse.builder()
				.flashSaleActivityVOPage(new MicroServicePage<>(voList, flashSaleActivityPageRequest.getPageable(), pages.getTotalElements()))
				.build();
		return BaseResponse.success(response);
	}

	@Override
	public BaseResponse<FlashSaleActivityListResponse> list(@RequestBody @Valid FlashSaleActivityListRequest flashSaleActivityListRequest) {
		FlashSaleActivityQueryRequest queryReq = new FlashSaleActivityQueryRequest();
		KsBeanUtil.copyPropertiesThird(flashSaleActivityListRequest, queryReq);
		List<FlashSaleActivityResponse> flashSaleGoodsList = flashSaleActivityService.list(queryReq);
		List<FlashSaleActivityVO> voList = wraperVos(flashSaleGoodsList);
		return BaseResponse.success(FlashSaleActivityListResponse.builder().flashSaleActivityVOList(voList).build());
	}

	@Override
	public BaseResponse<FlashSaleActivityListNewResponse> listNew(@Valid FlashSaleActivityQueryRequest request) {
		List<FlashSaleActivityVO> list = flashSaleActivityService.listNew(request);
		return BaseResponse.success(FlashSaleActivityListNewResponse.builder().flashSaleActivityVOList(list).build());
	}

	private List<FlashSaleActivityVO> wraperVos(List<FlashSaleActivityResponse> flashSaleActivityResponseList) {
		if (CollectionUtils.isNotEmpty(flashSaleActivityResponseList)) {
			return flashSaleActivityResponseList.stream().map(info -> {
				FlashSaleActivityVO vo = new FlashSaleActivityVO();
				KsBeanUtil.copyPropertiesThird(info, vo);
				return vo;
			}).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
}

