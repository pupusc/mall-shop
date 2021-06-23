package com.wanmi.sbc.appointmentsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.store.ListStoreByNameRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelListRequest;
import com.wanmi.sbc.customer.api.response.store.StoreByIdResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleProvider;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.appointmentsalegoods.AppointmentSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleByIdRequest;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSalePageRequest;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdsRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleModifyResponse;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSalePageResponse;
import com.wanmi.sbc.goods.api.response.appointmentsalegoods.AppointmentSaleGoodsDetailResponse;
import com.wanmi.sbc.goods.bean.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@Api(description = "BOSS预约抢购管理API", tags = "AppointmentSaleController")
@RestController
@RequestMapping(value = "/appointmentsale")
public class AppointmentSaleController {

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private AppointmentSaleProvider appointmentSaleProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;


    @Autowired
    private AppointmentSaleGoodsQueryProvider appointmentSaleGoodsQueryProvider;

    @Autowired
    private GoodsInfoSpecDetailRelQueryProvider goodsInfoSpecDetailRelQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @ApiOperation(value = "分页查询预约抢购")
    @PostMapping("/page")
    public BaseResponse<AppointmentSalePageResponse> getPage(@RequestBody @Valid AppointmentSalePageRequest request) {
        request.setDelFlag(DeleteFlag.NO);
        request.putSort("createTime", "desc");
        request.setStatus(request.getQueryTab());
        request.setPlatform(Platform.BOSS);
        if (StringUtils.isNotEmpty(request.getStoreName())) {
            List<StoreVO> storeVOList = storeQueryProvider.listByName(ListStoreByNameRequest.builder().storeName(request.getStoreName()).build()).getContext()
                    .getStoreVOList();
            if (CollectionUtils.isEmpty(storeVOList)) {
                return BaseResponse.success(AppointmentSalePageResponse.builder().build());
            }
            request.setStoreIds(storeVOList.stream().map(StoreVO::getStoreId).collect(Collectors.toList()));
        }
        return appointmentSaleQueryProvider.pageNew(request);
//        AppointmentSalePageResponse response = appointmentSaleQueryProvider.page(request).getContext();
//        if (Objects.isNull(response) || Objects.isNull(response.getAppointmentSaleVOPage()) || CollectionUtils.isEmpty(response.getAppointmentSaleVOPage().getContent())) {
//            return BaseResponse.success(response);
//        }
//        Map<Long, StoreVO> storeVOMap = storeQueryProvider.listByIds(ListStoreByIdsRequest.builder().storeIds(
//                response.getAppointmentSaleVOPage().getContent().stream().map(AppointmentSaleVO::getStoreId).collect(Collectors.toList())
//        ).build()).getContext().getStoreVOList().stream().collect(Collectors.toMap(StoreVO::getStoreId, v -> v));
//
//        //平台客户等级
//        List<CustomerLevelVO> customerLevelVOList = customerLevelQueryProvider.listAllCustomerLevel().getContext().getCustomerLevelVOList();
//
//        response.getAppointmentSaleVOPage().getContent().forEach
//                (s -> {
//                    StoreVO storeVO = storeVOMap.get(s.getStoreId());
//                    if (storeVO == null) {
//                        s.setLevelName("");
//                    } else {
//                        s.setStoreName(storeVO.getStoreName());
//                        //填充客户等级名称
//                        BoolFlag companyType = storeVO.getCompanyType();
//                        List<String> levels = Arrays.asList(s.getJoinLevel().split(","));
//                        String levelName = "";
//                        if (BoolFlag.NO.equals(companyType)) {
//                            //平台
//                            if (CollectionUtils.isNotEmpty(customerLevelVOList) && CollectionUtils.isNotEmpty(levels)) {
//                                levelName = levels.stream().flatMap(level -> customerLevelVOList.stream()
//                                        .filter(customerLevelVO -> level.equals(customerLevelVO.getCustomerLevelId().toString()))
//                                        .map(v -> v.getCustomerLevelName())).collect(Collectors.joining(","));
//                            }
//                        } else {
//                            //商家
//                            StoreLevelListRequest storeLevelListRequest = StoreLevelListRequest.builder().storeId(s.getStoreId()).build();
//                            List<StoreLevelVO> storeLevelVOList = storeLevelQueryProvider
//                                    .listAllStoreLevelByStoreId(storeLevelListRequest)
//                                    .getContext().getStoreLevelVOList();
//                            if (CollectionUtils.isNotEmpty(storeLevelVOList) && CollectionUtils.isNotEmpty(levels)) {
//                                levelName = levels.stream().flatMap(level -> storeLevelVOList.stream()
//                                        .filter(storeLevelVO -> level.equals(storeLevelVO.getStoreLevelId().toString()))
//                                        .map(v -> v.getLevelName())).collect(Collectors.joining(","));
//                            }
//                        }
//                        s.setLevelName(levelName);
//                    }
//                });
//
//
//        return BaseResponse.success(response);
    }


