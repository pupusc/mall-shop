package com.wanmi.sbc.topic.service;

import com.alibaba.fastjson.JSONArray;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.index.RefreshConfig;
import com.wanmi.sbc.index.response.ProductConfigResponse;
import com.wanmi.sbc.setting.api.provider.AtmosphereProvider;
import com.wanmi.sbc.setting.api.request.AtmosphereQueryRequest;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AtmosphereService {

    @Autowired
    private AtmosphereProvider atmosphereProvider;

    @Autowired
    private RefreshConfig  refreshConfig;

    @Value("${atmos.flag:false}")
    private Boolean atmosFlag;

    public List<AtmosphereDTO> getAtmosphere(){
        //开关，用nacos配置
        if(!atmosFlag){
            List<ProductConfigResponse> list = JSONArray.parseArray(refreshConfig.getRibbonConfig(), ProductConfigResponse.class);
            List<AtmosphereDTO> atmosphereDTOS= new ArrayList<>(list.size());
            list.stream().filter(productConfig -> new Date().after(productConfig.getStartTime()) && new Date().before(productConfig.getEndTime())).forEach(p->{
                AtmosphereDTO atmosphereDTO = new AtmosphereDTO();
                atmosphereDTO.setSkuId(p.getSkuId());
                atmosphereDTO.setElementOne(p.getTitle());
                atmosphereDTO.setElementTwo(p.getContent());
                atmosphereDTO.setImageUrl(p.getImageUrl());
                atmosphereDTO.setElementFour(p.getPrice());
                atmosphereDTOS.add(atmosphereDTO);
               });
            return atmosphereDTOS;
        }
        //查缓存
        AtmosphereQueryRequest request = new AtmosphereQueryRequest();
        request.setPageNum(0);
        request.setPageSize(10000);
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now());
        BaseResponse<MicroServicePage<AtmosphereDTO>> page = atmosphereProvider.page(request);
        if(page == null || page.getContext() == null || CollectionUtils.isEmpty(page.getContext().getContent())){
            return null;
        }
        return page.getContext().getContent();
    }
}
