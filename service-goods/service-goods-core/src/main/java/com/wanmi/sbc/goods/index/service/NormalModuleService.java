package com.wanmi.sbc.goods.index.service;
import com.soybean.common.resp.CommonPageResp;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.enums.StateEnum;
import com.wanmi.sbc.goods.api.request.index.NormalModuleReq;
import com.wanmi.sbc.goods.api.request.index.NormalModuleSearchReq;
import com.wanmi.sbc.goods.api.request.index.NormalModuleSkuReq;
import com.wanmi.sbc.goods.api.request.index.NormalModuleSkuSearchReq;
import com.wanmi.sbc.goods.api.response.index.NormalModuleResp;
import com.wanmi.sbc.goods.api.response.index.NormalModuleSkuResp;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import com.wanmi.sbc.goods.index.model.NormalModule;
import com.wanmi.sbc.goods.index.model.NormalModuleSku;
import com.wanmi.sbc.goods.index.repository.NormalModuleRepository;
import com.wanmi.sbc.goods.index.repository.NormalModuleSkuRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/11 11:00 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@Service
public class NormalModuleService {

    @Autowired
    private NormalModuleRepository normalModuleRepository;

    @Autowired
    private NormalModuleSkuRepository normalModuleSkuRepository;

    /**
     * 新增栏目
     * @param normalModuleReq
     */
    @Transactional
    public void add(NormalModuleReq normalModuleReq) {

        if (normalModuleReq.getBeginTime().isAfter(normalModuleReq.getEndTime())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "开始时间不能小于结束时间");
        }

