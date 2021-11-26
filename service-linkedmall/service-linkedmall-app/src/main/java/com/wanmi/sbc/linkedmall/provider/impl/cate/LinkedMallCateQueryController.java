package com.wanmi.sbc.linkedmall.provider.impl.cate;

import com.aliyuncs.linkedmall.model.v20180116.GetCategoryChainResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.provider.cate.LinkedMallCateQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.cate.CateByIdRequest;
import com.wanmi.sbc.linkedmall.api.request.cate.CateChainByGoodsIdRequest;
import com.wanmi.sbc.linkedmall.api.response.cate.CategoryChainByGoodsIdResponse;
import com.wanmi.sbc.linkedmall.api.response.cate.LinkedMallCateGetResponse;
import com.wanmi.sbc.linkedmall.bean.vo.LinkedMallGoodsCateVO;
import com.wanmi.sbc.linkedmall.cate.LinkedMallCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LinkedMallCateQueryController implements LinkedMallCateQueryProvider {
    @Autowired
    private LinkedMallCateService linkedMallCateService;

    @Override
    public BaseResponse<LinkedMallCateGetResponse> getAllLinkedMallCate() {
        ArrayList<LinkedMallGoodsCateVO> allCategory = linkedMallCateService.getAllCategory();
        LinkedMallCateGetResponse getAllLinkedMallCateResponse = new LinkedMallCateGetResponse();
        getAllLinkedMallCateResponse.setLinkedMallGoodsCateVOS(allCategory);
        return BaseResponse.success(getAllLinkedMallCateResponse);
    }

    @Override
    public BaseResponse<LinkedMallCateGetResponse> getLinkedMallCateById(CateByIdRequest cateByIdRequest) {
        List<LinkedMallGoodsCateVO> categoryById = linkedMallCateService.getCategoryById(cateByIdRequest.getCateId());
        LinkedMallCateGetResponse linkedMallCateGetResponse = new LinkedMallCateGetResponse();
        linkedMallCateGetResponse.setLinkedMallGoodsCateVOS(categoryById);
        return BaseResponse.success(linkedMallCateGetResponse);
    }

    @Override
    public BaseResponse<CategoryChainByGoodsIdResponse> getCategoryChainByGoodsId(CateChainByGoodsIdRequest cateChainByGoodsIdRequest) {
        List<GetCategoryChainResponse.Category> categoryChainByGoodsId = linkedMallCateService.getCategoryChainById(cateChainByGoodsIdRequest.getGoodsId());
        CategoryChainByGoodsIdResponse categoryChainByGoodsIdResponse = new CategoryChainByGoodsIdResponse();
        categoryChainByGoodsIdResponse.setCategoryChain(categoryChainByGoodsId);
        return BaseResponse.success(categoryChainByGoodsIdResponse);
    }
}
