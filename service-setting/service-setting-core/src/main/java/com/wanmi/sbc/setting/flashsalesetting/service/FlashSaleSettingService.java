package com.wanmi.sbc.setting.flashsalesetting.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.request.flashsalesetting.FlashSaleSettingQueryRequest;
import com.wanmi.sbc.setting.bean.vo.FlashSaleSettingVO;
import com.wanmi.sbc.setting.flashsalesetting.model.root.FlashSaleSetting;
import com.wanmi.sbc.setting.flashsalesetting.repository.FlashSaleSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>秒杀设置业务逻辑</p>
 *
 * @author yxz
 * @date 2019-06-11 13:48:53
 */
@Service("FlashSaleSettingService")
public class FlashSaleSettingService {

    @Autowired
    private FlashSaleSettingRepository flashSaleSettingRepository;

    /**
     * 修改秒杀设置
     *
     * @author yxz
     */
    @Transactional
    public void modifyList(List<FlashSaleSetting> flashSaleSettings) {
        flashSaleSettingRepository.saveAll(flashSaleSettings);
    }

    /**
     * 列表查询秒杀设置
     *
     * @author yxz
     */
    public List<FlashSaleSetting> list(FlashSaleSettingQueryRequest queryReq) {
        Sort sort = queryReq.getSort();
        if (Objects.nonNull(sort)) {
            return flashSaleSettingRepository.findAll(FlashSaleSettingWhereCriteriaBuilder.build(queryReq), sort);
        } else {
            return flashSaleSettingRepository.findAll(FlashSaleSettingWhereCriteriaBuilder.build(queryReq));
        }
    }

    /**
     * 将实体包装成VO
     *
     * @author yxz
     */
    public FlashSaleSettingVO wrapperVo(FlashSaleSetting flashSaleSetting) {
        if (flashSaleSetting != null) {
            FlashSaleSettingVO flashSaleSettingVO = new FlashSaleSettingVO();
            KsBeanUtil.copyPropertiesThird(flashSaleSetting, flashSaleSettingVO);
            return flashSaleSettingVO;
        }
        return null;
    }
}
