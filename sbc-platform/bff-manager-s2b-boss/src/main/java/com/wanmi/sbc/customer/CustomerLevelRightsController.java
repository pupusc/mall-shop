package com.wanmi.sbc.customer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.constant.CustomerLevelRightsErrorCode;
import com.wanmi.sbc.customer.api.provider.levelrights.CustomerLevelRightsQueryProvider;
import com.wanmi.sbc.customer.api.provider.levelrights.CustomerLevelRightsSaveProvider;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsAddRequest;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsDeleteRequest;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsModifyRequest;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsQueryRequest;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsListResponse;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsPageResponse;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsResponse;
import com.wanmi.sbc.customer.bean.enums.LevelRightsType;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelRightsVO;
import com.wanmi.sbc.elastic.api.provider.coupon.EsCouponActivityProvider;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityAddRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityDeleteByIdRequest;
import com.wanmi.sbc.elastic.bean.dto.coupon.EsCouponActivityDTO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponActivityProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.*;
import com.wanmi.sbc.marketing.bean.constant.Constant;
import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import com.wanmi.sbc.marketing.bean.vo.CouponActivityVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(description = "会员权益API", tags = "BossCustomerLevelRightsController")
@RestController
@RequestMapping(value = "/customer/customerLevelRights")
public class CustomerLevelRightsController {

    @Autowired
    private CustomerLevelRightsQueryProvider customerLevelRightsQueryProvider;

    @Autowired
    private CustomerLevelRightsSaveProvider customerLevelRightsSaveProvider;

    @Autowired
    private CouponActivityProvider couponActivityProvider;

    @Autowired
    private CouponActivityQueryProvider couponActivityQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EsCouponActivityProvider esCouponActivityProvider;

    private static Integer MAX_COUPON_SIZE = 10;

    @ApiOperation(value = "分页查询会员权益")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<CustomerLevelRightsPageResponse> page(@RequestBody @Valid CustomerLevelRightsQueryRequest request) {
        return customerLevelRightsQueryProvider.page(request);
    }

    @ApiOperation(value = "列表查询会员权益")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResponse<CustomerLevelRightsListResponse> list() {
        CustomerLevelRightsQueryRequest request = new CustomerLevelRightsQueryRequest();
        request.setDelFlag(DeleteFlag.NO);
        return customerLevelRightsQueryProvider.list(request);
    }

    @ApiOperation(value = "列表查询开启会员权益")
    @RequestMapping(value = "/valid/list", method = RequestMethod.GET)
    public BaseResponse<CustomerLevelRightsListResponse> queryValidList() {
        CustomerLevelRightsQueryRequest request = new CustomerLevelRightsQueryRequest();
        request.setDelFlag(DeleteFlag.NO);
        request.setStatus(1);
        return customerLevelRightsQueryProvider.list(request);
    }

    @ApiOperation(value = "根据主键id查询会员权益")
    @RequestMapping(value = "/{rightsId}", method = RequestMethod.POST)
    public BaseResponse<CustomerLevelRightsResponse> getById(@PathVariable Integer rightsId) {
        if (rightsId == null) {
            throw new SbcRuntimeException("K-000009");
        }
        CustomerLevelRightsQueryRequest request = new CustomerLevelRightsQueryRequest();
        request.setRightsId(rightsId);
        return customerLevelRightsQueryProvider.getById(request);
    }

    @ApiOperation(value = "新增会员权益")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResponse<CustomerLevelRightsResponse> add(@RequestBody @Valid CustomerLevelRightsAddRequest request) {
        request.setDelFlag(DeleteFlag.NO);
        request.setCreatePerson(commonUtil.getOperatorId());
        request.setCreateTime(LocalDateTime.now());
        // 如果是券礼包，新建优惠券活动，绑定活动和优惠券关系
        if (request.getRightsType().equals(LevelRightsType.COUPON_GIFT)) {
            // 校验券礼包内优惠券数量
            JSONObject rightsRule = JSONObject.parseObject(request.getRightsRule());
            List<Map> couponList = JSONArray.parseArray(rightsRule.get("couponLists").toString(), Map.class);
            if (couponList.size() > MAX_COUPON_SIZE) {
                throw new SbcRuntimeException(CustomerLevelRightsErrorCode.RIGHTS_COUPON_NUM_EXCEED);
            }
            CouponActivityAddRequest couponActivityAddRequest = CouponActivityAddRequest.builder()
                    .activityName(request.getRightsName())
                    .couponActivityType(CouponActivityType.RIGHTS_COUPON)
                    .receiveType(DefaultFlag.NO)
                    .joinLevel("-1")
                    .pauseFlag(request.getStatus() == 0 ? DefaultFlag.YES : DefaultFlag.NO)
                    .platformFlag(DefaultFlag.YES)
                    .storeId(Constant.BOSS_DEFAULT_STORE_ID)
                    .createPerson(commonUtil.getOperatorId())
                    .couponActivityConfigs(getCouponActivityConfigSaveRequests(couponList))
                    .build();
            CouponActivityVO vo = couponActivityProvider.add(couponActivityAddRequest).getContext().getCouponActivity();
            EsCouponActivityDTO esCouponActivityDTO = KsBeanUtil.convert(vo, EsCouponActivityDTO.class);
            List<String> joinLevels = Splitter.on(",").trimResults().splitToList(vo.getJoinLevel());
            esCouponActivityDTO.setJoinLevels(joinLevels);
            esCouponActivityProvider.add(new EsCouponActivityAddRequest(esCouponActivityDTO));

            request.setActivityId(vo.getActivityId());
        }
        return customerLevelRightsSaveProvider.add(request);
    }

