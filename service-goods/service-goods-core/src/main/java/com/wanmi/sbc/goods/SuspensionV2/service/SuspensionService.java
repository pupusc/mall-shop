package com.wanmi.sbc.goods.SuspensionV2.service;

import com.wanmi.sbc.common.enums.DeleteFlag;

import com.wanmi.sbc.goods.SuspensionV2.model.Suspension;
import com.wanmi.sbc.goods.SuspensionV2.repository.SuspensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service("SuspensionService")
public class SuspensionService {

    @Autowired
    private SuspensionRepository suspensionRepository;


    public Suspension getSuspensionById(Long id) {
        return suspensionRepository.findById(id).orElse(null);
    }
}
