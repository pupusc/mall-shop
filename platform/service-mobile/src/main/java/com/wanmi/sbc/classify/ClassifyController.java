package com.wanmi.sbc.classify;

import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsCustomResponse;
import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsListResponse;
import com.wanmi.sbc.booklistmodel.response.BookListModelSimpleResponse;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.classify.request.ClassifyGoodsAndBookListModelRequest;
import com.wanmi.sbc.classify.response.ClassifyGoodsAndBookListModelResponse;
import com.wanmi.sbc.classify.response.ClassifyNoChildResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.api.request.goods.SortCustomBuilder;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.request.classify.BookListModelClassifyLinkPageProviderRequest;
import com.wanmi.sbc.goods.api.request.classify.ClassifyCollectionProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelGoodsIdProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.BookListModelClassifyLinkProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.util.RandomUtil;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
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
import java.util.Map;
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
    public BaseResponse<List<ClassifyGoodsAndBookListModelResponse>> listClassifyGoodsAndBookListModel(@RequestBody @Validated ClassifyGoodsAndBookListModelRequest classifyGoodsAndBookListModelRequest){
        List<ClassifyGoodsAndBookListModelResponse> result = new ArrayList<>();
        int pageSize = classifyGoodsAndBookListModelRequest.getPageSize(); //每次加载为 5的倍数
        int radix = 5; //基数
        if (pageSize % radix > 0) {
            throw new IllegalStateException("K-010109");
        }

        int quotient = pageSize / radix; //商
        pageSize = pageSize - quotient; //获取的数量

        //获取当前分类下的所有子分类
        ClassifyCollectionProviderRequest classifyCollectionProviderRequest = new ClassifyCollectionProviderRequest();
        classifyCollectionProviderRequest.setParentIdColl(Collections.singleton(classifyGoodsAndBookListModelRequest.getClassifyId()));
        BaseResponse<List<ClassifyProviderResponse>> listBaseResponse = classifyProvider.listClassifyNoChildByParentId(classifyCollectionProviderRequest);
        if (CollectionUtils.isEmpty(listBaseResponse.getContext())) {
            return BaseResponse.success(result);
        }
        Set<Integer> childClassifySet = listBaseResponse.getContext().stream().map(ClassifyProviderResponse::getId).collect(Collectors.toSet());
        //根据分类id 获取销量前300的商品列表
        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
        esGoodsCustomRequest.setPageNum(0);
        esGoodsCustomRequest.setPageSize(300);
        esGoodsCustomRequest.setClassifyIdList(childClassifySet);
        List<SortCustomBuilder> sortBuilderList = new ArrayList<>();
        //按照销售数量排序
        sortBuilderList.add(new SortCustomBuilder("goodsSalesNum", SortOrder.DESC));
        esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
        //获取随机的商品数量
        List<EsGoodsVO> esGoodsRandomList = bookListModelAndGoodsService.listRandomEsGoodsVo(esGoodsCustomRequest, pageSize);
        if (CollectionUtils.isEmpty(esGoodsRandomList)) {
            return BaseResponse.success(result);
        }

        //获取封装好的商品信息
        List<GoodsCustomResponse> goodsCustomResponsesList = bookListModelAndGoodsService.listGoodsCustom(esGoodsRandomList);
        if (CollectionUtils.isEmpty(goodsCustomResponsesList)) {
            return BaseResponse.success(result);
        }
        Set<String> goodsIdSet = esGoodsRandomList.stream().map(EsGoodsVO::getId).collect(Collectors.toSet());
        //获取商品所对应的书单
        Map<String, BookListModelGoodsIdProviderResponse> bookListModelGoodsIdProviderResponseMap =
                bookListModelAndGoodsService.mapBookLitModelByGoodsIdColl(goodsIdSet);

        //获取书单列表
        BookListModelClassifyLinkPageProviderRequest bookListModelClassifyLinkPageProviderRequest = new BookListModelClassifyLinkPageProviderRequest();
        bookListModelClassifyLinkPageProviderRequest.setClassifyIdColl(childClassifySet);
        bookListModelClassifyLinkPageProviderRequest.setBusinessTypeList(Arrays.asList(BusinessTypeEnum.BOOK_LIST.getCode(), BusinessTypeEnum.BOOK_RECOMMEND.getCode()));
        bookListModelClassifyLinkPageProviderRequest.setPageNum(0);
        bookListModelClassifyLinkPageProviderRequest.setPageSize(60);  //当前是一共300个商品，5个商品随机一个 书单，则300 / 5 为60个书单随机
        BaseResponse<List<BookListModelClassifyLinkProviderResponse>> bookListModelClassifyLinkProviderResponses = classifyProvider.listBookListModelByClassifyIdColl(bookListModelClassifyLinkPageProviderRequest);
        List<BookListModelClassifyLinkProviderResponse> context = bookListModelClassifyLinkProviderResponses.getContext();

        //开始封装数据
        for (int i = 0; i < goodsCustomResponsesList.size(); i++) {
            //根据销量获取商品列表
            ClassifyGoodsAndBookListModelResponse resultClassifyGoods = new ClassifyGoodsAndBookListModelResponse();

            BookListModelAndGoodsCustomResponse bookListModelAndGoodsCustomParam = new BookListModelAndGoodsCustomResponse();
            GoodsCustomResponse goodsCustomParam = goodsCustomResponsesList.get(i);
            bookListModelAndGoodsCustomParam.setGoodsCustomVo(goodsCustomParam);
            BookListModelGoodsIdProviderResponse bookListModelGoodsIdProviderResponse = bookListModelGoodsIdProviderResponseMap.get(goodsCustomParam.getGoodsId());
            if (bookListModelGoodsIdProviderResponse != null) {
                BookListModelSimpleResponse bookListModelSimpleResponse = new BookListModelSimpleResponse();
                BeanUtils.copyProperties(bookListModelGoodsIdProviderResponse, bookListModelSimpleResponse);
                bookListModelAndGoodsCustomParam.setBookListModel(bookListModelSimpleResponse);
            }
            resultClassifyGoods.setBookListModelAndGoodsCustomModel(bookListModelAndGoodsCustomParam);
            resultClassifyGoods.setType(1); //商品
            result.add(resultClassifyGoods);
            //表示书单
            if ((i + 1) % radix == 0) {
                Integer bookListModelRandomIndex = RandomUtil.getRandom(context.size());
                if (bookListModelRandomIndex != null) {
                    ClassifyGoodsAndBookListModelResponse classifyBookListModel = new ClassifyGoodsAndBookListModelResponse();
                    BookListModelSimpleResponse bookListModelSimpleResponse = new BookListModelSimpleResponse();
                    BookListModelGoodsIdProviderResponse bookListModelGoodsIdParam = context.get(bookListModelRandomIndex)
                    BeanUtils.copyProperties(bookListModelGoodsIdParam, bookListModelSimpleResponse);
                    classifyBookListModel.setBookListModel(bookListModelGoodsIdParam);
                    classifyBookListModel.setType(2); //书单
                    result.add(classifyBookListModel);
                }
            }
        }
        return BaseResponse.success(result);
    }
}
