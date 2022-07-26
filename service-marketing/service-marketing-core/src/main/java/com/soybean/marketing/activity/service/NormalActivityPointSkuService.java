package com.soybean.marketing.activity.service;
import com.soybean.marketing.api.req.NormalActivityPointSkuSearchReq;
import com.soybean.marketing.api.req.SpuNormalActivityReq;
import com.soybean.marketing.api.resp.NormalActivitySkuResp;
import com.soybean.marketing.api.resp.SkuNormalActivityResp;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.time.LocalDateTime;

import com.soybean.marketing.activity.model.NormalActivity;
import com.soybean.marketing.activity.model.NormalActivityPointSku;
import com.soybean.marketing.activity.repository.NormalActivityPointSkuRepository;
import com.soybean.marketing.api.req.NormalActivityPointSkuReq;
import com.soybean.marketing.api.req.NormalActivitySearchReq;
import com.soybean.marketing.api.req.NormalActivitySkuReq;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.enums.StateEnum;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 2:16 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class NormalActivityPointSkuService extends NormalActivityService {

    @Autowired
    private NormalActivityPointSkuRepository normalActivityPointSkuRepository;

    @Transactional
    public void add(NormalActivityPointSkuReq normalActivityPointSkuReq) {
        if (normalActivityPointSkuReq.getBeginTime().isAfter(normalActivityPointSkuReq.getEndTime())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "开始时间不能小于结束时间");
        }

        if (CollectionUtils.isEmpty(normalActivityPointSkuReq.getNormalActivitySkus()) || normalActivityPointSkuReq.getNormalActivitySkus().size() > 100 ) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "活动商品得有商品，最大为100个");
        }

        List<String> skuIds =
                normalActivityPointSkuReq.getNormalActivitySkus().stream().map(NormalActivitySkuReq::getSkuId).collect(Collectors.toList());
        this.verification(normalActivityPointSkuReq.getBeginTime(), normalActivityPointSkuReq.getEndTime(), skuIds, null);

        //查询活动下是否有对应的商品

        NormalActivity normalActivity = super.add(normalActivityPointSkuReq);

        LocalDateTime now = LocalDateTime.now();
        List<NormalActivityPointSku> result = new ArrayList<>();
        for (NormalActivitySkuReq normalActivitySkuParam : normalActivityPointSkuReq.getNormalActivitySkus()) {
            NormalActivityPointSku normalActivityPointSku = new NormalActivityPointSku();
            normalActivityPointSku.setNormalActivityId(normalActivity.getId());
            normalActivityPointSku.setSkuId(normalActivitySkuParam.getSkuId());
            normalActivityPointSku.setSkuNo(normalActivitySkuParam.getSkuNo());
            normalActivityPointSku.setSpuId(normalActivitySkuParam.getSpuId());
            normalActivityPointSku.setSpuNo(normalActivitySkuParam.getSpuNo());
            normalActivityPointSku.setNum(normalActivitySkuParam.getNum());
            normalActivityPointSku.setCreateTime(now);
            normalActivityPointSku.setUpdateTime(now);
            normalActivityPointSku.setDelFlag(DeleteFlag.NO);
            result.add(normalActivityPointSku);
        }
        normalActivityPointSkuRepository.saveAll(result);
    }


    /**
     * 编辑活动
     * @param normalActivityPointSkuReq
     */
    @Transactional
    public void update(NormalActivityPointSkuReq normalActivityPointSkuReq) {
        if (normalActivityPointSkuReq.getBeginTime().isAfter(normalActivityPointSkuReq.getEndTime())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "开始时间不能小于结束时间");
        }
        //查询活动
        if (CollectionUtils.isEmpty(normalActivityPointSkuReq.getNormalActivitySkus()) || normalActivityPointSkuReq.getNormalActivitySkus().size() > 100 ) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "活动商品得有商品，最大为100个");
        }
        if (normalActivityPointSkuReq.getId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "活动id不能为空");
        }

        List<String> skuIds =
                normalActivityPointSkuReq.getNormalActivitySkus().stream().map(NormalActivitySkuReq::getSkuId).collect(Collectors.toList());
        this.verification(normalActivityPointSkuReq.getBeginTime(), normalActivityPointSkuReq.getEndTime(), skuIds, normalActivityPointSkuReq.getId());

        NormalActivity normalActivity = super.update(normalActivityPointSkuReq);

        LocalDateTime now = LocalDateTime.now();
        //获取要删除商品信息
        NormalActivityPointSkuSearchReq searchReq = new NormalActivityPointSkuSearchReq();
        searchReq.setNormalActivityId(normalActivity.getId());
        List<NormalActivityPointSku> rawNormalActivityPointSkus =
                normalActivityPointSkuRepository.findAll(normalActivityPointSkuRepository.buildSearchCondition(searchReq));
        //作废活动下的商品
        if (CollectionUtils.isNotEmpty(rawNormalActivityPointSkus)) {
            List<NormalActivityPointSku> normalActivityPointSkuList = new ArrayList<>();
            for (NormalActivityPointSku normalActivityPointSku : rawNormalActivityPointSkus) {
                normalActivityPointSku.setUpdateTime(now);
                normalActivityPointSku.setDelFlag(DeleteFlag.YES);
                normalActivityPointSkuList.add(normalActivityPointSku);
            }
            normalActivityPointSkuRepository.saveAll(normalActivityPointSkuList);
        }


        List<NormalActivityPointSku> result = new ArrayList<>();
        for (NormalActivitySkuReq normalActivitySkuParam : normalActivityPointSkuReq.getNormalActivitySkus()) {
            NormalActivityPointSku normalActivityPointSku = new NormalActivityPointSku();
            normalActivityPointSku.setNormalActivityId(normalActivity.getId());
            normalActivityPointSku.setSkuId(normalActivitySkuParam.getSkuId());
            normalActivityPointSku.setSkuNo(normalActivitySkuParam.getSkuNo());
            normalActivityPointSku.setSpuId(normalActivitySkuParam.getSpuId());
            normalActivityPointSku.setSpuNo(normalActivitySkuParam.getSpuNo());
            normalActivityPointSku.setNum(normalActivitySkuParam.getNum());
            normalActivityPointSku.setCreateTime(now);
            normalActivityPointSku.setUpdateTime(now);
            normalActivityPointSku.setDelFlag(DeleteFlag.NO);
            result.add(normalActivityPointSku);
        }
        normalActivityPointSkuRepository.saveAll(result);

    }


    /**
     * 活动下的商品列表
     * @param id
     * @return
     */
    public List<NormalActivitySkuResp> listActivityPointSku(Integer id) {
        NormalActivitySearchReq searchReq = new NormalActivitySearchReq();
        searchReq.setId(id);
        List<NormalActivity> normalActivities = super.listNoPage(searchReq);
        if (CollectionUtils.isEmpty(normalActivities)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "该活动不存在");
        }

        //获取商品列表
        NormalActivityPointSkuSearchReq normalActivityPointSkuSearchReq = new NormalActivityPointSkuSearchReq();
        normalActivityPointSkuSearchReq.setNormalActivityId(id);
        List<NormalActivityPointSku> normalActivityPointSkus =
                normalActivityPointSkuRepository.findAll(normalActivityPointSkuRepository.buildSearchCondition(normalActivityPointSkuSearchReq));

        List<NormalActivitySkuResp> resultSkus = new ArrayList<>();
        for (NormalActivityPointSku activityPointSkuParam : normalActivityPointSkus) {
            NormalActivitySkuResp normalActivitySkuResp = new NormalActivitySkuResp();
            BeanUtils.copyProperties(activityPointSkuParam, normalActivitySkuResp);
            resultSkus.add(normalActivitySkuResp);
        }

        return resultSkus;
    }


    /**
     * 开启/关闭
     */
    public void publish(Integer id, Boolean isOpen) {
        NormalActivitySearchReq searchReq = new NormalActivitySearchReq();
        searchReq.setId(id);
        List<NormalActivity> normalActivities = super.listNoPage(searchReq);
        if (CollectionUtils.isEmpty(normalActivities)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "该活动不存在");
        }
        NormalActivity normalActivity = normalActivities.get(0);
        if (isOpen) {
            //获取商品列表
            NormalActivityPointSkuSearchReq normalActivityPointSkuSearchReq = new NormalActivityPointSkuSearchReq();
            normalActivityPointSkuSearchReq.setNormalActivityId(id);
            List<NormalActivityPointSku> normalActivityPointSkus =
                    normalActivityPointSkuRepository.findAll(normalActivityPointSkuRepository.buildSearchCondition(normalActivityPointSkuSearchReq));
            //获取商品信息
            if (CollectionUtils.isNotEmpty(normalActivityPointSkus)) {
                List<String> skuIds = normalActivityPointSkus.stream().map(NormalActivityPointSku::getSkuId).collect(Collectors.toList());
                this.verification(normalActivity.getBeginTime(), normalActivity.getEndTime(), skuIds, id);
            }
        }

        normalActivity.setPublishState(isOpen ? PublishState.ENABLE.toValue() : PublishState.NOT_ENABLE.toValue());
        normalActivityRepository.save(normalActivity);
    }


    /**
     * 校验是否有重叠的活动
     * @param beginTime
     * @param endTime
     * @param skuIds
     */
    private void verification(LocalDateTime beginTime, LocalDateTime endTime, List<String> skuIds, Integer excludeActivityId) {
        //查询是否有重叠的活动；
        NormalActivitySearchReq searchReq = new NormalActivitySearchReq();
        searchReq.setPublishState(PublishState.ENABLE.toValue());
        searchReq.setBeginTimeR(beginTime);
        searchReq.setEndTimeR(endTime);
        List<NormalActivity> normalActivities = super.listNoPage(searchReq);
        if (CollectionUtils.isNotEmpty(normalActivities)) {
            List<Integer> normalActivityIds = normalActivities.stream()
                    .map(NormalActivity::getId)
                    .filter(id -> !Objects.equals(id, excludeActivityId)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(normalActivityIds)) {
                return;
            }
            //有重叠的活动，则查看是否有重叠的商品
            NormalActivityPointSkuSearchReq pointSkuSearchReq = new NormalActivityPointSkuSearchReq();
            pointSkuSearchReq.setNormalActivityIds(normalActivityIds);
            pointSkuSearchReq.setSkuIds(skuIds);
            List<NormalActivityPointSku> normalActivityPointSkus =
                    normalActivityPointSkuRepository.findAll(normalActivityPointSkuRepository.buildSearchCondition(pointSkuSearchReq));
            if (CollectionUtils.isNotEmpty(normalActivityPointSkus)) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品" + normalActivityPointSkus.get(0).getSkuNo() + "和其他活动有冲突");
            }
        }
    }




    /**
     * 查看 sku对应的活动
     * @param spuNormalActivityReq
     * @return
     */
    public List<SkuNormalActivityResp> listSpuRunningNormalActivity(SpuNormalActivityReq spuNormalActivityReq) {
        if (CollectionUtils.isEmpty(spuNormalActivityReq.getSpuIds()) && CollectionUtils.isEmpty(spuNormalActivityReq.getSkuIds())) {
            return new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(spuNormalActivityReq.getChannelTypes())) {
            return new ArrayList<>();
        }
        NormalActivitySearchReq normalActivitySearchReq = new NormalActivitySearchReq();
        if (spuNormalActivityReq.getStatus() != null) {
            normalActivitySearchReq.setStatus(spuNormalActivityReq.getStatus());
        }

        if (spuNormalActivityReq.getPublishState() != null) {
            normalActivitySearchReq.setPublishState(spuNormalActivityReq.getPublishState());
        }

        if (spuNormalActivityReq.getBeginTime() != null && spuNormalActivityReq.getEndTime() != null) {
            normalActivitySearchReq.setBeginTime(spuNormalActivityReq.getBeginTime());
            normalActivitySearchReq.setEndTime(spuNormalActivityReq.getEndTime());
        }

        normalActivitySearchReq.setChannelTypes(spuNormalActivityReq.getChannelTypes());
        List<NormalActivity> normalActivities = super.listNoPage(normalActivitySearchReq); //进行中的活动
        if (CollectionUtils.isEmpty(normalActivities)) {
            return new ArrayList<>();
        }
        //获取商品对应的活动
        Map<Integer, NormalActivity> activityId2NormalActivityMap = new HashMap<>();
        List<Integer> activityIds = new ArrayList<>();
        for (NormalActivity normalActivity : normalActivities) {
            activityId2NormalActivityMap.put(normalActivity.getId(), normalActivity);
            activityIds.add(normalActivity.getId());
        }
        NormalActivityPointSkuSearchReq pointSkuSearchReq = new NormalActivityPointSkuSearchReq();
        pointSkuSearchReq.setNormalActivityIds(activityIds);
        if (CollectionUtils.isNotEmpty(spuNormalActivityReq.getSpuIds())) {
            pointSkuSearchReq.setSpuIds(spuNormalActivityReq.getSpuIds());
        }
        if (CollectionUtils.isNotEmpty(spuNormalActivityReq.getSkuIds())) {
            pointSkuSearchReq.setSkuIds(spuNormalActivityReq.getSkuIds());
        }

        List<NormalActivityPointSku> normalActivityPointSkus =
                normalActivityPointSkuRepository.findAll(normalActivityPointSkuRepository.buildSearchCondition(pointSkuSearchReq));
        if (CollectionUtils.isEmpty(normalActivityPointSkus)) {
            return new ArrayList<>();
        }

        List<SkuNormalActivityResp> result = new ArrayList<>();
        for (NormalActivityPointSku activityPointSkuParam : normalActivityPointSkus) {
            NormalActivity normalActivity = activityId2NormalActivityMap.get(activityPointSkuParam.getNormalActivityId());
            if (normalActivity == null) {
                continue;
            }
            SkuNormalActivityResp skuForNormalActivityResp = new SkuNormalActivityResp();
            skuForNormalActivityResp.setId(activityPointSkuParam.getId());
            skuForNormalActivityResp.setNormalActivityId(normalActivity.getId());
            skuForNormalActivityResp.setSkuId(activityPointSkuParam.getSkuId());
            skuForNormalActivityResp.setSkuNo(activityPointSkuParam.getSkuNo());
            skuForNormalActivityResp.setSpuId(activityPointSkuParam.getSpuId());
            skuForNormalActivityResp.setSpuNo(activityPointSkuParam.getSpuNo());
            skuForNormalActivityResp.setNum(activityPointSkuParam.getNum());
            skuForNormalActivityResp.setId(normalActivity.getId());
            skuForNormalActivityResp.setName(normalActivity.getName());
            skuForNormalActivityResp.setBeginTime(normalActivity.getBeginTime());
            skuForNormalActivityResp.setEndTime(normalActivity.getEndTime());
            skuForNormalActivityResp.setChannelType(normalActivity.getChannelType());
            skuForNormalActivityResp.setPublishState(normalActivity.getPublishState());
            result.add(skuForNormalActivityResp);
        }
        return result;
    }
}
