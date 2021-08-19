package com.wanmi.sbc.bookingsale;

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
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.bookingsalegoods.BookingSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleByIdRequest;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSalePageRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdsRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSaleByIdResponse;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSalePageResponse;
import com.wanmi.sbc.goods.api.response.bookingsalegoods.BookingSaleGoodsListResponse;
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


@Api(description = "Boss预售信息管理API", tags = "BookingSaleBossController")
@RestController
@RequestMapping(value = "/booking/sale")
public class BookingSaleBossController {

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    @Autowired
    private BookingSaleGoodsQueryProvider bookingSaleGoodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private GoodsInfoSpecDetailRelQueryProvider goodsInfoSpecDetailRelQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @ApiOperation(value = "分页查询预售信息")
    @PostMapping("/page")
    public BaseResponse<BookingSalePageResponse> getPage(@RequestBody @Valid BookingSalePageRequest request) {
        request.setDelFlag(DeleteFlag.NO);
        request.putSort("createTime", "desc");
        request.setPlatform(Platform.BOSS);
        if (StringUtils.isNotEmpty(request.getStoreName())) {
            List<StoreVO> storeVOList = storeQueryProvider.listByName(ListStoreByNameRequest.builder().storeName(request.getStoreName()).build()).getContext()
                    .getStoreVOList();
            if (CollectionUtils.isEmpty(storeVOList)) {
                return BaseResponse.success(BookingSalePageResponse.builder().build());
            }
            request.setStoreIds(storeVOList.stream().map(StoreVO::getStoreId).collect(Collectors.toList()));
        }
        return bookingSaleQueryProvider.pageNew(request);
//        if (Objects.isNull(response) || Objects.isNull(response.getBookingSaleVOPage()) || CollectionUtils.isEmpty(response.getBookingSaleVOPage().getContent())) {
//            return BaseResponse.success(response);
//        }
//        Map<Long, StoreVO> storeVOMap = storeQueryProvider.listByIds(ListStoreByIdsRequest.builder().storeIds(
//                response.getBookingSaleVOPage().getContent().stream().map(s -> s.getStoreId()).collect(Collectors.toList())
//        ).build()).getContext().getStoreVOList().stream().collect(Collectors.toMap(StoreVO::getStoreId, v -> v));
//
//        //平台客户等级
//        List<CustomerLevelVO> customerLevelVOList = customerLevelQueryProvider.listAllCustomerLevel().getContext().getCustomerLevelVOList();
//        response.getBookingSaleVOPage().getContent().forEach(s -> {
//            StoreVO storeVO = storeVOMap.get(s.getStoreId());
//            if (storeVO == null) {
//                s.setLevelName("");
//            } else {
//                s.setStoreName(storeVO.getStoreName());
//                //填充客户等级名称
//                BoolFlag companyType = storeVO.getCompanyType();
//                List<String> levels = Arrays.asList(s.getJoinLevel().split(","));
//                String levelName = "";
//                if (BoolFlag.NO.equals(companyType)) {
//                    //平台
//                    if (CollectionUtils.isNotEmpty(customerLevelVOList) && CollectionUtils.isNotEmpty(levels)) {
//                        levelName = levels.stream().flatMap(level -> customerLevelVOList.stream()
//                                .filter(customerLevelVO -> level.equals(customerLevelVO.getCustomerLevelId().toString()))
//                                .map(v -> v.getCustomerLevelName())).collect(Collectors.joining(","));
//                    }
//                } else {
//                    //商家
//                    StoreLevelListRequest storeLevelListRequest = StoreLevelListRequest.builder().storeId(s.getStoreId()).build();
//                    List<StoreLevelVO> storeLevelVOList = storeLevelQueryProvider
//                            .listAllStoreLevelByStoreId(storeLevelListRequest)
//                            .getContext().getStoreLevelVOList();
//                    if (CollectionUtils.isNotEmpty(storeLevelVOList) && CollectionUtils.isNotEmpty(levels)) {
//                        levelName = levels.stream().flatMap(level -> storeLevelVOList.stream()
//                                .filter(storeLevelVO -> level.equals(storeLevelVO.getStoreLevelId().toString()))
//                                .map(v -> v.getLevelName())).collect(Collectors.joining(","));
//                    }
//                }
//                s.setLevelName(levelName);
//            }
//        });
//        return BaseResponse.success(response);
    }


    @ApiOperation(value = "根据id查询预售详情信息(编辑)")
    @GetMapping("/{id}")
    public BaseResponse<BookingSaleByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        BookingSaleVO bookingSaleVO = bookingSaleQueryProvider.getOne(BookingSaleByIdRequest.builder().id(id).build()).getContext().getBookingSaleVO();

        List<BookingSaleGoodsVO> bookingSaleGoodsList = getBookingSaleGoodsInfo(id);

        bookingSaleVO.setBookingSaleGoodsList(bookingSaleGoodsList);
        StoreByIdResponse store  =
                storeQueryProvider.getById(StoreByIdRequest.builder().storeId(bookingSaleVO.getStoreId()).build()).getContext();
        if(Objects.nonNull(store) && Objects.nonNull(store.getStoreVO())){
            bookingSaleVO.setStoreName(store.getStoreVO().getStoreName());

            BoolFlag companyType = store.getStoreVO().getCompanyType();
            List<String> levels = Arrays.asList(bookingSaleVO.getJoinLevel().split(","));
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
                        StoreLevelListRequest.builder().storeId(bookingSaleVO.getStoreId()).build();
                List<StoreLevelVO> storeLevelVOList = storeLevelQueryProvider
                        .listAllStoreLevelByStoreId(storeLevelListRequest)
                        .getContext().getStoreLevelVOList();
                if (CollectionUtils.isNotEmpty(storeLevelVOList) && CollectionUtils.isNotEmpty(levels)) {
                    levelName = levels.stream().flatMap(level -> storeLevelVOList.stream()
                            .filter(storeLevelVO -> level.equals(storeLevelVO.getStoreLevelId().toString()))
                            .map(v -> v.getLevelName())).collect(Collectors.joining(","));
                }
            }
            bookingSaleVO.setLevelName(levelName);
        }
        return BaseResponse.success(BookingSaleByIdResponse.builder().bookingSaleVO(bookingSaleVO).build());
    }


    @ApiOperation(value = "根据id查询预售详情信息")
    @GetMapping("/detail/{id}")
    public BaseResponse<BookingSaleGoodsListResponse> getBookingSaleDetail(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return BaseResponse.success(BookingSaleGoodsListResponse.builder().bookingSaleGoodsVOList(getBookingSaleGoodsInfo(id)).build());
    }


    /**
     * 获取活动商品详情
     *
     * @param id
     * @return
     */
    private List<BookingSaleGoodsVO> getBookingSaleGoodsInfo(Long id) {
        Map<String, String> goodsInfoSpecDetailMap = new HashMap<>();
        List<BookingSaleGoodsVO> bookingSaleGoodsVOList = bookingSaleGoodsQueryProvider.list(BookingSaleGoodsListRequest.builder().
                bookingSaleId(id).build()).getContext().getBookingSaleGoodsVOList();
        if (CollectionUtils.isEmpty(bookingSaleGoodsVOList)) {
            return bookingSaleGoodsVOList;
        }
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().
                goodsInfoIds(bookingSaleGoodsVOList.stream().map(BookingSaleGoodsVO::getGoodsInfoId).collect(Collectors.toList())).build()).getContext().getGoodsInfos();

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

        bookingSaleGoodsVOList.forEach(saleGood -> {
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
        return bookingSaleGoodsVOList;
    }
}
