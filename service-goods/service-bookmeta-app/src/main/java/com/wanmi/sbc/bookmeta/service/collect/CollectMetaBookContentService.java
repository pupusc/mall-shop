package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.entity.MetaBookContent;
import com.wanmi.sbc.bookmeta.enums.BookContentTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description: 采集内容信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/16 8:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectMetaBookContentService extends AbstractCollectBookService{



    /**
     * 采集内容信息
     * @param collectMetaReq
     */
    public CollectMetaResp collectMetaBookContentByTime(CollectMetaReq collectMetaReq){
        CollectMetaResp collectMetaResp = new CollectMetaResp();
        List<MetaBookContent> metaBookContents = metaBookContentMapper.collectMetaBookContentByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        if (CollectionUtils.isEmpty(metaBookContents)) {
            return collectMetaResp;
        }
        collectMetaResp.setLastBizId(metaBookContents.get(metaBookContents.size() -1).getId());
        collectMetaResp.setHasMore(metaBookContents.size() >= collectMetaReq.getPageSize());
        List<Integer> bookIds =
                metaBookContents.stream().filter(ex -> Objects.equals(ex.getType(), BookContentTypeEnum.INTRODUCE.getCode())).map(MetaBookContent::getBookId).collect(Collectors.toList());
        //获取商品信息
        collectMetaResp.setMetaBookResps(super.collectBookByIds(bookIds));
        return collectMetaResp;
    }
}
