package com.wanmi.sbc.setting.provider.impl.thirdaddress;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.thirdaddress.ThirdAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressListRequest;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressPageRequest;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressQueryRequest;
import com.wanmi.sbc.setting.api.response.thirdaddress.ThirdAddressListResponse;
import com.wanmi.sbc.setting.api.response.thirdaddress.ThirdAddressPageResponse;
import com.wanmi.sbc.setting.bean.vo.ThirdAddressPageVO;
import com.wanmi.sbc.setting.bean.vo.ThirdAddressVO;
import com.wanmi.sbc.setting.thirdaddress.model.root.ThirdAddress;
import com.wanmi.sbc.setting.thirdaddress.service.ThirdAddressService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * <p>第三方地址映射表查询服务接口实现</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@RestController
@Validated
public class ThirdAddressQueryController implements ThirdAddressQueryProvider {
	@Autowired
	private ThirdAddressService thirdAddressService;

	@Override
	public BaseResponse<ThirdAddressPageResponse> page(@RequestBody @Valid ThirdAddressPageRequest thirdAddressPageReq) {
		MicroServicePage<ThirdAddressPageVO> thirdAddressPage = thirdAddressService.page(thirdAddressPageReq);
		ThirdAddressPageResponse finalRes = new ThirdAddressPageResponse(thirdAddressPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<ThirdAddressListResponse> list(@RequestBody @Valid ThirdAddressListRequest request){
		ThirdAddressQueryRequest queryRequest = KsBeanUtil.convert(request,ThirdAddressQueryRequest.class);
		List<ThirdAddress> addresses = thirdAddressService.list(queryRequest);
		if (CollectionUtils.isEmpty(addresses)){
			return BaseResponse.success(new ThirdAddressListResponse(Collections.emptyList()));
		}
		List<ThirdAddressVO> addressVOS = KsBeanUtil.convert(addresses,ThirdAddressVO.class);
		return BaseResponse.success(new ThirdAddressListResponse(addressVOS));
	}
}

