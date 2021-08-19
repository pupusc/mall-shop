package com.wanmi.sbc.setting.thirdaddress.service;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.constant.SettingErrorCode;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressQueryRequest;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressModifyRequest;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressPageRequest;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressQueryRequest;
import com.wanmi.sbc.setting.bean.dto.ThirdAddressDTO;
import com.wanmi.sbc.setting.bean.enums.AddrLevel;
import com.wanmi.sbc.setting.bean.vo.ThirdAddressPageVO;
import com.wanmi.sbc.setting.bean.vo.ThirdAddressVO;
import com.wanmi.sbc.setting.platformaddress.model.root.PlatformAddress;
import com.wanmi.sbc.setting.platformaddress.service.PlatformAddressService;
import com.wanmi.sbc.setting.thirdaddress.model.root.ThirdAddress;
import com.wanmi.sbc.setting.thirdaddress.repository.ThirdAddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>第三方地址映射表业务逻辑</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@Slf4j
@Service("ThirdAddressService")
public class ThirdAddressService {
	@Autowired
	private ThirdAddressRepository thirdAddressRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private PlatformAddressService platformAddressService;

	/**
	 * 修改第三方地址映射表
	 * @author dyt
	 */
	@Transactional
	public void modify(ThirdAddressModifyRequest request) {
		//四级街道，判空要对应
		if(StringUtils.isNotBlank(request.getPlatformStreetId()) && StringUtils.isBlank(request.getThirdStreetId())){
			throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
		}else if(StringUtils.isBlank(request.getPlatformStreetId()) && StringUtils.isNotBlank(request.getThirdStreetId())){
			throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
		}

		//验证平台街道是否被映射过
		if(StringUtils.isNotBlank(request.getPlatformStreetId()) && StringUtils.isNotBlank(request.getThirdStreetId())) {
			List<ThirdAddress> thirdAddressList = list(ThirdAddressQueryRequest.builder().platformAddrId(request.getPlatformStreetId()).thirdFlag(request.getThirdFlag()).build());
			if (CollectionUtils.isNotEmpty(thirdAddressList)) {
				if (thirdAddressList.stream().filter(a -> request.getThirdStreetId().equals(a.getThirdAddrId())).count() < 1) {
					throw new SbcRuntimeException(SettingErrorCode.ADDRESS_MAPPING_EXISTS);
				}
			}
		}

		//提取第三方地址数据
		Map<String, ThirdAddress> thirdAddressMap = list(ThirdAddressQueryRequest.builder().thirdAddrIdList(
				Arrays.asList(request.getThirdProvId(), request.getThirdCityId(), request.getThirdDistrictId(), request.getThirdStreetId()))
				.thirdFlag(request.getThirdFlag())
				.build()).stream().collect(Collectors.toMap(ThirdAddress::getThirdAddrId, t -> t));
		if(MapUtils.isEmpty(thirdAddressMap)){
			throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
		}

		//提取平台地址数据
		Map<String, PlatformAddress> platformAddressMap = platformAddressService.list(PlatformAddressQueryRequest.builder().addrIdList(
				Arrays.asList(request.getPlatformProvId(), request.getPlatformCityId(), request.getPlatformDistrictId(), request.getPlatformStreetId()))
				.build()).stream().collect(Collectors.toMap(PlatformAddress::getAddrId, t -> t));
		if(MapUtils.isEmpty(platformAddressMap)){
			throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
		}

		ThirdAddress thirdProv = thirdAddressMap.get(request.getThirdProvId());
		ThirdAddress thirdCity = thirdAddressMap.get(request.getThirdCityId());
		ThirdAddress thirdDistrict = thirdAddressMap.get(request.getThirdDistrictId());
		ThirdAddress thirdStreet = null;

		PlatformAddress platformProv = platformAddressMap.get(request.getPlatformProvId());
		PlatformAddress platformCity = platformAddressMap.get(request.getPlatformCityId());
		PlatformAddress platformDistrict = platformAddressMap.get(request.getPlatformDistrictId());
		PlatformAddress platformStreet = null;

		if(thirdProv == null || thirdCity == null || thirdDistrict == null
				|| platformProv == null || platformCity == null || platformDistrict == null ){
			throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
		}

		if(StringUtils.isNotBlank(request.getThirdStreetId())) {
			thirdStreet = thirdAddressMap.get(request.getThirdStreetId());
			if (thirdStreet == null) {
				throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
			}
		}

		if(StringUtils.isNotBlank(request.getPlatformStreetId())) {
			platformStreet = platformAddressMap.get(request.getPlatformStreetId());
		}

		if ((!this.contains(thirdProv.getAddrName(), platformProv.getAddrName()))
				|| (!AddrLevel.PROVINCE.equals(platformProv.getAddrLevel()))) {
			throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"请选择正确的省份"});
		}

		if ((!this.contains(thirdCity.getAddrName(), platformCity.getAddrName()))
				|| (!platformCity.getAddrParentId().equals(platformProv.getAddrId()))
				|| (!AddrLevel.CITY.equals(platformCity.getAddrLevel()))) {
			throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"请选择正确的城市"});
		}

		if ((!this.contains(thirdDistrict.getAddrName(), platformDistrict.getAddrName()))
				|| (!platformDistrict.getAddrParentId().equals(platformCity.getAddrId()))
				|| (!AddrLevel.DISTRICT.equals(platformDistrict.getAddrLevel()))) {
			throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"请选择正确的区县"});
		}

		if(thirdStreet != null) {
			if (platformStreet == null
					|| (!platformStreet.getAddrParentId().equals(platformDistrict.getAddrId()))
					|| (!AddrLevel.STREET.equals(platformStreet.getAddrLevel()))) {
				throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"请选择正确的街道"});
			}

			thirdStreet.setPlatformAddrId(platformStreet.getAddrId());
			thirdAddressRepository.saveAndFlush(thirdStreet);
		}
		thirdProv.setPlatformAddrId(platformProv.getAddrId());
		thirdCity.setPlatformAddrId(platformCity.getAddrId());
		thirdDistrict.setPlatformAddrId(platformDistrict.getAddrId());
		thirdAddressRepository.saveAndFlush(thirdProv);
		thirdAddressRepository.saveAndFlush(thirdCity);
		thirdAddressRepository.saveAndFlush(thirdDistrict);
	}

	/**
	 * 映射
	 * @param thirdPlatformType 第三方平台类型
	 */
	public void mapping(ThirdPlatformType thirdPlatformType) {
		log.info("linkedMall省市区映射开始");
		ThirdAddressQueryRequest queryRequest = ThirdAddressQueryRequest.builder().thirdFlag(thirdPlatformType).notAddrName("-").emplyPlatformAddrId(true).build();
		Long totalCount = thirdAddressRepository.count(ThirdAddressWhereCriteriaBuilder.build(queryRequest));
		if (totalCount > 0) {
			//提取平台地址的模糊名称数据
			List<PlatformAddress> platformAddrList = platformAddressService.list(PlatformAddressQueryRequest.builder().delFlag(DeleteFlag.NO).build());
			Map<String, String> platformAddrName = platformAddrList.stream().collect(Collectors.toMap(PlatformAddress::getAddrId, PlatformAddress::getAddrName));

			Integer pageSize = 2000;
			long pageCount = 0L;
			long m = totalCount % pageSize;
			if (m > 0) {
				pageCount = totalCount / pageSize + 1;
			} else {
				pageCount = totalCount / pageSize;
			}
			queryRequest.setPageSize(pageSize);
			//省市区优先处理
			queryRequest.putSort("level", SortType.ASC.toValue());

			int err = 0;
			for (int i = 0; i <= pageCount; i++) {
				queryRequest.setPageNum(i);
				try {
					//提取第三方地址
					List<ThirdAddress> thirdAddresses = thirdAddressRepository.findAll(ThirdAddressWhereCriteriaBuilder.build(queryRequest), queryRequest.getPageRequest()).getContent();
					//提取第三方父节点的数据
					List<String> parentIds = thirdAddresses.stream().filter(a -> (!AddrLevel.PROVINCE.equals(a.getLevel())) && StringUtils.isNotBlank(a.getThirdParentId())).map(ThirdAddress::getThirdParentId).collect(Collectors.toList());
					Map<String, ThirdAddress> parentAddrs = this.list(ThirdAddressQueryRequest.builder().thirdFlag(thirdPlatformType)
							.thirdAddrIdList(parentIds).build()).stream().collect(Collectors.toMap(ThirdAddress::getThirdAddrId, a -> a));

					thirdAddresses.stream()
							.filter(t -> StringUtils.isBlank(t.getPlatformAddrId()))
							.forEach(thirdAddress -> {
								String tmpName = thirdAddress.getAddrName();
								//根据交叉匹配和等级一致得到平台地址数据
								List<PlatformAddress> platformAddresses = platformAddrList.stream()
										.filter(p -> this.contains(p.getAddrName(), tmpName) && p.getAddrLevel().equals(thirdAddress.getLevel()))
										.sorted(Comparator.comparing((p) -> {
											if(p.getAddrName().equals(tmpName)){
												return -1;
											}else if(this.contains(p.getAddrName(), tmpName)){
												return 0;
											}else {
												return 1;
											}})
										).collect(Collectors.toList());
								if (CollectionUtils.isNotEmpty(platformAddresses)) {
									//省级别
									if (AddrLevel.PROVINCE.equals(thirdAddress.getLevel())) {
										thirdAddress.setPlatformAddrId(platformAddresses.get(0).getAddrId());
										this.save(thirdAddress);
									} else {
										//其他级别
										//第三方的父节点
										ThirdAddress thirdParentAddress = parentAddrs.getOrDefault(thirdAddress.getThirdParentId(), new ThirdAddress());
										PlatformAddress platformAddress = null;

										//比较父节点的映射平台地址id
										if(StringUtils.isNotBlank(thirdParentAddress.getPlatformAddrId())){
											platformAddress = platformAddresses.stream()
													.filter(address -> address.getAddrParentId().equals(thirdParentAddress.getPlatformAddrId()))
													.findFirst().orElse(null);
										}

										//比较父节点的名称
										if(platformAddress == null){
											String thirdParentName = thirdParentAddress.getAddrName();
											platformAddress = platformAddresses.stream()
													.filter(p -> this.contains(platformAddrName.get(p.getAddrParentId()), thirdParentName))
													.sorted(Comparator.comparing((p) -> {
														if(platformAddrName.get(p.getAddrParentId()).equals(thirdParentName)){
															return -1;
														}else if(this.contains(platformAddrName.get(p.getAddrParentId()), tmpName)){
															return 0;
														}else {
															return 1;
														}})
													).findFirst().orElse(null);
										}

										if (platformAddress != null) {
											thirdAddress.setPlatformAddrId(platformAddress.getAddrId());
											this.save(thirdAddress);
										}
									}
								}
							});
					err = 0;
				} catch (Exception e) {
					log.error("映射异常", e);
					if (err < 3) {
						i--;
					}
					err++;
				}
			}
		}
		log.info("linkedMall省市区映射结束");
	}

	@Transactional
	public void save(ThirdAddress thirdAddress) {
		thirdAddressRepository.saveAndFlush(thirdAddress);
	}


	/**
	 * 分页查询第三方地址映射表
	 * @author dyt
	 */
	public MicroServicePage<ThirdAddressPageVO> page(ThirdAddressPageRequest queryReq) {
		StringBuilder sql = new StringBuilder();
		sql.append("select street.third_addr_id streetId, street.addr_name streetName, street.platform_addr_id platformStreetId, ");
		sql.append("area.third_addr_id areaId, area.addr_name areaName, area.platform_addr_id platformAreaId,");
		sql.append("city.third_addr_id cityId, city.addr_name cityName, city.platform_addr_id platformCityId,");
		sql.append("prov.third_addr_id provId, prov.addr_name provName, prov.platform_addr_id platformProvId, ");
		sql.append("street.third_flag thirdFlag ");

		StringBuilder whereSql = new StringBuilder();
		whereSql.append("from third_address street ");
		whereSql.append("INNER JOIN third_address area on area.third_addr_id = street.third_parent_id ");
		whereSql.append("INNER JOIN third_address city on city.third_addr_id = area.third_parent_id ");
		whereSql.append("INNER JOIN third_address prov on prov.third_addr_id = city.third_parent_id ");
		whereSql.append("where street.level = ").append(AddrLevel.STREET.toValue());
		whereSql.append(" and street.third_flag = :thirdFlag");
		if (StringUtils.isNotBlank(queryReq.getStreetName())) {
			whereSql.append(" AND street.addr_name LIKE concat('%', :streetName, '%') ");
		}
		if (StringUtils.isNotBlank(queryReq.getDistrictName())) {
			whereSql.append(" AND area.addr_name LIKE concat('%', :districtName, '%') ");
		}
		if (StringUtils.isNotBlank(queryReq.getCityName())) {
			whereSql.append(" AND city.addr_name LIKE concat('%', :cityName, '%') ");
		}
		if (StringUtils.isNotBlank(queryReq.getProvName())) {
			whereSql.append(" AND prov.addr_name LIKE concat('%', :provName, '%') ");
		}
		//映射状态
		if (Objects.nonNull(queryReq.getMappingFlag())) {
			if (queryReq.getMappingFlag()) {
				whereSql.append(" AND (street.addr_name = '-' or street.platform_addr_id is not null)");
				whereSql.append(" AND area.platform_addr_id is not null");
				whereSql.append(" AND city.platform_addr_id is not null");
				whereSql.append(" AND prov.platform_addr_id is not null");
			} else {
				whereSql.append(" AND ((street.addr_name != '-' and street.platform_addr_id is null)");
				whereSql.append(" OR area.platform_addr_id is null");
				whereSql.append(" OR city.platform_addr_id is null");
				whereSql.append(" OR prov.platform_addr_id is null)");
			}
		}

		Query queryCount = entityManager.createNativeQuery("select count(1) ".concat(whereSql.toString()));
		//组装查询参数
		this.wrapperQueryParam(queryCount, queryReq);
		long count = Long.parseLong(queryCount.getSingleResult().toString());
		if (count > 0) {
			Query query = entityManager.createNativeQuery(sql.append(whereSql).toString());
			//组装查询参数
			this.wrapperQueryParam(query, queryReq);
			query.setFirstResult(queryReq.getPageNum() * queryReq.getPageSize());
			query.setMaxResults(queryReq.getPageSize());
			query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List<ThirdAddressPageVO> pageVOList = ThirdAddressWhereCriteriaBuilder.converter(query.getResultList());
			this.fillArea(pageVOList);
			this.fillOther(pageVOList);
			return new MicroServicePage<>(pageVOList, queryReq.getPageable(), count);
		}
		return new MicroServicePage<>(Collections.emptyList(), queryReq.getPageable(), count);
	}

	/**
	 * 列表查询第三方地址映射表
	 * @author dyt
	 */
	public List<ThirdAddress> list(ThirdAddressQueryRequest queryReq){
		return thirdAddressRepository.findAll(ThirdAddressWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 统计查询第三方地址映射表
	 * @author dyt
	 */
	public long count(ThirdAddressQueryRequest queryReq){
		return thirdAddressRepository.count(ThirdAddressWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author dyt
	 */
	public ThirdAddressVO wrapperVo(ThirdAddress thirdAddress) {
		if (thirdAddress != null){
			return KsBeanUtil.convert(thirdAddress, ThirdAddressVO.class);
		}
		return null;
	}



	//合并数据
	@Transactional
	public void batchMerge(List<ThirdAddressDTO> items) {
		if(CollectionUtils.isEmpty(items)){
			return;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("insert INTO third_address(id, third_addr_id, third_parent_id, addr_name, level, third_flag, del_flag, create_time, update_time) values");
		String sqlValue = " (?, ?, ?, ?, ?, ?, 0, now(), now() ) ";
		String empStr = "";
		int len = items.size();
		for (int i = 0; i < len; i++) {
			if (i > 0) {
				sql.append(" , ");
			}
			sql.append(sqlValue);
		}
		sql.append("on duplicate key update addr_name=values(addr_name), update_time=values(update_time)");
		Query query = entityManager.createNativeQuery(sql.toString());
		int paramIndex = 1;
		for (int i = 0; i < len; i++) {
			ThirdAddressDTO item = items.get(i);
			query.setParameter(paramIndex++, item.getThirdAddrId());
			query.setParameter(paramIndex++, item.getThirdAddrId());
			query.setParameter(paramIndex++, Objects.toString(item.getThirdParentId(), empStr));
			query.setParameter(paramIndex++, Objects.toString(item.getAddrName(), empStr));
			query.setParameter(paramIndex++, item.getLevel().toValue());
			query.setParameter(paramIndex++, ThirdPlatformType.LINKED_MALL.toValue());
		}
		query.executeUpdate();
	}

	/**
	 * 互相包含
	 * @param str1
	 * @param str2
	 * @return
	 */
	private boolean contains(String str1, String str2){
		if(str1 == null || str2 == null){
			return false;
		}
		return str1.contains(this.replaceLast(str2)) || str2.contains(this.replaceLast(str1));
	}

	//去除尾部字符
	private String replaceLast(String str) {
		if(str == null){
			return null;
		}
		String prov = "省";
		String city = "市";
		String prov1 = "特别行政区";
		String prov2 = "自治区";
		String prov3 = "街道";
		int len = str.length() - 1;
		if (str.lastIndexOf(prov) == len) {
			return str.substring(0, str.lastIndexOf(prov));
		} else if (str.lastIndexOf(city) == len) {
			return str.substring(0, str.lastIndexOf(city));
		} else if (str.lastIndexOf(prov1) > -1) {
			return str.substring(0, str.lastIndexOf(prov1));
		} else if (str.lastIndexOf(prov2) > -1) {
			return str.substring(0, str.lastIndexOf(prov2));
		} else if (str.lastIndexOf(prov3) > -1) {
			return str.substring(0, str.lastIndexOf(prov3));
		}
		return str.trim();
	}

	/**
	 * 填充省市区
	 *
	 * @param pageVOList
	 */
	private void fillArea(List<ThirdAddressPageVO> pageVOList) {
		if (CollectionUtils.isNotEmpty(pageVOList)) {
			List<String> addrIds = new ArrayList<>();
			pageVOList.forEach(detail -> {
				addrIds.add(Objects.toString(detail.getPlatformProvId()));
				addrIds.add(Objects.toString(detail.getPlatformCityId()));
				addrIds.add(Objects.toString(detail.getPlatformDistrictId()));
				addrIds.add(Objects.toString(detail.getPlatformStreetId()));
			});
			if (CollectionUtils.isEmpty(addrIds)) {
				return;
			}
			Map<String, String> platformAddressMap = platformAddressService.list(PlatformAddressQueryRequest.builder().addrIdList(addrIds)
					.delFlag(DeleteFlag.NO)
					.build()).stream().filter(a -> StringUtils.isNotBlank(a.getAddrName()))
					.collect(Collectors.toMap(PlatformAddress::getAddrId, PlatformAddress::getAddrName));
			if (MapUtils.isNotEmpty(platformAddressMap)) {
				pageVOList.forEach(detail -> {
					StringBuilder name = new StringBuilder();
					if (StringUtils.isNotBlank(detail.getPlatformProvId()) && platformAddressMap.containsKey(detail.getPlatformProvId())) {
						name.append(platformAddressMap.get(detail.getPlatformProvId()));
						if (StringUtils.isNotBlank(detail.getPlatformCityId()) && platformAddressMap.containsKey(detail.getPlatformCityId())) {
							name.append(platformAddressMap.get(detail.getPlatformCityId()));
							if (StringUtils.isNotBlank(detail.getPlatformDistrictId()) && platformAddressMap.containsKey(detail.getPlatformDistrictId())) {
								name.append(platformAddressMap.get(detail.getPlatformDistrictId()));
								if (StringUtils.isNotBlank(detail.getPlatformStreetId()) && platformAddressMap.containsKey(detail.getPlatformStreetId())) {
									name.append(platformAddressMap.get(detail.getPlatformStreetId()));
								}
							}
						}
					}
					detail.setPlatformAddress(name.toString());
				});
			}
		}
	}

	/**
	 * 填充其他街道
	 *
	 * @param pageVOList
	 */
	private void fillOther(List<ThirdAddressPageVO> pageVOList) {
		pageVOList.stream().filter(p -> String.valueOf(p.getStreetName()).contains("-")).forEach(p -> p.setPlatformStreetId("0"));
	}

	/**
	 * 组装查询参数
	 *
	 * @param query
	 * @param queryReq
	 */
	private void wrapperQueryParam(Query query, ThirdAddressPageRequest queryReq) {
		query.setParameter("thirdFlag", queryReq.getThirdFlag().toValue());
		if (StringUtils.isNotBlank(queryReq.getStreetName())) {
			query.setParameter("streetName", XssUtils.replaceLikeWildcard(queryReq.getStreetName()));
		}
		if (StringUtils.isNotBlank(queryReq.getDistrictName())) {
			query.setParameter("districtName", XssUtils.replaceLikeWildcard(queryReq.getDistrictName()));
		}
		if (StringUtils.isNotBlank(queryReq.getCityName())) {
			query.setParameter("cityName", XssUtils.replaceLikeWildcard(queryReq.getCityName()));
		}
		if (StringUtils.isNotBlank(queryReq.getProvName())) {
			query.setParameter("provName", XssUtils.replaceLikeWildcard(queryReq.getProvName()));
		}
	}
}

