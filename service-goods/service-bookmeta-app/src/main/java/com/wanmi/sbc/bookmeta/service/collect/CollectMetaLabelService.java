package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.bookmeta.entity.MetaLabel;
import com.wanmi.sbc.bookmeta.enums.LabelTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description: 标签信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/16 8:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectMetaLabelService extends AbstractCollectBookService{

    /**
     * 采集 标签信息
     * @param collectMetaReq
     */
    public CollectMetaResp collectMetaLabelByTime(CollectMetaReq collectMetaReq){

        CollectMetaResp collectMetaResp = new CollectMetaResp();
        List<MetaLabel> metaLabels =
                metaLabelMapper.collectMetaLabelByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        if (CollectionUtils.isEmpty(metaLabels)) {
            return collectMetaResp;
        }
        collectMetaResp.setLastBizId(metaLabels.get(metaLabels.size() -1).getId());
        collectMetaResp.setHasMore(metaLabels.size() >= collectMetaReq.getPageSize());
        List<Integer> thirdLabels = new ArrayList<>();
        List<Integer> secondLabels = new ArrayList<>();
        for (MetaLabel metaLabelParam : metaLabels) {
            //过滤一级分类
            if (Objects.equals(metaLabelParam.getParentId(), 0)) {
                continue;
            }
            if (Objects.equals(metaLabelParam.getType(), LabelTypeEnum.CATEGORY.getCode())) {
                secondLabels.add(metaLabelParam.getId());
            }
            if (Objects.equals(metaLabelParam.getType(), LabelTypeEnum.LABEL.getCode())) {
                thirdLabels.add(metaLabelParam.getId());
            }
        }
        if (!CollectionUtils.isEmpty(secondLabels)) {
            //获取三级分类
            List<MetaLabel> metaLabelsThird = metaLabelMapper.collectMetaLabel(secondLabels);
            thirdLabels.addAll(metaLabelsThird.stream().map(MetaLabel::getId).collect(Collectors.toList()));
        }

        if (CollectionUtils.isEmpty(thirdLabels)) {
            return collectMetaResp;
        }

        List<MetaBookLabel> metaBookLabels = metaBookLabelMapper.collectMetaBookLabel(thirdLabels);
        List<Integer> bookIds = metaBookLabels.stream().map(MetaBookLabel::getBookId).collect(Collectors.toList());
        collectMetaResp.setMetaBookResps(super.collectBookByIds(bookIds));
        return collectMetaResp;
    }
}
