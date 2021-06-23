package com.wanmi.sbc.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelSaveProvider;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaQueryRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelAddRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelByIdRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelListRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelPageRequest;
import com.wanmi.sbc.customer.api.response.store.StoreCustomerRelaResponse;
import com.wanmi.sbc.customer.api.response.storelevel.StoreLevelByIdResponse;
import com.wanmi.sbc.customer.api.response.storelevel.StoreLevelListResponse;
import com.wanmi.sbc.customer.api.response.storelevel.StoreLevelPageResponse;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;


/**
 * 会员等级
 * Created by CHENLI on 2017/4/17.
 */
@Api(tags = "StoreCustomerLevelController", description = "会员等级 API")
@RestController
@RequestMapping("/storelevel")
@Validated
public class StoreCustomerLevelController {

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @Autowired
    private StoreLevelSaveProvider storeLevelSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private StoreCustomerQueryProvider storeCustomerQueryProvider;

    /**
     * 分页查询会员等级
     *
     * @return
     */
    @ApiOperation(value = "分页查询会员等级")
    @RequestMapping(value = "/levels", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<StoreLevelVO>> levels(@RequestBody StoreLevelPageRequest request) {
        request.setStoreId(commonUtil.getStoreId());
        request.putSort("createTime", SortType.ASC.toValue());
        request.putSort("customerLevelId", SortType.ASC.toValue());
        BaseResponse<StoreLevelPageResponse> storeCustomerLevelPageResponseBaseResponse =
                storeLevelQueryProvider.page(request);
        StoreLevelPageResponse storeLevelPageResponse = storeCustomerLevelPageResponseBaseResponse.getContext();
        if (Objects.nonNull(storeLevelPageResponse)) {
            return BaseResponse.success(storeLevelPageResponse.getStoreLevelVOPage());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询所有会员等级
     *
     * @return
     */
    @ApiOperation(value = "查询所有会员等级")
    @RequestMapping(value = "/levels", method = RequestMethod.GET)
    public BaseResponse<List<StoreLevelVO>> levels() {
        BaseResponse<StoreLevelListResponse> listResponseBaseResponse
                = storeLevelQueryProvider.list(StoreLevelListRequest.builder().storeId(commonUtil.getStoreId()).build());
        StoreLevelListResponse storeLevelListResponse = listResponseBaseResponse.getContext();
        if (Objects.nonNull(storeLevelListResponse)) {
            return BaseResponse.success(storeLevelListResponse.getStoreLevelVOList());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 详情会员等级
     *
     * @param storeLevelId
     * @return
     */
    @ApiOperation(value = "详情会员等级")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeLevelId", value = "会员等级Id",
            required = true)
    @RequestMapping(value = "/level/{storeLevelId}", method = RequestMethod.GET)
    public BaseResponse<StoreLevelVO> info(@PathVariable("storeLevelId") Long storeLevelId) {
        StoreLevelByIdRequest levelByIdRequest = StoreLevelByIdRequest.builder().storeLevelId(storeLevelId).build();
        BaseResponse<StoreLevelByIdResponse> response = storeLevelQueryProvider.getById(levelByIdRequest);
        StoreLevelByIdResponse context = response.getContext();
        if (Objects.nonNull(context)) {
            return BaseResponse.success(context.getStoreLevelVO());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 保存会员等级
     *
     * @param saveRequest
     * @return
     */
    @ApiOperation(value = "保存会员等级")
    @RequestMapping(value = "/level", method = RequestMethod.PUT)
    public BaseResponse save(@Valid @RequestBody StoreLevelAddRequest saveRequest) {
        saveRequest.setStoreId(commonUtil.getStoreId());
        StoreLevelVO storeLevelVO = storeLevelSaveProvider.add(saveRequest).getContext().getStoreLevelVO();

        StoreLevelByIdRequest levelByIdRequest = StoreLevelByIdRequest.builder().storeLevelId(storeLevelVO.getStoreLevelId()).build();
        BaseResponse<StoreLevelByIdResponse> response = storeLevelQueryProvider.getById(levelByIdRequest);
        StoreLevelByIdResponse storeLevelByIdResponse = response.getContext();
        if (nonNull(storeLevelByIdResponse)) {
            StoreLevelVO level = storeLevelByIdResponse.getStoreLevelVO();
            operateLogMQUtil.convertAndSend("客户", "设置客户等级", "设置客户等级：" +
                    level.getLevelName() + "折扣率为" + level.getDiscountRate());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取客户-商家的从属记录，一个客户只会从属于一个商家
     *
     * @return
     */
    @ApiOperation(value = "获取客户-商家的从属记录，一个客户只会从属于一个商家")
    @RequestMapping(value = "/customerBelong/{customerId}", method = RequestMethod.GET)
    public BaseResponse<StoreCustomerRelaResponse> getCustomerBelong(@PathVariable String customerId) {
        StoreCustomerRelaQueryRequest request = new StoreCustomerRelaQueryRequest();
        request.setCustomerId(customerId);
        request.setCompanyInfoId(commonUtil.getCompanyInfoId());
        return storeCustomerQueryProvider.getCustomerBelong(request);
    }
}
