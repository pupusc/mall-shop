package com.wanmi.sbc.marketing.provider.impl.coupon;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.ares.base.BaseResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeProvider;
import com.wanmi.sbc.marketing.api.request.coupon.*;
import com.wanmi.sbc.marketing.api.response.coupon.GetCouponGroupResponse;
import com.wanmi.sbc.marketing.bean.dto.CouponActivityConfigAndCouponInfoDTO;
import com.wanmi.sbc.marketing.coupon.model.root.CouponActivityConfig;
import com.wanmi.sbc.marketing.coupon.model.root.CouponInfo;
import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingCustomerScope;
import com.wanmi.sbc.marketing.coupon.mq.PaidCouponsMqService;
import com.wanmi.sbc.marketing.coupon.repository.CouponInfoRepository;
import com.wanmi.sbc.marketing.coupon.service.CouponActivityConfigService;
import com.wanmi.sbc.marketing.coupon.service.CouponCodeCopyService;
import com.wanmi.sbc.marketing.coupon.service.CouponCodeService;
import com.wanmi.sbc.marketing.coupon.service.CouponMarketingCustomerScopeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.representer.BaseRepresenter;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>对优惠券码操作接口</p>
 * Created by daiyitian on 2018-11-23-下午6:23.
 */
@Validated
@RestController
@Slf4j
public class CouponCodeController implements CouponCodeProvider {

    @Autowired
    private CouponCodeService couponCodeService;

    @Autowired
    private CouponCodeCopyService couponCodeCopyService;

    @Autowired
    private CouponMarketingCustomerScopeService couponMarketingCustomerScopeService;

    @Autowired
    private CouponActivityConfigService couponActivityConfigService;

    @Autowired
    private CouponInfoRepository couponInfoRepository;



    /**
     * 领取优惠券
     *
     * @param request 优惠券领取请求结构 {@link CouponFetchRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse fetch(@RequestBody @Valid CouponFetchRequest request){
        couponCodeService.customerFetchCoupon(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量更新券码使用状态
     *
     * @param request 批量修改请求结构 {@link CouponCodeBatchModifyRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse batchModify(@RequestBody @Valid CouponCodeBatchModifyRequest request){
        couponCodeService.batchModify(request.getModifyDTOList());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据id撤销优惠券使用
     *
     * @param request 包含id的撤销使用请求结构 {@link CouponCodeReturnByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse returnById(@RequestBody @Valid CouponCodeReturnByIdRequest request){
        couponCodeService.returnCoupon(request.getCouponCodeId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse precisionVouchers(@RequestBody @Valid CouponCodeBatchSendCouponRequest request){
        List<String> customerIds = request.getCustomerIds();
        List<CouponActivityConfigAndCouponInfoDTO> list = request.getList();
        if (CollectionUtils.isEmpty(customerIds)){
            List<CouponMarketingCustomerScope> customerScopes = couponMarketingCustomerScopeService.findByActivityId(list.get(0).getActivityId());
            customerIds = customerScopes.stream().map(CouponMarketingCustomerScope::getCustomerId).collect(Collectors.toList());
        }
        couponCodeService.precisionVouchers(customerIds,list);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 数据迁移：旧coupon_code按照新的分表规则进行拆分保存至新表中
     * @return
     */
    @Override
    public BaseResponse dataMigrationFromCouponCode() {
        Integer pageNum = NumberUtils.INTEGER_ZERO;
        Integer pageSize = 5000;
        int result = NumberUtils.INTEGER_ZERO;
        while (Boolean.TRUE) {
            int num = couponCodeCopyService.dataMigrationFromCouponCode(pageNum,pageSize);
            if (num == 0 ){
                break;
            }
            result += num;
        }
        return BaseResponse.success(result);
    }

    @Override
    public BaseResponse sendBatchCouponCodeByCustomerList(@RequestBody CouponCodeBatchSendCouponRequest request) {

        return BaseResponse.success(this.couponCodeService.sendBatchCouponCodeByCustomerList(request));
    }

