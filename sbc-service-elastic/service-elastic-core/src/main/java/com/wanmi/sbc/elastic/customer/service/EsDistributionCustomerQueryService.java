package com.wanmi.sbc.elastic.customer.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.api.provider.distribution.DistributorLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.quicklogin.ThirdLoginRelationQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelByIdsRequest;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationByCustomerRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelBaseResponse;
import com.wanmi.sbc.customer.api.response.quicklogin.ThirdLoginRelationResponse;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomeffBatchModifyRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerPageRequest;
import com.wanmi.sbc.elastic.api.response.customer.DistributionCustomerExportResponse;
import com.wanmi.sbc.elastic.api.response.customer.EsDistributionCustomerListResponse;
import com.wanmi.sbc.elastic.api.response.customer.EsDistributionCustomerPageResponse;
import com.wanmi.sbc.elastic.bean.vo.customer.EsDistributionCustomerVO;
import com.wanmi.sbc.elastic.customer.model.root.EsDistributionCustomer;
import com.wanmi.sbc.elastic.customer.repository.EsDistributionCustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: HouShuai
 * @date: 2020/12/7 11:29
 * @description:
 */
@Slf4j
@Service
public class EsDistributionCustomerQueryService {

    @Autowired
    private EsDistributionCustomerRepository esDistributionCustomerRepository;

    @Autowired
    private DistributorLevelQueryProvider distributorLevelQueryProvider;

    @Autowired
    private ThirdLoginRelationQueryProvider thirdLoginRelationQueryProvider;

    /**
     * 查询分销员列表
     *
     * @param request
     * @return
     */
    public EsDistributionCustomerPageResponse page(EsDistributionCustomerPageRequest request) {

        NativeSearchQuery searchQuery = request.esCriteria();

        Page<EsDistributionCustomer> esDistributionCustomerPage = esDistributionCustomerRepository.search(searchQuery);
        List<EsDistributionCustomer> esDistributionCustomerList = esDistributionCustomerPage.getContent();

        if (CollectionUtils.isEmpty(esDistributionCustomerList)) {
            return EsDistributionCustomerPageResponse.builder()
                    .distributionCustomerVOPage(new MicroServicePage<>())
                    .build();
        }
        Map<String, String> distributorLevelMap = this.getDistributorLevelMap(esDistributionCustomerList);
        Page<EsDistributionCustomerVO> newPage = esDistributionCustomerPage.map(entity -> {
            EsDistributionCustomerVO esDistributionCustomerVO = EsDistributionCustomerVO.builder().build();
            BeanUtils.copyProperties(entity, esDistributionCustomerVO);
            if (MapUtils.isNotEmpty(distributorLevelMap)) {
                String distributorLevelName = distributorLevelMap.get(esDistributionCustomerVO.getDistributorLevelId());
                esDistributionCustomerVO.setDistributorLevelName(distributorLevelName);
            }
            return esDistributionCustomerVO;
        });

        MicroServicePage<EsDistributionCustomerVO> microServicePage = new MicroServicePage<>(newPage, request.getPageable());

        return EsDistributionCustomerPageResponse.builder()
                .distributionCustomerVOPage(microServicePage)
                .build();
    }

    /**
     * 导出分销员信息
     *
     * @param request
     * @return
     */
    public BaseResponse<DistributionCustomerExportResponse> export(EsDistributionCustomerPageRequest request) {

        //分批取出数据
        List<DistributionCustomerVO> dataRecords = this.getDataRecords(request);

        List<DistributorLevelVO> distributorLevelList = distributorLevelQueryProvider.listAll().getContext().getDistributorLevelList();
        Map<String, String> map = distributorLevelList.stream().collect(Collectors.toMap(DistributorLevelVO::getDistributorLevelId, DistributorLevelVO::getDistributorLevelName));
        List<DistributionCustomerVO> dataRecordList = dataRecords.stream()
                .peek(distributionCustomerVO -> distributionCustomerVO.setDistributorLevelName(map.get(distributionCustomerVO.getDistributorLevelId())))
                .collect(Collectors.toList());
        DistributionCustomerExportResponse finalRes = new DistributionCustomerExportResponse(dataRecordList);
        return BaseResponse.success(finalRes);
    }


