package com.wanmi.sbc.setting.systemprivacypolicy.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.request.systemprivacypolicy.SystemPrivacyPolicyQueryRequest;
import com.wanmi.sbc.setting.bean.vo.SystemPrivacyPolicyVO;
import com.wanmi.sbc.setting.systemprivacypolicy.model.root.SystemPrivacyPolicy;
import com.wanmi.sbc.setting.systemprivacypolicy.repository.SystemPrivacyPolicyRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>隐私政策业务逻辑</p>
 *
 * @author yangzhen
 * @date 2020-09-23 14:52:35
 */
@Service("SystemPrivacyPolicyService")
public class SystemPrivacyPolicyService {
    @Autowired
    private SystemPrivacyPolicyRepository systemPrivacyPolicyRepository;


    /**
     * 查询隐私政策设置
     *
     * @return
     */
    public SystemPrivacyPolicy querySystemPrivacyPolicy() {
        List<SystemPrivacyPolicy> configs = systemPrivacyPolicyRepository.findByDelFlag(DeleteFlag.NO);
        SystemPrivacyPolicy systemPrivacyPolicy = new SystemPrivacyPolicy();
        if (CollectionUtils.isNotEmpty(configs)) {
            systemPrivacyPolicy = configs.get(0);
        }
        return systemPrivacyPolicy;
    }


    /**
     * 新增隐私政策
     *
     * @author yangzhen
     */
    @Transactional
    public SystemPrivacyPolicy add(SystemPrivacyPolicy entity) {
        systemPrivacyPolicyRepository.save(entity);
        return entity;
    }

    /**
     * 修改隐私政策
     *
     * @author yangzhen
     */
    @Transactional
    public SystemPrivacyPolicy modify(SystemPrivacyPolicy entity) {
        systemPrivacyPolicyRepository.save(entity);
        return entity;
    }

    /**
     * 单个删除隐私政策
     *
     * @author yangzhen
     */
    @Transactional
    public void deleteById(String id) {
        systemPrivacyPolicyRepository.deleteById(id);
    }

    /**
     * 批量删除隐私政策
     *
     * @author yangzhen
     */
    @Transactional
    public void deleteByIdList(List<String> ids) {
        systemPrivacyPolicyRepository.deleteByIdList(ids);
    }

    /**
     * 单个查询隐私政策
     *
     * @author yangzhen
     */
    public SystemPrivacyPolicy getById(String id) {
        return systemPrivacyPolicyRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询隐私政策
     *
     * @author yangzhen
     */
    public Page<SystemPrivacyPolicy> page(SystemPrivacyPolicyQueryRequest queryReq) {
        return systemPrivacyPolicyRepository.findAll(
                SystemPrivacyPolicyWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询隐私政策
     *
     * @author yangzhen
     */
    public List<SystemPrivacyPolicy> list(SystemPrivacyPolicyQueryRequest queryReq) {
        return systemPrivacyPolicyRepository.findAll(
                SystemPrivacyPolicyWhereCriteriaBuilder.build(queryReq),
                queryReq.getSort());
    }

    /**
     * 将实体包装成VO
     *
     * @author yangzhen
     */
    public SystemPrivacyPolicyVO wrapperVo(SystemPrivacyPolicy systemPrivacyPolicy) {
        if (systemPrivacyPolicy != null) {
            SystemPrivacyPolicyVO systemPrivacyPolicyVO = new SystemPrivacyPolicyVO();
            KsBeanUtil.copyPropertiesThird(systemPrivacyPolicy, systemPrivacyPolicyVO);
            return systemPrivacyPolicyVO;
        }
        return null;
    }
}
