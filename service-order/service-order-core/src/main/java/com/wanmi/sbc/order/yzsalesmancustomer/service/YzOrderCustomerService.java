package com.wanmi.sbc.order.yzsalesmancustomer.service;

import com.wanmi.sbc.order.api.request.yzorder.YzOrderCustomerQueryRequest;
import com.wanmi.sbc.order.yzsalesmancustomer.model.root.YzOrderCustomer;
import com.wanmi.sbc.order.yzsalesmancustomer.repository.YzOrderCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class YzOrderCustomerService {

    @Autowired
    private YzOrderCustomerRepository yzOrderCustomerRepository;

    /**
     * 查看订单会员对应关系缺失数据
     * @author he
     */
    public List<YzOrderCustomer> list(){
        return yzOrderCustomerRepository.findAll();
    }

    /**
     * 分页查询有赞销售员客户关系
     *
     * @author he
     */
    public Page<YzOrderCustomer> page(YzOrderCustomerQueryRequest queryReq) {
        return yzOrderCustomerRepository.findAll(YzOrderCustomerWhereCriteraBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 更新补偿状态
     * @param yzOrderCustomer
     * @return
     */
    @Transactional
    public YzOrderCustomer update(YzOrderCustomer yzOrderCustomer){
        yzOrderCustomerRepository.save(yzOrderCustomer);
        return yzOrderCustomer;
    }

    /**
     * 新增
     * @param yzOrderCustomer
     * @return
     */
    @Transactional
    public void addAll(List<YzOrderCustomer> yzOrderCustomer){
        yzOrderCustomerRepository.saveAll(yzOrderCustomer);
    }
}
