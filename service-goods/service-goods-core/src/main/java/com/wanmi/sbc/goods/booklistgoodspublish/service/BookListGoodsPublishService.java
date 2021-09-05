package com.wanmi.sbc.goods.booklistgoodspublish.service;

import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.booklistgoods.model.root.BookListGoodsDTO;
import com.wanmi.sbc.goods.booklistgoods.service.BookListGoodsService;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.repository.BookListGoodsPublishRepository;
import com.wanmi.sbc.goods.booklistmodel.service.BookListModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 2:27 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class BookListGoodsPublishService {

    @Resource
    private BookListGoodsPublishRepository bookListGoodsPublishRepository;

    @Resource
    private BookListGoodsService bookListGoodsService;

    @Resource
    private BookListModelService bookListModelService;

    @Transactional
    public void publish(Integer bookListId, Integer categoryId, String operator) {
        //获取书单模版对应的 商品列表,即待发布的列表
        List<BookListGoodsDTO> bookListGoodsDTOList = bookListGoodsService.list(bookListId, categoryId);
        if (CollectionUtils.isEmpty(bookListGoodsDTOList)) {
            log.error("-------->>> BookListGoodsPublishService.publish bookListId:{}, categoryId: {} is empty return", bookListId, CategoryEnum.BOOK_LIST_MODEL.getCode());
            return;
        }

        //删除已经发布的
        List<BookListGoodsPublishDTO> rawBookListGoodsPublishDTOList = this.list(bookListId, categoryId, null, operator);
        Date now = new Date();
        for (BookListGoodsPublishDTO bookListGoodsPublishParam : rawBookListGoodsPublishDTOList) {
            bookListGoodsPublishParam.setUpdateTime(now);
            bookListGoodsPublishParam.setDelFlag(DeleteFlagEnum.DELETE.getCode());
        }
        bookListGoodsPublishRepository.saveAll(rawBookListGoodsPublishDTOList);

        //新增已经发布的
        List<BookListGoodsPublishDTO> bookListGoodsPublishList = new ArrayList<>();
        for (BookListGoodsDTO bookListGoodsParam : bookListGoodsDTOList) {
            BookListGoodsPublishDTO bookListGoodsPublishModel = new BookListGoodsPublishDTO();
            BeanUtils.copyProperties(bookListGoodsParam, bookListGoodsPublishModel);
            bookListGoodsPublishList.add(bookListGoodsPublishModel);
        }
        bookListGoodsPublishRepository.saveAll(bookListGoodsPublishList);
    }

    /**
     * 发布商品列表
     * @param bookListId
     * @return
     */
    public List<BookListGoodsPublishDTO> list(Integer bookListId, Integer categoryId, String spu, String operator) {
        log.info("---->> BookListGoodsPublishService.list operator:{} bookListId:{} categoryId: {}",
                operator, bookListId, categoryId);
        return bookListGoodsPublishRepository.findAll(this.packageWhere(bookListId, categoryId, spu));
    }


    private Specification<BookListGoodsPublishDTO> packageWhere(Integer bookListId, Integer categoryId, String spu) {
        return new Specification<BookListGoodsPublishDTO>() {
            final List<Predicate> predicateList = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root<BookListGoodsPublishDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                predicateList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL));

                if (bookListId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("bookListId"), bookListId));
                }
                if (categoryId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("category"), categoryId));
                }
                if (!StringUtils.isEmpty(spu)) {
                    predicateList.add(criteriaBuilder.equal(root.get("spu"), spu));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }

}
