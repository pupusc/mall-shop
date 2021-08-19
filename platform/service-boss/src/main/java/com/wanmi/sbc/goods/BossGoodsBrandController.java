package com.wanmi.sbc.goods;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsBrandProvider;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsBrandQueryProvider;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsBrandDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsBrandPageRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsBrandSaveRequest;
import com.wanmi.sbc.goods.api.provider.brand.ContractBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandProvider;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.ContractBrandListRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandDeleteByIdRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandPageRequest;
import com.wanmi.sbc.goods.api.response.brand.GoodsBrandResponse;
import com.wanmi.sbc.goods.bean.vo.ContractBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandSimpleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: songhanlin
 * @Date: Created In 上午10:05 2017/11/1
 * @Description: 商品品牌Controller
 */
@Slf4j
@Api(description = "商品品牌API", tags = "BossGoodsBrandController")
@RestController("bossGoodsBrandController")
@RequestMapping("/goods")
@Validated
public class BossGoodsBrandController {

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private GoodsBrandProvider goodsBrandProvider;

    @Autowired
    private ContractBrandQueryProvider contractBrandQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EsGoodsBrandQueryProvider esGoodsBrandQueryProvider;

    @Autowired
    private EsGoodsBrandProvider esGoodsBrandProvider;


    /**
     * 分页商品品牌
     *
     * @param queryRequest 商品品牌参数
     * @return 商品详情
     */
    @ApiOperation(value = "分页商品品牌")
    @RequestMapping(value = "/goodsBrands", method = RequestMethod.POST)
    public BaseResponse<Page<GoodsBrandResponse>> page(@RequestBody GoodsBrandPageRequest queryRequest) {
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.putSort("createTime", SortType.DESC.toValue());
        queryRequest.putSort("brandId", SortType.ASC.toValue());

       /* MicroServicePage<GoodsBrandVO> page =
                goodsBrandQueryProvider.page(queryRequest).getContext().getGoodsBrandPage();*/
        //分页查询es
        EsGoodsBrandPageRequest pageRequest = EsGoodsBrandPageRequest.builder().build();
        BeanUtils.copyProperties(queryRequest, pageRequest);
        MicroServicePage<GoodsBrandVO> page = esGoodsBrandQueryProvider.page(pageRequest).getContext().getGoodsBrandPage();

        List<ContractBrandVO> contractBrands;
        if (page.getTotalElements() > 0L) {
            List<Long> brandIds = page.getContent().stream().map(GoodsBrandVO::getBrandId).collect(Collectors.toList());
            ContractBrandListRequest contractBrandQueryRequest = new ContractBrandListRequest();
            contractBrandQueryRequest.setGoodsBrandIds(brandIds);
            contractBrands =
                    contractBrandQueryProvider.list(contractBrandQueryRequest).getContext().getContractBrandVOList();
        } else {
            contractBrands = new ArrayList<>();
        }

        List<GoodsBrandResponse> goodsBrandResponses = new ArrayList<>();
        page.getContent().forEach(info -> {
            GoodsBrandResponse goodsBrandResponse = new GoodsBrandResponse();
            BeanUtils.copyProperties(info, goodsBrandResponse);
            List<Long> storeIds =
                    contractBrands.stream().filter(contractBrand -> info.getBrandId().equals(contractBrand.getGoodsBrand().getBrandId()))
                            .map(ContractBrandVO::getStoreId).collect(Collectors.toList());
            if (!storeIds.isEmpty()) {
//                List<Store> stores = storeService.findList(storeIds);
                List<StoreVO> stores = storeQueryProvider.listNoDeleteStoreByIds(
                        ListNoDeleteStoreByIdsRequest.builder().storeIds(storeIds).build()
                ).getContext().getStoreVOList();
                //过滤出已审核通过的店铺商家信息
                List<String> companyNames =
                        stores.stream().filter(store -> CheckState.CHECKED.equals(store.getAuditState())).map(store -> store.getCompanyInfo().getSupplierName()).distinct().collect(Collectors.toList());
                goodsBrandResponse.setSupplierNames(StringUtils.join(companyNames.toArray(), ","));
            }
            goodsBrandResponses.add(goodsBrandResponse);
        });
        return BaseResponse.success(new PageImpl<>(goodsBrandResponses, queryRequest.getPageable(),
                page.getTotalElements()));
    }


