package com.wanmi.sbc.goods.collect;
import java.util.ArrayList;
import java.util.Date;

import com.wanmi.sbc.goods.api.request.collect.CollectClassifyProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectClassifyRelSpuDetailResp;
import com.wanmi.sbc.goods.api.response.collect.CollectClassifyRelSpuResp;
import com.wanmi.sbc.goods.classify.model.root.ClassifyGoodsRelDTO;
import com.wanmi.sbc.goods.classify.repository.ClassifyGoodsRelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/14 12:31 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectClassifyRelSpuService {

    @Autowired
    private ClassifyGoodsRelRepository classifyGoodsRelRepository;

    /**
     * 采集店铺分类 商品关系表
     * @param req
     * @return
     */
    public List<CollectClassifyRelSpuResp> collectClassifySpuIdByTime(CollectClassifyProviderReq req){
        List<ClassifyGoodsRelDTO> classifyGoodsRelDTOS =
                classifyGoodsRelRepository.collectClassifySpuIdByTime(req.getBeginTime(), req.getEndTime(), req.getFromId(), req.getPageSize());
        List<CollectClassifyRelSpuResp> result = new ArrayList<>();
        for (ClassifyGoodsRelDTO classifyGoodsRelDTO : classifyGoodsRelDTOS) {
            CollectClassifyRelSpuResp collectClassifyRelSpuResp = new CollectClassifyRelSpuDetailResp();
            collectClassifyRelSpuResp.setClassifySpuRelId(classifyGoodsRelDTO.getId());
            collectClassifyRelSpuResp.setClassifyId(classifyGoodsRelDTO.getClassifyId());
            collectClassifyRelSpuResp.setSpuId(classifyGoodsRelDTO.getGoodsId());
            collectClassifyRelSpuResp.setUpdateTime(classifyGoodsRelDTO.getUpdateTime());
            result.add(collectClassifyRelSpuResp);
        }
        return result;
    }
}
