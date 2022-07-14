package com.soybean.mall.order.gift.repository;

import com.soybean.mall.order.gift.model.OrderGiftRecordLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/13 10:19 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public interface PayOrderGiftRecordLogRepository extends JpaRepository<OrderGiftRecordLog, Integer>, JpaSpecificationExecutor<OrderGiftRecordLog> {
}
