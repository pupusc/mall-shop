package com.wanmi.sbc.order.distribution.repository;

import com.wanmi.sbc.order.distribution.model.root.ConsumeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 消费记录Repository
 * @Autho qiaokang
 * @Date：2019-03-05 16:39:04
 */
@Repository
public interface ConsumeRecordRepository extends JpaRepository<ConsumeRecord, String>,
        JpaSpecificationExecutor<ConsumeRecord> {

    /**
     * 更新消费记录
     *
     * @param consumeRecord
     * @return
     */
    @Modifying
    @Query(value = "update consume_record r set r.valid_consume_sum = :#{#consumeRecord.validConsumeSum}, r.flow_state = :#{#consumeRecord.flowState} " +
            "where r.order_id = :#{#consumeRecord.orderId}", nativeQuery = true)
    int modifyConsumeRecord(@Param("consumeRecord") ConsumeRecord consumeRecord);

    /**
     * 统计累计的有效消费金额，订单数，
     *
     * @return
     */
    @Query(value = "SELECT r.customer_id ,COUNT(*) ,SUM(r.valid_consume_sum) ,r.customer_name  " +
            "FROM consume_record r WHERE r.customer_id IN(:ids) and r.flow_state !='VOID' GROUP BY r.customer_id", nativeQuery = true)
    List<Object> countValidConsume(@Param("ids") List<String> ids);

    /**
     * 统计累计的消费金额，订单数，
     *
     * @return
     */
    @Query(value = "SELECT r.customer_id ,COUNT(*) ,SUM(r.consume_sum)  ,r.customer_name  " +
            "FROM consume_record r WHERE r.customer_id IN(:ids) GROUP BY r.customer_id", nativeQuery = true)
    List<Object> countConsume(@Param("ids") List<String> ids);

    @Query(value = "select  customer_id " +
            ",IF(find_in_set('0',GROUP_CONCAT(is_yz)) =0 , 0 , COUNT( is_yz = 0 or NULL)" +
            ") , consume_sum,customer_name,head_img,order_create_time" +
            " from (" +
            "select `r`.`customer_id` AS `customer_id`,count(0) AS `COUNT( * )`,sum(`r`.`consume_sum`) AS `consume_sum`," +
            "`r`.`customer_name` AS `customer_name`,`r`.`head_img` AS `head_img`," +
            "`r`.`order_create_time` AS `order_create_time`,`r`.`is_yz` AS `is_yz` " +
            "from `consume_record` `r` where " +
            "((`r`.`distribution_customer_id` =?1) and (`r`.`distribution_customer_id` <> `r`.`customer_id`)) " +
            "group by `r`.`customer_id`,`r`.`is_yz` order by `r`.`order_create_time` desc) t  GROUP BY customer_id limit ?2,?3", nativeQuery = true)
    List<Object> pageByCustomerId(String customerId,int startNum,int endNum);

    @Query(value = "SELECT COUNT(DISTINCT r.customer_id) FROM consume_record r WHERE r.distribution_customer_id=?1 " +
            "and r.distribution_customer_id != r.customer_id", nativeQuery = true)
    Integer pageByDistributionCustomerIdCountNum(String customerId);

}
