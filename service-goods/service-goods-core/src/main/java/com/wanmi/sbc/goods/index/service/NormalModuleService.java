package com.wanmi.sbc.goods.index.service;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.request.index.NormalModuleReq;
import com.wanmi.sbc.goods.api.request.index.NormalModuleSkuReq;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import com.wanmi.sbc.goods.index.model.NormalModule;
import com.wanmi.sbc.goods.index.model.NormalModuleSku;
import com.wanmi.sbc.goods.index.repository.NormalModuleRepository;
import com.wanmi.sbc.goods.index.repository.NormalModuleSkuRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "必须的有商品信息，最大为100个");
        }

        NormalModule normalModule = new NormalModule();
        normalModule.setName(normalModuleReq.getName());
        normalModule.setBeginTime(normalModuleReq.getBeginTime());
        normalModule.setEndTime(normalModuleReq.getEndTime());
        normalModule.setModelCategory(normalModuleReq.getModelCategory());
        normalModule.setPublishState(PublishState.NOT_ENABLE.toValue());
        normalModule.setModelTag(normalModuleReq.getModelTag());
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
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "必须的有商品信息，最大为100个");
        }

        List<NormalModule> normalModules = normalModuleRepository.findAll(normalModuleRepository.packageWhere());
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
        if (StringUtils.isNotBlank(normalModuleReq.getModelTag())) {
            normalModule.setModelTag(normalModuleReq.getModelTag());
        }
        normalModule.setUpdateTime(now);
        normalModuleRepository.save(normalModule);

        //获取要删除商品信息
        List<NormalModuleSku> rawNormalModuleSkus = normalModuleSkuRepository.findAll(normalModuleSkuRepository.packageWhere());
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
     * 获取列表，无分页
     * @return
     */
    public List<NormalModule> listNoPage() {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        return normalModuleRepository.findAll(normalModuleRepository.packageWhere(), sort);
    }

    /**
     * 获取列表，分页
     * @return
     */
    public List<NormalModule> list() {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
//        Pageable pageable = PageRequest.of(request.getPageNum(), request.getPageSize(), sort);
//        return normalModuleRepository.findAll(normalModuleRepository.packageWhere(request), pageable);
        return null;
    }

    public void delete() {

    }
}
