package com.wanmi.sbc.setting.systemconfig.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.systemconfig.repository.SystemConfigRepository;
import com.wanmi.sbc.setting.systemconfig.model.root.SystemConfig;
import com.wanmi.sbc.setting.api.request.systemconfig.SystemConfigQueryRequest;
import com.wanmi.sbc.setting.bean.vo.SystemConfigVO;
import com.wanmi.sbc.common.util.KsBeanUtil;

import java.util.List;
import java.util.Objects;

/**
 * <p>系统配置表业务逻辑</p>
 *
 * @author yang
 * @date 2019-11-05 18:33:04
 */
@Service("SystemConfigService")
public class SystemConfigService {
    @Autowired
    private SystemConfigRepository systemConfigRepository;

    /**
     * 新增系统配置表
     *
     * @author yang
     */
    @Transactional
    public SystemConfig add(SystemConfig entity) {
        systemConfigRepository.save(entity);
        return entity;
    }

    /**
     * 修改系统配置表
     *
     * @author yang
     */
    @Transactional
    public SystemConfig modify(SystemConfig entity) {
        systemConfigRepository.save(entity);
        return entity;
    }

    /**
     * 单个删除系统配置表
     *
     * @author yang
     */
    @Transactional
    public void deleteById(Long id) {
        systemConfigRepository.deleteById(id);
    }

    /**
     * 批量删除系统配置表
     *
     * @author yang
     */
    @Transactional
    public void deleteByIdList(List<Long> ids) {
        systemConfigRepository.deleteByIdList(ids);
    }

    /**
     * 单个查询系统配置表
     *
     * @author yang
     */
    public SystemConfig getById(Long id) {
        return systemConfigRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询系统配置表
     *
     * @author yang
     */
    public Page<SystemConfig> page(SystemConfigQueryRequest queryReq) {
        return systemConfigRepository.findAll(
                SystemConfigWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询系统配置表
     *
     * @author yang
     */
    public List<SystemConfig> list(SystemConfigQueryRequest queryReq) {
        return systemConfigRepository.findAll(
                SystemConfigWhereCriteriaBuilder.build(queryReq),
                queryReq.getSort());
    }

    /**
     * 查询可用的云配置
     * @return
     */
    public SystemConfig getAvailableYun() {
        SystemConfigQueryRequest queryRequest = SystemConfigQueryRequest.builder()
                .configKey(ConfigKey.RESOURCESERVER.toString())
                .status(EnableStatus.ENABLE.toValue())
                .delFlag(DeleteFlag.NO)
                .build();
        List<SystemConfig> systemConfigList = list(queryRequest);
        if (Objects.nonNull(systemConfigList) && systemConfigList.size() > 0) {
            return systemConfigList.get(0);
        } else {
            throw new SbcRuntimeException("K-061001");
        }
    }

    /**
     * 将实体包装成VO
     *
     * @author yang
     */
    public SystemConfigVO wrapperVo(SystemConfig systemConfig) {
        if (systemConfig != null) {
            SystemConfigVO systemConfigVO = new SystemConfigVO();
            KsBeanUtil.copyPropertiesThird(systemConfig, systemConfigVO);
            return systemConfigVO;
        }
        return null;
    }
}
