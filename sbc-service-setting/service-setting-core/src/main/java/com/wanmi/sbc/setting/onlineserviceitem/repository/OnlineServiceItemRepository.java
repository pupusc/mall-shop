package com.wanmi.sbc.setting.onlineserviceitem.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.onlineserviceitem.model.root.OnlineServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>onlineerviceItemDAO</p>
 * @author lq
 * @date 2019-11-05 16:10:54
 */
@Repository
public interface OnlineServiceItemRepository extends JpaRepository<OnlineServiceItem, Integer>,
        JpaSpecificationExecutor<OnlineServiceItem> {

    /**
     * 通过店铺id appKey 查询在线客服座席
     * @param onlineServiceId
     * @param deleteFlag
     * @return findByAgeOrderByLastnameDesc
     */
    List<OnlineServiceItem> findByOnlineServiceIdAndDelFlagOrderByCreateTimeDesc(Integer onlineServiceId, DeleteFlag deleteFlag);

    /**
     * 批量删除在线客服下面的座席
     * @param onlineServiceId
     */
    void deleteByOnlineServiceId(Integer onlineServiceId);

    /**
     * 查询QQ号是否重复
     * @param serverAccount
     * @return
     */
    @Query("from OnlineServiceItem l where l.delFlag = 0 and l.customerServiceAccount in ?1 ")
    List<OnlineServiceItem> checkDuplicateAccount(List<String> serverAccount);
}
