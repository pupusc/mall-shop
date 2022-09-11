package com.wanmi.sbc.customer.storereturnaddress.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.customer.api.constant.StoreReturnAddressCode;
import com.wanmi.sbc.customer.api.request.storereturnaddress.StoreReturnAddressQueryRequest;
import com.wanmi.sbc.customer.bean.vo.StoreReturnAddressVO;
import com.wanmi.sbc.customer.storereturnaddress.model.root.StoreReturnAddress;
import com.wanmi.sbc.customer.storereturnaddress.repository.StoreReturnAddressRepository;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.request.WareHouseQueryRequest;
import com.wanmi.sbc.erp.api.response.WareHouseListResponse;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressListRequest;
import com.wanmi.sbc.setting.bean.vo.PlatformAddressVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>店铺退货地址表业务逻辑</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
@Service("StoreReturnAddressService")
public class StoreReturnAddressService {
	@Autowired
	private StoreReturnAddressRepository storeReturnAddressRepository;

	@Autowired
	private PlatformAddressQueryProvider platformAddressQueryProvider;

//	@Autowired
//	private GuanyierpProvider guanyierpProvider;



	private final static int LABEL_MAX_LENGTH = 100;

	/**
	 * 新增店铺退货地址表
	 * @author dyt
	 */
	@Transactional
	public void add(StoreReturnAddress entity) {
		if(Objects.isNull(entity.getIsDefaultAddress())){
			entity.setIsDefaultAddress(Boolean.FALSE);
		}
		List<StoreReturnAddress> addresses = this.list(StoreReturnAddressQueryRequest.builder().storeId(entity.getStoreId()).delFlag(DeleteFlag.NO).build());
		if (CollectionUtils.isNotEmpty(addresses)) {
			//限制数量20个
			if (addresses.size() >= LABEL_MAX_LENGTH) {
				throw new SbcRuntimeException(StoreReturnAddressCode.MAX_LENGTH_LIMIT);
			}
			//将其他默认地址变为非默认
			if (Boolean.TRUE.equals(entity.getIsDefaultAddress())) {
				addresses.stream().filter(a -> Boolean.TRUE.equals(a.getIsDefaultAddress()))
						.forEach(a -> a.setIsDefaultAddress(Boolean.FALSE));
				storeReturnAddressRepository.saveAll(addresses);
			}
		} else {
			//第一次必须是默认
			entity.setIsDefaultAddress(Boolean.TRUE);
		}
		entity.setAddressId(UUIDUtil.getUUID());
		entity.setDelFlag(DeleteFlag.NO);
		entity.setUpdateTime(LocalDateTime.now());
		storeReturnAddressRepository.save(entity);
	}


	/**
	 * 新增店铺退货地址表
	 * @author wugongjiang
	 */
//	@Transactional
//	public void batchAdd() {
//		//todo 获取店铺编号
//		List<StoreReturnAddress> addresses = new ArrayList<>();
//		WareHouseQueryRequest wareHouseQueryRequest = WareHouseQueryRequest.builder().hasDelData(false).build();
//		BaseResponse<WareHouseListResponse> responseBaseResponse = guanyierpProvider.getWareHouseList(wareHouseQueryRequest);
//		if (!CollectionUtils.isEmpty(responseBaseResponse.getContext().getWareHouseVOList())){
//			responseBaseResponse.getContext().getWareHouseVOList().stream().forEach(erpWareHouseVO -> {
//				StoreReturnAddress storeReturnAddress = new StoreReturnAddress();
//				storeReturnAddress.setAddressId(erpWareHouseVO.getCode());
//				storeReturnAddress.setCompanyInfoId(25L);
//				storeReturnAddress.setStoreId(123456881L);
//				storeReturnAddress.setIsDefaultAddress(Boolean.FALSE);
//				storeReturnAddress.setReturnAddress(erpWareHouseVO.getAddress());
//				storeReturnAddress.setCreatePerson("system");
//				addresses.add(storeReturnAddress);
//			});
//		}
//		storeReturnAddressRepository.saveAll(addresses);
//	}


