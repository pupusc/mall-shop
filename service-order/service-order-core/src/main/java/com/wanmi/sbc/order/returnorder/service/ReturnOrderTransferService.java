package com.wanmi.sbc.order.returnorder.service;

import com.wanmi.sbc.order.returnorder.model.root.ReturnOrderTransfer;
import com.wanmi.sbc.order.returnorder.repository.ReturnOrderTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2019-03-28 17:32
 */
@Service
public class ReturnOrderTransferService {

    @Autowired
    private ReturnOrderTransferRepository returnOrderTransferRepository;

    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param returnOrderTransfer
     */
    public void addReturnOrderTransfer(ReturnOrderTransfer returnOrderTransfer) {
        returnOrderTransferRepository.save(returnOrderTransfer);
    }

    /**
     * 修改文档
     * 专门用于数据修改服务,不允许数据新增的时候调用
     *
     * @param returnOrderTransfer
     */
    public void updateReturnOrderTransfer(ReturnOrderTransfer returnOrderTransfer) {
        returnOrderTransferRepository.save(returnOrderTransfer);
    }

    /**
     * 删除文档
     *
     * @param id
     */
    public void deleteReturnOrderTransfer(String id) {
        returnOrderTransferRepository.deleteById(id);
    }


}
