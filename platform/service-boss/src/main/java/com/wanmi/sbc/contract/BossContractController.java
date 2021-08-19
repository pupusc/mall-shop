package com.wanmi.sbc.contract;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsBrandDeleteByIdsRequest;
import com.wanmi.sbc.goods.api.provider.brand.ContractBrandProvider;
import com.wanmi.sbc.goods.api.provider.brand.ContractBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.ContractCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.contract.ContractProvider;
import com.wanmi.sbc.goods.api.request.brand.ContractBrandListRequest;
import com.wanmi.sbc.goods.api.request.brand.ContractBrandListVerifyByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.brand.ContractBrandTransferByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.cate.ContractCateDelVerifyRequest;
import com.wanmi.sbc.goods.api.request.cate.ContractCateListCateByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.cate.ContractCateListRequest;
import com.wanmi.sbc.goods.api.request.contract.ContractSaveRequest;
import com.wanmi.sbc.goods.bean.vo.ContractBrandVO;
import com.wanmi.sbc.goods.bean.vo.ContractCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商家签约信息(分类，品牌)
 * Created by sunkun on 2017/11/20.
 */
@RestController
@RequestMapping("/contract")
@Api(tags = "BossContractController", description = "S2B 平台端-商家签约信息(分类，品牌)管理API")
public class BossContractController {

    @Autowired
    private ContractBrandQueryProvider contractBrandQueryProvider;

    @Autowired
    private ContractBrandProvider contractBrandProvider;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;


    @Autowired
    private ContractCateQueryProvider contractCateQueryProvider;

    @Resource
    private CommonUtil commonUtil;

