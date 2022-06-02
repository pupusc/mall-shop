package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.MetaBookContent;
import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.bookmeta.entity.MetaBookRcmmd;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.enums.BookContentTypeEnum;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaBookContentMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookRcmmdMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
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
    @Resource
    private MetaBookContentMapper metaBookContentMapper;
    @Resource
    private MetaBookRcmmdMapper metaBookRcmmdMapper;

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

    @Transactional
    public void deleteById(Integer id) {
        if (id == null) {
            return;
        }
        this.metaFigureMapper.deleteById(id);
        //书籍人物
        MetaBookFigure metaBookFigure = new MetaBookFigure();
        metaBookFigure.setFigureId(id);
        this.metaBookFigureMapper.delete(metaBookFigure);
        //人物推荐
        Example example = new Example(MetaBookRcmmd.class);
        example.createCriteria().andEqualTo("bizId", id)
                .andIn("bizType", Arrays.asList(
                        BookRcmmdTypeEnum.EDITOR.getCode(),
                        BookRcmmdTypeEnum.MEDIA.getCode(),
                        BookRcmmdTypeEnum.ORGAN.getCode(),
                        BookRcmmdTypeEnum.EXPERT.getCode()));
        this.metaBookRcmmdMapper.deleteByExample(example);
        //出版内容（作序人）
        MetaBookContent metaBookContent = new MetaBookContent();
        metaBookContent.setFigureId(id);
        metaBookContent.setType(BookContentTypeEnum.PRELUDE.getCode());
        this.metaBookContentMapper.delete(metaBookContent);
    }
}
