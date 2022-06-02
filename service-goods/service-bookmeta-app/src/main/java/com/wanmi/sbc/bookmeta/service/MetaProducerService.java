package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaProducerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Liang Jun
 * @date 2022-06-01 14:00:00
 */
@Service
public class MetaProducerService {
    @Autowired
    private MetaProducerMapper metaProducerMapper;
    @Autowired
    private MetaBookMapper metaBookMapper;

    @Transactional
    public void deleteById(Integer id) {
        if (id == null) {
            return;
        }
        this.metaProducerMapper.deleteById(id);
        this.metaBookMapper.removeProducerId(id);
    }
}
