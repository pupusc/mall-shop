package com.wanmi.sbc.order.returnorder.repository;

import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 退货repository
 * Created by jinwei on 20/4/2017.
 */
@Repository
public interface ReturnOrderRepository extends MongoRepository<ReturnOrder, String> {

    /**
     * 根据订单号查询退单列表
     * @param tid
     * @return
     */
    List<ReturnOrder> findByTid(String tid);

//    /**
//     * 根据订单id查询
//     * @param id
//     * @return
//     */
//    ReturnOrder findById(String id);

    /**
     * 查询账期内的退单信息
     * @param storeId
     * @param startTime
     * @param endTime
     * @return
     */
    Page<ReturnOrder> findByCompany_StoreIdAndFinishTimeBetweenAndReturnFlowState(Long storeId, Date startTime, Date
            endTime, Pageable pageRequest, ReturnFlowState returnFlowState);


    Optional<ReturnOrder> findByCompany_StoreIdAndId(Long storeId, String tid);

    /**
     *
     * @param ptid
     * @return
     */
    List<ReturnOrder> findByPtid(String ptid);

}
