package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.bookmeta.entity.MetaLabel;
import com.wanmi.sbc.bookmeta.mapper.MetaBookLabelMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaLabelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private MetaBookLabelMapper metaBookLabelMapper;

    public List<MetaLabel> listLabelById(List<Integer> bookLabelIds) {
        if (CollectionUtils.isEmpty(bookLabelIds)) {
            return Collections.EMPTY_LIST;
        }
        Example example = new Example(MetaLabel.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("id", bookLabelIds);
        return metaLabelMapper.selectByExample(example);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (id == null) {
            return;
        }
        this.metaLabelMapper.deleteById(id);
        //书籍标签
        MetaBookLabel metaBookLabel = new MetaBookLabel();
        metaBookLabel.setLabelId(id);
        this.metaBookLabelMapper.delete(metaBookLabel);
    }
}
