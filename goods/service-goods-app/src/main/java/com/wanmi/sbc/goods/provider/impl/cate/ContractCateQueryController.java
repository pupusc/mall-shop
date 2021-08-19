package com.wanmi.sbc.goods.provider.impl.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.cate.ContractCateQueryProvider;
import com.wanmi.sbc.goods.api.request.cate.*;
import com.wanmi.sbc.goods.api.response.cate.ContractCateByIdResponse;
import com.wanmi.sbc.goods.api.response.cate.ContractCateListByConditionResponse;
import com.wanmi.sbc.goods.api.response.cate.ContractCateListCateByStoreIdResponse;
import com.wanmi.sbc.goods.api.response.cate.ContractCateListResponse;
import com.wanmi.sbc.goods.bean.vo.ContractCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.request.ContractCateQueryRequest;
import com.wanmi.sbc.goods.cate.response.ContractCateResponse;
import com.wanmi.sbc.goods.cate.service.ContractCateService;
import com.wanmi.sbc.goods.cate.service.GoodsCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-07 19:34
 */
@Validated
@RestController
public class ContractCateQueryController implements ContractCateQueryProvider {

    @Autowired
    private ContractCateService contractCateService;

    @Autowired
    private GoodsCateService goodsCateService;

    /**
     * @param request 查询签约分类 {@link ContractCateListRequest}
     * @return
     */
    @Override

    public BaseResponse<ContractCateListResponse> list(@RequestBody @Valid ContractCateListRequest request) {
        ContractCateQueryRequest contractCateQueryRequest = KsBeanUtil.convert(request, ContractCateQueryRequest.class);
        List<ContractCateResponse> contractCateResponses = contractCateService.queryList(contractCateQueryRequest);
        List<ContractCateVO> contractCateVOList = KsBeanUtil.convert(contractCateResponses, ContractCateVO.class);
        ContractCateListResponse contractCateListResponse = new ContractCateListResponse();
        contractCateListResponse.setContractCateList(contractCateVOList);

        return BaseResponse.success(contractCateListResponse);
    }

    /**
     * @param request 查询店铺已签约的类目列表，包含上级类目 {@link ContractCateListCateByStoreIdRequest}
     * @return
     */
    @Override

    public BaseResponse<ContractCateListCateByStoreIdResponse> listCateByStoreId(@RequestBody @Valid ContractCateListCateByStoreIdRequest request) {

        List<GoodsCate> goodsCateList = contractCateService.listCate(request.getStoreId());
        List<GoodsCateVO> goodsCateVOList = KsBeanUtil.convert(goodsCateList, GoodsCateVO.class);
        ContractCateListCateByStoreIdResponse contractCateListCateByStoreIdResponse = new ContractCateListCateByStoreIdResponse();
        contractCateListCateByStoreIdResponse.setGoodsCateList(goodsCateVOList);

        return BaseResponse.success(contractCateListCateByStoreIdResponse);
    }

    /**
     * @param request 根据主键查询签约分类 {@link ContractCateByIdRequest}
     * @return
     */
    @Override

    public BaseResponse<ContractCateByIdResponse> getById(@RequestBody @Valid ContractCateByIdRequest request) {

        return BaseResponse.success(CateConvert.convertContractCate2ByIdResponse(contractCateService
                .queryById(request.getContractCateId())));
    }

    /**
     * @param request 校验签约分类是否满足删除条件 {@link ContractCateDelVerifyRequest}
     * @return
     */
    @Override

    public BaseResponse cateDelVerify(@RequestBody @Valid ContractCateDelVerifyRequest request) {
        contractCateService.cateDelVerify(request.getCateId(), request.getStoreId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据动态条件查询签约分类
     * @param request 条件查询签约分类 {@link ContractCateListByConditionRequest}
     * @return 签约分类列表 {@link ContractCateListByConditionResponse}
     */
    @Override

    public BaseResponse<ContractCateListByConditionResponse> listByCondition(@RequestBody @Valid
                                                                                         ContractCateListByConditionRequest request) {
        ContractCateQueryRequest queryRequest = new ContractCateQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);

        return BaseResponse.success(ContractCateListByConditionResponse.builder()
                .contractCateList(KsBeanUtil.convert(contractCateService.queryContractCateList(queryRequest),
                        ContractCateVO.class)).build());
    }
}
