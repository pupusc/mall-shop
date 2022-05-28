package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.MetaLabel;
import com.wanmi.sbc.bookmeta.mapper.MetaLabelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-05-27 23:18:00
 */
@Service
public class MetaLabelService {
    @Autowired
    private MetaLabelMapper metaLabelMapper;

    public List<MetaLabel> listLabelById(List<Integer> bookLabelIds) {
        if (CollectionUtils.isEmpty(bookLabelIds)) {
            return Collections.EMPTY_LIST;
        }
        Example example = new Example(MetaLabel.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("id", bookLabelIds);
        return metaLabelMapper.selectByExample(example);
    }
}
