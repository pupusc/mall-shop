package com.wanmi.sbc.customer.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.DistributionInviteNewByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.*;
import com.wanmi.sbc.customer.api.response.distribution.*;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerShowPhoneVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerSimVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.detail.service.CustomerDetailService;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomer;
import com.wanmi.sbc.customer.distribution.model.root.DistributorLevel;
import com.wanmi.sbc.customer.distribution.model.root.InviteNewRecord;
import com.wanmi.sbc.customer.distribution.service.CustomerDistributionInviteNewService;
import com.wanmi.sbc.customer.distribution.service.DistributionCustomerService;
import com.wanmi.sbc.customer.distribution.service.DistributorLevelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>分销员查询服务接口实现</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@Slf4j
@RestController
@Validated
public class DistributionCustomerQueryController implements DistributionCustomerQueryProvider {

    @Autowired
    private DistributionCustomerService distributionCustomerService;

    @Autowired
    private CustomerDistributionInviteNewService customerDistributionInviteNewService;

    @Autowired
    private CustomerDetailService customerDetailService;

    @Autowired
    private DistributorLevelService distributorLevelService;

    /**
     * 分页查询分销员API
     *
     * @param distributionCustomerPageReq 分页请求参数和筛选对象 {@link DistributionCustomerPageRequest}
     * @return 分销员分页列表信息 {@link DistributionCustomerPageResponse}
     * @author lq
     */
    @Override
    public BaseResponse<DistributionCustomerPageResponse>
    page(@RequestBody @Valid DistributionCustomerPageRequest distributionCustomerPageReq) {
        DistributionCustomerQueryRequest queryReq = new DistributionCustomerQueryRequest();
        KsBeanUtil.copyPropertiesThird(distributionCustomerPageReq, queryReq);


        Page<DistributionCustomer> distributionCustomerPage = distributionCustomerService.page(queryReq);
        List<DistributionCustomer> distributionCustomerList = distributionCustomerPage.getContent();
        Map<String, String> map = new HashMap<>(queryReq.getPageSize());
        if (CollectionUtils.isNotEmpty(distributionCustomerList)) {
            List<String> distributorLevelIds = distributionCustomerList.stream().map(DistributionCustomer::getDistributorLevelId).collect(Collectors.toList());
            List<DistributorLevel> distributorLevelList = distributorLevelService.findByDistributorLevelIdIn(distributorLevelIds);
            map = distributorLevelList.stream().collect(Collectors.toMap(DistributorLevel::getDistributorLevelId, DistributorLevel::getDistributorLevelName));
        }
        final Map<String, String> resultMap = map;
        Page<DistributionCustomerVO> newPage = KsBeanUtil.convertPage(distributionCustomerPage, DistributionCustomerVO.class);
        //stributionCustomerPage.map(entity -> distributionCustomerService.wrapperVo(entity));
        if (MapUtils.isNotEmpty(resultMap) && Objects.nonNull(newPage)) {
            newPage = newPage.map(vo -> {
                vo.setDistributorLevelName(resultMap.get(vo.getDistributorLevelId()));
                return vo;
            });
        }

        MicroServicePage<DistributionCustomerVO> microPage = new MicroServicePage<>(newPage,
                distributionCustomerPageReq.getPageable());
        DistributionCustomerPageResponse finalRes = new DistributionCustomerPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<EsDistributionCustomerPageResponse> pageShowPhone(@RequestBody @Valid DistributionCustomerPageRequest distributionCustomerPageReq) {
        DistributionCustomerQueryRequest queryReq = new DistributionCustomerQueryRequest();
        KsBeanUtil.copyPropertiesThird(distributionCustomerPageReq, queryReq);


        Page<DistributionCustomer> distributionCustomerPage = distributionCustomerService.page(queryReq);
        List<DistributionCustomer> distributionCustomerList = distributionCustomerPage.getContent();
        Map<String, String> map = new HashMap<>(queryReq.getPageSize());
        if (CollectionUtils.isNotEmpty(distributionCustomerList)) {
            List<String> distributorLevelIds = distributionCustomerList.stream().map(DistributionCustomer::getDistributorLevelId).collect(Collectors.toList());
            List<DistributorLevel> distributorLevelList = distributorLevelService.findByDistributorLevelIdIn(distributorLevelIds);
            map = distributorLevelList.stream().collect(Collectors.toMap(DistributorLevel::getDistributorLevelId, DistributorLevel::getDistributorLevelName));
        }
        final Map<String, String> resultMap = map;
        Page<DistributionCustomerShowPhoneVO> newPage = KsBeanUtil.convertPage(distributionCustomerPage, DistributionCustomerShowPhoneVO.class);
        if (MapUtils.isNotEmpty(resultMap) && Objects.nonNull(newPage)) {
            newPage = newPage.map(vo -> {
                vo.setDistributorLevelName(resultMap.get(vo.getDistributorLevelId()));
                return vo;
            });
        }

        MicroServicePage<DistributionCustomerShowPhoneVO> microPage = new MicroServicePage<>(newPage,
                distributionCustomerPageReq.getPageable());
        EsDistributionCustomerPageResponse finalRes = new EsDistributionCustomerPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<DistributionCustomerExportResponse> export(@RequestBody @Valid DistributionCustomerExportRequest distributionCustomerExportReq) {
        DistributionCustomerQueryRequest queryReq = new DistributionCustomerQueryRequest();
        KsBeanUtil.copyPropertiesThird(distributionCustomerExportReq, queryReq);

        List<DistributionCustomerVO> dataRecords = new ArrayList<>();
        // 分批次查询，避免出现hibernate事务超时 共查询1000条
        for (int i = 0; i < 10; i++) {
            queryReq.setPageNum(i);
            queryReq.setPageSize(100);

            Page<DistributionCustomer> distributionCustomerPage = distributionCustomerService.page(queryReq);
            Page<DistributionCustomerVO> newPage = distributionCustomerPage.map(entity -> distributionCustomerService.wrapperVo(entity));
            List<DistributionCustomerVO> data = newPage.getContent();

            dataRecords.addAll(data);
            if (data.size() < 100) {
                break;
            }
        }

        List<DistributorLevel> distributorLevelList = distributorLevelService.findAll();
        Map<String, String> map = distributorLevelList.stream().collect(Collectors.toMap(DistributorLevel::getDistributorLevelId, DistributorLevel::getDistributorLevelName));
        dataRecords = dataRecords.stream().map(distributionCustomerVO -> {
            distributionCustomerVO.setDistributorLevelName(map.get(distributionCustomerVO.getDistributorLevelId()));
            return distributionCustomerVO;
        }).collect(Collectors.toList());
        DistributionCustomerExportResponse finalRes = new DistributionCustomerExportResponse(dataRecords);
        return BaseResponse.success(finalRes);
    }

    /**
     * 列表查询分销员API
     *
     * @param distributionCustomerListReq 列表请求参数和筛选对象 {@link DistributionCustomerListRequest}
     * @return 分销员的列表信息 {@link DistributionCustomerListResponse}
     * @author lq
     */
    @Override
    public BaseResponse<DistributionCustomerListResponse> list(@RequestBody @Valid DistributionCustomerListRequest distributionCustomerListReq) {
        DistributionCustomerQueryRequest queryReq = new DistributionCustomerQueryRequest();
        KsBeanUtil.copyPropertiesThird(distributionCustomerListReq, queryReq);
        List<DistributionCustomer> distributionCustomerList = distributionCustomerService.list(queryReq);
        List<DistributionCustomerVO> newList =
                distributionCustomerList.stream().map(entity -> distributionCustomerService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new DistributionCustomerListResponse(newList));
    }

    /**
     * 查询下单人的佣金受益人列表
     *
     * @param request 请求对象
     * @return
     */
    @Override
    public BaseResponse<DistributionCustomerListForOrderCommitResponse> listDistributorsForOrderCommit(
            @RequestBody @Valid DistributionCustomerListForOrderCommitRequest request) {
        List<DistributionCustomer> customers = distributionCustomerService.listDistributorsForOrderCommit(request);
        return BaseResponse.success(new DistributionCustomerListForOrderCommitResponse(
                KsBeanUtil.convertList(customers, DistributionCustomerSimVO.class)));
    }


    /**
     * 单个查询分销员API 显示手机号
     *
     * @param distributionCustomerByIdRequest 单个查询分销员请求参数 {@link DistributionCustomerByIdRequest}
     * @return 分销员详情 {@link DistributionCustomerByIdResponse}
     * @author lq
     */
    @Override
    public BaseResponse<DistributionCustomerByIdResponse> getById(@RequestBody @Valid DistributionCustomerByIdRequest distributionCustomerByIdRequest) {
        DistributionCustomer distributionCustomer =
                distributionCustomerService.getById(distributionCustomerByIdRequest.getDistributionId());
        return BaseResponse.success(new DistributionCustomerByIdResponse(distributionCustomerService.wrapperVo(distributionCustomer)));
    }
    /**
     * 单个查询分销员API
     *
     * @param distributionCustomerByIdRequest 单个查询分销员请求参数 {@link DistributionCustomerByIdRequest}
     * @return 分销员详情 {@link EsDistributionCustomerByIdResponse}
     * @author lq
     */
    @Override
    public BaseResponse<EsDistributionCustomerByIdResponse> getByIdShowPhone(@RequestBody @Valid DistributionCustomerByIdRequest distributionCustomerByIdRequest) {
        DistributionCustomer distributionCustomer =
                distributionCustomerService.getById(distributionCustomerByIdRequest.getDistributionId());
        return BaseResponse.success(new EsDistributionCustomerByIdResponse(distributionCustomerService.wrapperEsVo(distributionCustomer)));
    }


    /**
     * 简单查询分销员的信息——会员端使用
     *
     * @param distributionCustomerSimByIdRequest 单个查询分销员请求参数 {@link DistributionCustomerSimByIdRequest}
     * @return 分销员简单信息 {@link DistributionCustomerSimByIdResponse}
     * @author baijz
     */
    @Override
    public BaseResponse<DistributionCustomerSimByIdResponse> getSimInfoById(@RequestBody @Valid
                                                                                    DistributionCustomerSimByIdRequest distributionCustomerSimByIdRequest) {
        DistributionCustomer distributionCustomer =
                distributionCustomerService.getByCustomerIdAndDelFlag(distributionCustomerSimByIdRequest.getInviteeId());
        DistributionCustomerSimVO simVO = new DistributionCustomerSimVO();
        KsBeanUtil.copyPropertiesThird(distributionCustomer, simVO);
        return BaseResponse.success(new DistributionCustomerSimByIdResponse(simVO));
    }

    /**
     * 根据会员编号查询分销员API（未删除）
     *
     * @param distributionCustomerByCustomerIdRequest 单个查询分销员请求参数 {@link DistributionCustomerByCustomerIdRequest}
     * @return 分销员详情 {@link DistributionCustomerByIdResponse}
     * @author lq
     */
    @Override
    public BaseResponse<DistributionCustomerByCustomerIdResponse> getByCustomerId(@RequestBody @Valid DistributionCustomerByCustomerIdRequest distributionCustomerByCustomerIdRequest) {
        DistributionCustomer distributionCustomer =
                distributionCustomerService.getByCustomerIdAndDelFlag(distributionCustomerByCustomerIdRequest.getCustomerId());
        return BaseResponse.success(new DistributionCustomerByCustomerIdResponse(distributionCustomerService.wrapperVo(distributionCustomer)));

    }

    /**
     * 根据会员编号查询分销员API（未删除）
     *
     * @param distributionCustomerByCustomerIdRequest 单个查询分销员请求参数 {@link DistributionCustomerByCustomerIdRequest}
     * @return 分销员详情 {@link DistributionCustomerByIdResponse}
     * @author lq
     */
    @Override
    public BaseResponse<EsDistributionCustomerByCustomerIdResponse> getByCustomerIdShowPhone(@RequestBody @Valid DistributionCustomerByCustomerIdRequest distributionCustomerByCustomerIdRequest) {
        DistributionCustomer distributionCustomer =
                distributionCustomerService.getByCustomerId(distributionCustomerByCustomerIdRequest.getCustomerId());
        return BaseResponse.success(new EsDistributionCustomerByCustomerIdResponse(distributionCustomerService.wrapperEsVo(distributionCustomer)));

    }

    /**
     * 根据会员编号查询分销员
     *
     * @param distributionCustomerByCustomerIdRequest
     * @return 分销员详情 {@link DistributionCustomerByIdResponse}
     * @author lq
     */
    @Override
    public BaseResponse<DistributionCustomerByCustomerIdResponse> getByCustomerIdAndDistributorFlagAndDelFlag(@RequestBody @Valid
                                                                                                                      DistributionCustomerByCustomerIdRequest distributionCustomerByCustomerIdRequest) {
        DistributionCustomer distributionCustomer =
                distributionCustomerService.getByCustomerIdAndDistributorFlagAndDelFlag(distributionCustomerByCustomerIdRequest
                        .getCustomerId(), DefaultFlag.YES);
        DistributionCustomerVO distributionCustomerVO = distributionCustomerService.wrapperVo(distributionCustomer);
        // 分销员等级名称
        if (Objects.nonNull(distributionCustomer) && StringUtils.isNoneBlank(distributionCustomer.getDistributorLevelId
                ())) {
            DistributorLevel distributorLevel = distributorLevelService.findByDistributorLevelId(distributionCustomer.getDistributorLevelId());
            distributionCustomerVO.setDistributorLevelName(distributorLevel.getDistributorLevelName());
        }
        return BaseResponse.success(DistributionCustomerByCustomerIdResponse.builder().distributionCustomerVO(distributionCustomerVO).build());

    }

    @Override
    public BaseResponse<DistributionCustomerByCustomerIdResponse> getByInviteCustomer(@RequestBody @Valid
                                                                                              DistributionCustomerByInviteCustomerIdRequest distributionCustomerByInviteCustomerIdRequest) {
        DistributionCustomer distributionCustomer =
                distributionCustomerService.getByInviteCustomerId(distributionCustomerByInviteCustomerIdRequest
                        .getInviteCustomerId(), DefaultFlag.YES);
        return BaseResponse.success(DistributionCustomerByCustomerIdResponse.builder().distributionCustomerVO(Objects.isNull(distributionCustomer) ? null :
                KsBeanUtil.copyPropertiesThird(distributionCustomer, DistributionCustomerVO.class)).build());

    }

    /**
     * 根据编号查询分销员分销状态
     *
     * @param request
     * @author lq
     */
    @Override
    public BaseResponse<DistributionCustomerEnableByIdResponse> checkEnableByDistributionId(@RequestBody @Valid DistributionCustomerEnableByIdRequest request) {
        boolean distributionEnable = Boolean.FALSE;
        DistributionCustomer distributionCustomer =
                distributionCustomerService.getByDistributionIdAndDelFlag(request.getDistributionId());
        if (Objects.nonNull(distributionCustomer) && Objects.equals(DefaultFlag.YES,
                distributionCustomer.getDistributorFlag()) && Objects.equals(DefaultFlag.NO,
                distributionCustomer.getForbiddenFlag())) {
            //验证分销员状态--有分销员资格，且启用中
            distributionEnable = Boolean.TRUE;
        }
        return BaseResponse.success(
                DistributionCustomerEnableByIdResponse.builder().distributionEnable(distributionEnable).build());
    }

    /**
     * 根据会员编号查询分销员分销状态
     *
     * @param request
     * @author lq
     */
    @Override
    public BaseResponse<DistributionCustomerEnableByCustomerIdResponse> checkEnableByCustomerId(@RequestBody @Valid DistributionCustomerEnableByCustomerIdRequest request) {
        boolean distributionEnable = Boolean.FALSE;
        DistributionCustomer distributionCustomer =
                distributionCustomerService.getByCustomerIdAndDelFlag(request.getCustomerId());
        if (Objects.nonNull(distributionCustomer) && Objects.equals(DefaultFlag.YES,
                distributionCustomer.getDistributorFlag()) && Objects.equals(DefaultFlag.NO,
                distributionCustomer.getForbiddenFlag())) {
            //验证分销员状态--有分销员资格，且启用中
            distributionEnable = Boolean.TRUE;
        }
        return BaseResponse.success(new DistributionCustomerEnableByCustomerIdResponse(distributionEnable,
                Objects.nonNull(distributionCustomer) ? distributionCustomer.getForbiddenReason() : ""));
    }

    /**
     * 根据会员的Id查询邀请人信息——会员端使用
     *
     * @param request 单个查询分销员请求参数 {@link DistributionCustomerSimByIdRequest}
     * @return 分销员简单信息 {@link DistributionCustomerSimByIdResponse}
     * @author baijz
     */
    @Override
    public BaseResponse<DistributionCustomerSimByIdResponse> findDistributionCutomerByCustomerId(@RequestBody @Valid
                                                                                                         DistributionInviteNewByCustomerIdRequest request) {
        InviteNewRecord inviteNewRecord =
                customerDistributionInviteNewService.queryValidRecord(request.getInvitedNewCustomerId());
        if (inviteNewRecord != null) {
            CustomerDetail customerDetail =
                    customerDetailService.findAnyByCustomerId(inviteNewRecord.getRequestCustomerId());
            if (Objects.nonNull(customerDetail)) {
                DistributionCustomerSimVO simVO = KsBeanUtil.convert(customerDetail, DistributionCustomerSimVO.class);
                return BaseResponse.success(new DistributionCustomerSimByIdResponse(simVO));
            }
        }
        return BaseResponse.success(new DistributionCustomerSimByIdResponse(new DistributionCustomerSimVO()));
    }

    @Override
    public BaseResponse<DistributionCustomerByInviteCodeResponse> getByInviteCode(@RequestBody @Valid DistributionCustomerByInviteCodeRequest request) {
        DistributionCustomer distributionCustomer = distributionCustomerService.findByInviteCode(request.getInviteCode());
        if (Objects.isNull(distributionCustomer)) {
            return BaseResponse.success(new DistributionCustomerByInviteCodeResponse());
        }
        DistributionCustomerVO vo = KsBeanUtil.convert(distributionCustomer, DistributionCustomerVO.class);
        return BaseResponse.success(new DistributionCustomerByInviteCodeResponse(vo));
    }

    @Override
    public BaseResponse<DistributionCustomerCheckResponse> checkIsDistributor(@RequestBody @Valid DistributionCustomerCheckRequest request) {
        Boolean result = distributionCustomerService.isDistributor(request);
        return BaseResponse.success(new DistributionCustomerCheckResponse(result));
    }

}

