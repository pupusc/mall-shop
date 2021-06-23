package com.wanmi.sbc.store;

import com.alibaba.fastjson.JSONObject;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.customer.api.constant.CompanyInfoErrorCode;
import com.wanmi.sbc.customer.api.constant.EmployeeErrorCode;
import com.wanmi.sbc.customer.api.constant.StoreErrorCode;
import com.wanmi.sbc.customer.api.provider.ares.CustomerAresProvider;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoProvider;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.ares.DispatcherFunctionRequest;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoAllModifyRequest;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.api.request.company.CompanyListRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeeAccountNameExistsRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeeListRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeeModifyAllRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreGetByStoreNameRequest;
import com.wanmi.sbc.customer.api.request.store.StoreModifyRequest;
import com.wanmi.sbc.customer.api.request.store.StoreSaveRequest;
import com.wanmi.sbc.customer.api.response.store.NoDeleteStoreByStoreNameResponse;
import com.wanmi.sbc.customer.bean.enums.AresFunctionType;
import com.wanmi.sbc.customer.bean.enums.SmsTemplate;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.EmployeeListVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifySupplierNameRequest;
import com.wanmi.sbc.util.sms.SmsSendUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 店铺信息服务
 * Created by CHENLI on 2017/11/2.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class StoreBaseService {
    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private StoreProvider storeProvider;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private CompanyInfoProvider companyInfoProvider;

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private SmsSendUtil smsSendUtil;

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private CustomerAresProvider customerAresProvider;

    /**
     * s2b boss 修改商家
     *
     * @param saveRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StoreVO updateStore(StoreSaveRequest saveRequest) {
        return this.baseUpdateStore(saveRequest, true);
    }


    /**
     * s2b supplier 修改商家
     *
     * @param saveRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StoreVO updateStoreForSupplier(StoreSaveRequest saveRequest) {
        return this.baseUpdateStore(saveRequest, false);
    }


    /**
     * 修改店铺基本信息
     * 修改店铺基本信息 并且修改商家信息
     *
     * @param saveRequest
     * @return
     */
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public StoreVO baseUpdateStore(StoreSaveRequest saveRequest, Boolean isS2bBoss) {
        StoreVO store = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(saveRequest.getStoreId()))
                .getContext().getStoreVO();
        //店铺信息不存在
        if (Objects.isNull(store)) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        }
        //供应商名称重复
        companyInfoQueryProvider.listCompanyInfo(
                CompanyListRequest.builder()
                        .equalSupplierName(saveRequest.getSupplierName())
                        .deleteFlag(DeleteFlag.NO).build())
                .getContext().getCompanyInfoVOList()
                .forEach(companyInfo -> {
                    if (!companyInfo.getCompanyInfoId().equals(saveRequest.getCompanyInfoId())) {
                        throw new SbcRuntimeException(CompanyInfoErrorCode.NAME_ALREADY_EXISTS);
                    }
                });
        //店铺名称重复
        NoDeleteStoreByStoreNameResponse response = storeQueryProvider.getNoDeleteStoreByStoreName(new
                NoDeleteStoreGetByStoreNameRequest(saveRequest
                .getStoreName())).getContext();

        if (response.getStoreId() != null && !response.getStoreId().equals(saveRequest.getStoreId())) {
            throw new SbcRuntimeException(StoreErrorCode.NAME_ALREADY_EXISTS);
        }

        //供应商不存在
        CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(saveRequest.getCompanyInfoId()).build()
        ).getContext();

        if (Objects.isNull(companyInfo) || DeleteFlag.YES.equals(companyInfo.getDelFlag())) {
            throw new SbcRuntimeException(CompanyInfoErrorCode.NOT_EXIST);
        }
        KsBeanUtil.copyProperties(saveRequest, store);

        if (saveRequest.getCityId() == null) {
            store.setCityId(null);
        }

        if (saveRequest.getAreaId() == null) {
            store.setAreaId(null);
        }

        if (saveRequest.getStreetId() == null) {
            store.setStreetId(null);
        }

        KsBeanUtil.copyProperties(saveRequest, companyInfo);
        companyInfo.setContactName(saveRequest.getContactPerson());
        companyInfo.setContactPhone(saveRequest.getContactMobile());
        companyInfo.setDetailAddress(saveRequest.getAddressDetail());

        //s2bboss可以修改供应商账号 s2bsupplier不可以修改账号
        if (isS2bBoss) {
            //供应商的主账号
            EmployeeListRequest listRequest = new EmployeeListRequest();
            listRequest.setCompanyInfoId(companyInfo.getCompanyInfoId());
            listRequest.setIsMasterAccount(Constants.yes);
            EmployeeListVO employee = employeeQueryProvider.list(listRequest).getContext().getEmployeeList().get(0);
            //如果供应商账号变更
            if (!Objects.equals(employee.getAccountName(), saveRequest.getAccountName())) {
                //查询该变更账号是否重复

                AccountType accountType = StoreType.SUPPLIER.equals(store.getStoreType()) ?
                        AccountType.s2bSupplier : AccountType.s2bProvider;
                boolean isExists = employeeQueryProvider.accountNameIsExists(
                        EmployeeAccountNameExistsRequest.builder()
                                .accountName(saveRequest.getAccountName())
                                .accountType(accountType).build()
                ).getContext().isExists();

                if (isExists) {
                    //商家账号已存在
                    throw new SbcRuntimeException(EmployeeErrorCode.ACCOUNT_ALREADY_EXIST);
                }
                employee.setAccountName(saveRequest.getAccountName());
            }

            //修改登录密码，并发短信通知
            if (saveRequest.getIsResetPwd()) {
                if (StringUtils.isEmpty(saveRequest.getAccountPassword())) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
                String encodePwd = SecurityUtil.getStoreLogpwd(String.valueOf(employee.getEmployeeId()),
                        saveRequest.getAccountPassword(), employee.getEmployeeSaltVal());
                employee.setAccountPassword(encodePwd);
                //发短信通知
                smsSendUtil.send(SmsTemplate.EMPLOYEE_PASSWORD, new String[]{saveRequest.getAccountName()},
                        saveRequest.getAccountName(), saveRequest.getAccountPassword());
            }
            EmployeeModifyAllRequest modifyAllRequest = new EmployeeModifyAllRequest();
            KsBeanUtil.copyPropertiesThird(employee, modifyAllRequest);
            employeeProvider.modifyAllById(modifyAllRequest);
        }

        //更新供应商信息
        CompanyInfoAllModifyRequest request = new CompanyInfoAllModifyRequest();
        KsBeanUtil.copyPropertiesThird(companyInfo, request);
        companyInfoProvider.modifyAllCompanyInfo(request);
        store.setCompanyInfo(companyInfo);

        GoodsModifySupplierNameRequest goodsModifySupplierNameRequest = new GoodsModifySupplierNameRequest();
        goodsModifySupplierNameRequest.setSupplierName(saveRequest.getSupplierName());
        goodsModifySupplierNameRequest.setCompanyInfoId(saveRequest.getCompanyInfoId());
        goodsProvider.modifySupplierName(goodsModifySupplierNameRequest);
        //更新店铺信息
        StoreModifyRequest request1 = new StoreModifyRequest();
        KsBeanUtil.copyPropertiesThird(store, request1);

        StoreVO savedStore = storeProvider.modifyStoreInfo(request1).getContext();
        //ares埋点-会员-编辑店铺信息
        customerAresProvider.dispatchFunction(
                DispatcherFunctionRequest.builder()
                        .funcType(AresFunctionType.EDIT_STORE)
                        .objs(new String[]{JSONObject.toJSONString(store)})
                        .build()
        );
        // 接口日志记录需要获取companyInfo中的companyCode值
        savedStore.setCompanyInfo(companyInfo);
        return savedStore;
    }

    /**
     * 修改店铺运费计算方式
     *
     * @param storeId
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStoreFreightType(Long storeId, DefaultFlag freightTemplateType) {
        StoreVO store = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(storeId)).getContext().getStoreVO();
        if (store == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (!Objects.equals(store.getFreightTemplateType(), freightTemplateType)) {
            store.setFreightTemplateType(freightTemplateType);

            StoreModifyRequest request = new StoreModifyRequest();

            KsBeanUtil.copyPropertiesThird(store, request);

            storeProvider.modifyStoreInfo(request);
        }
    }
}
