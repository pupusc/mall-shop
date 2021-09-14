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
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
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
            bookListGoodsDTO.setErpGoodsNo(goodsIdListParam.getErpGoodsNo());
            bookListGoodsDTO.setErpGoodsInfoNo(goodsIdListParam.getErpGoodsInfoNo());
            bookListGoodsDTO.setOrderNum(orderNum.incrementAndGet());
            bookListGoodsDTO.setVersion(0);
            bookListGoodsDTO.setCreateTime(new Date());
            bookListGoodsDTO.setUpdateTime(new Date());
            bookListGoodsDTO.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
            bookListGoodsDTOList.add(bookListGoodsDTO);
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
                bookListGoodsRepository.findAll(this.packageWhere(bookListGoodsRequest.getChooseRuleId(), null, null, null));
        //全部列表删除
        Date now = new Date();
        for (BookListGoodsDTO bookListGoodsParam : bookListGoodsDTOList) {
            bookListGoodsParam.setUpdateTime(now);
            bookListGoodsParam.setDelFlag(DeleteFlagEnum.DELETE.getCode());
        }

        bookListGoodsRepository.saveAll(bookListGoodsDTOList);
        this.add(bookListGoodsRequest);
    }


    /**
     * 查询商品列表
     * @return
     */
    public List<BookListGoodsDTO> list(Collection<Integer> bookListIdCollection, Integer bookListId, Integer categoryId) {
        Sort orderNum = Sort.by(Sort.Direction.ASC, "orderNum");
        return bookListGoodsRepository.findAll(this.packageWhere(null, bookListId, categoryId, bookListIdCollection), orderNum);
    }


    private Specification<BookListGoodsDTO> packageWhere(Integer chooseRuleId, Integer bookListId, Integer categoryId, Collection<Integer> bookListIdCollection) {
        return new Specification<BookListGoodsDTO>() {
            final List<Predicate> predicateList = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root<BookListGoodsDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                predicateList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));

                if (chooseRuleId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("chooseRuleId"), chooseRuleId));
                }
                if (bookListId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("bookListId"), bookListId));
                }
                if (!CollectionUtils.isEmpty(bookListIdCollection)) {
                    predicateList.add(root.get("bookListId").in(bookListIdCollection));
                }
                if (categoryId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("category"), categoryId));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}
