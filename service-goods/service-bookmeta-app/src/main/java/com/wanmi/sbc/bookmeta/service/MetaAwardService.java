package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.mapper.MetaAwardMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Liang Jun
 * @date 2022-05-31 18:31:00
 */
@Service
public class MetaAwardService {
    @Resource
    private MetaAwardMapper metaAwardMapper;
}
