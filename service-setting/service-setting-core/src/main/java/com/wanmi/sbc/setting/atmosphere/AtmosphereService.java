package com.wanmi.sbc.setting.atmosphere;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.atmosphere.model.root.Atmosphere;
import com.wanmi.sbc.setting.atmosphere.repository.AtmosphereRepository;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AtmosphereService {

    @Autowired
    private AtmosphereRepository atmosphereRepository;

    public void add(List<AtmosphereDTO> list){
        List<Atmosphere> atmospheres = KsBeanUtil.convertList(list,Atmosphere.class);
        atmospheres.forEach(a->{
            a.setCreateTime(LocalDateTime.now());
            a.setUpdateTime(LocalDateTime.now());
            a.setDeleted(0);
        });
        atmosphereRepository.saveAll(atmospheres);
    }
}
