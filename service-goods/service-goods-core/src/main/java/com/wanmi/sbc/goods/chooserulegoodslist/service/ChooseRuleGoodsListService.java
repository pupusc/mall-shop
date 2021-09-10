package com.wanmi.sbc.goods.chooserulegoodslist.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.request.chooserulegoodslist.ChooseRuleGoodsListProviderRequest;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.BookListGoodsProviderResponse;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.ChooseRuleProviderResponse;
import com.wanmi.sbc.goods.booklistgoods.model.root.BookListGoodsDTO;
import com.wanmi.sbc.goods.booklistgoods.request.BookListGoodsRequest;
import com.wanmi.sbc.goods.booklistgoods.service.BookListGoodsService;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.service.BookListGoodsPublishService;
import com.wanmi.sbc.goods.booklistmodel.service.BookListModelService;
import com.wanmi.sbc.goods.chooserule.model.root.ChooseRuleDTO;
import com.wanmi.sbc.goods.chooserule.request.ChooseRuleRequest;
import com.wanmi.sbc.goods.chooserule.service.ChooseRuleService;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/4 3:37 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class ChooseRuleGoodsListService {

    @Resource
    private ChooseRuleService chooseRuleService;
    @Resource
    private BookListGoodsService bookListGoodsService;
    @Resource
    private BookListGoodsPublishService bookListGoodsPublishService;


    @Transactional
    public void add(ChooseRuleGoodsListProviderRequest chooseRuleGoodsListRequest, String operator) {
        ChooseRuleRequest chooseRuleRequest = new ChooseRuleRequest();
        chooseRuleRequest.setBookListId(chooseRuleGoodsListRequest.getBookListId());
        chooseRuleRequest.setFilterRule(chooseRuleGoodsListRequest.getFilterRule());
        chooseRuleRequest.setCategory(chooseRuleGoodsListRequest.getCategory());
        chooseRuleRequest.setChooseType(chooseRuleGoodsListRequest.getChooseType());
        chooseRuleRequest.setChooseCondition(chooseRuleGoodsListRequest.getChooseCondition());
        chooseRuleRequest.setOperator(operator);
        ChooseRuleDTO chooseRule = chooseRuleService.add(chooseRuleRequest);
        log.info("-------->> ChooseRuleGoodsListService.add operator: {} ChooseRule complete result: {}",
                operator, JSON.toJSONString(chooseRule));

        BookListGoodsRequest bookListGoodsRequest = new BookListGoodsRequest();
        bookListGoodsRequest.setChooseRuleId(chooseRule.getId());
        bookListGoodsRequest.setBookListId(chooseRule.getBookListId());
        bookListGoodsRequest.setCategory(chooseRule.getCategory());
        bookListGoodsRequest.setGoodsIdListRequestList(chooseRuleGoodsListRequest.getGoodsIdListRequestList());
        bookListGoodsService.add(bookListGoodsRequest);
        log.info("-------->> ChooseRuleGoodsListService.add operator: {} BookListGoods complete", operator);
    }


    @Transactional
    public void update(ChooseRuleGoodsListProviderRequest chooseRuleGoodsListProviderRequest, String operator) {
        ChooseRuleRequest chooseRuleRequest = new ChooseRuleRequest();
        chooseRuleRequest.setId(chooseRuleGoodsListProviderRequest.getChooseRuleId());
        chooseRuleRequest.setBookListId(chooseRuleGoodsListProviderRequest.getBookListId());
        chooseRuleRequest.setFilterRule(chooseRuleGoodsListProviderRequest.getFilterRule());
        chooseRuleRequest.setCategory(chooseRuleGoodsListProviderRequest.getCategory());
        chooseRuleRequest.setChooseType(chooseRuleGoodsListProviderRequest.getChooseType());
        chooseRuleRequest.setChooseCondition(chooseRuleGoodsListProviderRequest.getChooseCondition());
        chooseRuleRequest.setOperator(operator);
        ChooseRuleDTO chooseRuleUpdate = chooseRuleService.update(chooseRuleRequest);
        log.info("-------->> ChooseRuleGoodsListService.update operator: {} chooseRule complete", operator);

        if (CollectionUtils.isEmpty(chooseRuleGoodsListProviderRequest.getGoodsIdListRequestList())){
            log.info("-------->> ChooseRuleGoodsListService.update operator: {} bookListGoods is empty return complete", operator);
            return;
        }
        BookListGoodsRequest bookListGoodsRequest = new BookListGoodsRequest();
        bookListGoodsRequest.setChooseRuleId(chooseRuleGoodsListProviderRequest.getChooseRuleId());
        bookListGoodsRequest.setGoodsIdListRequestList(chooseRuleGoodsListProviderRequest.getGoodsIdListRequestList());
        bookListGoodsService.update(bookListGoodsRequest);
        log.info("-------->> ChooseRuleGoodsListService.update operator: {} bookListGoods complete", operator);

    }

    /**
     * 获取 控件和商品列表
     * @param bookListId
     * @param categoryId
     * @return
     */
    public ChooseRuleProviderResponse findRuleAndGoodsByCondition(Integer bookListId, Integer categoryId) {
        ChooseRuleRequest chooseRuleRequest = new ChooseRuleRequest();
        chooseRuleRequest.setBookListId(bookListId);
        chooseRuleRequest.setCategory(categoryId);
        ChooseRuleDTO chooseRuleDTO = chooseRuleService.findByCondition(chooseRuleRequest);
        if (chooseRuleDTO == null) {
            throw new SbcRuntimeException("chooseRuleId 不存在");
        }
        List<BookListGoodsDTO> bookListGoodsDTOList = bookListGoodsService.list(null, bookListId, categoryId);

        ChooseRuleProviderResponse chooseRuleProviderResponse = new ChooseRuleProviderResponse();
        chooseRuleProviderResponse.setChooseRuleId(chooseRuleDTO.getId());
        chooseRuleProviderResponse.setBookListId(chooseRuleDTO.getBookListId());
        chooseRuleProviderResponse.setCategory(chooseRuleDTO.getCategory());
        chooseRuleProviderResponse.setFilterRule(chooseRuleDTO.getFilterRule());
        chooseRuleProviderResponse.setChooseType(chooseRuleDTO.getChooseType());
        chooseRuleProviderResponse.setChooseCondition(chooseRuleDTO.getChooseCondition());
        chooseRuleProviderResponse.setCreateTime(chooseRuleDTO.getCreateTime());
        chooseRuleProviderResponse.setUpdateTime(chooseRuleDTO.getUpdateTime());

        List<BookListGoodsProviderResponse> bookListGoodsResponseList = new ArrayList<>();
        if (CollectionUtils.isEmpty(bookListGoodsDTOList)) {
            chooseRuleProviderResponse.setBookListGoodsList(bookListGoodsResponseList);
        } else {
            for (BookListGoodsDTO bookListGoodsResponseParam : bookListGoodsDTOList) {
                BookListGoodsProviderResponse bookListGoodsModel = new BookListGoodsProviderResponse();
                BeanUtils.copyProperties(bookListGoodsResponseParam, bookListGoodsModel);
                bookListGoodsResponseList.add(bookListGoodsModel);
            }
            chooseRuleProviderResponse.setBookListGoodsList(bookListGoodsResponseList);
        }
        return chooseRuleProviderResponse;
    }


    /**
     * 获取 控件和商品 已经发布的列表
     * @param bookListIdCollection
     * @param categoryId
     * @return
     */
    public List<ChooseRuleProviderResponse> findRuleAndPublishGoodsByCondition(Collection<Integer> bookListIdCollection, Integer categoryId) {
        ChooseRuleRequest chooseRuleRequest = new ChooseRuleRequest();
        chooseRuleRequest.setBookListIdCollection(bookListIdCollection);
        chooseRuleRequest.setCategory(categoryId);
        Collection<ChooseRuleDTO> chooseRuleCollection = chooseRuleService.findByConditionCollection(chooseRuleRequest);

        List<ChooseRuleProviderResponse> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(chooseRuleCollection)) {
            return result;
        }

        for (ChooseRuleDTO chooseRuleParam : chooseRuleCollection) {
            ChooseRuleProviderResponse chooseRuleProviderResponse = new ChooseRuleProviderResponse();
            chooseRuleProviderResponse.setChooseRuleId(chooseRuleParam.getId());
            chooseRuleProviderResponse.setBookListId(chooseRuleParam.getBookListId());
            chooseRuleProviderResponse.setCategory(chooseRuleParam.getCategory());
            chooseRuleProviderResponse.setFilterRule(chooseRuleParam.getFilterRule());
            chooseRuleProviderResponse.setChooseType(chooseRuleParam.getChooseType());
            chooseRuleProviderResponse.setChooseCondition(chooseRuleParam.getChooseCondition());
            chooseRuleProviderResponse.setCreateTime(chooseRuleParam.getCreateTime());
            chooseRuleProviderResponse.setUpdateTime(chooseRuleParam.getUpdateTime());
            chooseRuleProviderResponse.setBookListGoodsList(new ArrayList<>());
            result.add(chooseRuleProviderResponse);
        }

        Map<Integer, ChooseRuleProviderResponse> chooseRuleResponseMap =
                result.stream().collect(Collectors.toMap(ChooseRuleProviderResponse::getBookListId, Function.identity(), (k1, k2) -> k1));

        List<BookListGoodsPublishDTO> bookListGoodsPublishList = bookListGoodsPublishService.list(bookListIdCollection, null, categoryId, null, null);
        //发布商品列表
        if (CollectionUtils.isEmpty(bookListGoodsPublishList)) {
            return result;
        }

        for (BookListGoodsPublishDTO bookListGoodsPublishParam : bookListGoodsPublishList) {
            ChooseRuleProviderResponse chooseRuleProviderResponse = chooseRuleResponseMap.get(bookListGoodsPublishParam.getBookListId());
            if (chooseRuleProviderResponse == null) {
                log.error("--> ChooseRuleGoodsListService.listByCondition bookListGoodsPublishParam: {}, chooseRuleProviderResponse is null",
                        JSON.toJSONString(bookListGoodsPublishParam));
                continue;
            }
            BookListGoodsProviderResponse bookListGoodsModel = new BookListGoodsProviderResponse();
            BeanUtils.copyProperties(bookListGoodsPublishParam, bookListGoodsModel);
            chooseRuleProviderResponse.getBookListGoodsList().add(bookListGoodsModel);
        }
        return result;
    }
    

}