        if (CollectionUtils.isEmpty(normalModuleReq.getNormalModuleSkus()) || normalModuleReq.getNormalModuleSkus().size() > 100 ) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "必须得有商品信息，最大为100个");
        }
        this.verification(normalModuleReq.getBeginTime(), normalModuleReq.getEndTime(), null);

        NormalModule normalModule = new NormalModule();
        normalModule.setName(normalModuleReq.getName());
        normalModule.setBeginTime(normalModuleReq.getBeginTime());
        normalModule.setEndTime(normalModuleReq.getEndTime());
        normalModule.setModelCategory(normalModuleReq.getModelCategory());
        normalModule.setPublishState(PublishState.NOT_ENABLE.toValue());
        normalModule.setCreateTime(LocalDateTime.now());
        normalModule.setUpdateTime(LocalDateTime.now());
        normalModule.setDelFlag(DeleteFlag.NO);
        normalModuleRepository.save(normalModule);

        List<NormalModuleSku> normalModuleSkuResult = new ArrayList<>();

        List<NormalModuleSkuReq> normalModuleSkus = normalModuleReq.getNormalModuleSkus() == null
                ? new ArrayList<>() : normalModuleReq.getNormalModuleSkus();
        int sortNum = 1;
        for (NormalModuleSkuReq normalModuleSkuReq : normalModuleSkus) {
            NormalModuleSku normalModuleSku = new NormalModuleSku();
            normalModuleSku.setNormalModelId(normalModule.getId());
            normalModuleSku.setSkuId(normalModuleSkuReq.getSkuId());
            normalModuleSku.setSkuNo(normalModuleSkuReq.getSkuNo());
            normalModuleSku.setSpuId(normalModuleSkuReq.getSpuId());
            normalModuleSku.setSpuNo(normalModuleSkuReq.getSpuNo());
            normalModuleSku.setSortNum(sortNum++);
            normalModuleSku.setSkuTag(normalModuleSkuReq.getSkuTag());
            normalModuleSku.setCreateTime(LocalDateTime.now());
            normalModuleSku.setUpdateTime(LocalDateTime.now());
            normalModuleSku.setDelFlag(DeleteFlag.NO);
            normalModuleSkuResult.add(normalModuleSku);
        }
        if (CollectionUtils.isNotEmpty(normalModuleSkuResult)) {
            normalModuleSkuRepository.saveAll(normalModuleSkuResult);
        }
    }

    /**
     * 更新栏目
     * @param normalModuleReq
     */
    @Transactional
    public void update(NormalModuleReq normalModuleReq) {
        if (normalModuleReq.getBeginTime().isAfter(normalModuleReq.getEndTime())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "开始时间不能小于结束时间");
        }

        if (CollectionUtils.isEmpty(normalModuleReq.getNormalModuleSkus()) || normalModuleReq.getNormalModuleSkus().size() > 100 ) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "必须得有商品信息，最大为100个");
        }

        if (normalModuleReq.getId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "请输入id");
        }

        this.verification(normalModuleReq.getBeginTime(), normalModuleReq.getEndTime(), normalModuleReq.getId());

        NormalModuleSearchReq searchReq = new NormalModuleSearchReq();
        searchReq.setId(normalModuleReq.getId());
        List<NormalModule> normalModules = normalModuleRepository.findAll(normalModuleRepository.packageWhere(searchReq));
        if (CollectionUtils.isEmpty(normalModules)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "id有误");
        }
        NormalModule normalModule = normalModules.get(0);
        LocalDateTime now = LocalDateTime.now();
        if (StringUtils.isNotBlank(normalModuleReq.getName())) {
            normalModule.setName(normalModuleReq.getName());
        }
        if (normalModuleReq.getBeginTime() != null) {
            normalModule.setBeginTime(normalModuleReq.getBeginTime());
        }
        if (normalModuleReq.getEndTime() != null) {
            normalModule.setEndTime(normalModuleReq.getEndTime());
        }
        normalModule.setUpdateTime(now);
        normalModuleRepository.save(normalModule);

        //获取要删除商品信息
        NormalModuleSkuSearchReq normalModuleSkuSearchReq = new NormalModuleSkuSearchReq();
        normalModuleSkuSearchReq.setNormalModuleId(normalModule.getId());
        List<NormalModuleSku> rawNormalModuleSkus =
                normalModuleSkuRepository.findAll(normalModuleSkuRepository.packageWhere(normalModuleSkuSearchReq));
        //逻辑删除
        if (CollectionUtils.isNotEmpty(rawNormalModuleSkus)) {
            List<NormalModuleSku> normalModuleSkuList = new ArrayList<>();
            for (NormalModuleSku normalModuleSku : rawNormalModuleSkus) {
                normalModuleSku.setUpdateTime(now);
                normalModuleSku.setDelFlag(DeleteFlag.YES);
                normalModuleSkuList.add(normalModuleSku);
            }
            normalModuleSkuRepository.saveAll(normalModuleSkuList);
        }

        //新增商品信息
        List<NormalModuleSku> normalModuleSkuResult = new ArrayList<>();
        List<NormalModuleSkuReq> normalModuleSkus = normalModuleReq.getNormalModuleSkus() == null
                ? new ArrayList<>() : normalModuleReq.getNormalModuleSkus();
        int sortNum = 1;
        for (NormalModuleSkuReq normalModuleSkuReq : normalModuleSkus) {
            NormalModuleSku normalModuleSku = new NormalModuleSku();
            normalModuleSku.setNormalModelId(normalModule.getId());
            normalModuleSku.setSkuId(normalModuleSkuReq.getSkuId());
            normalModuleSku.setSkuNo(normalModuleSkuReq.getSkuNo());
            normalModuleSku.setSpuId(normalModuleSkuReq.getSpuId());
            normalModuleSku.setSpuNo(normalModuleSkuReq.getSpuNo());
            normalModuleSku.setSortNum(sortNum++);
            normalModuleSku.setSkuTag(normalModuleSkuReq.getSkuTag());
            normalModuleSku.setCreateTime(now);
            normalModuleSku.setUpdateTime(now);
            normalModuleSku.setDelFlag(DeleteFlag.NO);
            normalModuleSkuResult.add(normalModuleSku);
        }
        if (CollectionUtils.isNotEmpty(normalModuleSkuResult)) {
            normalModuleSkuRepository.saveAll(normalModuleSkuResult);
        }
    }


    /**
     * 获取列表，分页
     * @return
     */
    public CommonPageResp<List<NormalModuleResp>> list(NormalModuleSearchReq normalModuleSearchReq) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(normalModuleSearchReq.getPageNum(), normalModuleSearchReq.getPageSize(), sort);
        Page<NormalModule> normalModulePage = normalModuleRepository.findAll(normalModuleRepository.packageWhere(normalModuleSearchReq), pageable);
        List<NormalModuleResp> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (NormalModule normalModule : normalModulePage.getContent()) {
            NormalModuleResp normalModuleResp = new NormalModuleResp();
            normalModuleResp.setId(normalModule.getId());
            normalModuleResp.setName(normalModule.getName());
            normalModuleResp.setBeginTime(normalModule.getBeginTime());
            normalModuleResp.setEndTime(normalModule.getEndTime());
            normalModuleResp.setPublishState(normalModule.getPublishState());
            if (now.isBefore(normalModule.getBeginTime())) {
                normalModuleResp.setStatus(StateEnum.BEFORE.getCode());
            } else if (now.isAfter(normalModule.getBeginTime()) && now.isBefore(normalModule.getEndTime())) {
                normalModuleResp.setStatus(StateEnum.RUNNING.getCode());
            } else {
                normalModuleResp.setStatus(StateEnum.AFTER.getCode());
            }
            result.add(normalModuleResp);
        }
        return new CommonPageResp<>(normalModulePage.getTotalElements(), result);
    }

    /**
     * 无分页列表
     * @param normalModuleSearchReq
     * @return
     */
    public List<NormalModule> listNoPage(NormalModuleSearchReq normalModuleSearchReq) {
        return normalModuleRepository.findAll(normalModuleRepository.packageWhere(normalModuleSearchReq));
    }

    /**
     * 开启
     */
    public void publish(Integer id, Boolean isOpen) {
        NormalModuleSearchReq searchReq = new NormalModuleSearchReq();
        searchReq.setId(id);
        List<NormalModule> normalModules = normalModuleRepository.findAll(normalModuleRepository.packageWhere(searchReq));
        if (CollectionUtils.isEmpty(normalModules)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "id有误");
        }
        NormalModule normalModule = normalModules.get(0);
        if (isOpen) {
            //获取商品列表
            NormalModuleSkuSearchReq normalModuleSkuSearchReq = new NormalModuleSkuSearchReq();
            normalModuleSkuSearchReq.setNormalModuleId(id);
            List<NormalModuleSku> normalModuleSkuList =
                    normalModuleSkuRepository.findAll(normalModuleSkuRepository.packageWhere(normalModuleSkuSearchReq));
            //获取商品信息
            if (CollectionUtils.isNotEmpty(normalModuleSkuList)) {
                this.verification(normalModule.getBeginTime(), normalModule.getEndTime(), id);
            }
        }

        normalModule.setPublishState(isOpen ? PublishState.ENABLE.toValue() : PublishState.NOT_ENABLE.toValue());
        normalModuleRepository.save(normalModule);
    }


    /**
     * 查询栏目商品
     * @param normalModuleSkuSearchReq
     * @return
     */
    public List<NormalModuleSkuResp> listNormalModuleSku(NormalModuleSkuSearchReq normalModuleSkuSearchReq) {
        Sort sort = Sort.by(Sort.Direction.ASC, "sortNum");
        List<NormalModuleSku> normalModuleSkus =
                normalModuleSkuRepository.findAll(normalModuleSkuRepository.packageWhere(normalModuleSkuSearchReq), sort);
        List<NormalModuleSkuResp> result = new ArrayList<>();
        for (NormalModuleSku moduleSku : normalModuleSkus) {
            NormalModuleSkuResp normalModuleSkuResp = new NormalModuleSkuResp();
            normalModuleSkuResp.setId(moduleSku.getId());
            normalModuleSkuResp.setNormalModelId(moduleSku.getNormalModelId());
            normalModuleSkuResp.setSkuId(moduleSku.getSkuId());
            normalModuleSkuResp.setSkuNo(moduleSku.getSkuNo());
            normalModuleSkuResp.setSpuId(moduleSku.getSpuId());
            normalModuleSkuResp.setSpuNo(moduleSku.getSpuNo());
            normalModuleSkuResp.setSortNum(moduleSku.getSortNum());
            normalModuleSkuResp.setSkuTag(moduleSku.getSkuTag());
            result.add(normalModuleSkuResp);
        }
        return result;
    }


    /**
     * 校验是否有重叠的栏目
     * @param beginTime
     * @param endTime
     */
    private void verification(LocalDateTime beginTime, LocalDateTime endTime, Integer excludeNormalModuleId) {
        //查询是否有重叠的活动；
        NormalModuleSearchReq searchReq = new NormalModuleSearchReq();
        searchReq.setPublishState(PublishState.ENABLE.toValue());
        searchReq.setBeginTimeR(beginTime);
        searchReq.setEndTimeR(endTime);
        searchReq.setExcludeNormalModuleId(excludeNormalModuleId);
        List<NormalModule> normalModules = this.listNoPage(searchReq);
        if (CollectionUtils.isEmpty(normalModules)) {
            return;
        }

        for (NormalModule normalModule : normalModules) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "当前投放与其他进行中投放时间重叠，请修改");
        }
    }
}
