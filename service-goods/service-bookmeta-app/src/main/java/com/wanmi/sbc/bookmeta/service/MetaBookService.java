package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.bookmeta.mapper.MetaBookFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookLabelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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
    @Autowired
    private MetaBookFigureMapper metaBookFigureMapper;

    public List<Integer> listBookLabelId(Integer id) {
        if (id == null) {
            return Collections.emptyList();
        }
        MetaBookLabel query = new MetaBookLabel();
        query.setBookId(id);
        query.setDelFlag(0);
        List<MetaBookLabel> labels = this.metaBookLabelMapper.select(query);
        return labels.stream().map(MetaBookLabel::getLabelId).collect(Collectors.toList());
    }

    public List<MetaBookFigure> listBookFigureByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        Example example = new Example(MetaBookFigure.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("bookId", ids);
        return metaBookFigureMapper.selectByExample(example);
    }
}
