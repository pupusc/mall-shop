package com.wanmi.sbc.customer.provider.impl.storereturnaddress;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storereturnaddress.StoreReturnAddressProvider;
import com.wanmi.sbc.customer.api.request.storereturnaddress.*;
import com.wanmi.sbc.customer.storereturnaddress.model.root.StoreReturnAddress;
import com.wanmi.sbc.customer.storereturnaddress.service.StoreReturnAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>店铺退货地址表保存服务接口实现</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
@RestController
@Validated
public class StoreReturnAddressController implements StoreReturnAddressProvider {
	@Autowired
	private StoreReturnAddressService storeReturnAddressService;

	@Override
	public BaseResponse add(@RequestBody @Valid StoreReturnAddressAddRequest storeReturnAddressAddRequest) {
		StoreReturnAddress storeReturnAddress = KsBeanUtil.convert(storeReturnAddressAddRequest, StoreReturnAddress.class);
		storeReturnAddressService.add(storeReturnAddress);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse modify(@RequestBody @Valid StoreReturnAddressModifyRequest storeReturnAddressModifyRequest) {
		StoreReturnAddress address = storeReturnAddressService.getOne(storeReturnAddressModifyRequest.getAddressId(), storeReturnAddressModifyRequest.getStoreId());
		KsBeanUtil.copyPropertiesThird(storeReturnAddressModifyRequest, address);
		storeReturnAddressService.modify(address);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse modifyDefault(@RequestBody @Valid StoreReturnAddressDefaultRequest storeReturnAddressDefaultRequest) {
		StoreReturnAddress address = storeReturnAddressService.getOne(storeReturnAddressDefaultRequest.getAddressId(), storeReturnAddressDefaultRequest.getStoreId());
		address.setIsDefaultAddress(Boolean.TRUE);
		address.setUpdatePerson(storeReturnAddressDefaultRequest.getUpdatePerson());
		storeReturnAddressService.modify(address);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid StoreReturnAddressDelByIdRequest storeReturnAddressDelByIdRequest) {
		StoreReturnAddress address = storeReturnAddressService.getOne(storeReturnAddressDelByIdRequest.getAddressId(), storeReturnAddressDelByIdRequest.getStoreId());
		address.setDelFlag(DeleteFlag.YES);
		address.setDeletePerson(storeReturnAddressDelByIdRequest.getDeletePerson());
		storeReturnAddressService.deleteById(address);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid StoreReturnAddressDelByIdListRequest storeReturnAddressDelByIdListRequest) {
		List<StoreReturnAddress> storeReturnAddressList = storeReturnAddressDelByIdListRequest.getAddressIdList().stream()
			.map(AddressId -> {
				StoreReturnAddress storeReturnAddress = KsBeanUtil.convert(AddressId, StoreReturnAddress.class);
				storeReturnAddress.setDelFlag(DeleteFlag.YES);
				storeReturnAddress.setDeleteTime(LocalDateTime.now());
				return storeReturnAddress;
			}).collect(Collectors.toList());
		storeReturnAddressService.deleteByIdList(storeReturnAddressList);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse batchAdd() {
		 storeReturnAddressService.batchAdd();
		 return BaseResponse.SUCCESSFUL();
	}

}

