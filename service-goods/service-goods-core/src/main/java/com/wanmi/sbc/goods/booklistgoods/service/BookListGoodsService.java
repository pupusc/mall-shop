package com.wanmi.sbc.goods.booklistgoods.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.request.booklistgoods.BookListGoodsProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistgoods.BookListGoodsSortProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistgoods.GoodsIdListProviderRequest;
import com.wanmi.sbc.goods.api.request.chooserule.ChooseRuleProviderRequest;
import com.wanmi.sbc.goods.booklistgoods.model.root.BookListGoodsDTO;
import com.wanmi.sbc.goods.booklistgoods.repository.BookListGoodsRepository;
import com.wanmi.sbc.goods.chooserule.model.root.ChooseRuleDTO;
import com.wanmi.sbc.goods.chooserule.service.ChooseRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/2 1:47 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class BookListGoodsService {

    @Resource
    private BookListGoodsRepository bookListGoodsRepository;

    @Autowired
    private ChooseRuleService chooseRuleService;

    /**
     * 生成批次
     * @return
     */
//    private String generateBatchId() {
//
//    }

    /**
     * 新增商品列表
     * @param bookListGoodsProviderRequest
     */
    @Transactional
    public void add(BookListGoodsProviderRequest bookListGoodsProviderRequest) {

        ChooseRuleProviderRequest chooseRuleProviderRequest =
                ChooseRuleProviderRequest.builder().id(bookListGoodsProviderRequest.getChooseRuleId()).build();
        ChooseRuleDTO chooseRuleDTO = chooseRuleService.findByCondition(chooseRuleProviderRequest);
        if (chooseRuleDTO == null) {
            log.error("BookListGoodsService.add get chooseRule by Id {}, return null", bookListGoodsProviderRequest.getChooseRuleId());
            throw new SbcRuntimeException(String.format("chooseRule id %s not exists", bookListGoodsProviderRequest.getChooseRuleId()));
        }
        List<BookListGoodsDTO> bookListGoodsDTOList = new ArrayList<>();
        for (GoodsIdListProviderRequest goodsIdListParam : bookListGoodsProviderRequest.getGoodsIdListProviderRequestList()) {
            BookListGoodsDTO bookListGoodsDTO = new BookListGoodsDTO();
            bookListGoodsDTO.setChooseRuleId(bookListGoodsProviderRequest.getChooseRuleId());
            bookListGoodsDTO.setBookListId(chooseRuleDTO.getBookListId());
            bookListGoodsDTO.setCategory(chooseRuleDTO.getCategory());
            bookListGoodsDTO.setSpuId(goodsIdListParam.getSpuId());
            bookListGoodsDTO.setSpuNo(goodsIdListParam.getSpuNo());
            bookListGoodsDTO.setSkuId(goodsIdListParam.getSkuId());
            bookListGoodsDTO.setSkuNo(goodsIdListParam.getSkuNo());
//            bookListGoodsDTO.setOrderNum();
            bookListGoodsDTO.setVersion(0);
            bookListGoodsDTO.setCreateTime(new Date());
            bookListGoodsDTO.setUpdateTime(new Date());
            bookListGoodsDTO.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
        }
        bookListGoodsRepository.saveAll(bookListGoodsDTOList);
    }


    /**
     * 排序
     * @param bookListGoodsSortProviderRequestList
     */
    @Transactional
    public void sort(List<BookListGoodsSortProviderRequest> bookListGoodsSortProviderRequestList) {
        //批量获取id
        Set<Integer> bookListGoodsIdSet =
                bookListGoodsSortProviderRequestList.stream().map(BookListGoodsSortProviderRequest::getId).collect(Collectors.toSet());
        List<BookListGoodsDTO> allById = bookListGoodsRepository.findAllById(bookListGoodsIdSet);
        List<BookListGoodsDTO> rawAllNormalBookListGoods =
                allById.stream().filter(ex -> Objects.equals(ex.getDelFlag(), DeleteFlagEnum.NORMAL.getCode())).collect(Collectors.toList());
        if (bookListGoodsSortProviderRequestList.size() != rawAllNormalBookListGoods.size()) {
            throw new IllegalArgumentException("请求参数数量和根据id获取结果的数据不想等，传递的数据有误");
        }

        Map<Integer, Integer> bookListGoodsSortMap =
                bookListGoodsSortProviderRequestList.stream().collect(Collectors.toMap(BookListGoodsSortProviderRequest::getId, BookListGoodsSortProviderRequest::getSortNum, (k1, k2) -> k1));
        if (rawAllNormalBookListGoods.size() != bookListGoodsSortMap.size()) {
            log.error(" rawAllNormalBookListGoods --> : {}", JSON.toJSONString(rawAllNormalBookListGoods));
            throw new IllegalArgumentException("对象 rawAllNormalBookListGoods 存在重复的id");
        }
        //重新排序
        Date now = new Date();
        for (BookListGoodsDTO bookListGoodsParam : rawAllNormalBookListGoods) {
            bookListGoodsParam.setUpdateTime(now);
            bookListGoodsParam.setOrderNum(bookListGoodsSortMap.getOrDefault(bookListGoodsParam.getId(), 0));
        }

        bookListGoodsRepository.saveAll(rawAllNormalBookListGoods);
    }


    public void update() {

    }


    public void list() {

    }


    private void packageWhere() {

    }
}
