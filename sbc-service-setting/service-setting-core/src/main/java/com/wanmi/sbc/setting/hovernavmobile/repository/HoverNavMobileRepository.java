package com.wanmi.sbc.setting.hovernavmobile.repository;

import com.wanmi.sbc.setting.hovernavmobile.model.root.HoverNavMobile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>移动端悬浮导航栏DAO</p>
 * @author dyt
 * @date 2020-04-29 14:28:21
 */
@Repository
public interface HoverNavMobileRepository extends MongoRepository<HoverNavMobile, Long> {

}
