package com.wanmi.sbc.goods.collect;
import java.util.Date;

import com.wanmi.sbc.goods.api.request.collect.CollectClassifyProviderReq;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.goods.api.response.collect.CollectClassifyRelSpuDetailResp;
import com.wanmi.sbc.goods.api.response.collect.CollectClassifyRelSpuResp;
import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import com.wanmi.sbc.goods.classify.model.root.ClassifyGoodsRelDTO;
import com.wanmi.sbc.goods.classify.repository.ClassifyGoodsRelRepository;
import com.wanmi.sbc.goods.classify.repository.ClassifyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 采集店铺分类
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/14 2:05 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class CollectClassifyService {

    @Autowired
    private ClassifyRepository classifyRepository;

    @Autowired
    private ClassifyGoodsRelRepository classifyGoodsRelRepository;


    /**
     * 按照时间获取店铺分类修改对应的商品列表信息
     * @param req
     * @return
     */
    public List<CollectClassifyRelSpuResp> collectClassifySpuIdByTime(CollectClassifyProviderReq req) {
        List<CollectClassifyRelSpuResp> result = new ArrayList<>();
        List<ClassifyDTO> classifyList =
                classifyRepository.collectClassifyByTime(req.getBeginTime(), req.getEndTime(), req.getFromId(), req.getPageSize());
        List<Integer> firstClassifyIdList = new ArrayList<>();
        List<Integer> secondClassifyIdList = new ArrayList<>();
        for (ClassifyDTO classifyParam : classifyList) {
            if (classifyParam.getParentId() == 0) {
                firstClassifyIdList.add(classifyParam.getId());
            } else {
                secondClassifyIdList.add(classifyParam.getId());
            }
        }
        if (!CollectionUtils.isEmpty(firstClassifyIdList)) {
            //获取二级分类
            List<ClassifyDTO> classifySecondList = classifyRepository.collectClassifyByPClassifyIds(firstClassifyIdList);
            secondClassifyIdList.addAll(classifySecondList.stream().map(ClassifyDTO::getId).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(secondClassifyIdList)) {
            List<ClassifyGoodsRelDTO> classifyByClassifyIds = classifyGoodsRelRepository.collectClassifyByClassifyIds(secondClassifyIdList);
            for (ClassifyGoodsRelDTO classifyByClassifyIdParam : classifyByClassifyIds) {
                CollectClassifyRelSpuResp collectClassifyRelSpuResp = new CollectClassifyRelSpuResp();
                collectClassifyRelSpuResp.setClassifyId(classifyByClassifyIdParam.getClassifyId());
                collectClassifyRelSpuResp.setSpuId(classifyByClassifyIdParam.getGoodsId());
                collectClassifyRelSpuResp.setUpdateTime(classifyByClassifyIdParam.getUpdateTime());
                result.add(collectClassifyRelSpuResp);
            }
        }
        return result;
    }


    /**
     * 根据商品id采集店铺分类信息
     * @param req
     * @return
     */
    public List<CollectClassifyRelSpuDetailResp> collectClassifyBySpuIds(CollectClassifyProviderReq req) {
        List<ClassifyGoodsRelDTO> classifyGoodsRelDTOS = classifyGoodsRelRepository.collectClassifyBySpuIds(req.getSpuIds());
        //获取店铺详细信息
        List<Integer> secondClassifyIdList = classifyGoodsRelDTOS.stream().map(ClassifyGoodsRelDTO::getClassifyId).collect(Collectors.toList());;
        List<ClassifyDTO> secondClassifyDTOS = classifyRepository.collectClassifyByIds(secondClassifyIdList);
        Map<Integer, ClassifyDTO> secondClassifyId2ModelMap = secondClassifyDTOS.stream().collect(Collectors.toMap(ClassifyDTO::getId, Function.identity(), (k1, k2) -> k1));


        List<Integer> firstClassifyIdList = secondClassifyDTOS.stream().map(ClassifyDTO::getParentId).collect(Collectors.toList());
        List<ClassifyDTO> firstClassifyDTOS = classifyRepository.collectClassifyByIds(firstClassifyIdList);
        Map<Integer, ClassifyDTO> firstClassifyId2ModelMap = firstClassifyDTOS.stream().collect(Collectors.toMap(ClassifyDTO::getId, Function.identity(), (k1, k2) -> k1));


        List<CollectClassifyRelSpuDetailResp> result = new ArrayList<>();
        for (ClassifyGoodsRelDTO classifyGoodsRelDTO : classifyGoodsRelDTOS) {
            ClassifyDTO secondClassifyDTO = secondClassifyId2ModelMap.get(classifyGoodsRelDTO.getClassifyId());
            ClassifyDTO firstClassifyDTO = null;
            if (secondClassifyDTO != null) {
                firstClassifyDTO = firstClassifyId2ModelMap.getOrDefault(secondClassifyDTO.getParentId(), new ClassifyDTO());
            } else {
                log.warn("CollectClassifyService collectClassifyBySpuIds classifyId:{} ClassifyDTO is null continue", classifyGoodsRelDTO.getClassifyId());
                continue;
            }

            CollectClassifyRelSpuDetailResp collectClassifyRelSpuDetailResp = new CollectClassifyRelSpuDetailResp();
            collectClassifyRelSpuDetailResp.setClassifyName(secondClassifyDTO.getClassifyName());
            collectClassifyRelSpuDetailResp.setFClassifyId(firstClassifyDTO.getId());
            collectClassifyRelSpuDetailResp.setFClasssifyName(firstClassifyDTO.getClassifyName());
            collectClassifyRelSpuDetailResp.setClassifyId(secondClassifyDTO.getId());
            collectClassifyRelSpuDetailResp.setSpuId(classifyGoodsRelDTO.getGoodsId());
            collectClassifyRelSpuDetailResp.setUpdateTime(classifyGoodsRelDTO.getUpdateTime());
            result.add(collectClassifyRelSpuDetailResp);
        }

        return result;
    }
}
