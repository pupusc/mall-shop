package com.wanmi.sbc.goods.provider.impl.distributor.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.distributor.goods.DistributorGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoListByCustomerIdAndGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoListByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoPageByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoVerifyRequest;
import com.wanmi.sbc.goods.api.response.distributor.goods.*;
import com.wanmi.sbc.goods.bean.vo.DistributorGoodsInfoVO;
import com.wanmi.sbc.goods.distributor.goods.model.root.DistributorGoodsInfo;
import com.wanmi.sbc.goods.distributor.goods.service.DistributorGoodsInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分销员商品控制层
 *
 * @author: Geek Wang
 * @createDate: 2019/3/1 11:15
 * @version: 1.0
 */
@RestController
@Validated
public class DistributorGoodsInfoQueryController implements DistributorGoodsInfoQueryProvider {

    @Autowired
    private DistributorGoodsInfoService distributorGoodsInfoService;

    /**
     * 根据分销员的会员ID查询分销员商品
     *
     * @param request {@link DistributorGoodsInfoListByCustomerIdRequest}
     * @return {@link DistributorGoodsInfoListByCustomerIdResponse}
     */
    @Override
    public BaseResponse<DistributorGoodsInfoListByCustomerIdResponse> listByCustomerId(@RequestBody @Valid DistributorGoodsInfoListByCustomerIdRequest request) {

        List<DistributorGoodsInfo> distributorGoodsInfoList;
        if (Objects.nonNull(request.getStoreId())) {
            distributorGoodsInfoList = distributorGoodsInfoService.findByCustomerIdAndStoreId(request.getCustomerId()
                    , request.getStoreId());
        } else {
            distributorGoodsInfoList = distributorGoodsInfoService.findByCustomerId(request.getCustomerId());
        }


        List<DistributorGoodsInfoVO> distributorGoodsInfoVOList =
                distributorGoodsInfoList.stream().map(distributorGoodsInfo -> {
            DistributorGoodsInfoVO distributorGoodsInfoVO = new DistributorGoodsInfoVO();
            KsBeanUtil.copyPropertiesThird(distributorGoodsInfo, distributorGoodsInfoVO);
            return distributorGoodsInfoVO;
        }).collect(Collectors.toList());
        return BaseResponse.success(new DistributorGoodsInfoListByCustomerIdResponse(distributorGoodsInfoVOList));
    }

    /**
     * 根据分销员，过滤非精选的单品
     *
     * @param request {@link DistributorGoodsInfoVerifyRequest}
     * @return {@link DistributorGoodsInfoVerifyResponse}
     */
    @Override
    public BaseResponse<DistributorGoodsInfoVerifyResponse> verifyDistributorGoodsInfo(
            @RequestBody @Valid DistributorGoodsInfoVerifyRequest request) {
        List<String> invalidIds = distributorGoodsInfoService.verifyDistributorGoodsInfo(request);
        return BaseResponse.success(new DistributorGoodsInfoVerifyResponse(invalidIds));
    }

    /**
     * 根据分销员的会员ID和SPU编号查询分销员商品
     *
     * @param request {@link DistributorGoodsInfoListByCustomerIdAndGoodsIdRequest}
     * @return {@link DistributorGoodsInfoListByCustomerIdAndGoodsIdResponse}
     */
    @Override
    public BaseResponse<DistributorGoodsInfoListByCustomerIdAndGoodsIdResponse> listByCustomerIdAndGoodsId(@RequestBody @Valid DistributorGoodsInfoListByCustomerIdAndGoodsIdRequest request) {
        List<DistributorGoodsInfo> distributorGoodsInfoList =
                distributorGoodsInfoService.findByCustomerIdAndGoodsId(request.getDistributorId(),
                        request.getGoodsId());
        List<DistributorGoodsInfoVO> distributorGoodsInfoVOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(distributorGoodsInfoList)) {
            distributorGoodsInfoVOList =
                    distributorGoodsInfoList.stream().map(distributorGoodsInfo -> {
                        DistributorGoodsInfoVO distributorGoodsInfoVO = new DistributorGoodsInfoVO();
                        KsBeanUtil.copyPropertiesThird(distributorGoodsInfo, distributorGoodsInfoVO);
                        return distributorGoodsInfoVO;
                    }).collect(Collectors.toList());
        }
        return BaseResponse.success(new DistributorGoodsInfoListByCustomerIdAndGoodsIdResponse(distributorGoodsInfoVOList));
    }

    /**
     * 根据分销员的会员ID查询分销员商品
     *
     * @param request {@link DistributorGoodsInfoPageByCustomerIdRequest}
     * @return {@link DistributorGoodsInfoPageByCustomerIdResponse}
     */
    @Override
    public BaseResponse<DistributorGoodsInfoPageByCustomerIdResponse> pageByCustomerId(@RequestBody @Valid DistributorGoodsInfoPageByCustomerIdRequest request) {
        Page<DistributorGoodsInfo> page =
                distributorGoodsInfoService.findByCustomerIdAndStatusOrderBySequence(request.getCustomerId(),
                        NumberUtils.INTEGER_ZERO, request.getPageable());
        MicroServicePage<DistributorGoodsInfoVO> microServicePage = KsBeanUtil.convertPage(page,
                DistributorGoodsInfoVO.class);
        return BaseResponse.success(new DistributorGoodsInfoPageByCustomerIdResponse(microServicePage));
    }

    /**
     * 查询分销员商品表-店铺ID集合数据
     *
     * @return
     */
    @Override
    public BaseResponse<DistributorGoodsInfoListAllStoreIdResponse> findAllStoreId() {
        return BaseResponse.success(new DistributorGoodsInfoListAllStoreIdResponse(distributorGoodsInfoService.findAllStoreId()));
    }

    /**
     * 查询分销员商品表-销量排序前10
     *
     * @return
     */
    @Override
    public BaseResponse<DistributorGoodsIdsResp> salesNumDescLimit10() {
        return BaseResponse.success(new DistributorGoodsIdsResp(distributorGoodsInfoService.salesNumDescLimit10()));
    }
}
