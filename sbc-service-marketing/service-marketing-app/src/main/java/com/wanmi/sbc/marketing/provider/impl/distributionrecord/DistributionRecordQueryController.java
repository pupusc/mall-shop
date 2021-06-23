package com.wanmi.sbc.marketing.provider.impl.distributionrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyListRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailListByConditionRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerListRequest;
import com.wanmi.sbc.customer.api.request.store.ListStoreByIdsRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerByCustomerIdResponse;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.distributionrecord.DistributionRecordQueryProvider;
import com.wanmi.sbc.marketing.api.request.distributionrecord.*;
import com.wanmi.sbc.marketing.api.response.distributionrecord.*;
import com.wanmi.sbc.marketing.bean.vo.DistributionRecordVO;
import com.wanmi.sbc.marketing.bean.vo.GoodsInfoForDistribution;
import com.wanmi.sbc.marketing.distributionrecord.model.root.DistributionRecord;
import com.wanmi.sbc.marketing.distributionrecord.service.DistributionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * <p>DistributionRecord查询服务接口实现</p>
 *
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@RestController
@Validated
public class DistributionRecordQueryController implements DistributionRecordQueryProvider {

    @Autowired
    private GoodsInfoSpecDetailRelQueryProvider goodsInfoSpecDetailRelQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private DistributionRecordService distributionRecordService;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Override
    public BaseResponse<DistributionRecordPageResponse> page(@RequestBody @Valid DistributionRecordPageRequest distributionRecordPageReq) {
        DistributionRecordQueryRequest queryReq = new DistributionRecordQueryRequest();
        KsBeanUtil.copyPropertiesThird(distributionRecordPageReq, queryReq);
        Page<DistributionRecordVO> newPage = getDistributionRecordVOPage(queryReq);
        MicroServicePage<DistributionRecordVO> microPage = new MicroServicePage<>(newPage, distributionRecordPageReq.getPageable());
        DistributionRecordPageResponse finalRes = new DistributionRecordPageResponse(microPage);
        return BaseResponse.success(finalRes);

    }

    @Override
    public BaseResponse<DistributionRecordExportResponse> export(@RequestBody @Valid DistributionRecordExportRequest distributionRecordExportReq) {
        DistributionRecordQueryRequest queryReq = new DistributionRecordQueryRequest();
        KsBeanUtil.copyPropertiesThird(distributionRecordExportReq, queryReq);

        List<DistributionRecordVO> dataRecords = new ArrayList<>();
        // 分批次查询，避免出现hibernate事务超时 共查询1000条
        for (int i = 0; i < 20; i++) {
            queryReq.setPageNum(i);
            queryReq.setPageSize(50);
            List<DistributionRecordVO> data = getDistributionRecordVOPage(queryReq).getContent();
            dataRecords.addAll(data);
            if (data.size() < 50) {
                break;
            }
        }
        DistributionRecordExportResponse finalRes = new DistributionRecordExportResponse(dataRecords);
        return BaseResponse.success(finalRes);
    }