	/**
	 * 修改店铺退货地址表
	 * @author dyt
	 */
	@Transactional
	public void modify(StoreReturnAddress entity) {
		if (Boolean.TRUE.equals(entity.getIsDefaultAddress())) {
			//将其他默认地址变为非默认
			List<StoreReturnAddress> addresses = this.list(StoreReturnAddressQueryRequest.builder().storeId(entity.getStoreId()).delFlag(DeleteFlag.NO).build());
			if (CollectionUtils.isNotEmpty(addresses)) {
				List<StoreReturnAddress> addrList = addresses.stream()
						.filter(a ->(!a.getAddressId().equals(entity.getAddressId())) && Boolean.TRUE.equals(a.getIsDefaultAddress()))
						.collect(Collectors.toList());
				addrList.forEach(a -> a.setIsDefaultAddress(Boolean.FALSE));
				storeReturnAddressRepository.saveAll(addrList);
			}
		}else if(Objects.isNull(entity.getIsDefaultAddress())){
			entity.setIsDefaultAddress(Boolean.FALSE);
		}
		storeReturnAddressRepository.save(entity);
	}

	/**
	 * 单个删除店铺退货地址表
	 * @author dyt
	 */
	@Transactional
	public void deleteById(StoreReturnAddress entity) {
		entity.setDeleteTime(LocalDateTime.now());
		storeReturnAddressRepository.save(entity);
	}

	/**
	 * 批量删除店铺退货地址表
	 * @author dyt
	 */
	@Transactional
	public void deleteByIdList(List<StoreReturnAddress> infos) {
		storeReturnAddressRepository.saveAll(infos);
	}

	/**
	 * 单个查询店铺退货地址表
	 * @author dyt
	 */
	public StoreReturnAddress getOne(String id, Long storeId){
		return storeReturnAddressRepository.findByAddressIdAndStoreIdAndDelFlag(id, storeId, DeleteFlag.NO)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "店铺退货地址不存在"));
	}

	/**
	 * 分页查询店铺退货地址表
	 * @author dyt
	 */
	public Page<StoreReturnAddress> page(StoreReturnAddressQueryRequest queryReq){
		return storeReturnAddressRepository.findAll(
				StoreReturnAddressWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询店铺退货地址表
	 * @author dyt
	 */
	public List<StoreReturnAddress> list(StoreReturnAddressQueryRequest queryReq){
		return storeReturnAddressRepository.findAll(StoreReturnAddressWhereCriteriaBuilder.build(queryReq), queryReq.getSort());
	}

	/**
	 * 填充省市区
	 * @param details
	 */
	public void fillArea(List<StoreReturnAddressVO> details){
		if(CollectionUtils.isNotEmpty(details)){
			List<String> addrIds = new ArrayList<>();
			details.forEach(detail -> {
				addrIds.add(Objects.toString(detail.getProvinceId()));
				addrIds.add(Objects.toString(detail.getCityId()));
				addrIds.add(Objects.toString(detail.getAreaId()));
				addrIds.add(Objects.toString(detail.getStreetId()));
			});
			List<PlatformAddressVO> voList = platformAddressQueryProvider.list(PlatformAddressListRequest.builder().addrIdList(addrIds).build()).getContext().getPlatformAddressVOList();
			if(CollectionUtils.isNotEmpty(voList)){
				Map<String, String> addrMap = voList.stream().collect(Collectors.toMap(PlatformAddressVO::getAddrId, PlatformAddressVO::getAddrName));
				details.forEach(detail -> {
					detail.setProvinceName(addrMap.get(Objects.toString(detail.getProvinceId())));
					detail.setCityName(addrMap.get(Objects.toString(detail.getCityId())));
					detail.setAreaName(addrMap.get(Objects.toString(detail.getAreaId())));
					detail.setStreetName(addrMap.get(Objects.toString(detail.getStreetId())));
				});
			}
		}
	}

	/**
	 * 将实体包装成VO
	 * @author dyt
	 */
	public StoreReturnAddressVO wrapperVo(StoreReturnAddress storeReturnAddress) {
		if (storeReturnAddress != null){
			return KsBeanUtil.convert(storeReturnAddress, StoreReturnAddressVO.class);
		}
		return null;
	}
}

