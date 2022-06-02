package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.mapper.MetaBookGroupMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Liang Jun
 * @date 2022-06-01 11:34:00
 */
@Service
public class MetaBookGroupService {
    @Autowired
    private MetaBookGroupMapper metaBookGroupMapper;
    @Autowired
    private MetaBookMapper metaBookMapper;

    @Transactional
    public void deleteById(Integer id) {
        if (id == null) {
            return;
        }
        this.metaBookGroupMapper.deleteById(id);
        this.metaBookMapper.removeBookGroupId(id);
    }
}
