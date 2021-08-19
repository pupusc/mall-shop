package com.wanmi.sbc.linkedmall.address;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.linkedmall.model.v20180116.QueryAddressRequest;
import com.aliyuncs.linkedmall.model.v20180116.QueryAddressResponse;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.linkedmall.api.request.address.SbcAddressQueryRequest;
import com.wanmi.sbc.linkedmall.util.LinkedMallUtil;
import com.wanmi.sbc.setting.api.provider.thirdaddress.ThirdAddressProvider;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressBatchMergeRequest;
import com.wanmi.sbc.setting.bean.dto.ThirdAddressDTO;
import com.wanmi.sbc.setting.bean.enums.AddrLevel;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LinkedMallAddressService {

    @Autowired
    private IAcsClient iAcsClient;

    @Autowired
    private ThirdAddressProvider thirdAddressProvider;

    @Autowired
    private LinkedMallUtil linkedMallUtil;

    /**
     * 对接 linkedMall 查询地址 接口
     * @param request
     * @return
     */
    public List<QueryAddressResponse.DivisionAddressItem> queryAddress(SbcAddressQueryRequest request) {
        QueryAddressRequest listRequest = new QueryAddressRequest();
        listRequest.setDivisionCode(request.getDivisionCode());
        listRequest.setIp(request.getIp());
        listRequest.setBizId(linkedMallUtil.getLinkedMallBizId());
        try {
            QueryAddressResponse response = iAcsClient.getAcsResponse(listRequest);
            log.info("查询四级地址，请求参数：{}，请求标识：{}，返回码：{}，返回信息：{}", request.toString(), listRequest.getBizId(), response.getCode(), response.getMessage());
            if (StringUtils.equalsIgnoreCase(LinkedMallUtil.SUCCESS_CODE, response.getCode())) {
                if (CollectionUtils.isNotEmpty(response.getDivisionAddress())) {
                    return response.getDivisionAddress();
                }
                return Collections.emptyList();
            }
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "LikeMall调用异常:".concat(response.getMessage()));
        } catch (ClientException e) {
            log.error("LikeMall查询四级地址异常", e);
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "LikeMall调用异常:".concat(e.getErrMsg()));
        }
    }

    /**
     * 初始化likedMall地址数据
     */
    public void init() throws RuntimeException {
        log.info("linkedMall省市区初始化开始");
        //省
        List<QueryAddressResponse.DivisionAddressItem> provList = queryAddress(SbcAddressQueryRequest.builder().build());
        try {
            this.merge(provList, AddrLevel.PROVINCE);
        } catch (FeignException e) {
            log.error("省初始化异常", e);
        }

        int pLen = provList.size();
        int pErr = 0;
        for (int i = 0; i < pLen; i++) {
            //市
            List<QueryAddressResponse.DivisionAddressItem> cites = null;
            try {
                cites = queryAddress(SbcAddressQueryRequest.builder().divisionCode(String.valueOf(provList.get(i).getDivisionCode())).build());
                this.merge(cites, AddrLevel.CITY);
                pErr = 0;
            } catch (FeignException e) {
                log.error("城市初始化异常", e);
                if (pErr < 3) {
                    i--;
                }
                pErr++;
            }
            if(CollectionUtils.isEmpty(cites)){
                continue;
            }
            int cLen = cites.size();
            int cErr = 0;
            for (int j = 0; j < cLen; j++) {
                //区县
                List<QueryAddressResponse.DivisionAddressItem> districts = null;
                try {
                    districts = queryAddress(SbcAddressQueryRequest.builder().divisionCode(String.valueOf(cites.get(j).getDivisionCode())).build());
                    this.merge(districts, AddrLevel.DISTRICT);
                    cErr = 0;
                } catch (Exception e) {
                    log.error("区县初始化异常", e);
                    if (cErr < 3) {
                        j--;
                    }
                    cErr++;
                }
                if(CollectionUtils.isEmpty(districts)){
                    continue;
                }
                int dLen = districts.size();
                int dErr = 0;
                for (int k = 0; k < dLen; k++) {
                    //街道
                    try {
                        Long code = districts.get(k).getDivisionCode();
                        List<QueryAddressResponse.DivisionAddressItem> streets = new ArrayList<>();
                        if (String.valueOf(districts.get(k).getDivisionName()).contains("其它区")) {
                            QueryAddressResponse.DivisionAddressItem otherItem = new QueryAddressResponse.DivisionAddressItem();
                            otherItem.setParentId(code);
                            otherItem.setDivisionCode(NumberUtils.toLong(String.valueOf(code).concat("000")));
                            otherItem.setDivisionLevel(AddrLevel.STREET.toValue());
                            otherItem.setDivisionName("-");
                            streets.add(otherItem);
                        } else {
                            streets = queryAddress(SbcAddressQueryRequest.builder().divisionCode(String.valueOf(code)).build());
                        }
                        this.merge(streets, AddrLevel.STREET);
                        dErr = 0;
                    } catch (Exception e) {
                        log.error("街道初始化异常", e);
                        if (dErr < 3) {
                            k--;
                        }
                        dErr++;
                    }
                }
            }
        }
        log.info("linkedMall省市区初始化结束");
    }


    private void merge(List<QueryAddressResponse.DivisionAddressItem> items, AddrLevel level){
        if(CollectionUtils.isEmpty(items)){
            return;
        }
        List<ThirdAddressDTO> dtos = items.stream().map(i -> {
            ThirdAddressDTO dto = new ThirdAddressDTO();
            dto.setId(String.valueOf(i.getDivisionCode()));
            dto.setThirdAddrId(String.valueOf(i.getDivisionCode()));
            dto.setThirdParentId(String.valueOf(i.getParentId()));
            dto.setAddrName(i.getDivisionName());
            dto.setThirdFlag(ThirdPlatformType.LINKED_MALL);
            dto.setLevel(level);
            return dto;
        }).collect(Collectors.toList());
        thirdAddressProvider.batchMerge(ThirdAddressBatchMergeRequest.builder().thirdAddressList(dtos).build());
    }
}
