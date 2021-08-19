package com.wanmi.sbc.crm.provider.impl.customerplan;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.crm.api.provider.customerplan.CustomerPlanQueryProvider;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanByActivityIdRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanByIdRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanListRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanPageRequest;
import com.wanmi.sbc.crm.api.response.customerplan.CustomerPlanByActivityIdResponse;
import com.wanmi.sbc.crm.api.response.customerplan.CustomerPlanByIdResponse;
import com.wanmi.sbc.crm.api.response.customerplan.CustomerPlanListResponse;
import com.wanmi.sbc.crm.api.response.customerplan.CustomerPlanPageResponse;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanAppPushVO;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanSmsVO;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanVO;
import com.wanmi.sbc.crm.customerplan.model.CustomerPlan;
import com.wanmi.sbc.crm.customerplan.service.CustomerPlanService;
import com.wanmi.sbc.crm.customerplanapppush.service.CustomerPlanAppPushRelService;
import com.wanmi.sbc.crm.customerplancoupon.model.root.CustomerPlanCouponRel;
import com.wanmi.sbc.crm.customerplancoupon.service.CustomerPlanCouponRelService;
import com.wanmi.sbc.crm.customerplansms.service.CustomerPlanSmsRelService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p> 人群运营计划查询服务接口实现</p>
 * @author dyt
 * @date 2020-01-07 17:07:02
 */
@RestController
@Validated
public class CustomerPlanQueryController implements CustomerPlanQueryProvider {
	@Autowired
	private CustomerPlanService customerPlanService;

	@Autowired
	private CustomerPlanCouponRelService customerPlanCouponRelService;

	@Autowired
	private CustomerPlanSmsRelService customerPlanSmsRelService;

	@Autowired
	private CustomerPlanAppPushRelService customerPlanAppPushRelService;

	@Override
	public BaseResponse<CustomerPlanPageResponse> page(@RequestBody @Valid CustomerPlanPageRequest customerPlanPageReq) {
		Page<CustomerPlan> customerPlanPage = customerPlanService.page(customerPlanPageReq);
		Page<CustomerPlanVO> newPage = customerPlanPage.map(entity -> customerPlanService.wrapperVo(entity));
		MicroServicePage<CustomerPlanVO> microPage = new MicroServicePage<>(newPage, customerPlanPageReq.getPageable());
		//为每个计划填充赠送优惠券id
		if(CollectionUtils.isNotEmpty(microPage.getContent())){
            List<CustomerPlanCouponRel> rels = customerPlanCouponRelService.listByPlanIds(
                    microPage.getContent().stream().filter(CustomerPlanVO::getCouponFlag).map(CustomerPlanVO::getId).collect(Collectors.toList()));
            Map<Long, List<String>> couponMap = rels.stream().collect(Collectors.groupingBy(CustomerPlanCouponRel::getPlanId, Collectors.mapping(CustomerPlanCouponRel::getCouponId, Collectors.toList())));
            microPage.getContent().forEach(customerPlanVO -> customerPlanVO.setCouponIds(couponMap.get(customerPlanVO.getId())));
        }
		CustomerPlanPageResponse finalRes = new CustomerPlanPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<CustomerPlanByIdResponse> getById(@RequestBody @Valid CustomerPlanByIdRequest customerPlanByIdRequest) {
		CustomerPlan customerPlan = customerPlanService.getById(customerPlanByIdRequest.getId());
        CustomerPlanVO customerPlanVO = customerPlanService.wrapperVo(customerPlan);
        List<CustomerPlanCouponRel> couponList = new ArrayList<>();
        CustomerPlanSmsVO smsVO = null;
        CustomerPlanAppPushVO pushVO = null;
        if(Objects.nonNull(customerPlanVO)){
            if(customerPlanVO.getCouponFlag()) {
                couponList = customerPlanCouponRelService.listByPlanIds(Collections.singletonList(customerPlanVO.getId()));
            }
            if(customerPlanVO.getSmsFlag()){
                smsVO = customerPlanSmsRelService.wrapperVo(customerPlanSmsRelService.findByPlanId(customerPlanVO.getId()));
            }
            if(customerPlanVO.getAppPushFlag()){
                pushVO = customerPlanAppPushRelService.wrapperVo(customerPlanAppPushRelService.findByPlanId(customerPlanVO.getId()));
            }
        }
		return BaseResponse.success(new CustomerPlanByIdResponse(
		        customerPlanVO,
                couponList.stream().map(customerPlanCouponRelService::wrapperVo).collect(Collectors.toList()),
                smsVO, pushVO));
	}

	public BaseResponse<CustomerPlanByActivityIdResponse> getByActivityId(@RequestBody @Valid
                                                                                  CustomerPlanByActivityIdRequest customerPlanByIdRequest){
        List<CustomerPlan>  planList = customerPlanService.list(
                CustomerPlanListRequest.builder().activityId(customerPlanByIdRequest.getActivityId()).delFlag(DeleteFlag.NO).build());
        if(CollectionUtils.isEmpty(planList)){
            return BaseResponse.success(new CustomerPlanByActivityIdResponse());
        }
        CustomerPlanVO customerPlanVO = customerPlanService.wrapperVo(planList.get(0));
        List<CustomerPlanCouponRel> couponList = new ArrayList<>();
        CustomerPlanSmsVO smsVO = null;
        CustomerPlanAppPushVO pushVO = null;
        if(Objects.nonNull(customerPlanVO)){
            if(customerPlanVO.getCouponFlag()) {
                couponList = customerPlanCouponRelService.listByPlanIds(Collections.singletonList(customerPlanVO.getId()));
            }
            if(customerPlanVO.getSmsFlag()){
                smsVO = customerPlanSmsRelService.wrapperVo(customerPlanSmsRelService.findByPlanId(customerPlanVO.getId()));
            }
            if(customerPlanVO.getAppPushFlag()){
                pushVO = customerPlanAppPushRelService.wrapperVo(customerPlanAppPushRelService.findByPlanId(customerPlanVO.getId()));
            }
        }
        return BaseResponse.success(new CustomerPlanByActivityIdResponse(
                customerPlanVO,
                couponList.stream().map(customerPlanCouponRelService::wrapperVo).collect(Collectors.toList()),
                smsVO, pushVO));
    }

    @Override
    public BaseResponse<CustomerPlanListResponse> list(@RequestBody @Valid CustomerPlanListRequest customerPlanListReq) {
        List<CustomerPlan> plans = customerPlanService.list(customerPlanListReq);
        return BaseResponse.success(new CustomerPlanListResponse(plans.stream().map(customerPlanService::wrapperVo).collect(Collectors.toList())));
    }
}

