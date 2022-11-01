package com.soybean.mall.address.controller;

import com.soybean.mall.address.resp.FixedAddressResp;
import com.soybean.mall.address.req.FixedAddressReq;
import com.soybean.mall.common.CommonUtil;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.address.CustomerDeliveryAddressQueryProvider;
import com.wanmi.sbc.customer.api.request.address.CustomerDeliveryAddressRequest;
import com.wanmi.sbc.customer.api.response.address.CustomerDeliveryAddressResponse;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressListRequest;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressListResponse;
import com.wanmi.sbc.setting.bean.enums.AddrLevel;
import com.wanmi.sbc.setting.bean.vo.PlatformAddressVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/17 8:03 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@RestController
@RequestMapping("/address")
public class AddressController {


    @Autowired
    private PlatformAddressQueryProvider platformAddressQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CustomerDeliveryAddressQueryProvider customerDeliveryAddressQueryProvider;

    /**
     * 定位地址转化
     * @menu 搜索功能
     * @param fixedAddressReq
     * @return
     */
    @PostMapping("/fixedChangeAddress")
    public BaseResponse<FixedAddressResp> fixedChangeAddress(@RequestBody FixedAddressReq fixedAddressReq) {

        FixedAddressResp fixedAddressResp = new FixedAddressResp();

        //获取客户信息默认地址
        String userId = commonUtil.getOperatorId();
        if (StringUtils.isNotBlank(userId)) {
            CustomerDeliveryAddressRequest request = new CustomerDeliveryAddressRequest();
            request.setCustomerId(userId);
            BaseResponse<CustomerDeliveryAddressResponse> customerDeliveryAddressResponseBaseResponse = customerDeliveryAddressQueryProvider.getDefaultOrAnyOneByCustomerId(request);
            CustomerDeliveryAddressResponse customerDeliveryAddressResponse = customerDeliveryAddressResponseBaseResponse.getContext();
            if (!Objects.isNull(customerDeliveryAddressResponse)) {

                PlatformAddressListRequest platformAddressListRequest = new PlatformAddressListRequest();
                platformAddressListRequest.setDelFlag(DeleteFlag.NO);
                platformAddressListRequest.setLeafFlag(false);
                platformAddressListRequest.setAddrIdList(Arrays.asList(customerDeliveryAddressResponse.getProvinceId() + "",
                        customerDeliveryAddressResponse.getCityId() + "",
                        customerDeliveryAddressResponse.getAreaId() + "",
                        customerDeliveryAddressResponse.getStreetId() + ""
                        ));
                PlatformAddressListResponse context = platformAddressQueryProvider.list(platformAddressListRequest).getContext();
                if (CollectionUtils.isNotEmpty(context.getPlatformAddressVOList())) {

                    Map<String, PlatformAddressVO> collect =
                            context.getPlatformAddressVOList().stream().collect(Collectors.toMap(PlatformAddressVO::getAddrId, Function.identity(), (k1, k2) -> k1));
                    fixedAddressResp.setProvinceId(customerDeliveryAddressResponse.getProvinceId()+"");
                    fixedAddressResp.setProvinceName(collect.get(customerDeliveryAddressResponse.getProvinceId()+"") == null ? "" : collect.get(customerDeliveryAddressResponse.getProvinceId()+"").getAddrName());
                    fixedAddressResp.setCityId(customerDeliveryAddressResponse.getCityId() + "");
                    fixedAddressResp.setCityName(collect.get(customerDeliveryAddressResponse.getCityId()+"") == null ? "" : collect.get(customerDeliveryAddressResponse.getCityId()+"").getAddrName());
                    fixedAddressResp.setAreaId(customerDeliveryAddressResponse.getAreaId() + "");
                    fixedAddressResp.setAreaName(collect.get(customerDeliveryAddressResponse.getAreaId()+"") == null ? "" : collect.get(customerDeliveryAddressResponse.getAreaId()+"").getAddrName());
                    fixedAddressResp.setStreetId(customerDeliveryAddressResponse.getStreetId() + "");
                    fixedAddressResp.setStreetName(collect.get(customerDeliveryAddressResponse.getStreetId()+"") == null ? "" : collect.get(customerDeliveryAddressResponse.getStreetId()+"").getAddrName());
                    return BaseResponse.success(fixedAddressResp);
                }
            }
        }

        //获取省份信息
        if (StringUtils.isNotBlank(fixedAddressReq.getProvinceName())) {
            PlatformAddressListRequest platformAddressListRequest = new PlatformAddressListRequest();
            platformAddressListRequest.setDelFlag(DeleteFlag.NO);
            platformAddressListRequest.setAddrLevel(AddrLevel.PROVINCE);
            platformAddressListRequest.setLeafFlag(false);
            platformAddressListRequest.setAddrName(fixedAddressReq.getProvinceName());
            PlatformAddressListResponse context = platformAddressQueryProvider.list(platformAddressListRequest).getContext();
            if (CollectionUtils.isNotEmpty(context.getPlatformAddressVOList()) && context.getPlatformAddressVOList().size() == 1) {
                PlatformAddressVO platformAddressVO = context.getPlatformAddressVOList().get(0);
                fixedAddressResp.setProvinceId(platformAddressVO.getAddrId());
                fixedAddressResp.setProvinceName(platformAddressVO.getAddrName());
            }
        }

        //获取城市信息
        if (StringUtils.isNotBlank(fixedAddressReq.getCityName()) && StringUtils.isNotBlank(fixedAddressResp.getProvinceId())) {
            PlatformAddressListRequest platformAddressListRequest = new PlatformAddressListRequest();
            platformAddressListRequest.setDelFlag(DeleteFlag.NO);
            platformAddressListRequest.setAddrLevel(AddrLevel.CITY);
            platformAddressListRequest.setLeafFlag(false);
            platformAddressListRequest.setAddrName(fixedAddressReq.getCityName());
            PlatformAddressListResponse context = platformAddressQueryProvider.list(platformAddressListRequest).getContext();
            if (CollectionUtils.isNotEmpty(context.getPlatformAddressVOList()) && context.getPlatformAddressVOList().size() == 1) {
                PlatformAddressVO platformAddressVO = context.getPlatformAddressVOList().get(0);
                fixedAddressResp.setCityId(platformAddressVO.getAddrId());
                fixedAddressResp.setCityName(platformAddressVO.getAddrName());
            }
        }

        //区域
        if (StringUtils.isNotBlank(fixedAddressReq.getAreaName()) && StringUtils.isNotBlank(fixedAddressResp.getCityId())) {
            PlatformAddressListRequest platformAddressListRequest = new PlatformAddressListRequest();
            platformAddressListRequest.setDelFlag(DeleteFlag.NO);
            platformAddressListRequest.setAddrLevel(AddrLevel.DISTRICT);
            platformAddressListRequest.setLeafFlag(false);
            platformAddressListRequest.setAddrName(fixedAddressReq.getAreaName());
            PlatformAddressListResponse context = platformAddressQueryProvider.list(platformAddressListRequest).getContext();
            if (CollectionUtils.isNotEmpty(context.getPlatformAddressVOList()) && context.getPlatformAddressVOList().size() == 1) {
                PlatformAddressVO platformAddressVO = context.getPlatformAddressVOList().get(0);
                fixedAddressResp.setAreaId(platformAddressVO.getAddrId());
                fixedAddressResp.setAreaName(platformAddressVO.getAddrName());
            }
        }

        //街道
        if (StringUtils.isNotBlank(fixedAddressReq.getStreetName()) && StringUtils.isNotBlank(fixedAddressResp.getAreaId())) {
            PlatformAddressListRequest platformAddressListRequest = new PlatformAddressListRequest();
            platformAddressListRequest.setDelFlag(DeleteFlag.NO);
            platformAddressListRequest.setAddrLevel(AddrLevel.DISTRICT);
            platformAddressListRequest.setLeafFlag(false);
            platformAddressListRequest.setAddrName(fixedAddressReq.getStreetName());
            PlatformAddressListResponse context = platformAddressQueryProvider.list(platformAddressListRequest).getContext();
            if (CollectionUtils.isNotEmpty(context.getPlatformAddressVOList()) && context.getPlatformAddressVOList().size() == 1) {
                PlatformAddressVO platformAddressVO = context.getPlatformAddressVOList().get(0);
                fixedAddressResp.setStreetId(platformAddressVO.getAddrId());
                fixedAddressResp.setStreetName(platformAddressVO.getAddrName());
            }
        }

        //如果传递为空，给默认上海地址
        if (StringUtils.isBlank(fixedAddressResp.getCityId())
            && StringUtils.isBlank(fixedAddressResp.getProvinceId())
            && StringUtils.isBlank(fixedAddressResp.getAreaId())
            && StringUtils.isBlank(fixedAddressResp.getStreetId())) {
            fixedAddressResp.setProvinceId("310000");
            fixedAddressResp.setProvinceName("上海市");
            fixedAddressResp.setCityId("310100");
            fixedAddressResp.setCityName("上海市");
            fixedAddressResp.setAreaId("310105");
            fixedAddressResp.setAreaName("长宁区");
        }
        return BaseResponse.success(fixedAddressResp);
    }

}