    public BaseResponse sendCouponCodeByCustomizeStr(String json){
        Map<String, Object> response = JSONObject.parseObject(json);
        String customerId = response.get("customerId").toString();
        String activityId = response.get("activityId").toString();
        List<CouponCodeByCustomizeProviderRequest> couponCodeByCustomizeProviderRequestList = new ArrayList<>();
        CouponCodeByCustomizeProviderRequest request = new CouponCodeByCustomizeProviderRequest();
        request.setCustomerId(customerId);
        request.setActivityId(activityId);
        couponCodeByCustomizeProviderRequestList.add(request);
        return this.sendCouponCodeByCustomize(couponCodeByCustomizeProviderRequestList);
    }

    @Override
    public BaseResponse sendCouponCodeByCustomize(@RequestBody List<CouponCodeByCustomizeProviderRequest> couponCodeByCustomizeProviderRequestList) {
        log.info("****************手动发放优惠券   开始 ****************");
        for (CouponCodeByCustomizeProviderRequest param : couponCodeByCustomizeProviderRequestList) {
            log.info("手动发放优惠券：activityId:{} customerId:{} couponCodeService: {}", param.getActivityId(), param.getCustomerId(), couponCodeService);
            List<CouponActivityConfig> couponActivityConfigList = couponActivityConfigService.queryByActivityId(param.getActivityId());
            // 根据配置查询需要发放的优惠券列表
            List<CouponInfo> couponInfoList = couponInfoRepository.queryByIds(couponActivityConfigList.stream().map(
                    CouponActivityConfig::getCouponId).collect(Collectors.toList()));
//            log.info("根据配置查询需要发放的优惠券列表:{}",couponInfoList);
            // 组装优惠券发放数据
            List<GetCouponGroupResponse> getCouponGroupResponse = KsBeanUtil.copyListProperties(couponInfoList, GetCouponGroupResponse.class);
            getCouponGroupResponse = getCouponGroupResponse.stream().peek(item -> couponActivityConfigList.forEach(config -> {
                if (item.getCouponId().equals(config.getCouponId())) {
                    item.setTotalCount(config.getTotalCount());
                }
            })).collect(Collectors.toList());
            couponCodeService.sendBatchCouponCodeByCustomer(getCouponGroupResponse, param.getCustomerId(), param.getActivityId());
        }
        log.info("****************手动发放优惠券   结束 ****************");
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse sendCouponCodeByFileCustomize(CouponCodeByFileCustomizeProviderRequest couponCodeByFileCustomizeProviderRequest) {
        log.info("****************手动文件发放优惠券   开始 ****************");
//        if (StringUtils.isBlank(couponCodeByFileCustomizeProviderRequest.getPath()) || StringUtils.isBlank(couponCodeByFileCustomizeProviderRequest.getActivityId())) {
//            return BaseResponse.FAILED();
//        }
        if (CollectionUtils.isEmpty(couponCodeByFileCustomizeProviderRequest.getCustomerIdList()) || StringUtils.isBlank(couponCodeByFileCustomizeProviderRequest.getActivityId())) {
            return BaseResponse.FAILED();
        }
        sendCouponCodeByOuterFileCustomize(couponCodeByFileCustomizeProviderRequest);
        log.info("****************手动文件发放优惠券   结束 ****************");
        return BaseResponse.SUCCESSFUL();
    }

    public void sendCouponCodeByOuterFileCustomize(CouponCodeByFileCustomizeProviderRequest couponCodeByFileCustomizeProviderRequest) {
        String activityId = couponCodeByFileCustomizeProviderRequest.getActivityId();
        List<CouponActivityConfig> couponActivityConfigList = couponActivityConfigService.queryByActivityId(activityId);
        // 根据配置查询需要发放的优惠券列表
        List<CouponInfo> couponInfoList = couponInfoRepository.queryByIds(couponActivityConfigList.stream().map(
                CouponActivityConfig::getCouponId).collect(Collectors.toList()));
        // 组装优惠券发放数据
        List<GetCouponGroupResponse> getCouponGroupResponse = KsBeanUtil.copyListProperties(couponInfoList, GetCouponGroupResponse.class);
        getCouponGroupResponse = getCouponGroupResponse.stream().peek(item -> couponActivityConfigList.forEach(config -> {
            if (item.getCouponId().equals(config.getCouponId())) {
                item.setTotalCount(config.getTotalCount());
            }
        })).collect(Collectors.toList());
        couponCodeService.sendBatchCouponCodeByCustomer2(getCouponGroupResponse, couponCodeByFileCustomizeProviderRequest.getCustomerIdList(), activityId);
    }


//    public void executeAsync(CouponCodeByFileCustomizeProviderRequest couponCodeByFileCustomizeProviderRequest) {
//        log.info("start executeAsync");
//        //读取文件
//        FileReader fileReader = null;
//        BufferedReader bufferedReader = null;
//        try {
//
//            fileReader = new FileReader(couponCodeByFileCustomizeProviderRequest.getPath());
//            bufferedReader = new BufferedReader(fileReader);
//
//
//            String activityId = couponCodeByFileCustomizeProviderRequest.getActivityId();
//            List<CouponActivityConfig> couponActivityConfigList = couponActivityConfigService.queryByActivityId(activityId);
//            // 根据配置查询需要发放的优惠券列表
//            List<CouponInfo> couponInfoList = couponInfoRepository.queryByIds(couponActivityConfigList.stream().map(
//                    CouponActivityConfig::getCouponId).collect(Collectors.toList()));
//            // 组装优惠券发放数据
//            List<GetCouponGroupResponse> getCouponGroupResponse = KsBeanUtil.copyListProperties(couponInfoList, GetCouponGroupResponse.class);
//            getCouponGroupResponse = getCouponGroupResponse.stream().peek(item -> couponActivityConfigList.forEach(config -> {
//                if (item.getCouponId().equals(config.getCouponId())) {
//                    item.setTotalCount(config.getTotalCount());
//                }
//            })).collect(Collectors.toList());
//
//            String customerId = "";
//            int i = 0;
//            List<String> customerIdList = new ArrayList<>();
//            while ((customerId = bufferedReader.readLine()) != null) {
////                String[] arrs = line.split(",");
////                String activityId = arrs[0];
////                String customerId = arrs[0];
//                log.info("手动文件发放优惠券：activityId:{} customerId:{}", activityId, customerId);
//                long beginTime = System.currentTimeMillis();
//                customerIdList.add(customerId);
//                if (customerIdList.size() >= 100) {
//                    couponCodeService.sendBatchCouponCodeByCustomer2(getCouponGroupResponse, customerIdList, activityId);
//                    customerIdList.clear();
//                }
//
//                i++;
//                log.info("**************** customerId:{} activityId:{} time:{} ms ", customerId, activityId, (System.currentTimeMillis() - beginTime));
//            }
//            log.info("****************手动文件发放优惠券   结束 ****************");
//        } catch (FileNotFoundException e) {
//            log.error("读取文件异常", e);
//        } catch (IOException e) {
//            log.error("读取文件异常2", e);
//        }  finally {
//
//            try {
//                if (bufferedReader != null) {
//                    bufferedReader.close();
//                }
//                if (fileReader != null) {
//                    fileReader.close();
//                }
//            } catch (IOException e) {
//                log.error("读取文件关闭异常", e);
//            }
//
//        }
//        log.info("end executeAsync");
//    }


    @Override
    public BaseResponse sendCouponCodeByGoodsIds(CouponCodeByGoodsIdsRequest couponCodeByGoodsIdsRequest) {
        couponCodeService.sendCouponByGoodsIds(couponCodeByGoodsIdsRequest.getGoodsIds(),couponCodeByGoodsIdsRequest.getCustomerId());
        return BaseResponse.SUCCESSFUL();
    }
}
