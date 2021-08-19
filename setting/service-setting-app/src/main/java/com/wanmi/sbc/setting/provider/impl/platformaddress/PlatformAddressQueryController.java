package com.wanmi.sbc.setting.provider.impl.platformaddress;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.platformaddress.*;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressByIdResponse;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressListResponse;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressPageResponse;
import com.wanmi.sbc.setting.bean.vo.PlatformAddressVO;
import com.wanmi.sbc.setting.platformaddress.model.root.PlatformAddress;
import com.wanmi.sbc.setting.platformaddress.service.PlatformAddressService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>平台地址信息查询服务接口实现</p>
 * @author dyt
 * @date 2020-03-30 14:39:57
 */
@RestController
@Validated
public class PlatformAddressQueryController implements PlatformAddressQueryProvider {
	@Autowired
	private PlatformAddressService platformAddressService;

	@Override
	public BaseResponse<PlatformAddressPageResponse> page(@RequestBody @Valid PlatformAddressPageRequest platformAddressPageReq) {
		PlatformAddressQueryRequest queryReq = KsBeanUtil.convert(platformAddressPageReq, PlatformAddressQueryRequest.class);
		Page<PlatformAddress> platformAddressPage = platformAddressService.page(queryReq);
		Page<PlatformAddressVO> newPage = platformAddressPage.map(entity -> platformAddressService.wrapperVo(entity));
		MicroServicePage<PlatformAddressVO> microPage = new MicroServicePage<>(newPage, platformAddressPageReq.getPageable());
		PlatformAddressPageResponse finalRes = new PlatformAddressPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<PlatformAddressListResponse> list(@RequestBody @Valid PlatformAddressListRequest platformAddressListReq) {
		PlatformAddressQueryRequest queryReq = KsBeanUtil.convert(platformAddressListReq, PlatformAddressQueryRequest.class);
		List<PlatformAddress> platformAddressList = platformAddressService.list(queryReq);
        Set<String> parenAddIds = new HashSet<>();
        //填充是否叶子节点标识
		if(Boolean.TRUE.equals(platformAddressListReq.getLeafFlag()) && CollectionUtils.isNotEmpty(platformAddressList)){
            List<String> addrIds = platformAddressList.stream().map(PlatformAddress::getAddrId).collect(Collectors.toList());
            List<PlatformAddress> childList = platformAddressService.list(PlatformAddressQueryRequest.builder().addrParentIdList(addrIds).delFlag(DeleteFlag.NO).build());
            if(CollectionUtils.isNotEmpty(childList)){
				parenAddIds.addAll(childList.stream().map(PlatformAddress::getAddrParentId).distinct().collect(Collectors.toSet()));
			}
        }
		List<PlatformAddressVO> newList = platformAddressList.stream().map(entity -> {
            PlatformAddressVO vo = platformAddressService.wrapperVo(entity);
            if(parenAddIds.contains(vo.getAddrId())){
                vo.setLeafFlag(Boolean.FALSE);
            }
            return vo;
		}).collect(Collectors.toList());
		return BaseResponse.success(new PlatformAddressListResponse(newList));
	}

	@Override
	public BaseResponse<PlatformAddressByIdResponse> getById(@RequestBody @Valid PlatformAddressByIdRequest platformAddressByIdRequest) {
		PlatformAddress platformAddress =
		platformAddressService.getOne(platformAddressByIdRequest.getId());
		return BaseResponse.success(new PlatformAddressByIdResponse(platformAddressService.wrapperVo(platformAddress)));
	}

	@Override
	public BaseResponse<PlatformAddressListResponse> provinceOrCitylist(@RequestBody @Valid PlatformAddressListRequest platformAddressListReq) {
		List<PlatformAddress> platformAddressList = platformAddressService.provinceCityList(platformAddressListReq.getAddrIdList());
		List<PlatformAddressVO> newList = platformAddressList.stream().map(entity -> {
			PlatformAddressVO vo = platformAddressService.wrapperVo(entity);
			return vo;
		}).collect(Collectors.toList());
		return BaseResponse.success(new PlatformAddressListResponse(newList));
	}

	/**
	 * 校验是否需要完善地址,true表示需要完善，false表示不需要完善
	 * @param request
	 * @return
	 */
	@Override
	public BaseResponse<Boolean> verifyAddress(@RequestBody @Valid PlatformAddressVerifyRequest request) {
		String provinceId = request.getProvinceId();
		String cityId = request.getCityId();
		String areaId = request.getAreaId();
		String streetId = request.getStreetId();

		// 省、市、区id任一个为null，都需要完善
		if(Objects.isNull(provinceId) || Objects.isNull(cityId) || Objects.isNull(areaId)) {
			return BaseResponse.success(Boolean.TRUE);
		}
		// 根据省市区街道id 统计数据库是否有对应数据，没有则需要完善
		int provinceNum = platformAddressService.countNum(provinceId);
		int cityNum = platformAddressService.countNum(cityId);
		int areaNum = platformAddressService.countNum(areaId);
		int streetNum = 1;
		// 如果有街道id，需统计街道id 数据库是否有对应数据
		if(Objects.nonNull(streetId) && !"0".equals(streetId)) {
			streetNum = platformAddressService.countNum(streetId);
		}
		if(provinceNum == 0 || cityNum == 0 || areaNum == 0 || streetNum == 0){
			return BaseResponse.success(Boolean.TRUE);
		}

		Boolean flag = Boolean.TRUE;

		// 通过省、市父子id 统计市级地址数量，数量小于1 需要完善
		int provinceCityNum = platformAddressService.countNumByIdAndParentId(cityId, provinceId);
		if(provinceCityNum > 0) {
			// 通过市、区父子id 统计区级地址数量，数量小于1 需要完善
			int cityAreaNum = platformAddressService.countNumByIdAndParentId(areaId, cityId);
			if(cityAreaNum > 0 && Objects.nonNull(streetId) && !"0".equals(streetId)) {
				// 有街道id：通过区、街道父子id 统计街道地址数量，数量小于1 则需要完善
				int areaStreetNum = platformAddressService.countNumByIdAndParentId(streetId, areaId);
				flag = areaStreetNum > 0 ? Boolean.FALSE : Boolean.TRUE;
			} else if (cityAreaNum > 0) {
				// 没有街道id：通过区id 统计街道地址数量，数量大于0 则需要完善
				int areaStreetNum = platformAddressService.countNumByParentId(areaId);
				flag = areaStreetNum > 0 ? Boolean.TRUE : Boolean.FALSE;
			}
		}

		return BaseResponse.success(flag);
	}

}

