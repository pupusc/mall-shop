package com.wanmi.sbc.topic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.redis.RedisService;
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


    public List<AtmosphereDTO> getAtmosphere(List<String> skuIds){
        AtmosphereQueryRequest request = new AtmosphereQueryRequest();
        request.setPageNum(0);
        request.setPageSize(10000);
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now());
        request.setSkuId(skuIds);
        BaseResponse<MicroServicePage<AtmosphereDTO>> page = atmosphereProvider.page(request);
        if(page == null || page.getContext() == null || CollectionUtils.isEmpty(page.getContext().getContent())){
            return null;
        }
        return page.getContext().getContent();
    }
}
