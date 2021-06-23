package com.wanmi.sbc.vas.iepsetting.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.vas.redis.RedisService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.vas.iepsetting.repository.IepSettingRepository;
import com.wanmi.sbc.vas.iepsetting.model.root.IepSetting;
import com.wanmi.sbc.vas.api.request.iepsetting.IepSettingQueryRequest;
import com.wanmi.sbc.vas.bean.vo.IepSettingVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.enums.DeleteFlag;

import java.util.List;

/**
 * <p>企业购设置业务逻辑</p>
 *
 * @author 宋汉林
 * @date 2020-03-02 20:15:04
 */
@Service("IepSettingService")
public class IepSettingService {
    @Autowired
    private IepSettingRepository iepSettingRepository;

    @Autowired
    private RedisService redisService;

    /**
     * 新增企业购设置
     *
     * @author 宋汉林
     */
    @Transactional
    public IepSetting add(IepSetting entity) {
        iepSettingRepository.save(entity);
        return entity;
    }

    /**
     * 修改企业购设置
     *
     * @author 宋汉林
     */
    @Transactional
    public IepSetting modify(IepSetting entity) {
        IepSetting iepSetting = this.getOne(entity.getId());
        iepSetting.setUpdatePerson(entity.getUpdatePerson());
        iepSetting.setEnterpriseCustomerRegisterContent(entity.getEnterpriseCustomerRegisterContent());
        iepSetting.setEnterpriseGoodsAuditFlag(entity.getEnterpriseGoodsAuditFlag());
        iepSetting.setEnterpriseCustomerAuditFlag(entity.getEnterpriseCustomerAuditFlag());
        iepSetting.setEnterpriseCustomerName(entity.getEnterpriseCustomerName());
        iepSetting.setEnterprisePriceName(entity.getEnterprisePriceName());
        iepSetting.setEnterpriseCustomerLogo(entity.getEnterpriseCustomerLogo());
        iepSettingRepository.save(iepSetting);
        // 缓存到redis中
        redisService.setString(CacheKeyConstant.IEP_SETTING, JSONObject.toJSONString(iepSetting));
        return iepSetting;
    }

    /**
     * 单个删除企业购设置
     *
     * @author 宋汉林
     */
    @Transactional
    public void deleteById(IepSetting entity) {
        iepSettingRepository.save(entity);
    }

    /**
     * 批量删除企业购设置
     *
     * @author 宋汉林
     */
    @Transactional
    public void deleteByIdList(List<IepSetting> infos) {
        iepSettingRepository.saveAll(infos);
    }

    /**
     * 单个查询企业购设置
     *
     * @author 宋汉林
     */
    public IepSetting getOne(String id) {
        return iepSettingRepository.findByIdAndDelFlag(id, DeleteFlag.NO)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "企业购设置不存在"));
    }

    /**
     * 分页查询企业购设置
     *
     * @author 宋汉林
     */
    public Page<IepSetting> page(IepSettingQueryRequest queryReq) {
        return iepSettingRepository.findAll(
                IepSettingWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询企业购设置
     *
     * @author 宋汉林
     */
    public List<IepSetting> list(IepSettingQueryRequest queryReq) {
        return iepSettingRepository.findAll(IepSettingWhereCriteriaBuilder.build(queryReq));
    }

	/**
	 * 缓存企业购信息
	 */
	public IepSetting cacheIepSetting() {
        IepSetting iepSetting = this.findTopOne();
        redisService.setString(CacheKeyConstant.IEP_SETTING, JSONObject.toJSONString(iepSetting));
        return iepSetting;
    }

    /**
     * 查询第一个企业购设置信息
     */
    public IepSetting findTopOne() {
        return iepSettingRepository.findTop1ByDelFlag(DeleteFlag.NO)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "企业购设置不存在"));
    }

    /**
     * 将实体包装成VO
     *
     * @author 宋汉林
     */
    public IepSettingVO wrapperVo(IepSetting iepSetting) {
        if (iepSetting != null) {
            IepSettingVO iepSettingVO = KsBeanUtil.convert(iepSetting, IepSettingVO.class);
            return iepSettingVO;
        }
        return null;
    }
}

