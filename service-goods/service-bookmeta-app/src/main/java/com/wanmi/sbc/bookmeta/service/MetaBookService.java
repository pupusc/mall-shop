package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.bookmeta.mapper.MetaBookFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookLabelMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
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
    private MetaBookMapper metaBookMapper;
    @Autowired
    private MetaBookLabelMapper metaBookLabelMapper;
    @Autowired
    private MetaBookFigureMapper metaBookFigureMapper;

    public List<Integer> listBookLabelId(Integer id) {
        if (id == null) {
            return Collections.EMPTY_LIST;
        }
        MetaBookLabel query = new MetaBookLabel();
        query.setBookId(id);
        query.setDelFlag(0);
        List<MetaBookLabel> labels = this.metaBookLabelMapper.select(query);
        return labels.stream().map(MetaBookLabel::getLabelId).collect(Collectors.toList());
    }

    public List<MetaBookFigure> listBookFigureByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Example example = new Example(MetaBookFigure.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("bookId", ids.stream().distinct().collect(Collectors.toList()));
        return metaBookFigureMapper.selectByExample(example);
    }

    public List<MetaBook> listEntityByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        Example example = new Example(MetaBook.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("id", ids.stream().distinct().collect(Collectors.toList()));
        return metaBookMapper.selectByExample(example);
    }
}
