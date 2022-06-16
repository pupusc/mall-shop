package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.CollectMetaReq;
import com.wanmi.sbc.bookmeta.entity.MetaAward;
import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.bookmeta.entity.MetaBookRcmmd;
import com.wanmi.sbc.bookmeta.entity.MetaFigureAward;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaAwardMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookRcmmdMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureAwardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 奖项信息采集
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/16 7:10 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectMetaAwardService extends AbstractCollectBookService{



    /**
     * 采集奖项信息
     * @param collectMetaReq
     */
    public void collectMetaAwardByTime(CollectMetaReq collectMetaReq) {

        //奖项 获取奖项表信息
        List<MetaAward> metaAwards = metaAwardMapper.collectMetaAwardByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        if (CollectionUtils.isEmpty(metaAwards)) {
            return;
        }
        List<Integer> awardIds = metaAwards.stream().map(MetaAward::getId).collect(Collectors.toList());
        List<Integer> bookIds = this.packBookIdByFigureAward(awardIds);
        bookIds.addAll(this.packBookIdByRecomAward(awardIds));
        if (CollectionUtils.isEmpty(bookIds)) {
            return;
        }
        //获取商品信息
    }

    /**
     * 根据人员奖项获取图书信息
     */
    private List<Integer> packBookIdByFigureAward(List<Integer> awardIds) {
        //奖项对应的人物
        List<MetaFigureAward> metaFigureAwards = metaFigureAwardMapper.collectMetaFigureAwardByIds(awardIds);
        if (CollectionUtils.isEmpty(metaFigureAwards)) {
            return new ArrayList<>();
        }

        //人物对应的商品信息
        List<Integer> figureIds = metaFigureAwards.stream().map(MetaFigureAward::getFigureId).collect(Collectors.toList());
        List<MetaBookFigure> metaBookFigures = metaBookFigureMapper.collectMetaBookFigureByIds(figureIds);
        if (CollectionUtils.isEmpty(metaBookFigures)) {
            return new ArrayList<>();
        }
        return metaBookFigures.stream().map(MetaBookFigure::getBookId).collect(Collectors.toList());
    }


    /**
     * 根据商品奖项获取图书信息
     * @param awardIds
     */
    private List<Integer> packBookIdByRecomAward(List<Integer> awardIds) {

        List<MetaBookRcmmd> metaBookRcmmds =
                metaBookRcmmdMapper.collectMetaBookRcmmd(awardIds, Collections.singletonList(BookRcmmdTypeEnum.AWARD.getCode()));
        return metaBookRcmmds.stream().map(MetaBookRcmmd::getBookId).collect(Collectors.toList());
    }
}
