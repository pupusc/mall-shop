package com.wanmi.sbc.customer.customersignrecord.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.customersignrecord.CustomerSignRecordQueryRequest;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailAddRequest;
import com.wanmi.sbc.customer.bean.enums.GrowthValueServiceType;
import com.wanmi.sbc.customer.bean.enums.OperateType;
import com.wanmi.sbc.customer.bean.enums.PointsServiceType;
import com.wanmi.sbc.customer.bean.vo.CustomerSignRecordVO;
import com.wanmi.sbc.customer.customersignrecord.model.root.CustomerSignRecord;
import com.wanmi.sbc.customer.customersignrecord.repository.CustomerSignRecordRepository;
import com.wanmi.sbc.customer.growthvalue.service.GrowthValueIncreaseService;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.points.service.CustomerPointsDetailService;
import com.wanmi.sbc.customer.repository.CustomerRepository;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>用户签到记录业务逻辑</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@Service("CustomerSignRecordService")
public class CustomerSignRecordService {
	@Autowired
	private CustomerSignRecordRepository customerSignRecordRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private GrowthValueIncreaseService growthValueIncreaseService;

	@Autowired
	private CustomerPointsDetailService customerPointsDetailService;

	/** 
	 * 新增用户签到记录
	 * @author wangtao
	 */
	@Transactional
	public CustomerSignRecord add(CustomerSignRecord entity) {
		//查询昨天是否签到
		Customer customer = customerRepository.findById(entity.getCustomerId()).get();
		Integer signContinuousDays = customer.getSignContinuousDays() == null ? 0 : customer.getSignContinuousDays();
		CustomerSignRecord yesterdayRecord = this.getRecordByDays(entity.getCustomerId(), 1L);
		if(Objects.isNull(yesterdayRecord)) {
			signContinuousDays = 1;
		} else {
			signContinuousDays += 1;
		}
		customer.setSignContinuousDays(signContinuousDays);
		customerRepository.save(customer);
		customerSignRecordRepository.save(entity);
		//增加成长值和积分
		growthValueIncreaseService.increaseGrowthValue(CustomerGrowthValueAddRequest.builder()
				.customerId(entity.getCustomerId())
				.type(OperateType.GROWTH)
				.serviceType(GrowthValueServiceType.SIGNIN)
				.build());
		customerPointsDetailService.increasePoints(CustomerPointsDetailAddRequest.builder()
						.customerId(entity.getCustomerId())
						.type(OperateType.GROWTH)
						.serviceType(PointsServiceType.SIGNIN)
						.build(), ConfigType.POINTS_BASIC_RULE_SIGN_IN);
		return entity;
	}
	
	/** 
	 * 修改用户签到记录
	 * @author wangtao
	 */
	@Transactional
	public CustomerSignRecord modify(CustomerSignRecord entity) {
		customerSignRecordRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除用户签到记录
	 * @author wangtao
	 */
	@Transactional
	public void deleteById(String id) {
		customerSignRecordRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除用户签到记录
	 * @author wangtao
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		customerSignRecordRepository.deleteByIdList(ids);
	}
	
	/** 
	 * 单个查询用户签到记录
	 * @author wangtao
	 */
	public CustomerSignRecord getById(String id){
		return customerSignRecordRepository.findById(id).get();
	}
	
	/** 
	 * 分页查询用户签到记录
	 * @author wangtao
	 */
	public Page<CustomerSignRecord> page(CustomerSignRecordQueryRequest queryReq){
		return customerSignRecordRepository.findAll(
				CustomerSignRecordWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询用户签到记录
	 * @author wangtao
	 */
	public List<CustomerSignRecord> list(CustomerSignRecordQueryRequest queryReq){
		return customerSignRecordRepository.findAll(
				CustomerSignRecordWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 列表查询用户当月签到记录
	 * @author wangtao
	 */
	public List<CustomerSignRecord> listByMonth(CustomerSignRecordQueryRequest queryReq){
		return customerSignRecordRepository.listByMonth(queryReq.getCustomerId());
	}

	/**
	 * 查询当前昨天是否有签到记录
	 * @return
	 */
	public CustomerSignRecord getRecordByDays(String customerId, Long days) {
		return customerSignRecordRepository.getRecordByDays(customerId,days);
	}

	/**
	 * 将实体包装成VO
	 * @author wangtao
	 */
	public CustomerSignRecordVO wrapperVo(CustomerSignRecord customerSignRecord) {
		if (customerSignRecord != null){
			CustomerSignRecordVO customerSignRecordVO=new CustomerSignRecordVO();
			KsBeanUtil.copyPropertiesThird(customerSignRecord,customerSignRecordVO);
			return customerSignRecordVO;
		}
		return null;
	}
}
