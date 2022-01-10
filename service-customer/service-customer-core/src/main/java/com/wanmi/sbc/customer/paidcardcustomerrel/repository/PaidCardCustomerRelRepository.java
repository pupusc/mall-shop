package com.wanmi.sbc.customer.paidcardcustomerrel.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelQueryRequest;
import com.wanmi.sbc.customer.paidcardcustomerrel.model.root.PaidCardCustomerRel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>付费会员DAO</p>
 * @author xuhai
 * @date 2021-01-29 14:03:59
 */
@Repository
public interface PaidCardCustomerRelRepository extends JpaRepository<PaidCardCustomerRel, String>,
        JpaSpecificationExecutor<PaidCardCustomerRel> {

    /**
     * 单个删除付费会员
     * @author xuhai
     */
    @Modifying
    @Query("update PaidCardCustomerRel set delFlag = 1 where id = ?1")
    void deleteById(String id);

    /**
     * 批量删除付费会员
     * @author xuhai
     */
    @Modifying
    @Query("update PaidCardCustomerRel set delFlag = 1 where id in ?1")
    int deleteByIdList(List<String> idList);

    /**
     * 查询付费会员卡分组统计信息
     * @param cardIdList
     * @return
     */
//    @Query("select count(rel.paidCardId) as customerNum ,rel.paidCardId from PaidCardCustomerRel rel where rel.paidCardId in ?1 and rel.delFlag = ?2 group by rel.paidCardId")
    @Query(value = "SELECT\n" +
            "\tcount( customer_id ) AS customerNum,\n" +
            "\tpaid_card_id paidCardId \n" +
            "FROM\n" +
            "\t(\n" +
            "\tSELECT DISTINCT\n" +
            "\t\tcustomer_id,\n" +
            "\t\tpaid_card_id \n" +
            "\tFROM\n" +
            "\t\tpaid_card_customer_rel\n" +
            "\tWHERE\n" +
            "\t\tpaid_card_id IN ?1  \n" +
            "\t\tAND del_flag = 0 \n" +
            "\t) a \n" +
            "GROUP BY\n" +
            "\tpaid_card_id",nativeQuery = true)
    List<Object> findByPaidCardGroup(List<String> cardIdList, DeleteFlag deleteFlag);

    @Query("update PaidCardCustomerRel set sendMsgFlag=true where id in ?1")
    @Modifying
    void changeSendMsgFlag(List<String> relIdList);

    @Query("from PaidCardCustomerRel where delFlag = 0 and paidCardId = ?1 and  endTime > ?2 and customerId = ?3 ")
    PaidCardCustomerRel findInstanceByPaidCard(String paidCardId, LocalDateTime now,String customerId);

    @Query("select distinct customerId from PaidCardCustomerRel where delFlag = 0 and endTime > now()")
    List<String> findCustomerIdByPageable(Pageable pageable);
    @Query("update PaidCardCustomerRel set sendExpireMsgFlag = true where id in ?1")
    @Modifying
    void changeExpireSendMsgFlag(List<String> relIdList);

    @Modifying
    @Query("DELETE FROM PaidCardCustomerRel  where paidSource = ?1 and customerId = ?2")
    void deleteByCustomerId(Integer paidSource,String customerId);

    /**
     * 根据最大id获取对应的优惠券
     * @param paidCardIdList
     * @param tmpId
     * @return
     */
    @Query(value = "select * from paid_card_customer_rel where del_flag = 0 and paid_card_id in ?1 and ?2 > begin_time  and end_time > ?2 and tmp_id > ?3 limit ?4", nativeQuery = true)
    List<PaidCardCustomerRel> pageByMaxAutoId(List<String> paidCardIdList, LocalDateTime currentTime, Integer tmpId, Integer pageSize);
}
