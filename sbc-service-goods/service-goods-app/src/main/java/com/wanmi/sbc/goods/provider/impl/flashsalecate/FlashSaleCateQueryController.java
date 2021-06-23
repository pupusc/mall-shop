package com.wanmi.sbc.goods.provider.impl.flashsalecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.flashsalecate.FlashSaleCateQueryProvider;
import com.wanmi.sbc.goods.api.request.flashsalecate.FlashSaleCateListRequest;
import com.wanmi.sbc.goods.api.request.flashsalecate.FlashSaleCateQueryRequest;
import com.wanmi.sbc.goods.api.response.flashsalecate.FlashSaleCateListResponse;
import com.wanmi.sbc.goods.bean.vo.FlashSaleCateVO;
import com.wanmi.sbc.goods.flashsalecate.model.root.FlashSaleCate;
import com.wanmi.sbc.goods.flashsalecate.service.FlashSaleCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>秒杀分类查询服务接口实现</p>
 * @author yxz
 * @date 2019-06-11 10:11:15
 */
@RestController
@Validated
public class FlashSaleCateQueryController implements FlashSaleCateQueryProvider {
	@Autowired
	private FlashSaleCateService flashSaleCateService;

	@Override
	public BaseResponse<FlashSaleCateListResponse> list(@RequestBody @Valid FlashSaleCateListRequest flashSaleCateListReq) {
		FlashSaleCateQueryRequest queryReq = new FlashSaleCateQueryRequest();
		KsBeanUtil.copyPropertiesThird(flashSaleCateListReq, queryReq);
		List<FlashSaleCate> flashSaleCateList = flashSaleCateService.list(queryReq);
		List<FlashSaleCateVO> newList = flashSaleCateList.stream().map(entity -> flashSaleCateService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new FlashSaleCateListResponse(newList));
	}

}

