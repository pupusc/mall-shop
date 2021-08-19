package com.wanmi.sbc.appointmentsale.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.ErrorCodeConstant;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerSimplifyByIdRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeeByCompanyIdRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelByCustomerIdAndStoreIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaListByConditionRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerSimplifyByIdResponse;
import com.wanmi.sbc.customer.api.response.employee.EmployeeByCompanyIdResponse;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelByCustomerIdAndStoreIdResponse;
import com.wanmi.sbc.customer.api.response.store.StoreByIdResponse;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerRelaVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.order.api.provider.appointmentrecord.AppointmentRecordProvider;
import com.wanmi.sbc.order.api.provider.appointmentrecord.AppointmentRecordQueryProvider;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleProvider;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.appointmentsalegoods.AppointmentSaleGoodsProvider;
import com.wanmi.sbc.goods.api.provider.appointmentsalegoods.AppointmentSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordQueryRequest;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordRequest;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleListRequest;
import com.wanmi.sbc.goods.api.request.appointmentsale.RushToAppointmentSaleGoodsRequest;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.order.api.response.appointmentrecord.AppointmentRecordResponse;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.bean.dto.AppointmentSaleGoodsInfoDTO;
import com.wanmi.sbc.goods.bean.dto.AppointmentSaleInfoDTO;
import com.wanmi.sbc.goods.bean.dto.SupplierDTO;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import com.wanmi.sbc.mq.appointment.RushToAppointmentSaleGoodsMqService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.CommonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName AppointmentSaleService
 * @Description 预约活动service
 * @Author zxd
 **/
