package com.wanmi.sbc.distribution;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerSaveProvider;
import com.wanmi.sbc.customer.api.request.distribution.*;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerByIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.EsDistributionCustomerAddResponse;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerShowPhoneVO;
import com.wanmi.sbc.elastic.api.provider.customer.EsDistributionCustomerProvider;
import com.wanmi.sbc.elastic.api.provider.customer.EsDistributionCustomerQueryProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomeffBatchModifyRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerPageRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerAddRequest;
import com.wanmi.sbc.elastic.api.response.customer.EsDistributionCustomerListResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分销员API
 */
@Api(tags = "DistributionCustomerController", description = "分销员API")
@RestController
@RequestMapping("/distribution-customer")
public class DistributionCustomerController {


    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

    @Autowired
    private DistributionCustomerSaveProvider distributionCustomerSaveProvider;


    @Autowired
    private EsDistributionCustomerProvider esDistributionCustomerProvider;

    @Autowired
    private EsDistributionCustomerQueryProvider esDistributionCustomerQueryProvider;


    @Autowired
    private CommonUtil commonUtil;


    /**
     * 分页查询分销员
     *
     * @return 会员信息
     */
    /*@ApiOperation(value = "分页查询分销员")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> page(@RequestBody EsDistributionCustomerPageRequest queryRequest) {
        queryRequest.setDistributorFlag(DefaultFlag.YES);
        queryRequest.putSort("createTime", SortType.DESC.toValue());
        return ResponseEntity.ok(distributionCustomerQueryProvider.page(queryRequest));
    }*/
    @ApiOperation(value = "分页查询分销员")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> page(@RequestBody EsDistributionCustomerPageRequest queryRequest) {
        queryRequest.setDistributorFlag(DefaultFlag.YES);
        queryRequest.putSort("createTime", SortType.DESC.toValue());
        return ResponseEntity.ok(esDistributionCustomerQueryProvider.page(queryRequest));
    }

    /**
     * 新增分销员
     *
     * @param distributionCustomerAddForBossRequest DistributionCustomerAddForBossRequest
     * @return employee
     */
    @ApiOperation(value = "新增分销员")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> addForBoss(@Valid @RequestBody DistributionCustomerAddForBossRequest distributionCustomerAddForBossRequest) {
        distributionCustomerAddForBossRequest.setCustomerType(CustomerType.PLATFORM);
        distributionCustomerAddForBossRequest.setEmployeeId(commonUtil.getOperatorId());
        BaseResponse<EsDistributionCustomerAddResponse> response = distributionCustomerSaveProvider.addForBoss(distributionCustomerAddForBossRequest);
        DistributionCustomerShowPhoneVO distributionCustomerVO = response.getContext().getEsDistributionCustomerVO();
        EsDistributionCustomerAddRequest request = EsDistributionCustomerAddRequest.builder().list(Lists.newArrayList(distributionCustomerVO)).build();
        //同步入到es
        esDistributionCustomerProvider.add(request);
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }


    /**
     * 根据分销员ID查询分销员信息
     *
     * @param distributionId 分销员ID
     * @return
     */
    @ApiOperation(value = "根据分销员ID查询分销员信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "distributionId", value = "分销员ID", required = true)
    @RequestMapping(value = "/{distributionId}", method = RequestMethod.GET)
    public BaseResponse<DistributionCustomerVO> getInfoById(@PathVariable String distributionId) {
        if (StringUtils.isEmpty(distributionId)) {
            throw new SbcRuntimeException("K-000009");
        }
        DistributionCustomerByIdResponse distributionCustomer = distributionCustomerQueryProvider.getById(new DistributionCustomerByIdRequest(distributionId)).getContext();
        return BaseResponse.success(distributionCustomer.getDistributionCustomerVO());
    }

    /**
     * 根据会员ID查询分销员信息
     *
     * @param customerId 会员ID
     * @return
     */
    @ApiOperation(value = "根据会员ID查询分销员信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "会员id", required = true)
    @RequestMapping(value = "/customer-id/{customerId}", method = RequestMethod.GET)
    public BaseResponse<DistributionCustomerVO> getInfoByCustomerId(@PathVariable String customerId) {
        if (StringUtils.isEmpty(customerId)) {
            throw new SbcRuntimeException("K-000009");
        }
        DistributionCustomerByCustomerIdResponse distributionCustomer = distributionCustomerQueryProvider.getByCustomerId(new DistributionCustomerByCustomerIdRequest(customerId)).getContext();
        return BaseResponse.success(distributionCustomer.getDistributionCustomerVO());
    }


    /**
     * 批量启用/禁用分销员
     *
     * @param queryRequest
     * @return
     */
    @ApiOperation(value = "批量启用/禁用分销员")
    @RequestMapping(value = "/forbidden-flag", method = RequestMethod.POST)
    public BaseResponse updateForbiddenFlag(@RequestBody DistributionCustomeffBatchModifyRequest queryRequest) {
        if (null == queryRequest.getForbiddenFlag() || CollectionUtils.isEmpty(queryRequest.getDistributionIds())) {
            throw new SbcRuntimeException("K-000009");
        }
        BaseResponse baseResponse = distributionCustomerSaveProvider.modifyForbiddenFlagById(queryRequest);

        //同步es
        EsDistributionCustomeffBatchModifyRequest request = new EsDistributionCustomeffBatchModifyRequest(queryRequest.getDistributionIds());
        //同步查询es分销员信息
        BaseResponse<EsDistributionCustomerListResponse> response = esDistributionCustomerQueryProvider.listByIds(request);

        List<DistributionCustomerShowPhoneVO> newList = response.getContext().getList();
        if (CollectionUtils.isNotEmpty(newList)) {
            List<DistributionCustomerShowPhoneVO> list = response.getContext().getList().stream()
                    .peek(entity -> {
                        entity.setForbiddenFlag(queryRequest.getForbiddenFlag());
                        entity.setForbiddenReason(queryRequest.getForbiddenReason());
                    }).collect(Collectors.toList());

            EsDistributionCustomerAddRequest req = EsDistributionCustomerAddRequest.builder().list(list).build();
            //数据覆盖
            esDistributionCustomerProvider.add(req);
        }
        return baseResponse;
    }
}
