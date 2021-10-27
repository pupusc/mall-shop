package com.wanmi.sbc.classify;

import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.BookListModelSimpleResponse;
import com.wanmi.sbc.classify.request.ClassifyGoodsAndBookListModelRequest;
import com.wanmi.sbc.classify.response.ClassifyGoodsAndBookListModelResponse;
import com.wanmi.sbc.classify.response.ClassifyNoChildResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.request.classify.BookListModelClassifyLinkPageProviderRequest;
import com.wanmi.sbc.goods.api.request.classify.ClassifyCollectionProviderRequest;
import com.wanmi.sbc.goods.api.response.classify.BookListModelClassifyLinkProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: 分类
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/18 2:43 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/classify")
@RestController
public class ClassifyController {

    @Autowired
    private ClassifyProvider classifyProvider;

    @Autowired
    private BookListModelAndGoodsService bookListModelAndGoodsService;

    /**
     * 分类  获取分类信息
     * @menu 新版首页
     *
     * @return
     */
    @GetMapping("/listClassify/root")
    public BaseResponse<List<ClassifyNoChildResponse>> listClassify() {
        List<ClassifyNoChildResponse> result = new ArrayList<>();
        ClassifyCollectionProviderRequest classifyCollectionProviderRequest = new ClassifyCollectionProviderRequest();
        classifyCollectionProviderRequest.setParentIdColl(Collections.singleton(0));
        BaseResponse<List<ClassifyProviderResponse>> listBaseResponse = classifyProvider.listClassifyNoChildByParentId(classifyCollectionProviderRequest);
        for (ClassifyProviderResponse classifyProviderResponseParam : listBaseResponse.getContext()) {
            ClassifyNoChildResponse classifyNoChildResponse = new ClassifyNoChildResponse();
            classifyNoChildResponse.setId(classifyProviderResponseParam.getId());
            classifyNoChildResponse.setClassifyName(classifyProviderResponseParam.getClassifyName());
            result.add(classifyNoChildResponse);
        }
        return BaseResponse.success(result);
    }

    /**
     * 分类 获取分类下的 商品列表和 书单
     * @menu 新版首页
     * @param classifyGoodsAndBookListModelRequest
     * @return
     */
    @PostMapping("/listClassifyGoodsAndBookListModel")
    public BaseResponse<ClassifyGoodsAndBookListModelResponse> listClassifyGoodsAndBookListModel(@RequestBody @Validated ClassifyGoodsAndBookListModelRequest classifyGoodsAndBookListModelRequest){
        //根据销量获取商品列表
        ClassifyGoodsAndBookListModelResponse resultClassifyGoodsAndBookListModel = new ClassifyGoodsAndBookListModelResponse();

        //获取当前分类
        ClassifyCollectionProviderRequest classifyCollectionProviderRequest = new ClassifyCollectionProviderRequest();
        classifyCollectionProviderRequest.setParentIdColl(Collections.singleton(classifyGoodsAndBookListModelRequest.getClassifyId()));
        BaseResponse<List<ClassifyProviderResponse>> listBaseResponse = classifyProvider.listClassifyNoChildByParentId(classifyCollectionProviderRequest);
        if (CollectionUtils.isEmpty(listBaseResponse.getContext())) {
            return BaseResponse.success(resultClassifyGoodsAndBookListModel);
        }
        Set<Integer> childClassifySet = listBaseResponse.getContext().stream().map(ClassifyProviderResponse::getId).collect(Collectors.toSet());

        //根据分类id 获取销量前300的商品列表


//
//        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
//        esGoodsCustomRequest.setPageNum(0);
//        esGoodsCustomRequest.setPageSize(300);
//        List<SortCustomBuilder> sortBuilderList = new ArrayList<>();
//        sortBuilderList.add(new SortCustomBuilder("goodsSalesNum", SortOrder.DESC));
//        esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
//        List<GoodsCustomResponse> result = new ArrayList<>(bookListModelAndGoodsService.listRandomGoodsCustomer(esGoodsCustomRequest, classifyGoodsAndBookListModelRequest.getPageSize()));

        //获取书单列表
        BookListModelClassifyLinkPageProviderRequest bookListModelClassifyLinkPageProviderRequest = new BookListModelClassifyLinkPageProviderRequest();
        bookListModelClassifyLinkPageProviderRequest.setClassifyIdColl(childClassifySet);
        bookListModelClassifyLinkPageProviderRequest.setBusinessTypeList(Arrays.asList(BusinessTypeEnum.BOOK_LIST.getCode(), BusinessTypeEnum.BOOK_RECOMMEND.getCode()));
        BaseResponse<List<BookListModelClassifyLinkProviderResponse>> bookListModelClassifyLinkProviderResponses = classifyProvider.listBookListModelByClassifyIdColl(bookListModelClassifyLinkPageProviderRequest);
        List<BookListModelClassifyLinkProviderResponse> context = bookListModelClassifyLinkProviderResponses.getContext();
        if (CollectionUtils.isEmpty(context)) {
            return BaseResponse.success(resultClassifyGoodsAndBookListModel);
        }

        Collection<Integer> randomIndexColl = RandomUtil.getRandom(context.size(), 1);

        for (Integer index : randomIndexColl) {
            BookListModelClassifyLinkProviderResponse bookListModelClassifyLinkProviderResponse = context.get(index);
            BookListModelSimpleResponse bookListModelSimpleResponse = new BookListModelSimpleResponse();
            bookListModelSimpleResponse.setId(bookListModelClassifyLinkProviderResponse.getBookListModelId());
            bookListModelSimpleResponse.setName(bookListModelClassifyLinkProviderResponse.getName());
//            bookListModelSimpleResponse.setFamousName(bookListModelClassifyLinkProviderResponse.getFamousName());
            bookListModelSimpleResponse.setDesc(bookListModelClassifyLinkProviderResponse.getDesc());
            bookListModelSimpleResponse.setBusinessType(bookListModelClassifyLinkProviderResponse.getBusinessType());
//            bookListModelSimpleResponse.setHeadImgHref(bookListModelClassifyLinkProviderResponse.getHeadImgHref());
//            bookListModelSimpleResponse.setPageHref(bookListModelClassifyLinkProviderResponse.getPageHref());
//            bookListModelSimpleResponse.setPublishState(bookListModelClassifyLinkProviderResponse.getPublishState());
//            bookListModelSimpleResponse.setCreateTime(bookListModelClassifyLinkProviderResponse.getCreateTime());
//            bookListModelSimpleResponse.setUpdateTime(bookListModelClassifyLinkProviderResponse.getUpdateTime());
//            resultClassifyGoodsAndBookListModel.setBookListModelMobile(bookListModelMobileResponse);
        }
        return BaseResponse.success(resultClassifyGoodsAndBookListModel);
    }
}