    /**
     * 分批取数据
     *
     * @param request
     * @return
     */
    private List<DistributionCustomerVO> getDataRecords(EsDistributionCustomerPageRequest request) {
        List<DistributionCustomerVO> dataRecords = new ArrayList<>();
        // 分批次查询，避免出现hibernate事务超时 共查询1000条
        for (int i = 0; i < 10; i++) {
            request.setPageNum(i);
            request.setPageSize(100);

            NativeSearchQuery searchQuery = request.esCriteria();
            Page<EsDistributionCustomer> esDistributionCustomerPage = esDistributionCustomerRepository.search(searchQuery);
            Page<DistributionCustomerVO> newPage = this.copyPage(esDistributionCustomerPage);
            List<DistributionCustomerVO> data = newPage.getContent();
            dataRecords.addAll(data);
            if (data.size() < 100) {
                break;
            }
        }
        return dataRecords;
    }


    /**
     * 根据分销员id，查询分销员信息
     *
     * @param queryRequest
     * @return
     */
    public BaseResponse<EsDistributionCustomerListResponse> listByIds(EsDistributionCustomeffBatchModifyRequest queryRequest) {
        List<String> distributionIds = queryRequest.getDistributionIds();
        List<EsDistributionCustomer> esDistributionCustomerList = esDistributionCustomerRepository.findByDistributionIdIn(distributionIds);
        List<DistributionCustomerShowPhoneVO> esDistributionCustomerVOList = this.copyBeanList(esDistributionCustomerList);
        EsDistributionCustomerListResponse response = EsDistributionCustomerListResponse.builder().list(esDistributionCustomerVOList).build();
        return BaseResponse.success(response);
    }


    /**
     * EsDistributionCustomer转DistributionCustomerVO
     *
     * @param esDistributionCustomerPage
     * @return
     */
    private Page<DistributionCustomerVO> copyPage(Page<EsDistributionCustomer> esDistributionCustomerPage) {
        return esDistributionCustomerPage.map(entity -> {
            DistributionCustomerVO distributionCustomerVO = new DistributionCustomerVO();
            BeanUtils.copyProperties(entity, distributionCustomerVO);
            ThirdLoginRelationByCustomerRequest relationByCustomerRequest = ThirdLoginRelationByCustomerRequest.builder()
                    .customerId(entity.getCustomerId())
                    .thirdLoginType(ThirdLoginType.WECHAT)
                    .build();
            BaseResponse<ThirdLoginRelationResponse> thirdLoginRelationResponseBaseResponse = thirdLoginRelationQueryProvider.listThirdLoginRelationByCustomer(relationByCustomerRequest);
            ThirdLoginRelationVO thirdLoginRelation = thirdLoginRelationResponseBaseResponse.getContext().getThirdLoginRelation();
            if (Objects.nonNull(thirdLoginRelation)) {
                distributionCustomerVO.setHeadImg(thirdLoginRelation.getHeadimgurl());
            }
            return distributionCustomerVO;
        });
    }


    /**
     * 获取分销员等级信息
     *
     * @param esDistributionCustomerList
     * @return
     */
    private Map<String, String> getDistributorLevelMap(List<EsDistributionCustomer> esDistributionCustomerList) {
        if (CollectionUtils.isEmpty(esDistributionCustomerList)) {
            return Collections.emptyMap();
        }
        //取出分销员等级id
        List<String> idList = esDistributionCustomerList.stream()
                .map(EsDistributionCustomer::getDistributorLevelId)
                .collect(Collectors.toList());

        DistributorLevelByIdsRequest idsRequest = DistributorLevelByIdsRequest.builder().idList(idList).build();
        BaseResponse<DistributorLevelBaseResponse> listByIds = distributorLevelQueryProvider.listByIds(idsRequest);
        List<DistributorLevelBaseVO> list = listByIds.getContext().getList();
        return Optional.ofNullable(list).orElse(Collections.emptyList()).stream()
                .collect(Collectors.toMap(DistributorLevelBaseVO::getDistributorLevelId,
                        DistributorLevelBaseVO::getDistributorLevelName));

    }

    /**
     * EsDistributionCustomer转 DistributionCustomerShowPhoneVO
     *
     * @param esDistributionCustomerList
     * @return
     */
    private List<DistributionCustomerShowPhoneVO> copyBeanList(List<EsDistributionCustomer> esDistributionCustomerList) {

        return Optional.ofNullable(esDistributionCustomerList)
                .orElse(Collections.emptyList()).stream()
                .map(entity -> {
                    DistributionCustomerShowPhoneVO distributionCustomerVO = DistributionCustomerShowPhoneVO.builder().build();
                    BeanUtils.copyProperties(entity, distributionCustomerVO);
                    return distributionCustomerVO;
                }).collect(Collectors.toList());
    }

}
