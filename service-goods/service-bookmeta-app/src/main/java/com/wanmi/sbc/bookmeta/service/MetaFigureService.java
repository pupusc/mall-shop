package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-05-28 00:33:00
 */
@Service
public class MetaFigureService {
    @Resource
    private MetaFigureMapper metaFigureMapper;

    public List<MetaFigure> listFigureById(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }

        Example example = new Example(MetaFigure.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("id", ids);
        return metaFigureMapper.selectByExample(example);
    }
}
