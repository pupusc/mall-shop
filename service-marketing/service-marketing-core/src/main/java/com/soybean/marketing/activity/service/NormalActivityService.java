package com.soybean.marketing.activity.service;
import com.soybean.common.resp.CommonPageResp;
import com.soybean.marketing.api.req.NormalActivitySearchReq;
import com.soybean.marketing.api.resp.NormalActivityResp;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.soybean.marketing.activity.model.NormalActivity;
import com.soybean.marketing.activity.repository.NormalActivityRepository;
import com.soybean.marketing.api.req.NormalActivityReq;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.enums.StateEnum;
import com.wanmi.sbc.goods.api.request.index.NormalModuleSearchReq;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 2:16 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class NormalActivityService {

    @Autowired
    private NormalActivityRepository normalActivityRepository;


    /**
     * 新增
     * @param normalActivityReq
     */
    public NormalActivity add(NormalActivityReq normalActivityReq) {
        if (normalActivityReq.getBeginTime().isAfter(normalActivityReq.getEndTime())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "开始时间不能小于结束时间");
        }
        LocalDateTime now = LocalDateTime.now();
        NormalActivity normalActivity = new NormalActivity();
        normalActivity.setName(normalActivityReq.getName());
        normalActivity.setBeginTime(normalActivityReq.getBeginTime());
        normalActivity.setEndTime(normalActivityReq.getEndTime());
        normalActivity.setChannelType(normalActivityReq.getChannelType());
        normalActivity.setNormalCategory(normalActivityReq.getNormalCategory());
        normalActivity.setPublishState(PublishState.NOT_ENABLE.toValue());
        normalActivity.setCreateTime(now);
        normalActivity.setUpdateTime(now);
        normalActivity.setDelFlag(DeleteFlag.NO);
        return normalActivityRepository.save(normalActivity);
    }

    /**
     * 修改
     * @param normalActivityReq
     */
    public NormalActivity update(NormalActivityReq normalActivityReq) {
        if (normalActivityReq.getBeginTime().isAfter(normalActivityReq.getEndTime())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "开始时间不能小于结束时间");
        }
        if (normalActivityReq.getId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "活动id有误");
        }
        NormalActivitySearchReq searchReq = new NormalActivitySearchReq();
        searchReq.setId(normalActivityReq.getId());
        List<NormalActivity> normalActivities = normalActivityRepository.findAll(normalActivityRepository.buildSearchCondition(searchReq));
        if (CollectionUtils.isEmpty(normalActivities)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "活动不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        NormalActivity normalActivity = normalActivities.get(0);
        if (StringUtils.isNotBlank(normalActivityReq.getName())) {
            normalActivity.setName(normalActivityReq.getName());
        }
        if (normalActivityReq.getBeginTime() != null) {
            normalActivity.setBeginTime(normalActivityReq.getBeginTime());
        }
        if (normalActivityReq.getEndTime() != null) {
            normalActivity.setEndTime(normalActivityReq.getEndTime());
        }
        if (normalActivityReq.getChannelType() != null) {
            normalActivity.setChannelType(normalActivityReq.getChannelType());
        }
        if (normalActivityReq.getNormalCategory() != null) {
            normalActivity.setNormalCategory(normalActivityReq.getNormalCategory());
        }
        normalActivity.setUpdateTime(now);
        return normalActivityRepository.save(normalActivity);
    }


    /**
     * 获取活动列表
     */
    public CommonPageResp<List<NormalActivityResp>> list(NormalActivitySearchReq searchReq) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(searchReq.getPageNum(), searchReq.getPageSize(), sort);
        Page<NormalActivity> normalActivityPage =
                normalActivityRepository.findAll(normalActivityRepository.buildSearchCondition(searchReq), pageable);
        List<NormalActivityResp> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (NormalActivity normalActivity : normalActivityPage.getContent()) {
            NormalActivityResp normalActivityResp = new NormalActivityResp();
            BeanUtils.copyProperties(normalActivity, normalActivityResp);
            if (now.isBefore(normalActivity.getBeginTime())) {
                normalActivityResp.setStatus(StateEnum.BEFORE.getCode());
            } else if (now.isAfter(normalActivity.getBeginTime()) && now.isBefore(normalActivity.getEndTime())) {
                normalActivityResp.setStatus(StateEnum.RUNNING.getCode());
            } else {
                normalActivityResp.setStatus(StateEnum.AFTER.getCode());
            }
            result.add(normalActivityResp);
        }
        return new CommonPageResp<>(normalActivityPage.getTotalElements(), result);
    }


    /**
     * 开启/关闭
     */
    public void publish(Integer id, Boolean isOpen) {
        NormalActivitySearchReq searchReq = new NormalActivitySearchReq();
        searchReq.setId(id);
        List<NormalActivity> normalActivities = normalActivityRepository.findAll(normalActivityRepository.buildSearchCondition(searchReq));
        if (CollectionUtils.isEmpty(normalActivities)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "id有误");
        }
        NormalActivity normalActivity = normalActivities.get(0);
        normalActivity.setPublishState(isOpen ? PublishState.ENABLE.toValue() : PublishState.NOT_ENABLE.toValue());
        normalActivityRepository.save(normalActivity);
    }

}
