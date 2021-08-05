package com.wanmi.sbc.crm.customerplan.service;

import io.seata.spring.annotation.GlobalTransactional;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.request.customerplan.*;
import com.wanmi.sbc.crm.bean.enums.TriggerCondition;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanVO;
import com.wanmi.sbc.crm.customerplan.mapper.CustomerPlanMapper;
import com.wanmi.sbc.crm.customerplan.model.CustomerPlan;
import com.wanmi.sbc.crm.customerplanapppush.model.root.CustomerPlanAppPushRel;
import com.wanmi.sbc.crm.customerplanapppush.service.CustomerPlanAppPushRelService;
import com.wanmi.sbc.crm.customerplancoupon.model.root.CustomerPlanCouponRel;
import com.wanmi.sbc.crm.customerplancoupon.service.CustomerPlanCouponRelService;
import com.wanmi.sbc.crm.customerplansms.model.root.CustomerPlanSmsRel;
import com.wanmi.sbc.crm.customerplansms.service.CustomerPlanSmsRelService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> 人群运营计划业务逻辑</p>
 * @author dyt
 * @date 2020-01-07 17:07:02
 */
@Service("CustomerPlanService")
public class CustomerPlanService {

	@Autowired
	private CustomerPlanMapper customerPlanMapper;

	@Autowired
	private CustomerPlanCouponRelService customerPlanCouponRelService;

	@Autowired
	private CustomerPlanSmsRelService customerPlanSmsRelService;

	@Autowired
	private CustomerPlanAppPushRelService customerPlanAppPushRelService;

	
	/** 
	 * 新增 人群运营计划
	 * @author dyt
	 */
    @GlobalTransactional
	@Transactional
	public void add(CustomerPlanAddRequest customerPlanAddRequest) {
        CustomerPlan customerPlan = KsBeanUtil.convert(customerPlanAddRequest, CustomerPlan.class);
        customerPlan.setTriggerCondition(this.caseTrigger2Str(customerPlanAddRequest.getTriggerConditions()));
        this.putBaseInfo(customerPlan);
        customerPlan.setDelFlag(DeleteFlag.NO);
        customerPlan.setCreateTime(new Date());
        customerPlan.setPauseFlag(Boolean.FALSE);
        customerPlan.setGiftPackageCount(0);
        customerPlanMapper.insert(customerPlan);
        //优惠券
        if (customerPlanAddRequest.getCouponFlag()) {
            customerPlanCouponRelService.add(
                    customerPlanAddRequest.getPlanCouponList().stream().map(c -> {
                        CustomerPlanCouponRel rel = KsBeanUtil.convert(c, CustomerPlanCouponRel.class);
                        rel.setPlanId(customerPlan.getId());
                        return rel;
                    }).collect(Collectors.toList()));

        }
        //短信
        if(customerPlanAddRequest.getSmsFlag()){
            CustomerPlanSmsRel smsRel = KsBeanUtil.convert(customerPlanAddRequest.getPlanSms(), CustomerPlanSmsRel.class);
            smsRel.setPlanId(customerPlan.getId());
            customerPlanSmsRelService.add(smsRel);
        }
        //app通知
        if(customerPlanAddRequest.getAppPushFlag()){
            CustomerPlanAppPushRel pushRel = KsBeanUtil.convert(customerPlanAddRequest.getPlanAppPush(), CustomerPlanAppPushRel.class);
            pushRel.setPlanId(customerPlan.getId());
            customerPlanAppPushRelService.add(pushRel);
        }
    }

	/**
	 * 修改 人群运营计划
	 * @author dyt
	 */
    @GlobalTransactional
	@Transactional
	public void modify(CustomerPlanModifyRequest customerPlanModifyRequest) {
        CustomerPlan customerPlan = this.getById(customerPlanModifyRequest.getId());
        KsBeanUtil.copyPropertiesThird(customerPlanModifyRequest, customerPlan);
        customerPlan.setStartDate(chgLocalDateToDate(customerPlanModifyRequest.getStartDate()));
        customerPlan.setEndDate(chgLocalDateToDate(customerPlanModifyRequest.getEndDate()));
        customerPlan.setTriggerCondition(this.caseTrigger2Str(customerPlanModifyRequest.getTriggerConditions()));
        this.putBaseInfo(customerPlan);
        customerPlan.setUpdateTime(new Date());
        customerPlan.setGiftPackageCount(0);
		customerPlanMapper.updateByPrimaryKey(customerPlan);
        //优惠券
        if (customerPlanModifyRequest.getCouponFlag()) {
            customerPlanCouponRelService.modify(
                    customerPlanModifyRequest.getPlanCouponList().stream().map(c -> {
                        CustomerPlanCouponRel rel = KsBeanUtil.convert(c, CustomerPlanCouponRel.class);
                        rel.setPlanId(customerPlan.getId());
                        return rel;
                    }).collect(Collectors.toList()));
        }else{
            customerPlanCouponRelService.deleteByPlanId(customerPlan.getId());
        }
        //短信
        if(customerPlanModifyRequest.getSmsFlag()){
            CustomerPlanSmsRel smsRel = KsBeanUtil.convert(customerPlanModifyRequest.getPlanSms(), CustomerPlanSmsRel.class);
            smsRel.setPlanId(customerPlan.getId());
            customerPlanSmsRelService.modify(smsRel);
        }else {
            customerPlanSmsRelService.deleteByPlanId(customerPlan.getId());
        }
        //app通知
        if(customerPlanModifyRequest.getAppPushFlag()){
            CustomerPlanAppPushRel pushRel = KsBeanUtil.convert(customerPlanModifyRequest.getPlanAppPush(), CustomerPlanAppPushRel.class);
            pushRel.setPlanId(customerPlan.getId());
            customerPlanAppPushRelService.modify(pushRel);
        }else {
            customerPlanAppPushRelService.deleteByPlanId(customerPlan.getId());
        }
	}