@Service
public class AppointmentSaleService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private FlashSaleGoodsQueryProvider flashSaleGoodsQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private AppointmentRecordQueryProvider appointmentRecordQueryProvider;

    @Autowired
    private AppointmentSaleGoodsProvider appointmentSaleGoodsProvider;

    @Autowired
    private AppointmentSaleGoodsQueryProvider appointmentSaleGoodsQueryProvider;

    @Autowired
    private AppointmentSaleProvider appointmentSaleProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private AppointmentSaleService appointmentSaleService;

    @Autowired
    private StoreCustomerQueryProvider storeCustomerQueryProvider;

    @Autowired
    private AppointmentRecordProvider appointmentRecordProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private RushToAppointmentSaleGoodsMqService rushToAppointmentSaleGoodsMqService;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;


    /**
     * @param request
     */
    public void judgeAppointmentGoodsCondition(RushToAppointmentSaleGoodsRequest request) {
        judgeAppointmentGoodsCondition(request, true);
    }

    /**
     * 校验预约资格
     *
     * @param request
     */
    public void judgeAppointmentGoodsCondition(RushToAppointmentSaleGoodsRequest request, boolean flag) {

        // 判断是否已经预约，如果预约了，不可重复预约
        // 判断活动是否存在
        // 判断活动是否正在进行中
        AppointmentRecordResponse response = appointmentRecordQueryProvider.getAppointmentInfo(AppointmentRecordQueryRequest.builder().buyerId(request.getCustomerId())
                .goodsInfoId(request.getSkuId()).appointmentSaleId(request.getAppointmentSaleId()).build()).getContext();

        if (flag && Objects.nonNull(response) && Objects.nonNull(response.getAppointmentRecordVO())) {
            throw new SbcRuntimeException("K-170001");
        }

        if (!flag && (Objects.isNull(response) || Objects.isNull(response.getAppointmentRecordVO()))) {
            throw new SbcRuntimeException("K-170002");
        }

        List<AppointmentSaleVO> appointmentSaleVOList = appointmentSaleQueryProvider.list(AppointmentSaleListRequest.builder().id(request.getAppointmentSaleId()).build()).getContext().getAppointmentSaleVOList();
        if (CollectionUtils.isEmpty(appointmentSaleVOList)) {
            throw new SbcRuntimeException("K-170003");
        }
        AppointmentSaleVO appointmentSaleVO = appointmentSaleVOList.get(0);
        if (appointmentSaleVO.getPauseFlag() == 1) {
            throw new SbcRuntimeException("K-170004");
        }

        if (appointmentSaleVO.getAppointmentStartTime().isAfter(LocalDateTime.now())) {
            throw new SbcRuntimeException("K-170005");
        }

        if (appointmentSaleVO.getAppointmentEndTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException("K-170006");
        }

        //判断店铺是否关店或者禁用
        StoreByIdResponse storeByIdResponse = storeQueryProvider.getById(StoreByIdRequest.builder().storeId(appointmentSaleVO.getStoreId()).build()).getContext();
        EmployeeByCompanyIdResponse employeeByCompanyIdResponse = employeeQueryProvider.getByCompanyId(EmployeeByCompanyIdRequest.builder()
                .companyInfoId(storeByIdResponse.getStoreVO().getCompanyInfo().getCompanyInfoId()).build()).getContext();
        if (storeByIdResponse.getStoreVO().getStoreState() == StoreState.CLOSED || employeeByCompanyIdResponse.getAccountState() == AccountState.DISABLE) {
            throw new SbcRuntimeException("K-000001");
        }

        List<AppointmentSaleGoodsVO> saleGoodsVOList = appointmentSaleGoodsQueryProvider.list(AppointmentSaleGoodsListRequest.builder().appointmentSaleId
                (appointmentSaleVO.getId()).goodsInfoId(request.getSkuId()).build()).getContext().getAppointmentSaleGoodsVOList();
        if (CollectionUtils.isEmpty(saleGoodsVOList)) {
            throw new SbcRuntimeException("K-170007");
        }
        StoreVO store = storeQueryProvider.getNoDeleteStoreById(NoDeleteStoreByIdRequest.builder().storeId(appointmentSaleVO.getStoreId())
                .build())
                .getContext().getStoreVO();



        if(appointmentSaleVO.getJoinLevel().equals("-3")) {
            //企业会员
            CustomerGetByIdRequest customerGetByIdRequest = new CustomerGetByIdRequest();
            customerGetByIdRequest.setCustomerId(request.getCustomerId());
            CustomerGetByIdResponse customerResponse = customerQueryProvider.getCustomerById(customerGetByIdRequest).getContext();
            if(!EnterpriseCheckState.CHECKED.equals(customerResponse.getEnterpriseCheckState())) {
                throw new SbcRuntimeException("K-170009");
            }
        } else if(appointmentSaleVO.getJoinLevel().equals("-2")){
            //付费会员
            List<PaidCardCustomerRelVO> relVOList = paidCardCustomerRelQueryProvider
                    .list(PaidCardCustomerRelListRequest.builder()
                            .delFlag(DeleteFlag.NO)
                            .endTimeBegin(LocalDateTime.now())
                            .customerId(request.getCustomerId()).build())
                    .getContext().getPaidCardCustomerRelVOList();
            if(CollectionUtils.isEmpty(relVOList)) {
                throw new SbcRuntimeException("K-170009");
            }
        }else if (!appointmentSaleVO.getJoinLevel().equals("-1")) {
            // 判断活动是否是全平台客户还是店铺内客户
            if (BoolFlag.NO == store.getCompanyType()){
                CustomerSimplifyByIdRequest simplifyByIdRequest = new CustomerSimplifyByIdRequest();
                simplifyByIdRequest.setCustomerId(request.getCustomerId());
                CustomerSimplifyByIdResponse simplifyByIdResponse = customerQueryProvider.simplifyById(simplifyByIdRequest).getContext();
                Long customerLevelId = simplifyByIdResponse.getCustomerLevelId();
                if (!appointmentSaleVO.getJoinLevel().equals("0") && !Arrays.asList(appointmentSaleVO.getJoinLevel().split(",")).contains(customerLevelId.toString())) {
                    throw new SbcRuntimeException("K-170009");
                }
            }else{
                StoreCustomerRelaListByConditionRequest listByConditionRequest = new StoreCustomerRelaListByConditionRequest();
                listByConditionRequest.setCustomerId(request.getCustomerId());
                listByConditionRequest.setStoreId(appointmentSaleVO.getStoreId());
                List<StoreCustomerRelaVO> relaVOList = storeCustomerQueryProvider.listByCondition(listByConditionRequest).getContext().getRelaVOList();
                if (Objects.nonNull(relaVOList) && relaVOList.size() > 0) {
                    if (!appointmentSaleVO.getJoinLevel().equals("0") && !Arrays.asList(appointmentSaleVO.getJoinLevel().split(",")).contains(relaVOList.get(0).getStoreLevelId().toString())) {
                        throw new SbcRuntimeException("K-170009");
                    }
                } else {
                    //只限于店铺内会员
                    throw new SbcRuntimeException("K-170008");
                }
            }
        }

        if (flag) {
            //获取商品所属商家，店铺信息
            SupplierDTO supplier = SupplierDTO.builder()
                    .storeId(store.getStoreId())
                    .storeName(store.getStoreName())
                    .isSelf(store.getCompanyType() == BoolFlag.NO)
                    .supplierCode(store.getCompanyInfo().getCompanyCode())
                    .supplierId(store.getCompanyInfo().getCompanyInfoId())
                    .supplierName(store.getCompanyInfo().getSupplierName())
                    .freightTemplateType(store.getFreightTemplateType())
                    .build();

            GoodsInfoByIdResponse goodsInfoResponse = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(request.getSkuId()).build()).getContext();
            AppointmentSaleGoodsVO appointmentSaleGoodsVO = saleGoodsVOList.get(0);
            appointmentSaleGoodsVO.setSkuPic(goodsInfoResponse.getGoodsInfoImg());
            appointmentSaleGoodsVO.setSkuName(goodsInfoResponse.getGoodsInfoName());
            appointmentRecordProvider.add(AppointmentRecordRequest.builder().buyerId(commonUtil.getCustomer().getCustomerId()).supplier(supplier).goodsInfoId(request.getSkuId())
                    .appointmentSaleId(appointmentSaleVO.getId()).appointmentSaleGoodsInfo(KsBeanUtil.convert(appointmentSaleGoodsVO, AppointmentSaleGoodsInfoDTO.class))
                    .appointmentSaleInfo(KsBeanUtil.convert(appointmentSaleVO, AppointmentSaleInfoDTO.class)).build());
        }

    }


    /**
     * 抢购规则校验
     *
     * @param request
     */
    public void judgeAppointmentSaleGoodsCondition(RushToAppointmentSaleGoodsRequest request) {

        // 判断是否已经预约，如果预约了，不可重复预约
        // 判断活动是否存在
        // 判断活动是否正在进行中
        AppointmentRecordResponse response = appointmentRecordQueryProvider.getAppointmentInfo(AppointmentRecordQueryRequest.builder().buyerId(request.getCustomerId())
                .goodsInfoId(request.getSkuId()).appointmentSaleId(request.getAppointmentSaleId()).build()).getContext();

        List<AppointmentSaleVO> appointmentSaleVOList = appointmentSaleQueryProvider.list(AppointmentSaleListRequest.builder().id(request.getAppointmentSaleId()).build()).getContext().getAppointmentSaleVOList();
        if (CollectionUtils.isEmpty(appointmentSaleVOList)) {
            throw new SbcRuntimeException("K-160001");
        }
        AppointmentSaleVO appointmentSaleVO = appointmentSaleVOList.get(0);
        if (appointmentSaleVO.getPauseFlag() == 1) {
            throw new SbcRuntimeException("K-160001");
        }

        if (appointmentSaleVO.getSnapUpEndTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException("K-160005");
        }
        if (appointmentSaleVO.getAppointmentType() == 0 && (Objects.isNull(response) || Objects.isNull(response.getAppointmentRecordVO()))) {
            throw new SbcRuntimeException("K-160002");
        }

        //判断店铺是否关店或者禁用
        StoreByIdResponse storeByIdResponse = storeQueryProvider.getById(StoreByIdRequest.builder().storeId(appointmentSaleVO.getStoreId()).build()).getContext();
        EmployeeByCompanyIdResponse employeeByCompanyIdResponse = employeeQueryProvider.getByCompanyId(EmployeeByCompanyIdRequest.builder()
                .companyInfoId(storeByIdResponse.getStoreVO().getCompanyInfo().getCompanyInfoId()).build()).getContext();
        if (storeByIdResponse.getStoreVO().getStoreState() == StoreState.CLOSED || employeeByCompanyIdResponse.getAccountState() == AccountState.DISABLE) {
            throw new SbcRuntimeException("K-000001");
        }

        List<AppointmentSaleGoodsVO> saleGoodsVOList = appointmentSaleGoodsQueryProvider.list(AppointmentSaleGoodsListRequest.builder().appointmentSaleId
                (appointmentSaleVO.getId()).goodsInfoId(request.getSkuId()).build()).getContext().getAppointmentSaleGoodsVOList();
        if (CollectionUtils.isEmpty(saleGoodsVOList)) {
            throw new SbcRuntimeException("K-160001");
        }

        if(appointmentSaleVO.getJoinLevel().equals("-3")) {
            //企业会员
            CustomerGetByIdRequest customerGetByIdRequest = new CustomerGetByIdRequest();
            customerGetByIdRequest.setCustomerId(request.getCustomerId());
            CustomerGetByIdResponse customerResponse = customerQueryProvider.getCustomerById(customerGetByIdRequest).getContext();
            if(!EnterpriseCheckState.CHECKED.equals(customerResponse.getEnterpriseCheckState())) {
                throw new SbcRuntimeException("K-170009");
            }
        } else if(appointmentSaleVO.getJoinLevel().equals("-2")){
            //付费会员
            List<PaidCardCustomerRelVO> relVOList = paidCardCustomerRelQueryProvider
                    .list(PaidCardCustomerRelListRequest.builder()
                            .delFlag(DeleteFlag.NO)
                            .endTimeBegin(LocalDateTime.now())
                            .customerId(request.getCustomerId()).build())
                    .getContext().getPaidCardCustomerRelVOList();
            if(CollectionUtils.isEmpty(relVOList)) {
                throw new SbcRuntimeException("K-170009");
            }
        }else if (!appointmentSaleVO.getJoinLevel().equals("-1")) {
            //获取会员在该店铺的等级，自营店铺取平台等级；第三方店铺取店铺等级
            CustomerLevelByCustomerIdAndStoreIdResponse levelResponse = customerLevelQueryProvider
                    .getCustomerLevelByCustomerIdAndStoreId(CustomerLevelByCustomerIdAndStoreIdRequest.builder().customerId(commonUtil.getOperatorId()).storeId(appointmentSaleVO.getStoreId()).build())
                    .getContext();
            if (Objects.nonNull(levelResponse) && Objects.nonNull(levelResponse.getLevelId())) {
                if (!appointmentSaleVO.getJoinLevel().equals("0")
                        && !Arrays.asList(appointmentSaleVO.getJoinLevel().split(",")).contains(levelResponse.getLevelId().toString())) {
                    throw new SbcRuntimeException("K-160003");
                }
            } else {
                throw new SbcRuntimeException("K-160004");
            }
        }

    }


    /**
     * @return AppointmentSaleVO
     * @Author zhangxiaodong
     * @Description 从Redis缓存中商品抢购信息
     * @Date 16:32 2020/05/25
     * @Param []
     **/
    public AppointmentSaleVO getAppointmentSaleGoodsInfoForRedis(RushToAppointmentSaleGoodsRequest request) {
        String appointmentSaleGoodsInfoKey = RedisKeyConstant.APPOINTMENT_SALE_GOODS_INFO + request.getAppointmentSaleId() + ":" + request.getSkuId();
        //从redis缓存中获取对应的秒杀抢购商品信息
        AppointmentSaleVO appointmentSaleVO = redisService.getObj(appointmentSaleGoodsInfoKey, AppointmentSaleVO.class);
        if (Objects.isNull(appointmentSaleVO) || appointmentSaleVO.getStock() <= 0L) {
            //如果redis缓存中不存在秒杀抢购信息从数据获取，重新放入缓存中
            appointmentSaleVO = appointmentSaleQueryProvider.getAppointmentSaleRelaInfo(RushToAppointmentSaleGoodsRequest.builder()
                    .appointmentSaleId(request.getAppointmentSaleId())
                    .skuId(request.getSkuId())
                    .build()).getContext().getAppointmentSaleVO();
            long minutes = Duration.between(appointmentSaleVO.getSnapUpEndTime(), LocalDateTime.now()).toMinutes();
            redisService.setObj(appointmentSaleGoodsInfoKey, appointmentSaleVO, Math.abs(minutes) * 60);
        }
        return appointmentSaleVO;
    }

    /**
     * 获取预约资格
     *
     * @param request
     * @return
     */
    public RushToAppointmentSaleGoodsRequest getAppointmentSaleGoodsQualifications(RushToAppointmentSaleGoodsRequest request) {
        String appointment_sale_goods_qualifications = RedisKeyConstant.APPOINTMENT_SALE_GOODS_QUALIFICATIONS + request.getCustomerId()
                + ":" + request.getAppointmentSaleId() + ":" + request.getSkuId();
        String qualificationsInfo = redisService.getString(appointment_sale_goods_qualifications);
        AppointmentSaleVO appointmentSaleVO = getAppointmentSaleGoodsInfoForRedis(request);
        if (StringUtils.isBlank(qualificationsInfo) && (appointmentSaleVO.getStock().equals(NumberUtils.LONG_ZERO)
                || appointmentSaleVO.getStock() < request.getNum())) {
            throw new SbcRuntimeException(ErrorCodeConstant.FLASH_SALE_PANIC_BUYING_FAIL);
        } else {
            return JSONObject.parseObject(qualificationsInfo, RushToAppointmentSaleGoodsRequest.class);
        }
    }

    /**
     * 获取预约抢购信息
     *
     * @param request
     * @return
     */
    public AppointmentSaleByIdResponse getAppointmentSaleInfo(RushToAppointmentSaleGoodsRequest request) {

        return appointmentSaleQueryProvider.getAppointmentSaleRelaInfo(request).getContext();

    }
}
