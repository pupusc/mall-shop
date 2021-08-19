package com.wanmi.sbc.setting.platformaddress.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressQueryRequest;
import com.wanmi.sbc.setting.bean.vo.PlatformAddressVO;
import com.wanmi.sbc.setting.platformaddress.model.root.PlatformAddress;
import com.wanmi.sbc.setting.platformaddress.repository.PlatformAddressRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>平台地址信息业务逻辑</p>
 * @author dyt
 * @date 2020-03-30 14:39:57
 */
@Service("PlatformAddressService")
public class PlatformAddressService {
	@Autowired
	private PlatformAddressRepository platformAddressRepository;

	/**
	 * 新增平台地址信息
	 * @author dyt
	 */
	@Transactional
	public PlatformAddress add(PlatformAddress entity) {
		entity.setId(entity.getAddrId());
        entity.setDelFlag(DeleteFlag.NO);
		entity.setDataType(1);
		entity.setCreateTime(LocalDateTime.now());
		entity.setUpdateTime(LocalDateTime.now());
		platformAddressRepository.save(entity);
		return entity;
	}

	/**
	 * 修改平台地址信息
	 * @author dyt
	 */
	@Transactional
	public PlatformAddress modify(PlatformAddress entity) {
		platformAddressRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除平台地址信息
	 * @author dyt
	 */
	@Transactional
	public void deleteById(String id) {
		platformAddressRepository.deleteById(id);
	}

	/**
	 * 批量删除平台地址信息
	 * @author dyt
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		platformAddressRepository.deleteByIdList(ids);
	}

	/**
	 * 单个查询平台地址信息
	 * @author dyt
	 */
	public PlatformAddress getOne(String id){
		return platformAddressRepository.findByIdAndDelFlag(id, DeleteFlag.NO)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "平台地址信息不存在"));
	}

	/**
	 * 分页查询平台地址信息
	 * @author dyt
	 */
	public Page<PlatformAddress> page(PlatformAddressQueryRequest queryReq){
		return platformAddressRepository.findAll(
				PlatformAddressWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询平台地址信息
	 * @author dyt
	 */
	public List<PlatformAddress> list(PlatformAddressQueryRequest queryReq){
		return platformAddressRepository.findAll(PlatformAddressWhereCriteriaBuilder.build(queryReq), Sort.by(Arrays.asList(Sort.Order.desc("dataType"), Sort.Order.asc("sortNo"), Sort.Order.desc("createTime"))));
	}

	/**
	 * id转名称
	 * @param provId 省id
	 * @param cityId 市id
	 * @param districtId 区id
	 * @param streetId 街道id
	 * @return
	 */
	public String wrapperNameByIds(String provId, String cityId, String districtId, String streetId) {
		if(provId == null){
			return "";
		}
		Map<String, String> platformAddressMap = this.list(PlatformAddressQueryRequest.builder().addrIdList(
				Arrays.asList(provId, cityId, districtId, streetId))
				.build()).stream().filter(a -> StringUtils.isNotBlank(a.getAddrName()))
				.collect(Collectors.toMap(PlatformAddress::getAddrId, PlatformAddress::getAddrName));
		StringBuilder name = new StringBuilder();
		if (platformAddressMap.containsKey(provId)) {
			name.append(platformAddressMap.get(provId));
			if (StringUtils.isNotBlank(cityId) && platformAddressMap.containsKey(cityId)) {
				name.append(platformAddressMap.get(cityId));
				if (StringUtils.isNotBlank(districtId) && platformAddressMap.containsKey(districtId)) {
					name.append(platformAddressMap.get(districtId));
					if (StringUtils.isNotBlank(streetId) && platformAddressMap.containsKey(streetId)) {
						name.append(platformAddressMap.get(streetId));
					}
				}
			}
		}
		return name.toString();
	}

	/**
	 * 将实体包装成VO
	 * @author dyt
	 */
	public PlatformAddressVO wrapperVo(PlatformAddress platformAddress) {
		if (platformAddress != null){
			PlatformAddressVO platformAddressVO = KsBeanUtil.convert(platformAddress, PlatformAddressVO.class);
            platformAddressVO.setLeafFlag(Boolean.TRUE);
			return platformAddressVO;
		}
		return null;
	}

	/**
	 * 列表查询平台省市地址信息
	 * @author dyt
	 */
	public List<PlatformAddress> provinceCityList(List<String> addrIds){
		return platformAddressRepository.findAllProvinceAndCity(addrIds);
	}

	/**
	 * 统计数量
	 * @author yhy
	 */
	public int countNum(String id){
		return platformAddressRepository.countById(id);
	}

	/**
	 * 统计数量
	 * @author yhy
	 */
	public int countNumByIdAndParentId(String id,String addrParentId){
		return platformAddressRepository.countByIdAndParentId(id,addrParentId);
	}

	/**
	 * 统计数量
	 * @author yhy
	 */
	public int countNumByParentId(String addrParentId){
		return platformAddressRepository.countByParentId(addrParentId);
	}
}

