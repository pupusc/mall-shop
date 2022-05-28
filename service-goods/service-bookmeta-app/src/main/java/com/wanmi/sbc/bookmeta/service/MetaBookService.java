package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.bookmeta.mapper.MetaBookLabelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @date 2022-05-27 23:11:00
 */
@Service
public class MetaBookService {
    @Autowired
    private MetaBookLabelMapper metaBookLabelMapper;

    public List<Integer> listBookLabelId(Integer id) {
        if (id == null) {
            return Collections.emptyList();
        }
        MetaBookLabel queryLabel = new MetaBookLabel();
        queryLabel.setBookId(id);
        queryLabel.setDelFlag(0);
        List<MetaBookLabel> labels = this.metaBookLabelMapper.select(queryLabel);
        return labels.stream().map(MetaBookLabel::getLabelId).collect(Collectors.toList());
    }
}
