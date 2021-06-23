package com.wanmi.sbc.order.appointmentrecord.service;

import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordQueryRequest;
import com.wanmi.sbc.order.appointmentrecord.model.root.AppointmentRecord;
import com.wanmi.sbc.order.appointmentrecord.repository.AppointmentRecordRepository;
import com.wanmi.sbc.order.appointmentrecord.request.AppointmentQueryRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>预约抢购业务逻辑</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@Service("AppointmentRecordService")
public class AppointmentRecordService {

    @Autowired
    private AppointmentRecordRepository appointmentRecordRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param appointmentRecord
     */

    public void add(AppointmentRecord appointmentRecord) {
        appointmentRecord.setCreateTime(LocalDateTime.now());
        appointmentRecordRepository.save(appointmentRecord);
    }


    public AppointmentRecord getAppointmentInfo(AppointmentRecordQueryRequest request) {

        return appointmentRecordRepository.findByAppointmentSaleIdAndBuyerIdAndGoodsInfoId(request.getAppointmentSaleId(), request.getBuyerId(), request.getGoodsInfoId());
    }

    public Page<AppointmentRecord> page(Criteria criteria, AppointmentQueryRequest request) {
        long totalSize = this.countNum(criteria, request);
        if (totalSize < 1) {
            return new PageImpl<>(new ArrayList<>(), request.getPageRequest(), totalSize);
        }
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(criteria);
        return new PageImpl<>(mongoTemplate.find(query.with(request.getPageRequest()), AppointmentRecord.class), request
                .getPageable(), totalSize);
    }


    /**
     * 统计数量
     *
     * @param whereCriteria
     * @param request
     * @return
     */
    public long countNum(Criteria whereCriteria, AppointmentQueryRequest request) {
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        long totalSize = mongoTemplate.count(query, AppointmentRecord.class);
        return totalSize;
    }


    public List<AppointmentRecord> listSubscribeNotStartActivityCriteria(Criteria criteria) {
        Query query = new Query(criteria);
        return mongoTemplate.find(query, AppointmentRecord.class);
    }
}