    @GlobalTransactional
    @Transactional
	public void modifyPauseFlag(CustomerPlanModifyPauseFlagRequest request){
        CustomerPlan plan = new CustomerPlan();
        plan.setId(request.getId());
        plan.setPauseFlag(request.getPauseFlag());
        plan.setUpdateTime(new Date());
        plan.setUpdatePerson(request.getUpdatePerson());
        customerPlanMapper.updateByPrimaryKeySelective(plan);
    }

	/**
	 * 单个删除 人群运营计划
	 * @author dyt
	 */
    @GlobalTransactional
	@Transactional
	public void deleteById(Long id) {
        CustomerPlan plan = new CustomerPlan();
        plan.setDelFlag(DeleteFlag.YES);
        plan.setId(id);
		customerPlanMapper.updateByPrimaryKeySelective(plan);
	}
	
	/**
	 * 单个查询 人群运营计划
	 * @author dyt
	 */
	public CustomerPlan getById(Long id){
		return customerPlanMapper.selectByPrimaryKey(id);
	}
	
	/** 
	 * 分页查询 人群运营计划
	 * @author dyt
	 */
	public Page<CustomerPlan> page(CustomerPlanPageRequest queryReq){
        PageHelper.startPage(queryReq.getPageNum()+1,queryReq.getPageSize(),false);
        List<CustomerPlan> customerPlanList = customerPlanMapper.selectByPage(queryReq);
        PageInfo<CustomerPlan> pageInfo = PageInfo.of(customerPlanList);
        long pageTotal = customerPlanMapper.countByPageTotal(queryReq);
		return new PageImpl<>(customerPlanList,queryReq.getPageable(),pageTotal);
	}

    /**
     * 分页查询 人群运营计划
     * @author dyt
     */
    public PageInfo<CustomerPlan> taskPageInfo(CustomerPlanPageRequest queryReq){
        PageHelper.startPage(queryReq.getPageNum()+1,queryReq.getPageSize());
        List<CustomerPlan> customerPlanList = customerPlanMapper.selectTaskByPage(queryReq);
        PageInfo<CustomerPlan> pageInfo = new PageInfo<>(customerPlanList);
        return pageInfo;
    }
	/**
	 * @Author lvzhenwei
	 * @Description 分页查询数据总条数
	 * @Date 11:19 2020/1/9
	 * @Param [queryReq]
	 * @return long
	 **/
	public long countByPageTotal(CustomerPlanPageRequest queryReq){
	    return customerPlanMapper.countByPageTotal(queryReq);
    }


    /**
     * 分页查询 人群运营计划
     * @author dyt
     */
    public List<CustomerPlan> list(CustomerPlanListRequest queryReq){
        return customerPlanMapper.selectByCondition(queryReq);
    }

	/**
	 * 将实体包装成VO
	 * @author dyt
	 */
	public CustomerPlanVO wrapperVo(CustomerPlan customerPlan) {
		if (customerPlan != null){
            CustomerPlanVO customerPlanVO = KsBeanUtil.convert(customerPlan,CustomerPlanVO.class);
            customerPlanVO.setTriggerConditions(caseStr2Trigger(customerPlan.getTriggerCondition()));
			return customerPlanVO;
		}
		return null;
	}


	private String caseTrigger2Str(List<TriggerCondition> triggerConditions){
        if(CollectionUtils.isNotEmpty(triggerConditions)) {
            return triggerConditions.stream().map(c -> String.valueOf(c.toValue())).collect(Collectors.joining(","));
        }
        return null;
    }

    private List<TriggerCondition> caseStr2Trigger(String triggerCondition) {
        if (StringUtils.isNotEmpty(triggerCondition)) {
            return Arrays.stream(triggerCondition.split(","))
                    .map(c -> TriggerCondition.chgValue(NumberUtils.toInt(c))).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private void putBaseInfo(CustomerPlan customerPlan){
        //无触发
        if (!customerPlan.getTriggerFlag()) {
            customerPlan.setTriggerCondition(null);
            customerPlan.setCustomerLimitFlag(false);
        }

        if(!customerPlan.getCustomerLimitFlag()){
            customerPlan.setCustomerLimit(null);
        }

        if(!customerPlan.getPointFlag()){
            customerPlan.setPoints(null);
        }
    }

    private Date chgLocalDateToDate(LocalDate date) {
        return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
