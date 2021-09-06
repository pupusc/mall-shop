package com.wanmi.sbc.goods.chooserulegoodslist.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.request.chooserulegoodslist.ChooseRuleGoodsListProviderRequest;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.BookListGoodsProviderResponse;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.ChooseRuleProviderResponse;
import com.wanmi.sbc.goods.booklistgoods.model.root.BookListGoodsDTO;
import com.wanmi.sbc.goods.booklistgoods.request.BookListGoodsRequest;
import com.wanmi.sbc.goods.booklistgoods.service.BookListGoodsService;
import com.wanmi.sbc.goods.booklistmodel.service.BookListModelService;
import com.wanmi.sbc.goods.chooserule.model.root.ChooseRuleDTO;
import com.wanmi.sbc.goods.chooserule.request.ChooseRuleRequest;
import com.wanmi.sbc.goods.chooserule.service.ChooseRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    public ChooseRuleProviderResponse findByCondition(Integer bookListId, Integer categoryId) {
        ChooseRuleRequest chooseRuleRequest = new ChooseRuleRequest();
        chooseRuleRequest.setBookListId(bookListId);
        chooseRuleRequest.setCategory(categoryId);
        ChooseRuleDTO chooseRuleDTO = chooseRuleService.findByCondition(chooseRuleRequest);
        if (chooseRuleDTO == null) {
            throw new SbcRuntimeException("chooseRuleId 不存在");
        }
        List<BookListGoodsDTO> bookListGoodsDTOList = bookListGoodsService.list(bookListId, categoryId);

        ChooseRuleProviderResponse chooseRuleProviderResponse = new ChooseRuleProviderResponse();
        chooseRuleProviderResponse.setChooseRuleId(chooseRuleDTO.getId());
        chooseRuleProviderResponse.setBookListId(chooseRuleDTO.getBookListId());
        chooseRuleProviderResponse.setCategory(chooseRuleDTO.getCategory());
        chooseRuleProviderResponse.setFilterRule(chooseRuleDTO.getFilterRule());
        chooseRuleProviderResponse.setChooseType(chooseRuleDTO.getChooseType());
        chooseRuleProviderResponse.setChooseCondition(chooseRuleDTO.getChooseCondition());
        chooseRuleProviderResponse.setCreateTime(chooseRuleDTO.getCreateTime());
        chooseRuleProviderResponse.setUpdateTime(chooseRuleDTO.getUpdateTime());

        if (CollectionUtils.isEmpty(bookListGoodsDTOList)) {
            chooseRuleProviderResponse.setBookListGoodsList(new ArrayList<>());
        } else {
            List<BookListGoodsProviderResponse> bookListGoodsResponseList = new ArrayList<>();
            for (BookListGoodsDTO bookListGoodsResponseParam : bookListGoodsDTOList) {
                BookListGoodsProviderResponse bookListGoodsModel = new BookListGoodsProviderResponse();
                BeanUtils.copyProperties(bookListGoodsResponseParam, bookListGoodsModel);
                bookListGoodsResponseList.add(bookListGoodsModel);
            }
            chooseRuleProviderResponse.setBookListGoodsList(bookListGoodsResponseList);
        }
        return chooseRuleProviderResponse;
    }


//    public ChooseRuleProviderResponse findById(Integer chooseRuleId) {
//        ChooseRuleRequest chooseRuleRequest = new ChooseRuleRequest();
//        chooseRuleRequest.setId(chooseRuleId);
//        ChooseRuleDTO chooseRuleDTO = chooseRuleService.findByCondition(chooseRuleRequest);
//        if (chooseRuleDTO == null) {
//            throw new SbcRuntimeException("chooseRuleId 不存在");
//        }
//        List<BookListGoodsDTO> bookListGoodsDTOList = bookListGoodsService.list(chooseRuleId);
//
//        ChooseRuleProviderResponse chooseRuleProviderResponse = new ChooseRuleProviderResponse();
//        chooseRuleProviderResponse.setChooseRuleId(chooseRuleDTO.getId());
//        chooseRuleProviderResponse.setBookListId(chooseRuleDTO.getBookListId());
//        chooseRuleProviderResponse.setCategory(chooseRuleDTO.getCategory());
//        chooseRuleProviderResponse.setFilterRule(chooseRuleDTO.getFilterRule());
//        chooseRuleProviderResponse.setChooseType(chooseRuleDTO.getChooseType());
//        chooseRuleProviderResponse.setChooseCondition(chooseRuleDTO.getChooseCondition());
//        chooseRuleProviderResponse.setCreateTime(chooseRuleDTO.getCreateTime());
//        chooseRuleProviderResponse.setUpdateTime(chooseRuleDTO.getUpdateTime());
//
//        if (CollectionUtils.isEmpty(bookListGoodsDTOList)) {
//            chooseRuleProviderResponse.setBookListGoodsList(new ArrayList<>());
//        } else {
//            List<BookListGoodsProviderResponse> bookListGoodsResponseList = new ArrayList<>();
//            for (BookListGoodsDTO bookListGoodsResponseParam : bookListGoodsDTOList) {
//                BookListGoodsProviderResponse bookListGoodsModel = new BookListGoodsProviderResponse();
//                BeanUtils.copyProperties(bookListGoodsResponseParam, bookListGoodsModel);
//                bookListGoodsResponseList.add(bookListGoodsModel);
//            }
//            chooseRuleProviderResponse.setBookListGoodsList(bookListGoodsResponseList);
//        }
//        return chooseRuleProviderResponse;
//    }

//
//    public void sort(List<BookListGoodsSortProviderRequest> bookListGoodsSortProviderRequestList) {
//        bookListGoodsService.sort(bookListGoodsSortProviderRequestList);
//    }


}
