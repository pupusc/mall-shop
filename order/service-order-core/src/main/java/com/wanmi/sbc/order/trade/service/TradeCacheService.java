package com.wanmi.sbc.order.trade.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.address.CustomerDeliveryAddressQueryProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.invoice.CustomerInvoiceQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.address.CustomerDeliveryAddressByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.invoice.CustomerInvoiceByIdAndDelFlagRequest;
import com.wanmi.sbc.customer.api.request.store.ListCompanyStoreByCompanyIdsRequest;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.response.address.CustomerDeliveryAddressByIdResponse;
import com.wanmi.sbc.customer.api.response.detail.CustomerDetailGetCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.invoice.CustomerInvoiceByIdAndDelFlagResponse;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelDefaultResponse;
import com.wanmi.sbc.customer.api.response.store.ListCompanyStoreByCompanyIdsResponse;
import com.wanmi.sbc.customer.api.response.store.StoreByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.cate.ContractCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateStoreQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateGoodsRelaQueryProvider;
import com.wanmi.sbc.goods.api.request.cate.ContractCateListRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateStoreListByStoreIdAndDeleteFlagRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateGoodsRelaListByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.response.cate.ContractCateListResponse;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.vo.FreightTemplateGoodsVO;
import com.wanmi.sbc.goods.bean.vo.FreightTemplateStoreVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.StoreCateGoodsRelaVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.order.cache.WmCacheConfig;
import com.wanmi.sbc.order.trade.model.entity.value.Invoice;
import com.wanmi.sbc.order.trade.model.entity.value.Supplier;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeCacheService {


    @Autowired
    private StoreQueryProvider storeQueryProvider;


    @Autowired
    private CustomerDeliveryAddressQueryProvider customerDeliveryAddressQueryProvider;


    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private CustomerInvoiceQueryProvider customerInvoiceQueryProvider;


    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;


    @Autowired
    private ContractCateQueryProvider contractCateQueryProvider;


    @Autowired
    private FreightTemplateGoodsQueryProvider freightTemplateGoodsQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private StoreCateGoodsRelaQueryProvider storeCateGoodsRelaQueryProvider;

    @Autowired
    private FreightTemplateStoreQueryProvider freightTemplateStoreQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private VerifyService verifyService;

    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public List<StoreVO> queryStoreList(List<Long> storeIds) {
        return storeQueryProvider.listNoDeleteStoreByIds(
                ListNoDeleteStoreByIdsRequest.builder().storeIds(storeIds).build()).getContext().getStoreVOList();
    }


    /**
     * 根据用户提交的收货地址信息封装对象
     *
     * @param consigneeId         选择的收货地址id
     * @return 封装后的收货地址对象
     */
    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public BaseResponse<CustomerDeliveryAddressByIdResponse> getCustomerDeliveryAddressById(String consigneeId) {
        // 根据id查询收货人信息
        CustomerDeliveryAddressByIdRequest customerDeliveryAddressByIdRequest =
                new CustomerDeliveryAddressByIdRequest();
        customerDeliveryAddressByIdRequest.setDeliveryAddressId(consigneeId);
        BaseResponse<CustomerDeliveryAddressByIdResponse> customerDeliveryAddressByIdResponseBaseResponse =
                customerDeliveryAddressQueryProvider.getById(customerDeliveryAddressByIdRequest);
        CustomerDeliveryAddressByIdResponse customerDeliveryAddressByIdResponse =
                customerDeliveryAddressByIdResponseBaseResponse.getContext();
        if (customerDeliveryAddressByIdResponse == null || customerDeliveryAddressByIdResponse.getDelFlag() == DeleteFlag.YES) {
            throw new SbcRuntimeException("K-050313");
        }
        return customerDeliveryAddressByIdResponseBaseResponse;
    }

    /**
     * 查询会员增票信息
     * 主要是为了补充 联系人 与 联系地址
     *
     * @param specialInvoiceId  增值税/专用发票Id
     * @return 会员增票信息
     */
    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public BaseResponse<CustomerInvoiceByIdAndDelFlagResponse> getCustomerInvoiceByIdAndDelFlag(Long specialInvoiceId) {
        CustomerInvoiceByIdAndDelFlagRequest customerInvoiceByCustomerIdRequest =
                new CustomerInvoiceByIdAndDelFlagRequest();
        customerInvoiceByCustomerIdRequest.setCustomerInvoiceId(specialInvoiceId);
        return customerInvoiceQueryProvider.getByIdAndDelFlag(customerInvoiceByCustomerIdRequest);
    }


    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public Boolean isSupplierOrderAudit() {
        return auditQueryProvider.isSupplierOrderAudit().getContext().isAudit();
    }

    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public GoodsInfoViewByIdsResponse getGoodsInfoViewByIds(List<String> skuIds) {
        GoodsInfoViewByIdsRequest goodsInfoRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoRequest.setGoodsInfoIds(skuIds);
        goodsInfoRequest.setIsHavSpecText(Constants.yes);
        return goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();
    }



    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public BaseResponse<CustomerDetailGetCustomerIdResponse> getCustomerDetailByCustomerId(String customerId) {
        return customerDetailQueryProvider.getCustomerDetailByCustomerId(
                CustomerDetailByCustomerIdRequest
                        .builder()
                        .customerId(customerId)
                        .build());
    }

    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public BaseResponse<GoodsInfoListByIdsResponse> getGoodsInfoListByIds(List<String> skuIdList) {
        return goodsInfoQueryProvider.listByIds(GoodsInfoListByIdsRequest.builder().goodsInfoIds(skuIdList).build());
    }


    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public BaseResponse<ContractCateListResponse> queryContractCateList(Long storeId, Long cateId) {
        ContractCateListRequest req = new ContractCateListRequest();
        req.setStoreId(storeId);
        req.setCateId(cateId);
        return contractCateQueryProvider.list(req);
    }

    /**
     * 查询商品扣率
     * @param cateId
     * @return
     */
    public GoodsCateByIdResponse queryGoodsCate(Long cateId){
        GoodsCateByIdResponse context = goodsCateQueryProvider.getById(new GoodsCateByIdRequest(cateId)).getContext();
        if (context.getCateParentId().equals(0L)|| DefaultFlag.NO.equals(context.getIsParentCateRate())){
            return context;
        }
        //如果使用父级扣率则返回父级扣率
        return queryGoodsCate(context.getCateParentId());

    }

    public List<FreightTemplateGoodsVO> queryFreightTemplateGoodsListByIds(List<Long> tempIdList) {
        return freightTemplateGoodsQueryProvider.listByIds(
                FreightTemplateGoodsListByIdsRequest.builder().freightTempIds(tempIdList).build()
        ).getContext().getFreightTemplateGoodsVOList();
    }

    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public ConfigVO getTradeConfigByType(ConfigType configType) {
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(configType);
        return auditQueryProvider.getTradeConfigByType(request).getContext();
    }

    /**
     * 获取用户信息并缓存
     */
    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public CustomerVO getCustomerById(String id){
      return  customerQueryProvider.getCustomerById(new
              CustomerGetByIdRequest(id)).getContext();
    }

    /**
     * 缓存一系列营销价格
     * @return
     */
    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public List<GoodsInfoVO> marketingPluginGoodsListFilter(List<GoodsInfoVO> goodsInfos, CustomerVO customerVO) {
        //目前只计算商品的客户级别价格/客户指定价
        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class));
        filterRequest.setCustomerDTO(KsBeanUtil.convert(customerVO, CustomerDTO.class));
        return marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();
    }

    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public List<StoreCateGoodsRelaVO> listStoreCateByGoodsIds(List<String> spus) {
        StoreCateGoodsRelaListByGoodsIdsRequest request = new StoreCateGoodsRelaListByGoodsIdsRequest();
        request.setGoodsIds(spus);
        return storeCateGoodsRelaQueryProvider.listByGoodsIds(request)
                .getContext().getStoreCateGoodsRelaVOList();
    }

    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public List<FreightTemplateStoreVO> listStoreTemplateByStoreIdAndDeleteFlag(Long storeId, DeleteFlag no) {
         return freightTemplateStoreQueryProvider.listByStoreIdAndDeleteFlag(
                        FreightTemplateStoreListByStoreIdAndDeleteFlagRequest.builder()
                                .storeId(storeId).deleteFlag(no).build()
                ).getContext().getFreightTemplateStoreVOList();
    }

    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public CommonLevelVO fromCustomerLevel() {
        CustomerLevelDefaultResponse customerLevelVO = customerLevelQueryProvider.getDefaultCustomerLevel().getContext();
        if (customerLevelVO == null) {
            return null;
        }
        CommonLevelVO result = new CommonLevelVO();
        result.setLevelId(customerLevelVO.getCustomerLevelId());
        result.setLevelName(customerLevelVO.getCustomerLevelName());
        result.setLevelDiscount(customerLevelVO.getCustomerLevelDiscount());

        return result;
    }

    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public boolean verifyStore(List<Long> storeIds) {
        try {
            verifyService.verifyStore(storeIds);
            return true;
        }catch (Exception e){
            return false;
        }
    }

   // @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public boolean verifyInvoice(Invoice invoice, Supplier supplier) {
        try {
            verifyService.verifyInvoice(invoice, supplier);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public StoreVO getStoreById(Long storeId) {
        BaseResponse<StoreByIdResponse> response =  storeQueryProvider.getById(StoreByIdRequest.builder().storeId
                (storeId).build());
        return response.getContext().getStoreVO();
    }

    @Cacheable(value = WmCacheConfig.ORDER,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public ListCompanyStoreByCompanyIdsResponse listCompanyStoreByCompanyIds(List<Long> companyIds, List<Long> storeIds) {
        return storeQueryProvider.listCompanyStoreByCompanyIds(
                new ListCompanyStoreByCompanyIdsRequest(companyIds, storeIds)).getContext();
    }
}
