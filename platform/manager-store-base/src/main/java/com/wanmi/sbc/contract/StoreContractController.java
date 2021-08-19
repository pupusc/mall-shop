package com.wanmi.sbc.contract;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsBrandDeleteByIdsRequest;
import com.wanmi.sbc.goods.api.provider.brand.ContractBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.ContractCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.contract.ContractProvider;
import com.wanmi.sbc.goods.api.request.brand.ContractBrandListRequest;
import com.wanmi.sbc.goods.api.request.cate.ContractCateDelVerifyRequest;
import com.wanmi.sbc.goods.api.request.cate.ContractCateListCateByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.cate.ContractCateListRequest;
import com.wanmi.sbc.goods.api.request.contract.ContractSaveRequest;
import com.wanmi.sbc.goods.bean.vo.ContractBrandVO;
import com.wanmi.sbc.goods.bean.vo.ContractCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 签约信息(品牌，)
 * Created by sunkun on 2017/11/2.
 */
@Api(tags = "StoreContractController", description = "签约信息 API")
@RestController
@RequestMapping("/contract")
@Validated
public class StoreContractController {

    @Autowired
    private ContractBrandQueryProvider contractBrandQueryProvider;

    @Autowired
    private ContractProvider contractProvider;

    @Resource
    private CommonUtil commonUtil;

    @Autowired
    private ContractCateQueryProvider contractCateQueryProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 签约信息新增修改删除(签约分类，签约品牌)
     *
     * @return
     */
    @ApiOperation(value = "签约信息新增修改删除(签约分类，签约品牌)")
    @RequestMapping(value = "/renewal", method = RequestMethod.POST)
    public BaseResponse renewalAll(@Valid @RequestBody ContractSaveRequest contractRequest) {
        contractRequest.setStoreId(commonUtil.getStoreId());
        List<Long> ids = contractProvider.save(contractRequest).getContext().getBrandIds();
        //取消签约品牌的时候更新es
        if (CollectionUtils.isNotEmpty(ids) && CollectionUtils.isNotEmpty(contractRequest.getDelBrandIds())) {
            esGoodsInfoElasticProvider.delBrandIds(EsBrandDeleteByIdsRequest.builder().
                    deleteIds(ids).storeId(contractRequest.getStoreId()).build());
        }
        operateLogMQUtil.convertAndSend("设置", "店铺信息", "编辑店铺信息");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 校验签约分类是否有关联商品
     *
     * @param cateId
     * @return
     */
    @ApiOperation(value = "校验签约分类是否有关联商品")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "cateId", value = "分类Id", required = true)
    @RequestMapping(value = "/cate/del/verify/{cateId}", method = RequestMethod.GET)
    public BaseResponse cateDelVerify(@PathVariable Long cateId) {
        Long storeId = commonUtil.getStoreId();
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
    @RequestMapping(value = "/cate/list", method = RequestMethod.GET)
    public BaseResponse<List<ContractCateVO>> cateList() {
        ContractCateListRequest contractCateQueryRequest = new ContractCateListRequest();
        contractCateQueryRequest.setStoreId(commonUtil.getStoreId());
        return BaseResponse.success(contractCateQueryProvider.list(contractCateQueryRequest).getContext().getContractCateList());
    }

    /**
     * 获取商家签约品牌列表
     *
     * @return
     */
    @ApiOperation(value = "获取商家签约品牌列表")
    @RequestMapping(value = "/brand/list", method = RequestMethod.GET)
    public BaseResponse<List<ContractBrandVO>> brandList() {
        ContractBrandListRequest contractBrandQueryRequest = new ContractBrandListRequest();
        contractBrandQueryRequest.setStoreId(commonUtil.getStoreId());
        return BaseResponse.success(contractBrandQueryProvider.list(contractBrandQueryRequest).getContext().getContractBrandVOList());
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