    @ApiOperation(value = "根据id查询预约抢购商品详情")
    @GetMapping("/goods/{id}")
    public BaseResponse<AppointmentSaleGoodsDetailResponse> getAppointmentSaleGoodsDetail(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<AppointmentSaleGoodsVO> saleGoodsVOList = getAppointmentSaleGoodsInfo(id);

        return BaseResponse.success(AppointmentSaleGoodsDetailResponse.builder().appointmentSaleGoodsVOList(saleGoodsVOList).build());
    }

    @ApiOperation(value = "根据id查询预约抢购详情(编辑)")
    @GetMapping("/{id}")
    public BaseResponse<AppointmentSaleModifyResponse> getAppointmentSaleDetail(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        AppointmentSaleVO appointmentSaleVO = appointmentSaleQueryProvider.getById(AppointmentSaleByIdRequest.builder().id(id).build()).getContext().getAppointmentSaleVO();

        if (Objects.isNull(appointmentSaleVO)) {
            return BaseResponse.success(AppointmentSaleModifyResponse.builder().build());
        }

        List<AppointmentSaleGoodsVO> saleGoodsVOList = getAppointmentSaleGoodsInfo(id);

        appointmentSaleVO.setAppointmentSaleGoods(saleGoodsVOList);

        StoreByIdResponse store =
                storeQueryProvider.getById(StoreByIdRequest.builder().storeId(appointmentSaleVO.getStoreId()).build()).getContext();
        if (Objects.nonNull(store) && Objects.nonNull(store.getStoreVO())) {
            appointmentSaleVO.setStoreName(store.getStoreVO().getStoreName());

            BoolFlag companyType = store.getStoreVO().getCompanyType();
            List<String> levels = Arrays.asList(appointmentSaleVO.getJoinLevel().split(","));
            String levelName = "";
            if (BoolFlag.NO.equals(companyType)) {
                //平台客户等级
                List<CustomerLevelVO> customerLevelVOList =
                        customerLevelQueryProvider.listAllCustomerLevel().getContext().getCustomerLevelVOList();
                //平台
                if (CollectionUtils.isNotEmpty(customerLevelVOList) && CollectionUtils.isNotEmpty(levels)) {
                    levelName = levels.stream().flatMap(level -> customerLevelVOList.stream()
                            .filter(customerLevelVO -> level.equals(customerLevelVO.getCustomerLevelId().toString()))
                            .map(v -> v.getCustomerLevelName())).collect(Collectors.joining(","));
                }
            } else {
                //商家
                StoreLevelListRequest storeLevelListRequest =
                        StoreLevelListRequest.builder().storeId(appointmentSaleVO.getStoreId()).build();
                List<StoreLevelVO> storeLevelVOList = storeLevelQueryProvider
                        .listAllStoreLevelByStoreId(storeLevelListRequest)
                        .getContext().getStoreLevelVOList();
                if (CollectionUtils.isNotEmpty(storeLevelVOList) && CollectionUtils.isNotEmpty(levels)) {
                    levelName = levels.stream().flatMap(level -> storeLevelVOList.stream()
                            .filter(storeLevelVO -> level.equals(storeLevelVO.getStoreLevelId().toString()))
                            .map(v -> v.getLevelName())).collect(Collectors.joining(","));
                }
            }
            appointmentSaleVO.setLevelName(levelName);
        }
        return BaseResponse.success(AppointmentSaleModifyResponse.builder().appointmentSaleVO(appointmentSaleVO).build());
    }