    @ApiOperation(value = "编辑会员权益")
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public BaseResponse<CustomerLevelRightsResponse> modify(@RequestBody @Valid CustomerLevelRightsModifyRequest request) {
        if (request.getRightsType() == LevelRightsType.COUPON_GIFT && request.getActivityId() != null) {
            // 校验券礼包内优惠券数量
            JSONObject rightsRule = JSONObject.parseObject(request.getRightsRule());
            List<Map> couponList = JSONArray.parseArray(rightsRule.get("couponLists").toString(), Map.class);
            if (couponList.size() > MAX_COUPON_SIZE) {
                throw new SbcRuntimeException(CustomerLevelRightsErrorCode.RIGHTS_COUPON_NUM_EXCEED);
            }
            // 查询券礼包权益绑定的活动
            CouponActivityGetByIdRequest queryRequest = new CouponActivityGetByIdRequest();
            queryRequest.setId(request.getActivityId());
            CouponActivityVO vo = couponActivityQueryProvider.getById(queryRequest).getContext();

            CouponActivityModifyRequest couponActivityModifyRequest = new CouponActivityModifyRequest();
            KsBeanUtil.copyPropertiesThird(vo, couponActivityModifyRequest);
            couponActivityModifyRequest.setCouponActivityConfigs(getCouponActivityConfigSaveRequests(couponList));
            couponActivityModifyRequest.setPauseFlag(request.getStatus() == 0 ? DefaultFlag.YES : DefaultFlag.NO);
            couponActivityModifyRequest.setUpdatePerson(commonUtil.getOperatorId());
            couponActivityModifyRequest.setActivityName(request.getRightsName());
            CouponActivityVO couponActivity = couponActivityProvider.modify(couponActivityModifyRequest).getContext().getCouponActivity();
            EsCouponActivityDTO esCouponActivityDTO = KsBeanUtil.convert(couponActivity, EsCouponActivityDTO.class);
            List<String> joinLevels = Splitter.on(",").trimResults().splitToList(couponActivity.getJoinLevel());
            esCouponActivityDTO.setJoinLevels(joinLevels);
            esCouponActivityProvider.add(new EsCouponActivityAddRequest(esCouponActivityDTO));
        }
        request.setUpdatePerson(commonUtil.getOperatorId());
        request.setUpdateTime(LocalDateTime.now());
        return customerLevelRightsSaveProvider.modify(request);
    }

    @ApiOperation(value = "根据id删除会员权益")
    @RequestMapping(value = "/deleteById/{rightsId}", method = RequestMethod.DELETE)
    public BaseResponse deleteById(@PathVariable Integer rightsId) {
        // 根据id查询权益，若该权益绑定了活动则删除活动
        CustomerLevelRightsQueryRequest rightsQueryRequest = new CustomerLevelRightsQueryRequest();
        rightsQueryRequest.setRightsId(rightsId);
        CustomerLevelRightsVO rightsVO = customerLevelRightsQueryProvider.getById(rightsQueryRequest).getContext().getCustomerLevelRightsVO();
        if (rightsVO.getActivityId() != null) {
            couponActivityProvider.deleteByIdAndOperatorId(new CouponActivityDeleteByIdAndOperatorIdRequest(rightsVO.getActivityId(),
                    commonUtil.getOperatorId()));
            esCouponActivityProvider.deleteById(new EsCouponActivityDeleteByIdRequest(rightsVO.getActivityId()));
        }

        CustomerLevelRightsDeleteRequest request = new CustomerLevelRightsDeleteRequest();
        request.setRightsId(rightsId);
        request.setDelPerson(commonUtil.getOperatorId());
        request.setDelTime(LocalDateTime.now());
        return customerLevelRightsSaveProvider.deleteById(request);
    }

    @ApiOperation(value = "会员权益拖拽排序")
    @RequestMapping(value = "/editSort", method = RequestMethod.PUT)
    public BaseResponse editSort(@RequestBody CustomerLevelRightsQueryRequest request) {
        return customerLevelRightsSaveProvider.editSort(request);
    }

    private List<CouponActivityConfigSaveRequest> getCouponActivityConfigSaveRequests(List<Map> couponList) {
        List<CouponActivityConfigSaveRequest> activityConfigs = new ArrayList<>();
        couponList.forEach(coupon -> {
            CouponActivityConfigSaveRequest config = new CouponActivityConfigSaveRequest();
            config.setCouponId(coupon.get("couponId").toString());
            config.setTotalCount(Long.parseLong(coupon.get("totalCount").toString()));
            activityConfigs.add(config);
        });
        return activityConfigs;
    }
}
