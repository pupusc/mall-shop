package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.MetaAward;
import com.wanmi.sbc.bookmeta.entity.MetaBookRcmmd;
import com.wanmi.sbc.bookmeta.entity.MetaFigureAward;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaAwardMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookRcmmdMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureAwardMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-05-31 18:31:00
 */
@Service
public class MetaAwardService {
    @Resource
    private MetaAwardMapper metaAwardMapper;
    @Resource
    private MetaFigureAwardMapper metaFigureAwardMapper;
    @Resource
    private MetaBookRcmmdMapper metaBookRcmmdMapper;

    public void deleteById(Integer id) {
        if (id == null) {
            return;
        }
        //奖项
        this.metaAwardMapper.deleteById(id);
        //人物奖项
        MetaFigureAward metaFigureAward = new MetaFigureAward();
        metaFigureAward.setAwardId(id);
        this.metaFigureAwardMapper.delete(metaFigureAward);
        //获奖推荐
        MetaBookRcmmd metaBookRcmmd = new MetaBookRcmmd();
        metaBookRcmmd.setBizType(BookRcmmdTypeEnum.AWARD.getCode());
        metaBookRcmmd.setBizId(id);
        this.metaBookRcmmdMapper.delete(metaBookRcmmd);
    }

    public List<MetaAward> listEntityByIds(List<Integer> awardIds) {
        if (CollectionUtils.isEmpty(awardIds)) {
            return Collections.EMPTY_LIST;
        }

        Example example = new Example(MetaAward.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("id", awardIds);
        return this.metaAwardMapper.selectByExample(example);
    }
}