    /**
     * 获取商品品牌详情信息
     *
     * @param brandId 商品品牌编号
     * @return 商品详情
     */
    @ApiOperation(value = "获取商品品牌详情信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long",
            name = "brandId", value = "商品品牌编号", required = true)
    @RequestMapping(value = "/goodsBrand/{brandId}", method = RequestMethod.GET)
    public BaseResponse<GoodsBrandVO> list(@PathVariable Long brandId) {
        if (brandId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return BaseResponse.success(goodsBrandQueryProvider.getById(GoodsBrandByIdRequest.builder().brandId(brandId)
                .build()).getContext());
    }

    /**
     * 删除商品品牌
     */
    @ApiOperation(value = "删除商品品牌")
    @ApiImplicitParam(paramType = "path", dataType = "Long",
            name = "brandId", value = "商品品牌编号", required = true)
    @RequestMapping(value = "/goodsBrand/{brandId}", method = RequestMethod.DELETE)
    public BaseResponse delete(@PathVariable Long brandId) {
        long startTime = System.currentTimeMillis();
        log.info("开始时间：{}",startTime);
        if (Objects.isNull(brandId)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        GoodsBrandVO goodsBrand = goodsBrandProvider.delete(
                GoodsBrandDeleteByIdRequest.builder().brandId(brandId).build()
        ).getContext();
        esGoodsInfoElasticProvider.delBrandIds(EsBrandDeleteByIdsRequest.builder().deleteIds(
                Collections.singletonList(goodsBrand.getBrandId())).storeId(null).build());

        log.info("结束时间：{}",System.currentTimeMillis()-startTime);
        /*
         *  查询条件
         */
        EsGoodsBrandPageRequest pageRequest = EsGoodsBrandPageRequest.builder()
                .brandIds(Collections.singletonList(brandId))
                .delFlag(NumberUtils.INTEGER_ZERO)
                .build();

        MicroServicePage<GoodsBrandVO> page = esGoodsBrandQueryProvider.page(pageRequest).getContext().getGoodsBrandPage();
        List<GoodsBrandVO> newGoodsBrandVOList = page.getContent();
        if (CollectionUtils.isNotEmpty(newGoodsBrandVOList)) {
            List<GoodsBrandVO> delGoodsBrandVOList = newGoodsBrandVOList.stream().peek(entity -> entity.setDelFlag(DeleteFlag.YES)).collect(Collectors.toList());
            EsGoodsBrandSaveRequest request = EsGoodsBrandSaveRequest.builder().goodsBrandVOList(delGoodsBrandVOList).build();
            esGoodsBrandProvider.addGoodsBrandList(request);
        }

        //操作日志记录
        operateLogMQUtil.convertAndSend("商品", "删除品牌", "删除品牌：" + goodsBrand.getBrandName());
        return BaseResponse.SUCCESSFUL();
    }


    @ApiOperation(value = "分页商品品牌")
    @RequestMapping(value = "/goodsBrands/page", method = RequestMethod.POST)
    public BaseResponse<List<GoodsBrandSimpleVO>> brandPageList(@RequestBody GoodsBrandPageRequest queryRequest) {
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        MicroServicePage<GoodsBrandVO> page =
                goodsBrandQueryProvider.page(queryRequest).getContext().getGoodsBrandPage();
        List<GoodsBrandSimpleVO> brandList =null;
        if (page.getTotalElements() > 0L) {
            brandList = KsBeanUtil.convert(page.getContent(), GoodsBrandSimpleVO.class);
        }
        return BaseResponse.success(ListUtils.emptyIfNull(brandList));

    }

}
