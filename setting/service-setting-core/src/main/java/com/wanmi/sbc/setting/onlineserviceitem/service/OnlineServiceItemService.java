package com.wanmi.sbc.setting.onlineserviceitem.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.onlineserviceitem.repository.OnlineServiceItemRepository;
import com.wanmi.sbc.setting.onlineserviceitem.model.root.OnlineServiceItem;
import com.wanmi.sbc.setting.bean.vo.OnlineServiceItemVO;
import com.wanmi.sbc.common.util.KsBeanUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>onlineerviceItem业务逻辑</p>
 *
 * @author lq
 * @date 2019-11-05 16:10:54
 */
@Service("OnlineServiceItemService")
public class OnlineServiceItemService {
    @Autowired
    private OnlineServiceItemRepository onlineServiceItemRepository;

    /**
     * 新增onlineerviceItem
     *
     * @author lq
     */
    @Transactional
    public void save(List<OnlineServiceItem> entity) {
        onlineServiceItemRepository.saveAll(entity);
    }

    /**
     * 根据客服设置id删除客服列表onlineerviceItem
     *
     * @author lq
     */
    @Transactional
    public void deleteByOnlineServiceId(Integer id) {
        onlineServiceItemRepository.deleteByOnlineServiceId(id);
    }

    /**
     * 列表查询onlineerviceItem
     *
     * @author lq
     */
    public List<OnlineServiceItem> list(Integer onlineServiceId) {
        return onlineServiceItemRepository.findByOnlineServiceIdAndDelFlagOrderByCreateTimeDesc(
                onlineServiceId, DeleteFlag.NO);
    }

    /**
     * 查询客服的QQ号是否重复
     *
     * @param serverAccount
     * @return
     */
    public List<OnlineServiceItem> checkDuplicateAccount(List<String> serverAccount) {
        return onlineServiceItemRepository.checkDuplicateAccount(serverAccount);
    }

    /**
     * 将实体包装成VO
     *
     * @author lq
     */
    public OnlineServiceItemVO wrapperVo(OnlineServiceItem onlineServiceItem) {
        if (onlineServiceItem != null) {
            OnlineServiceItemVO onlineServiceItemVO = new OnlineServiceItemVO();
            KsBeanUtil.copyPropertiesThird(onlineServiceItem, onlineServiceItemVO);
            return onlineServiceItemVO;
        }
        return null;
    }

    /**
     * 转换对象
     *
     * @param onlineServerItemVoList
     * @return
     */
    public List<OnlineServiceItem> getOnlineServerItemList(List<OnlineServiceItemVO> onlineServerItemVoList) {
        List<OnlineServiceItem> onlineServerItemList = new ArrayList<>();
        onlineServerItemVoList.forEach(onlineServerItemVo -> {
            OnlineServiceItem onlineServerItem = new OnlineServiceItem();
            BeanUtils.copyProperties(onlineServerItemVo, onlineServerItem);
            onlineServerItem.setCreateTime(LocalDateTime.now());
            onlineServerItem.setDelFlag(DeleteFlag.NO);
            onlineServerItemList.add(onlineServerItem);
        });
        return onlineServerItemList;
    }
}
