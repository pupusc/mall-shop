package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.mapper.MetaBookClumpMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Liang Jun
 * @date 2022-06-01 11:04:00
 */
@Service
public class MetaBookClumpService {
    @Autowired
    private MetaBookClumpMapper metaBookClumpMapper;
    @Autowired
    private MetaBookMapper metaBookMapper;

    @Transactional
    public void deleteById(Integer id) {
        if (id == null) {
            return;
        }
        this.metaBookClumpMapper.deleteById(id);
        this.metaBookMapper.removeBookClumpId(id);
    }
}
