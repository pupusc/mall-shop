package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.MetaBookRcmmd;
import com.wanmi.sbc.bookmeta.entity.MetaFigureAward;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaAwardMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookRcmmdMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureAwardMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
