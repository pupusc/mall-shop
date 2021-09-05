package com.wanmi.sbc.goods.chooserulegoodslist.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.api.request.chooserulegoodslist.BookListGoodsSortProviderRequest;
import com.wanmi.sbc.goods.api.request.chooserulegoodslist.ChooseRuleGoodsListProviderRequest;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.BookListGoodsProviderResponse;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.ChooseRuleProviderResponse;
import com.wanmi.sbc.goods.booklistgoods.model.root.BookListGoodsDTO;
import com.wanmi.sbc.goods.booklistgoods.request.BookListGoodsRequest;
import com.wanmi.sbc.goods.booklistgoods.service.BookListGoodsService;
import com.wanmi.sbc.goods.booklistmodel.model.root.BookListModelDTO;
import com.wanmi.sbc.goods.booklistmodel.request.BookListModelRequest;
import com.wanmi.sbc.goods.booklistmodel.service.BookListModelService;
import com.wanmi.sbc.goods.chooserule.model.root.ChooseRuleDTO;
import com.wanmi.sbc.goods.chooserule.request.ChooseRuleRequest;
import com.wanmi.sbc.goods.chooserule.service.ChooseRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private BookListModelService bookListModelService;


    @Transactional
    public void add(ChooseRuleGoodsListProviderRequest chooseRuleGoodsListProviderRequest) {
        ChooseRuleRequest chooseRuleRequest = new ChooseRuleRequest();
        chooseRuleRequest.setBookListId(chooseRuleGoodsListProviderRequest.getBookListId());
        chooseRuleRequest.setFilterRule(chooseRuleGoodsListProviderRequest.getFilterRule());
        chooseRuleRequest.setCategory(chooseRuleGoodsListProviderRequest.getCategory());
        chooseRuleRequest.setChooseType(chooseRuleGoodsListProviderRequest.getChooseType());
        chooseRuleRequest.setChooseCondition(chooseRuleGoodsListProviderRequest.getChooseCondition());
        chooseRuleRequest.setOperator(chooseRuleGoodsListProviderRequest.getOperator());
        ChooseRuleDTO chooseRuleDTO = chooseRuleService.add(chooseRuleRequest);
        log.info("-------->> ChooseRuleGoodsListService.add chooseRule complete");

        BookListGoodsRequest bookListGoodsRequest = new BookListGoodsRequest();
        bookListGoodsRequest.setChooseRuleId(chooseRuleDTO.getId());
        bookListGoodsRequest.setBookListId(chooseRuleDTO.getBookListId());
        bookListGoodsRequest.setCategory(chooseRuleDTO.getCategory());
        bookListGoodsRequest.setGoodsIdListProviderRequestList(chooseRuleGoodsListProviderRequest.getGoodsIdListProviderRequestList());
        bookListGoodsService.add(bookListGoodsRequest);
    }


    @Transactional
    public void update(ChooseRuleGoodsListProviderRequest chooseRuleGoodsListProviderRequest) {
        ChooseRuleRequest chooseRuleRequest = new ChooseRuleRequest();
        chooseRuleRequest.setId(chooseRuleGoodsListProviderRequest.getChooseRuleId());
        chooseRuleRequest.setBookListId(chooseRuleGoodsListProviderRequest.getBookListId());
        chooseRuleRequest.setFilterRule(chooseRuleGoodsListProviderRequest.getFilterRule());
        chooseRuleRequest.setCategory(chooseRuleGoodsListProviderRequest.getCategory());
        chooseRuleRequest.setChooseType(chooseRuleGoodsListProviderRequest.getChooseType());
        chooseRuleRequest.setChooseCondition(chooseRuleGoodsListProviderRequest.getChooseCondition());
        chooseRuleRequest.setOperator(chooseRuleGoodsListProviderRequest.getOperator());
        ChooseRuleDTO chooseRuleUpdate = chooseRuleService.update(chooseRuleRequest);
        log.info("-------->> ChooseRuleGoodsListService.update operator: {} chooseRule complete",
                chooseRuleGoodsListProviderRequest.getOperator());

        BookListGoodsRequest bookListGoodsRequest = new BookListGoodsRequest();
        bookListGoodsRequest.setChooseRuleId(chooseRuleGoodsListProviderRequest.getChooseRuleId());
        bookListGoodsRequest.setGoodsIdListProviderRequestList(chooseRuleGoodsListProviderRequest.getGoodsIdListProviderRequestList());
        bookListGoodsService.update(bookListGoodsRequest);
        log.info("-------->> ChooseRuleGoodsListService.update operator: {} bookListGoods complete",
                chooseRuleGoodsListProviderRequest.getOperator());

        //如果类型为书单，更改书单类型为 编辑未发布
        if (chooseRuleUpdate.getCategory() != null && chooseRuleUpdate.getCategory() == CategoryEnum.BOOK_LIST_MODEL.getCode()) {
            BookListModelRequest bookListModelRequest = new BookListModelRequest();
            bookListModelRequest.setId(chooseRuleUpdate.getBookListId());
            bookListModelRequest.setPublishState(PublishStateEnum.EDIT_UN_PUBLISH.getCode());
            bookListModelService.update(bookListModelRequest, chooseRuleGoodsListProviderRequest.getOperator());
            log.info("-------->> ChooseRuleGoodsListService.update operator: {} bookListModel editUnPublish complete",
                    chooseRuleGoodsListProviderRequest.getOperator());
        }

    }

    /**
     * 获取 控件和商品列表
     * @param chooseRuleId
     * @return
     */
    public ChooseRuleProviderResponse findById(Integer chooseRuleId) {
        ChooseRuleRequest chooseRuleRequest = new ChooseRuleRequest();
        chooseRuleRequest.setId(chooseRuleId);
        ChooseRuleDTO chooseRuleDTO = chooseRuleService.findByCondition(chooseRuleRequest);
        if (chooseRuleDTO == null) {
            throw new SbcRuntimeException("chooseRuleId 不存在");
        }
        List<BookListGoodsDTO> bookListGoodsDTOList = bookListGoodsService.list();

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


    public void sort(List<BookListGoodsSortProviderRequest> bookListGoodsSortProviderRequestList) {
        bookListGoodsService.sort(bookListGoodsSortProviderRequestList);
    }

    
}
