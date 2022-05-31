package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.mapper.MetaBookFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @date 2022-05-28 00:33:00
 */
@Service
public class MetaFigureService {
    @Resource
    private MetaFigureMapper metaFigureMapper;
    @Resource
    private MetaBookFigureMapper metaBookFigureMapper;

    public List<MetaFigure> listFigureByIds(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }

        Example example = new Example(MetaFigure.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("id", ids);
        return metaFigureMapper.selectByExample(example);
    }

    public List<MetaFigure> listFigureByBookIds(List<Integer> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        Example example = new Example(MetaBookFigure.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("bookId", ids);
        List<Integer> figureIds = this.metaBookFigureMapper.selectByExample(example).stream().map(MetaBookFigure::getFigureId).collect(Collectors.toList());
        return listFigureByIds(figureIds);
    }
}