    /**
     * 获取Page<DistributionRecordVO>
     * @param queryReq
     * @return
     */
    private Page<DistributionRecordVO> getDistributionRecordVOPage(@Valid @RequestBody DistributionRecordQueryRequest queryReq) {
        Page<DistributionRecord> distributionRecordPage = distributionRecordService.page(queryReq);
        List<DistributionRecord> distributionRecordList = distributionRecordPage.getContent();
        if (!CollectionUtils.isEmpty(distributionRecordList) && distributionRecordList.size() > 0) {
            //根据会员的Ids查询会员信息，并插入分销信息
            List<String> cusotmerIds = distributionRecordList.stream().filter(d->d.getCustomerId() != null).map(v->v.getCustomerId()).collect(toList());
            if (!CollectionUtils.isEmpty(cusotmerIds)) {
                CustomerDetailListByConditionRequest conditionRequest = new CustomerDetailListByConditionRequest();
                conditionRequest.setCustomerIds(cusotmerIds);
                List<CustomerDetailVO> customerDetailVOList = customerDetailQueryProvider.listCustomerDetailByCondition(conditionRequest).getContext().getCustomerDetailVOList();
                IteratorUtils.zip(distributionRecordList, customerDetailVOList,
                        (collect1, levels1) -> Objects.nonNull(collect1.getCustomerId()) &&
                                collect1.getCustomerId().equals(levels1.getCustomerId()),
                        (collect2, levels2) -> {
                            collect2.setCustomerDetailVO(levels2);
                        }
                );
            }

            //根据分销员的Ids查询分销员的信息，并插入
            List<String> distributorIds = distributionRecordList.stream().filter(d->d.getDistributorId() != null).map(v->v.getDistributorId()).collect(toList());
            if(!CollectionUtils.isEmpty(distributorIds)){
                DistributionCustomerListRequest customerListRequest = new DistributionCustomerListRequest();
                customerListRequest.setDistributionIdList(distributorIds);
                List<DistributionCustomerVO> distributionCustomerVOS = distributionCustomerQueryProvider.list(customerListRequest).getContext().getDistributionCustomerVOList();
                IteratorUtils.zip(distributionRecordList, distributionCustomerVOS,
                        (collect1, levels1) -> Objects.nonNull(collect1.getDistributorId()) &&
                                collect1.getDistributorId().equals(levels1.getDistributionId()),
                        (collect2, levels2) -> {
                            collect2.setDistributionCustomerVO(levels2);
                        }
                );
            }

            //根据storeIds查询店铺信息，并塞入值
            List<Long> storeIds = distributionRecordList.stream().filter(d->d.getStoreId() != null).map(v->v.getStoreId()).collect(toList());
            if(!CollectionUtils.isEmpty(storeIds)){
                List<StoreVO> storeVOS = storeQueryProvider.listByIds(new ListStoreByIdsRequest(storeIds)).getContext().getStoreVOList();
                IteratorUtils.zip(distributionRecordList, storeVOS,
                        (collect1, levels1) -> Objects.nonNull(collect1.getStoreId()) &&
                                collect1.getStoreId().equals(levels1.getStoreId()),
                        (collect2, levels2) -> {
                            collect2.setStoreVO(levels2);
                        }
                );
            }

            //根据companyIds查询商家信息
            List<Long> companyIds = distributionRecordList.stream().filter(d->d.getStoreVO() !=  null
                    && d.getStoreVO().getCompanyInfo() != null && d.getStoreVO().getCompanyInfo().getCompanyInfoId() != null)
                    .map(v->v.getStoreVO().getCompanyInfo().getCompanyInfoId()).collect(toList());
            if(!CollectionUtils.isEmpty(companyIds)){;
                CompanyListRequest companyListRequest = new CompanyListRequest();
                companyListRequest.setCompanyInfoIds(companyIds);
                List<CompanyInfoVO> companyInfoVOList = companyInfoQueryProvider.listCompanyInfo(companyListRequest).getContext().getCompanyInfoVOList();
                IteratorUtils.zip(distributionRecordList, companyInfoVOList,
                        (collect1, levels1) -> Objects.nonNull(collect1.getStoreVO()) && Objects.nonNull(collect1.getStoreVO().getCompanyInfo())
                                && Objects.nonNull(collect1.getStoreVO().getCompanyInfo().getCompanyInfoId())
                                && collect1.getStoreVO().getCompanyInfo().getCompanyInfoId().equals(levels1.getCompanyInfoId()),
                        (collect2, levels2) -> {
                            collect2.setCompanyInfoVO(levels2);
                        }
                );
            }

            //根据货品ids获取货品信息
            List<String> goodsInfoIds = distributionRecordList.stream().filter(d->d.getGoodsInfoId() != null).map(v->v.getGoodsInfoId()).collect(toList());
            if(!CollectionUtils.isEmpty(goodsInfoIds)){
                List<GoodsInfoForDistribution> list = new ArrayList<>();
                GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest = new GoodsInfoViewByIdsRequest();
                goodsInfoViewByIdsRequest.setGoodsInfoIds(goodsInfoIds);
                List<GoodsInfoVO> goodsInfoVOS = goodsInfoQueryProvider.listViewByIds(goodsInfoViewByIdsRequest).getContext().getGoodsInfos();
                if(!CollectionUtils.isEmpty(goodsInfoVOS) && goodsInfoVOS.size() > 0) {
                    goodsInfoVOS.forEach(goodsInfoVO -> {
                        list.add(KsBeanUtil.convert(goodsInfoVO, GoodsInfoForDistribution.class));
                    });
                    IteratorUtils.zip(distributionRecordList, list,
                            (collect1, levels1) -> Objects.nonNull(collect1.getGoodsInfoId()) && collect1.getGoodsInfoId().equals(levels1.getGoodsInfoId()),
                            (collect2, levels2) -> {
                                collect2.setGoodsInfo(levels2);
                            }
                    );
                }
            }

            //查询规格值
            distributionRecordPage.getContent().stream().forEach(distributionRecord -> {
                //根据货品信息查询规格值列表
                GoodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest goodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest = new
                        GoodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest();
                if(distributionRecord.getGoodsInfo() != null && distributionRecord.getGoodsInfo().getGoodsId() != null
                        && distributionRecord.getGoodsInfo().getGoodsInfoId() != null) {
                    goodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest.setGoodsId(distributionRecord.getGoodsInfo().getGoodsId());
                    goodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest.setGoodsInfoId(distributionRecord.getGoodsInfoId());
                    distributionRecord.setGoodsInfoSpecDetailRelVOS(goodsInfoSpecDetailRelQueryProvider.listByGoodsIdAndSkuId
                            (goodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest).getContext().getGoodsInfoSpecDetailRelVOList());
                }

            });
        }
        return distributionRecordPage.map(entity -> distributionRecordService.wrapperVo(entity));
    }