    /**
     * 获取活动商品详情
     *
     * @param id
     * @return
     */
    private List<AppointmentSaleGoodsVO> getAppointmentSaleGoodsInfo(Long id) {
        Map<String, String> goodsInfoSpecDetailMap = new HashMap<>();
        List<AppointmentSaleGoodsVO> saleGoodsVOList = appointmentSaleGoodsQueryProvider.list(AppointmentSaleGoodsListRequest.builder().
                appointmentSaleId(id).build()).getContext().getAppointmentSaleGoodsVOList();
        if (CollectionUtils.isEmpty(saleGoodsVOList)) {
            return saleGoodsVOList;
        }
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().
                goodsInfoIds(saleGoodsVOList.stream().map(AppointmentSaleGoodsVO::getGoodsInfoId).collect(Collectors.toList())).build()).getContext().getGoodsInfos();

        Map<String, GoodsInfoVO> goodsInfoVOMap = goodsInfos.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, m -> m));

        List<String> skuIds = goodsInfos.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());

        goodsInfoSpecDetailMap.putAll(goodsInfoSpecDetailRelQueryProvider.listBySkuIds(new GoodsInfoSpecDetailRelBySkuIdsRequest(skuIds))
                .getContext().getGoodsInfoSpecDetailRelVOList().stream()
                .filter(v -> StringUtils.isNotBlank(v.getDetailName()))
                .collect(Collectors.toMap(GoodsInfoSpecDetailRelVO::getGoodsInfoId, GoodsInfoSpecDetailRelVO::getDetailName, (a, b) -> a.concat(" ").concat(b))));


        Map<Long, GoodsBrandVO> goodsBrandVOMap = goodsBrandQueryProvider.listByIds(GoodsBrandByIdsRequest.builder().brandIds(goodsInfos.stream().
                map(GoodsInfoVO::getBrandId).collect(Collectors.toList())).build()).getContext().getGoodsBrandVOList()
                .stream().collect(Collectors.toMap(GoodsBrandVO::getBrandId, m -> m));


        Map<Long, GoodsCateVO> goodsCateVOMap = goodsCateQueryProvider.getByIds(new GoodsCateByIdsRequest(goodsInfos.stream().map(GoodsInfoVO::getCateId).
                collect(Collectors.toList()))).getContext().getGoodsCateVOList().stream().collect(Collectors.toMap(GoodsCateVO::getCateId, m -> m));

        saleGoodsVOList.forEach(saleGood -> {
            if (goodsInfoVOMap.containsKey(saleGood.getGoodsInfoId())) {
                GoodsInfoVO goodsInfoVO = goodsInfoVOMap.get(saleGood.getGoodsInfoId());
                if (Objects.nonNull(goodsInfoVO.getBrandId()) && goodsBrandVOMap.containsKey(goodsInfoVO.getBrandId())) {
                    goodsInfoVO.setBrandName(goodsBrandVOMap.get(goodsInfoVO.getBrandId()).getBrandName());
                }
                if (goodsCateVOMap.containsKey(goodsInfoVO.getCateId())) {
                    goodsInfoVO.setCateName(goodsCateVOMap.get(goodsInfoVO.getCateId()).getCateName());
                }
                //填充规格值
                goodsInfoVO.setSpecText(goodsInfoSpecDetailMap.get(goodsInfoVO.getGoodsInfoId()));
                saleGood.setGoodsInfoVO(goodsInfoVO);
            }
        });
        return saleGoodsVOList;
    }

}
