package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.entity.MetaAward;
import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.bookmeta.entity.MetaBookRcmmd;
import com.wanmi.sbc.bookmeta.entity.MetaFigureAward;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
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
    public CollectMetaResp collectMetaAwardByTime(CollectMetaReq collectMetaReq) {

        CollectMetaResp collectMetaResp = new CollectMetaResp();
        //奖项 获取奖项表信息
        List<MetaAward> metaAwards = metaAwardMapper.collectMetaAwardByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        if (CollectionUtils.isEmpty(metaAwards)) {
            return collectMetaResp;
        }
        collectMetaResp.setLastBizId(metaAwards.get(metaAwards.size() -1).getId()); //设置最后一个采集id
        collectMetaResp.setHasMore(metaAwards.size() >= collectMetaReq.getPageSize());
        List<Integer> awardIds = metaAwards.stream().map(MetaAward::getId).collect(Collectors.toList());
        List<Integer> bookIds = this.packBookIdByFigureAward(awardIds);
        bookIds.addAll(this.packBookIdByRecomAward(awardIds));
        if (CollectionUtils.isEmpty(bookIds)) {
            return collectMetaResp;
        }
        //获取商品信息
        collectMetaResp.setMetaBookResps(super.collectBookByIds(bookIds));
        return collectMetaResp;
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
