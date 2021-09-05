package com.wanmi.sbc.goods.booklistgoods.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.request.booklistmodel.GoodsIdListProviderRequest;
import com.wanmi.sbc.goods.api.request.chooserulegoodslist.BookListGoodsSortProviderRequest;
import com.wanmi.sbc.goods.booklistgoods.model.root.BookListGoodsDTO;
import com.wanmi.sbc.goods.booklistgoods.repository.BookListGoodsRepository;
import com.wanmi.sbc.goods.booklistgoods.request.BookListGoodsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * 新增商品列表
     * @param bookListGoodsRequest
     */
    public void add(BookListGoodsRequest bookListGoodsRequest) {
        AtomicInteger orderNum = new AtomicInteger();
        List<BookListGoodsDTO> bookListGoodsDTOList = new ArrayList<>();
        for (GoodsIdListProviderRequest goodsIdListParam : bookListGoodsRequest.getGoodsIdListRequestList()) {
            BookListGoodsDTO bookListGoodsDTO = new BookListGoodsDTO();
            bookListGoodsDTO.setChooseRuleId(bookListGoodsRequest.getChooseRuleId());
            bookListGoodsDTO.setBookListId(bookListGoodsRequest.getBookListId());
            bookListGoodsDTO.setCategory(bookListGoodsRequest.getCategory());
            bookListGoodsDTO.setSpuId(goodsIdListParam.getSpuId());
            bookListGoodsDTO.setSpuNo(goodsIdListParam.getSpuNo());
            bookListGoodsDTO.setSkuId(goodsIdListParam.getSkuId());
            bookListGoodsDTO.setSkuNo(goodsIdListParam.getSkuNo());
            bookListGoodsDTO.setOrderNum(orderNum.incrementAndGet());
            bookListGoodsDTO.setVersion(0);
            bookListGoodsDTO.setCreateTime(new Date());
            bookListGoodsDTO.setUpdateTime(new Date());
            bookListGoodsDTO.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
        }
        bookListGoodsRepository.saveAll(bookListGoodsDTOList);
    }

    /**
     * 更新
     * @param bookListGoodsRequest
     */
    public void update(BookListGoodsRequest bookListGoodsRequest) {
        //根据 chooseRuleId 获取所有的商品列表信息
        List<BookListGoodsDTO> bookListGoodsDTOList =
                bookListGoodsRepository.findAll(this.packageWhere(bookListGoodsRequest.getChooseRuleId(), null, null));
        //全部列表删除
        Date now = new Date();
        for (BookListGoodsDTO bookListGoodsParam : bookListGoodsDTOList) {
            bookListGoodsParam.setUpdateTime(now);
            bookListGoodsParam.setDelFlag(DeleteFlagEnum.DELETE.getCode());
        }

        bookListGoodsRepository.saveAll(bookListGoodsDTOList);
        this.add(bookListGoodsRequest);
    }


//    /**
//     * 排序
//     * @param bookListGoodsSortProviderRequestList
//     */
//    public void sort(List<BookListGoodsSortProviderRequest> bookListGoodsSortProviderRequestList) {
//        //批量获取id
//        Set<Integer> bookListGoodsIdSet =
//                bookListGoodsSortProviderRequestList.stream().map(BookListGoodsSortProviderRequest::getId).collect(Collectors.toSet());
//        List<BookListGoodsDTO> allById = bookListGoodsRepository.findAllById(bookListGoodsIdSet);
//        List<BookListGoodsDTO> rawAllNormalBookListGoods =
//                allById.stream().filter(ex -> Objects.equals(ex.getDelFlag(), DeleteFlagEnum.NORMAL.getCode())).collect(Collectors.toList());
//        if (bookListGoodsSortProviderRequestList.size() != rawAllNormalBookListGoods.size()) {
//            throw new IllegalArgumentException("请求参数数量和根据id获取结果的数据不想等，传递的数据有误");
//        }
//
//        Map<Integer, Integer> bookListGoodsSortMap =
//                bookListGoodsSortProviderRequestList.stream().collect(Collectors.toMap(BookListGoodsSortProviderRequest::getId, BookListGoodsSortProviderRequest::getSortNum, (k1, k2) -> k1));
//        if (rawAllNormalBookListGoods.size() != bookListGoodsSortMap.size()) {
//            log.error(" rawAllNormalBookListGoods --> : {}", JSON.toJSONString(rawAllNormalBookListGoods));
//            throw new IllegalArgumentException("对象 rawAllNormalBookListGoods 存在重复的id");
//        }
//        //重新排序
//        Date now = new Date();
//        for (BookListGoodsDTO bookListGoodsParam : rawAllNormalBookListGoods) {
//            bookListGoodsParam.setUpdateTime(now);
//            bookListGoodsParam.setOrderNum(bookListGoodsSortMap.getOrDefault(bookListGoodsParam.getId(), 0));
//        }
//
//        bookListGoodsRepository.saveAll(rawAllNormalBookListGoods);
//    }

//    /**
//     * 查询商品列表
//     * @return
//     */
//    public List<BookListGoodsDTO> list(Integer chooseRuleId) {
//
//        Sort orderNum = Sort.by(Sort.Direction.ASC, "orderNum");
//        return bookListGoodsRepository.findAll(this.packageWhere(chooseRuleId, null, null), orderNum);
//    }

    /**
     * 查询商品列表
     * @return
     */
    public List<BookListGoodsDTO> list(Integer bookListId, Integer categoryId) {
        Sort orderNum = Sort.by(Sort.Direction.ASC, "orderNum");
        return bookListGoodsRepository.findAll(this.packageWhere(null, bookListId, categoryId), orderNum);
    }


    private Specification<BookListGoodsDTO> packageWhere(Integer chooseRuleId, Integer bookListId, Integer categoryId) {
        return new Specification<BookListGoodsDTO>() {
            final List<Predicate> predicateList = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root<BookListGoodsDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                predicateList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL));

                if (chooseRuleId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("chooseRuleId"), chooseRuleId));
                }
                if (bookListId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("bookListId"), bookListId));
                }
                if (categoryId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("category"), categoryId));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}
