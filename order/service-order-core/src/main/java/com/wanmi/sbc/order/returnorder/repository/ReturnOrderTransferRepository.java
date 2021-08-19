package com.wanmi.sbc.order.returnorder.repository;

import com.wanmi.sbc.order.returnorder.model.root.ReturnOrderTransfer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 退货repository
 * Created by jinwei on 20/4/2017.
 */
public interface ReturnOrderTransferRepository extends MongoRepository<ReturnOrderTransfer, String> {

    /**
     * 根据用户id查询
     *
     * @param cid
     * @return
     */
    ReturnOrderTransfer findReturnOrderTransferByBuyerId(String cid);

    /**
     * 根据用户id删除
     *
     * @param cid
     */
    void deleteReturnOrderTransferByBuyerId(String cid);

    /**
     * 根据用订单id查找
     *
     * @param tid
     */
    ReturnOrderTransfer findReturnOrderTransferByTid(String tid);

    /**
     * 根据用订单id删除
     *
     * @param tid
     */
    void deleteReturnOrderTransferByTid(String tid);




}
