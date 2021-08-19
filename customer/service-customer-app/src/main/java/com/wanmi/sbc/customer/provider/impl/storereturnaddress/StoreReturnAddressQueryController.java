package com.wanmi.sbc.customer.provider.impl.storereturnaddress;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storereturnaddress.StoreReturnAddressQueryProvider;
import com.wanmi.sbc.customer.api.request.storereturnaddress.StoreReturnAddressPageRequest;
import com.wanmi.sbc.customer.api.request.storereturnaddress.StoreReturnAddressQueryRequest;
import com.wanmi.sbc.customer.api.response.storereturnaddress.StoreReturnAddressPageResponse;
import com.wanmi.sbc.customer.api.request.storereturnaddress.StoreReturnAddressListRequest;
import com.wanmi.sbc.customer.api.response.storereturnaddress.StoreReturnAddressListResponse;
import com.wanmi.sbc.customer.api.request.storereturnaddress.StoreReturnAddressByIdRequest;
import com.wanmi.sbc.customer.api.response.storereturnaddress.StoreReturnAddressByIdResponse;
import com.wanmi.sbc.customer.bean.vo.StoreReturnAddressVO;
import com.wanmi.sbc.customer.storereturnaddress.service.StoreReturnAddressService;
import com.wanmi.sbc.customer.storereturnaddress.model.root.StoreReturnAddress;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>店铺退货地址表查询服务接口实现</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
@RestController
@Validated
public class StoreReturnAddressQueryController implements StoreReturnAddressQueryProvider {
	@Autowired
	private StoreReturnAddressService storeReturnAddressService;

	@Override
	public BaseResponse<StoreReturnAddressPageResponse> page(@RequestBody @Valid StoreReturnAddressPageRequest storeReturnAddressPageReq) {
		StoreReturnAddressQueryRequest queryReq = KsBeanUtil.convert(storeReturnAddressPageReq, StoreReturnAddressQueryRequest.class);
		Page<StoreReturnAddress> storeReturnAddressPage = storeReturnAddressService.page(queryReq);
		Page<StoreReturnAddressVO> newPage = storeReturnAddressPage.map(entity -> storeReturnAddressService.wrapperVo(entity));
		MicroServicePage<StoreReturnAddressVO> microPage = new MicroServicePage<>(newPage, storeReturnAddressPageReq.getPageable());
		StoreReturnAddressPageResponse finalRes = new StoreReturnAddressPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<StoreReturnAddressListResponse> list(@RequestBody @Valid StoreReturnAddressListRequest storeReturnAddressListReq) {
		StoreReturnAddressQueryRequest queryReq = KsBeanUtil.convert(storeReturnAddressListReq, StoreReturnAddressQueryRequest.class);
		queryReq.putSort("isDefaultAddress", "desc");
		queryReq.putSort("updateTime", "desc");
		List<StoreReturnAddress> storeReturnAddressList = storeReturnAddressService.list(queryReq);
		List<StoreReturnAddressVO> newList = storeReturnAddressList.stream().map(entity -> storeReturnAddressService.wrapperVo(entity)).collect(Collectors.toList());
		if(Boolean.TRUE.equals(storeReturnAddressListReq.getShowAreaNameFlag())){
			storeReturnAddressService.fillArea(newList);
		}
		return BaseResponse.success(new StoreReturnAddressListResponse(newList));
	}

	@Override
	public BaseResponse<StoreReturnAddressByIdResponse> getById(@RequestBody @Valid StoreReturnAddressByIdRequest storeReturnAddressByIdRequest) {
		StoreReturnAddress storeReturnAddress =
		storeReturnAddressService.getOne(storeReturnAddressByIdRequest.getAddressId(), storeReturnAddressByIdRequest.getStoreId());
		StoreReturnAddressVO vo = storeReturnAddressService.wrapperVo(storeReturnAddress);
		if(Boolean.TRUE.equals(storeReturnAddressByIdRequest.getShowAreaName())){
			storeReturnAddressService.fillArea(Collections.singletonList(vo));
		}
		return BaseResponse.success(new StoreReturnAddressByIdResponse(vo));
	}

}

