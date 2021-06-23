package com.wanmi.sbc.store;

import com.alibaba.fastjson.JSONObject;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.api.provider.ares.CustomerAresProvider;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelSaveProvider;
import com.wanmi.sbc.customer.api.request.ares.DispatcherFunctionRequest;
import com.wanmi.sbc.customer.api.request.company.CompanyTypeRequest;
import com.wanmi.sbc.customer.api.request.store.StoreAuditRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelInitRequest;
import com.wanmi.sbc.customer.bean.enums.AresFunctionType;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.brand.ContractBrandProvider;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateGoodsProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateProvider;
import com.wanmi.sbc.goods.api.request.brand.ContractBrandTransferByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsInitByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateInitByStoreIdRequest;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.request.GatewayInitByStoreIdRequest;
import com.wanmi.sbc.setting.api.provider.wechatloginset.WechatLoginSetSaveProvider;
import com.wanmi.sbc.setting.api.provider.wechatshareset.WechatShareSetSaveProvider;
import com.wanmi.sbc.setting.api.request.wechatloginset.WechatLoginSetAddRequest;
import com.wanmi.sbc.setting.api.request.wechatshareset.WechatShareSetAddRequest;
import com.wanmi.sbc.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Author: songhanlin
 * @Date: Created In 下午2:42 2017/11/7
 * @Description: 避免循环依赖所使用的Service
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class StoreSelfService {

    @Autowired
//    private StoreService storeService;
    private StoreProvider storeProvider;

    @Autowired
    private ContractBrandProvider contractBrandProvider;

    @Autowired
    private CompanyInfoProvider companyInfoProvider;

    @Autowired
    private StoreCateProvider storeCateProvider;

    @Autowired
    private CustomerAresProvider customerAresProvider;

    @Autowired
    private FreightTemplateGoodsProvider freightTemplateGoodsProvider;

    @Autowired
    private StoreLevelSaveProvider storeLevelSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private WechatLoginSetSaveProvider wechatLoginSetSaveProvider;

    @Autowired
    private WechatShareSetSaveProvider wechatShareSetSaveProvider;

    @Autowired
    private PayQueryProvider payQueryProvider;

    /**
     * 通过/驳回 审核
     *
     * @param request
     * @return
     */
    @GlobalTransactional
    @Transactional
    public StoreVO rejectOrPass(StoreAuditRequest request) {
        // 通过店铺
//        Store store = storeService.rejectOrPass(request);
        StoreVO store = storeProvider.auditStore(request).getContext().getStoreVO();
        if (Objects.equals(store.getAuditState(), CheckState.CHECKED)) {
            //迁移品牌
            contractBrandProvider.transferByStoreId(ContractBrandTransferByStoreIdRequest.builder().storeId(store.getStoreId()).build());
            //初始化店铺商品分类--默认分类_bail
            storeCateProvider.initByStoreId(new StoreCateInitByStoreIdRequest(store.getStoreId()));
            //非自营店铺初始化店铺等级
            if (store.getCompanyType() != null && store.getCompanyType() == BoolFlag.YES) {
                storeLevelSaveProvider.initStoreLevel(
                        StoreLevelInitRequest.builder()
                                .storeId(store.getStoreId())
                                .createPerson(commonUtil.getOperatorId())
                                .createTime(LocalDateTime.now())
                                .build()
                );
            }
            CompanyTypeRequest typeRequest = new CompanyTypeRequest();
            typeRequest.setCompanyInfoId(store.getCompanyInfo().getCompanyInfoId());
            typeRequest.setCompanyType(request.getCompanyType());
            typeRequest.setApplyEnterTime(LocalDateTime.now());
            companyInfoProvider.modifyCompanyType(typeRequest);

            //初始化店铺运费模板
            freightTemplateGoodsProvider.initByStoreId(
                    FreightTemplateGoodsInitByStoreIdRequest.builder().storeId(store.getStoreId()).build()
            );
            //initSaasSettings(store);
            //ares埋点-会员-店铺审核通过,推送店铺信息
            customerAresProvider.dispatchFunction(
                    DispatcherFunctionRequest.builder()
                            .funcType(AresFunctionType.ADD_STORE)
                            .objs(new String[]{JSONObject.toJSONString(store)})
                            .build()
            );
        }
        return store;
    }

    /**
     * saas化初始化信息
     * @param store
     */
    /*private void initSaasSettings(StoreVO store) {
        if(commonUtil.getSaasStatus()) {
            // 初始化域名配置信息
            domainStoreRelaProvider.add(DomainStoreRelaAddRequest.builder().
                    storeId(store.getStoreId()).
                    companyInfoId(store.getCompanyInfo().getCompanyInfoId()).
                    createPerson(commonUtil.getOperatorId()).
                    updatePerson(commonUtil.getOperatorId()).build());
            // 初始化登录接口
            wechatLoginSetSaveProvider.add(WechatLoginSetAddRequest.builder()
                    .mobileServerStatus(DefaultFlag.NO)
                    .pcServerStatus(DefaultFlag.NO)
                    .appServerStatus(DefaultFlag.NO)
                    .storeId(store.getStoreId())
                    .build());
            // 初始化分享接口
            wechatShareSetSaveProvider.add(WechatShareSetAddRequest.builder()
                    .storeId(store.getStoreId())
                    .operatePerson(commonUtil.getOperatorId())
                    .build());
            // 初始化支付方式
            payQueryProvider.initGatewayByStoreId(GatewayInitByStoreIdRequest.builder().storeId(store.getStoreId()).build());

        }
    }*/

}
