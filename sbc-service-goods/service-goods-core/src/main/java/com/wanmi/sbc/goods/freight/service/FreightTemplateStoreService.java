package com.wanmi.sbc.goods.freight.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.handler.aop.MasterRouteOnly;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.constant.FreightTemplateErrorCode;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateStorePageRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateStoreSaveRequest;
import com.wanmi.sbc.goods.freight.model.root.FreightTemplateStore;
import com.wanmi.sbc.goods.freight.repository.FreightTemplateStoreRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 店铺运费模板服务
 * Created by sunkun on 2018/5/3.
 */
@Service
public class FreightTemplateStoreService {

    @Resource
    private FreightTemplateStoreRepository freightTemplateStoreRepository;

    /**
     * 更新店铺运费模板
     *
     * @param request 店铺运费模板信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void renewalFreightTemplateStore(FreightTemplateStoreSaveRequest request) {
        FreightTemplateStore freightTemplateStore = null;
        if (request.getFreightTempId() == null) {
            this.freightTemplateNameIsRepetition(request.getStoreId(), request.getFreightTempName());
            //新增店铺运费模板
            freightTemplateStore = new FreightTemplateStore();
            BeanUtils.copyProperties(request, freightTemplateStore);
            freightTemplateStore.setCreateTime(LocalDateTime.now());
            freightTemplateStore.setDefaultFlag(DefaultFlag.NO);
            freightTemplateStore.setDelFlag(DeleteFlag.NO);
        } else {
            //修改店铺运费模板
            freightTemplateStore = this.queryById(request.getFreightTempId());
            if (!Objects.equals(freightTemplateStore.getStoreId(), request.getStoreId())) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            if (!StringUtils.equals(freightTemplateStore.getFreightTempName(), request.getFreightTempName())) {
                this.freightTemplateNameIsRepetition(freightTemplateStore.getStoreId(), request.getFreightTempName());
            }
            freightTemplateStore.setFixedFreight(request.getFixedFreight());
            freightTemplateStore.setFreightType(request.getFreightType());
            freightTemplateStore.setSatisfyFreight(request.getSatisfyFreight());
            freightTemplateStore.setSatisfyPrice(request.getSatisfyPrice());
            freightTemplateStore.setFreightTempName(request.getFreightTempName());
            freightTemplateStore.setDeliverWay(request.getDeliverWay());
        }
        freightTemplateStore.setDestinationArea(StringUtils.join(request.getDestinationArea(), ","));
        freightTemplateStore.setDestinationAreaName(StringUtils.join(request.getDestinationAreaName(), ","));
        List<Long> araeList = this.querySelectedArea(freightTemplateStore.getStoreId(), freightTemplateStore.getFreightTempId() != null ? freightTemplateStore.getFreightTempId() : 0L);
        //非默认模板校验区域是否重复
        if(Objects.equals(freightTemplateStore.getDefaultFlag(),DefaultFlag.NO)){
            araeList.addAll(Arrays.asList(request.getDestinationArea()).stream().map(Long::valueOf).collect(Collectors.toList()));
            if (this.verifyAreaRepetition(araeList)) {
                throw new SbcRuntimeException(FreightTemplateErrorCode.AREA_REPETITION_SETTING);
            }
        }
        freightTemplateStoreRepository.save(freightTemplateStore);
    }

    /**
     * 校验名称重复
     *
     * @param storeId 店铺id
     * @param freightTempName 模板名称
     */
    public void freightTemplateNameIsRepetition(Long storeId, String freightTempName) {
        FreightTemplateStore freightTemplateStore = freightTemplateStoreRepository.findByFreightTemplateName(storeId, freightTempName);
        if (freightTemplateStore != null) {
            throw new SbcRuntimeException(FreightTemplateErrorCode.STORE_NAME_EXIST);
        }
    }

    /**
     * 根据id查询店铺运费模板
     *
     * @param freightTempId 店铺运费模板id
     * @return 店铺运费模板
     */
    public FreightTemplateStore queryById(Long freightTempId) {
        FreightTemplateStore freightTemplateStore = freightTemplateStoreRepository.findByIdAndDefaultFlag(freightTempId);
        if (freightTemplateStore == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return freightTemplateStore;
    }

    /**
     * 查询店铺下所有店铺运费模板
     *
     * @param request 分页参数
     * @return 店铺运费模板分页列表
     */
    public Page<FreightTemplateStore> queryByAll(FreightTemplateStorePageRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("defaultFlag", "desc");
        map.put("createTime", "desc");
        request.setSortMap(map);
        Page<FreightTemplateStore> page = freightTemplateStoreRepository.findAll(getWhereCriteria(request.getStoreId()), request.getPageRequest());
        return page;
    }

    /**
     * 根据主键id删除店铺运费模板
     *
     * @param id 店铺运费模板id
     * @param storeId 店铺id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delById(Long id, Long storeId) {
        FreightTemplateStore freightTemplateStore = this.queryById(id);
        if (!Objects.equals(freightTemplateStore.getStoreId(), storeId) ||
                Objects.equals(freightTemplateStore.getDefaultFlag(), DefaultFlag.YES)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        freightTemplateStore.setDelFlag(DeleteFlag.YES);
        freightTemplateStoreRepository.save(freightTemplateStore);
    }

    /**
     * 根据店铺id和删除状态查询店铺运费模板列表
     * @param storeId 店铺id
     * @param deleteFlag 删除状态
     * @return 店铺运费模板列表
     */
    @MasterRouteOnly
    public List<FreightTemplateStore> findByStoreIdAndDeleteFlag(Long storeId, DeleteFlag deleteFlag){
        return freightTemplateStoreRepository.findByAll(storeId, deleteFlag);
    }

    /**
     * 查询店铺运费模板已选的区域
     *
     * @param storeId 店铺id
     * @param id 店铺运费模板id
     * @return 批量区域id数据
     */
    public List<Long> querySelectedArea(Long storeId, Long id) {
        List<FreightTemplateStore> freightTemplateStores = freightTemplateStoreRepository.findByAll(storeId, DeleteFlag.NO);
        return freightTemplateStores.stream().filter(info -> !Objects.equals(info.getFreightTempId(), id)).map(info -> {
            if (StringUtils.isBlank(info.getDestinationArea())) {
                return null;
            }
            return Arrays.asList(info.getDestinationArea().split(","));
        }).filter(Objects::nonNull).flatMap(Collection::stream).map(Long::valueOf).collect(Collectors.toList());
    }

    /**
     * 封装公共条件
     *
     * @param storeId 店铺id
     * @return 公共条件jpa类
     */
    public static Specification<FreightTemplateStore> getWhereCriteria(Long storeId) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cbuild.equal(root.get("delFlag"), DeleteFlag.NO));
            predicates.add(cbuild.equal(root.get("storeId"), storeId));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    /**
     * 验证区域id是否出现重复
     * @param areaList 区域id批量数据
     * @return true:有重复 false:没有重复
     */
    private boolean verifyAreaRepetition(List<Long> areaList) {
        Set<Long> set = new HashSet<>();
        areaList.forEach(id -> {
            set.add(id);
        });
        return areaList.size() != set.size();
    }
}