    @Override
    public BaseResponse<DistributionRecordListResponse> list(@RequestBody @Valid DistributionRecordListRequest distributionRecordListReq) {
        DistributionRecordQueryRequest queryReq = new DistributionRecordQueryRequest();
        KsBeanUtil.copyPropertiesThird(distributionRecordListReq, queryReq);
        List<DistributionRecord> distributionRecordList = distributionRecordService.list(queryReq);
        List<DistributionRecordVO> newList = distributionRecordList.stream().map(entity -> distributionRecordService.wrapperVo(entity)).collect(toList());
        return BaseResponse.success(new DistributionRecordListResponse(newList));
    }

    @Override
    public BaseResponse<DistributionRecordByIdResponse> getById(@RequestBody @Valid DistributionRecordByIdRequest distributionRecordByIdRequest) {
        DistributionRecord distributionRecord = distributionRecordService.getById(distributionRecordByIdRequest.getRecordId());
        return BaseResponse.success(new DistributionRecordByIdResponse(distributionRecordService.wrapperVo(distributionRecord)));
    }

    @Override
    public BaseResponse<DistributionRecordByInviteeIdResponse> getPerformanceByCustomerId(@RequestBody @Valid DistributionRecordByCustomerIdRequest distributionRecordByIdRequest) {
        DistributionCustomerByCustomerIdResponse response = distributionCustomerQueryProvider.getByCustomerIdAndDistributorFlagAndDelFlag(new DistributionCustomerByCustomerIdRequest(distributionRecordByIdRequest.getCustomerId())).getContext();
        if(response.getDistributionCustomerVO() != null && response.getDistributionCustomerVO().getDistributionId() != null ) {
            return BaseResponse.success(distributionRecordService.getPerformanceByInviteeId(response.getDistributionCustomerVO().getDistributionId()));
        }
        return BaseResponse.success(new DistributionRecordByInviteeIdResponse(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO));
    }

}

