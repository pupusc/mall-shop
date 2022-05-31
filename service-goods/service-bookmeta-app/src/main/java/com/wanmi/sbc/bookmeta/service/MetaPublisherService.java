package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import com.wanmi.sbc.bookmeta.mapper.MetaPublisherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @date 2022-05-31 09:30:00
 */
@Service
public class MetaPublisherService {
    @Autowired
    private MetaPublisherMapper metaPublisherMapper;

    public List<MetaPublisher> listEntityByIds(List<Integer> ids) {
        if (ids==null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        Example example = new Example(MetaPublisher.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("id", ids.stream().distinct().collect(Collectors.toList()));
        return this.metaPublisherMapper.selectByExample(example);
    }
}
