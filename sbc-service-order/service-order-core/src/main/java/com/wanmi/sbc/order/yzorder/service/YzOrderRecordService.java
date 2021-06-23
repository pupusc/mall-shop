package com.wanmi.sbc.order.yzorder.service;

import com.wanmi.sbc.order.api.request.yzorder.YzOrderCustomerQueryRequest;
import com.wanmi.sbc.order.api.request.yzorder.YzOrderRecordQueryRequest;
import com.wanmi.sbc.order.yzorder.model.root.YzOrderRecord;
import com.wanmi.sbc.order.yzorder.repository.YzOrderRecordRepository;
import com.wanmi.sbc.order.yzsalesmancustomer.model.root.YzOrderCustomer;
import com.wanmi.sbc.order.yzsalesmancustomer.service.YzOrderCustomerWhereCriteraBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YzOrderRecordService {

    @Autowired
    private YzOrderRecordRepository yzOrderRecordRepository;

    @Transactional
    public void addBatch(List<YzOrderRecord> list) {
        yzOrderRecordRepository.saveAll(list);
    }

    public List<String> getTids(PageRequest pageRequest){
        List<String> tids = yzOrderRecordRepository.findTidBy(pageRequest);
        return tids.stream().map(tid -> tid.toLowerCase()).collect(Collectors.toList());
    }

    /**
     * 分页查询有赞销售员客户关系
     *
     * @author he
     */
    public Page<YzOrderRecord> page(YzOrderRecordQueryRequest queryReq) {
        return yzOrderRecordRepository.findAll(YzOrderRecordWhereCriteraBuilder.build(queryReq),
                queryReq.getPageRequest());

    }

    @Transactional
    public void updateFlag(Boolean flag, List<String> tids){
        int i = yzOrderRecordRepository.updateFlag(flag, tids);
        log.info("---更新记录:{}---", i);
    }


}
