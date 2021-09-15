package com.wanmi.sbc.booklistmodel;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.booklistmodel.request.BookListMixRequest;
import com.wanmi.sbc.booklistmodel.request.BookListModelPageRequest;
import com.wanmi.sbc.booklistmodel.request.BookListModelRequest;
import com.wanmi.sbc.booklistmodel.response.BookListGoodsResponse;
import com.wanmi.sbc.booklistmodel.response.BookListMixResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsCustomQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListMixProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/6 1:09 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
@RequestMapping("/bookListModel")
public class BookListModelController {

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Resource
    private CommonUtil commonUtil;


    @Autowired
    private EsGoodsCustomQueryProvider esGoodsCustomQueryProvider;

    /**
     * 新增书单
     * @param bookListMixRequest
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @PostMapping("/add")
    public BaseResponse add(@Validated @RequestBody BookListMixRequest bookListMixRequest) {
//        BookListMixProviderRequest request = new BookListMixProviderRequest();
        String bookListMixRequestStr = JSON.toJSONString(bookListMixRequest);
        BookListMixProviderRequest request = JSON.parseObject(bookListMixRequestStr, BookListMixProviderRequest.class);
        request.setOperator(commonUtil.getOperatorId());
        if (request.getChooseRuleGoodsListModel() != null) {
            request.getChooseRuleGoodsListModel().setCategory(CategoryEnum.BOOK_LIST_MODEL.getCode());
        }
        bookListModelProvider.add(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     *  修改书单
     * @param bookListMixRequest
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @PostMapping("/update")
    public BaseResponse update(@Validated @RequestBody BookListMixRequest bookListMixRequest) {
        String bookListMixJsonStr = JSON.toJSONString(bookListMixRequest);
        BookListMixProviderRequest request = JSON.parseObject(bookListMixJsonStr, BookListMixProviderRequest.class);
        request.setOperator(commonUtil.getOperatorId());
        if (request.getChooseRuleGoodsListModel() != null) {
            request.getChooseRuleGoodsListModel().setCategory(CategoryEnum.BOOK_LIST_MODEL.getCode());
        }
        bookListModelProvider.update(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 删除书单
     * @param id
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable Integer id) {

        BookListModelProviderRequest request = new BookListModelProviderRequest();
        request.setId(id);
        request.setOperator(commonUtil.getOperatorId());
        bookListModelProvider.delete(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 书单列表书单
     * @param bookListModelPageRequest
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @PostMapping("/listByPage")
    public BaseResponse<MicroServicePage<BookListModelProviderResponse>> listByPage(
            @RequestBody BookListModelPageRequest bookListModelPageRequest){
        BookListModelPageProviderRequest request = new BookListModelPageProviderRequest();
        BeanUtils.copyProperties(bookListModelPageRequest, request);
        return bookListModelProvider.listByPage(request);
    }


    /**
     * 发布书单
     * @param id
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @GetMapping("/publish/{id}")
    public BaseResponse publish(@PathVariable("id") Integer id){
        BookListModelProviderRequest request = new BookListModelProviderRequest();
        request.setId(id);
        request.setOperator(commonUtil.getOperatorId());
        return bookListModelProvider.publish(request);
    }


    /**
     * 根据id获取书单
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @GetMapping("/findById/{id}")
    public BaseResponse<BookListMixResponse> findById(@PathVariable("id") Integer id){
        BookListModelProviderRequest bookListModelProviderRequest = new BookListModelProviderRequest();
        bookListModelProviderRequest.setId(id);
        BaseResponse<BookListMixProviderResponse> response = bookListModelProvider.findById(bookListModelProviderRequest);
        BookListMixProviderResponse resultResponse = response.getContext();
        String bookListMixStr = JSON.toJSONString(resultResponse);
        BookListMixResponse bookListMixResponse = JSON.parseObject(bookListMixStr, BookListMixResponse.class);
        List<BookListGoodsResponse> bookListGoodsList = new ArrayList<>();
        if (bookListMixResponse != null && bookListMixResponse.getChooseRuleMode() != null && !CollectionUtils.isEmpty(bookListMixResponse.getChooseRuleMode().getBookListGoodsList())) {
            bookListGoodsList.addAll(bookListMixResponse.getChooseRuleMode().getBookListGoodsList());
        }

        if (!CollectionUtils.isEmpty(bookListGoodsList)) {
            Set<String> goodsIdNo = bookListGoodsList.stream().map(BookListGoodsResponse::getSpuId).collect(Collectors.toSet());
            EsGoodsCustomQueryProviderRequest request = new EsGoodsCustomQueryProviderRequest();
            request.setGoodIdList(goodsIdNo);
            request.setPageNum(1);
            request.setPageSize(100);
            BaseResponse<MicroServicePage<EsGoodsVO>> microServicePageBaseResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(request);
            MicroServicePage<EsGoodsVO> contextPage = microServicePageBaseResponse.getContext();
            if (!CollectionUtils.isEmpty(contextPage.getContent())) {
                Map<String, EsGoodsVO> spuId2EsGoodsVoMap = new HashMap<>();
                for (EsGoodsVO esGoodsParam : contextPage.getContent()) {
                    spuId2EsGoodsVoMap.put(esGoodsParam.getId(), esGoodsParam);
                }
                //填充商品展示信息
                for (BookListGoodsResponse bookListGoodsResponseParam : bookListGoodsList) {
                    EsGoodsVO esGoodsVO = spuId2EsGoodsVoMap.get(bookListGoodsResponseParam.getSpuId());
                    if (esGoodsVO == null) {
                        continue;
                    }
                    //获取最小的 goodsInfo
                    List<GoodsInfoNestVO> goodsInfos = esGoodsVO.getGoodsInfos();
                    if (!CollectionUtils.isEmpty(goodsInfos)) {
                        BigDecimal marketPrice = goodsInfos.get(0).getMarketPrice();
                        String specText = goodsInfos.get(0).getSpecText();
                        for (GoodsInfoNestVO goodsInfo : esGoodsVO.getGoodsInfos()) {
                            if (marketPrice.compareTo(goodsInfo.getMarketPrice()) > 0) {
                                marketPrice = goodsInfo.getMarketPrice();
                                specText = goodsInfo.getSpecText();
                            }
                        }
                        bookListGoodsResponseParam.setMarketPrice(marketPrice);
                        bookListGoodsResponseParam.setSpecText(specText);
                    }

                    bookListGoodsResponseParam.setBuyPoint(esGoodsVO.getBuyPoint());
                    bookListGoodsResponseParam.setGoodsInfoName(esGoodsVO.getGoodsName());

                }
            }
        }
        return BaseResponse.success(bookListMixResponse);
    }


}