    /**
     * 签约信息新增修改删除(签约分类，签约品牌)
     *
     * @return
     */
    @ApiOperation(value = "签约信息新增修改删除(签约分类，签约品牌)")
    @RequestMapping(value = "/renewal", method = RequestMethod.POST)
    public BaseResponse renewalAll(@Valid @RequestBody ContractSaveRequest contractRequest) {
        if (contractRequest.getStoreId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        List<Long> ids =    contractProvider.save(contractRequest).getContext().getBrandIds();
        //取消签约品牌的时候更新es
        if (CollectionUtils.isNotEmpty(ids) && CollectionUtils.isNotEmpty(contractRequest.getDelBrandIds())) {
            esGoodsInfoElasticProvider.delBrandIds(EsBrandDeleteByIdsRequest.builder()
                            .deleteIds(ids).storeId(contractRequest.getStoreId()).build());
        }

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 校验签约分类是否有关联商品
     *
     * @param cateId
     * @param storeId
     * @return
     */
    @ApiOperation(value = "校验签约分类是否有关联商品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "cateId", value = "分类Id", required =
                    true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId", value = "店铺Id", required =
                    true)
    })
    @RequestMapping(value = "/cate/del/verify/{cateId}/{storeId}", method = RequestMethod.GET)
    public BaseResponse cateDelVerify(@PathVariable Long cateId, @PathVariable Long storeId) {
        ContractCateDelVerifyRequest request = new ContractCateDelVerifyRequest();
        request.setStoreId(storeId);
        request.setCateId(cateId);
        contractCateQueryProvider.cateDelVerify(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 获取商家签约分类列表
     *
     * @return
     */
    @ApiOperation(value = "获取商家签约分类列表")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId", value = "店铺Id", required = true)
    @RequestMapping(value = "/cate/list/{storeId}", method = RequestMethod.GET)
    public BaseResponse<List<ContractCateVO>> cateList(@PathVariable Long storeId) {
        ContractCateListRequest contractCateQueryRequest = new ContractCateListRequest();
        contractCateQueryRequest.setStoreId(storeId);
        return BaseResponse.success(contractCateQueryProvider.list(contractCateQueryRequest).getContext().getContractCateList()

        );
    }

    /**
     * 获取商家签约品牌列表
     *
     * @return
     */
    @ApiOperation(value = "获取商家签约品牌列表")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId", value = "店铺Id", required = true)
    @RequestMapping(value = "/brand/list/{storeId}", method = RequestMethod.GET)
    public BaseResponse<List<ContractBrandVO>> brandList(@PathVariable Long storeId) {
        ContractBrandListRequest contractBrandQueryRequest = new ContractBrandListRequest();
        contractBrandQueryRequest.setStoreId(storeId);
        return BaseResponse.success(contractBrandQueryProvider.list(contractBrandQueryRequest).getContext().getContractBrandVOList());
    }


    /**
     * 校验商家自定义品牌是否与平台重复
     *
     * @param storeId
     * @return
     */
    @ApiOperation(value = "校验商家自定义品牌是否与平台重复")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId", value = "店铺Id", required = true)
    @RequestMapping(value = "/brand/list/verify/{storeId}", method = RequestMethod.GET)
    public BaseResponse<List<ContractBrandVO>> brandListVerify(@PathVariable Long storeId) {
        return BaseResponse.success(contractBrandQueryProvider.listVerifyByStoreId(
                ContractBrandListVerifyByStoreIdRequest.builder().storeId(storeId).build()
        ).getContext().getContractBrandVOList());
    }


    /**
     * 直接关联品牌
     * @param storeId
     * @return
     */
    @ApiOperation(value = "直接关联品牌")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId", value = "店铺Id", required = true)
    @RequestMapping(value = "/brand/relevance/{storeId}",method = RequestMethod.GET)
    public BaseResponse brandRelevance(@PathVariable Long storeId) {
        return contractBrandProvider.transferByStoreId(
                ContractBrandTransferByStoreIdRequest.builder().storeId(storeId).build());
    }

    /**
     * 查询商家签约的平台类目列表，包含所有的父级类目
     *
     * @return 平台类目列表
     */
    @ApiOperation(value = "查询商家签约的平台类目列表，包含所有的父级类目")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId", value = "店铺Id", required = true)
    @RequestMapping(value = "/goods/cate/list/{storeId}", method = RequestMethod.GET)
    public BaseResponse<List<GoodsCateVO>> listCate(@PathVariable Long storeId) {
        ContractCateListCateByStoreIdRequest request = new ContractCateListCateByStoreIdRequest();
        request.setStoreId(storeId);
        return BaseResponse.success(contractCateQueryProvider.listCateByStoreId(request).getContext().getGoodsCateList());
    }

    /**
     * 查询商家签约的平台类目列表，包含所有的父级类目
     *
     * @return 平台类目列表
     */
    @ApiOperation(value = "查询商家签约的平台类目列表，包含所有的父级类目")
    @RequestMapping(value = "/goods/cate/list", method = RequestMethod.GET)
    public BaseResponse<List<GoodsCateVO>> listCate() {
        ContractCateListCateByStoreIdRequest request = new ContractCateListCateByStoreIdRequest();
        request.setStoreId(commonUtil.getStoreId());
        return BaseResponse.success(contractCateQueryProvider.listCateByStoreId(request).getContext().getGoodsCateList());
    }

    /**
     * 查询商家签约的平台品牌列表
     *
     * @return 商家签约的平台品牌列表
     */
    @ApiOperation(value = "查询商家签约的平台品牌列表")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId", value = "店铺Id", required = true)
    @RequestMapping(value = "/goods/brand/list/{storeId}", method = RequestMethod.GET)
    public BaseResponse<List<GoodsBrandVO>> listBrand(@PathVariable Long storeId) {
        ContractBrandListRequest request = new ContractBrandListRequest();
        request.setStoreId(storeId);
        List<GoodsBrandVO> brands = contractBrandQueryProvider.list(request).getContext().getContractBrandVOList()
                .stream().map(ContractBrandVO::getGoodsBrand).filter(Objects::nonNull).collect(Collectors.toList());
        return BaseResponse.success(brands);
    }

    /**
     * 查询商家签约的平台品牌列表
     *
     * @return 商家签约的平台品牌列表
     */
    @ApiOperation(value = "查询商家签约的平台品牌列表")
    @RequestMapping(value = "/goods/brand/list", method = RequestMethod.GET)
    public BaseResponse<List<GoodsBrandVO>> listBrand() {
        ContractBrandListRequest request = new ContractBrandListRequest();
        request.setStoreId(commonUtil.getStoreId());
        List<GoodsBrandVO> brands = contractBrandQueryProvider.list(request).getContext().getContractBrandVOList()
                .stream().map(ContractBrandVO::getGoodsBrand)
                .filter(Objects::nonNull).collect(Collectors.toList());
        return BaseResponse.success(brands);
    }
}
