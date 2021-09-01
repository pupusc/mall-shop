package com.wanmi.sbc.goods.booklist.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.booklist.BookListModel;
import com.wanmi.sbc.goods.booklist.model.root.BookListModelDTO;
import com.wanmi.sbc.goods.booklist.repository.BookListModelRepository;
import com.wanmi.sbc.goods.booklist.request.BookListModelPageRequest;
import com.wanmi.sbc.goods.booklist.request.BookListModelRequest;
import com.wanmi.sbc.goods.util.GoodsConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/8/31 7:35 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Transactional
@Slf4j
public class BookListModelService {


    @Resource
    private BookListModelRepository bookListModelRepository;

    /**
     * 新增 书单模板
     * @return
     */
    public BookListModelDTO add(BookListModelRequest bookListModelRequest, String operator) {
        log.info("bookListModel.add operator: {} ", operator);
        bookListModelRequest.setId(null); //防止误传，导致不是新增而是更新操作
        BookListModelDTO bookListModelDTO = new BookListModelDTO();
        BeanUtils.copyProperties(bookListModelRequest, bookListModelDTO);
        //新增对象信息
        return bookListModelRepository.save(bookListModelDTO);
    }

    /**
     * 更新操作
     * @param bookListModelRequest
     * @return
     */
    public BookListModelDTO update(BookListModelRequest bookListModelRequest, String operator) {
        log.info("bookListModel.add id:{}, operator:{}", bookListModelRequest.getId(), operator);
        Optional<BookListModelDTO> bookListModelOptional =
                bookListModelRepository.findById(bookListModelRequest.getId());
        if (!bookListModelOptional.isPresent() || Objects.equals(bookListModelOptional.get().getDelFlag(), GoodsConstants.DELETE)) {
            throw new SbcRuntimeException("书单" + bookListModelRequest.getId() + "不存在");
        }
        BookListModelDTO existBookListModelDtoUpdate = bookListModelOptional.get();
        if (!StringUtils.isEmpty(bookListModelRequest.getName())) {
            existBookListModelDtoUpdate.setName(bookListModelRequest.getName());
        }
        if (!StringUtils.isEmpty(bookListModelRequest.getDesc())) {
            existBookListModelDtoUpdate.setDesc(bookListModelRequest.getDesc());
        }
        if (bookListModelRequest.getBusinessType() != null) {
            existBookListModelDtoUpdate.setBusinessType(bookListModelRequest.getBusinessType());
        }
        if (!StringUtils.isEmpty(bookListModelRequest.getHeadImgUrl())) {
            existBookListModelDtoUpdate.setHeadImgUrl(bookListModelRequest.getHeadImgUrl());
        }
        if (!StringUtils.isEmpty(bookListModelRequest.getHeadImgHref())) {
            existBookListModelDtoUpdate.setHeadImgHref(bookListModelRequest.getHeadImgHref());
        }
        if (!StringUtils.isEmpty(bookListModelRequest.getPageHref())) {
            existBookListModelDtoUpdate.setPageHref(bookListModelRequest.getPageHref());
        }
        if (bookListModelRequest.getPublishState() != null) {
            existBookListModelDtoUpdate.setPublishState(bookListModelRequest.getPublishState());
        }
        return bookListModelRepository.save(existBookListModelDtoUpdate);
    }

    /**
     * 删除订单模板
     * @param bookListModelId
     * @return
     */
    public Boolean delete(Integer bookListModelId, String operater) {
        log.info("bookListModel.delete id {} by user: {}", bookListModelId, operater);
        Optional<BookListModelDTO> bookListModelOptional =
                bookListModelRepository.findById(bookListModelId);
        if (!bookListModelOptional.isPresent()) {
            log.error("bookListModel.delete id {} not exists ", bookListModelId);
            throw new SbcRuntimeException("书单" + bookListModelId + "不存在");
        }
        if (Objects.equals(bookListModelOptional.get().getDelFlag(), GoodsConstants.DELETE)) {
            log.error("bookListModel.delete id {} already delete ", bookListModelId);
            return true;
        }

        Integer result = bookListModelRepository.deleteBookListModelByCustomer(bookListModelId, bookListModelOptional.get().getVersion());
        return result > 0;
    }


    /**
     * 查询模板列表
     * @param bookListModelPageRequest
     * @return
     */
    public Page<BookListModelDTO> list(BookListModelPageRequest bookListModelPageRequest) {
        //查询数量
        Specification<BookListModelDTO> requestCondition = this.packageWhere(bookListModelPageRequest);
        Pageable pageable = PageRequest.of(bookListModelPageRequest.getPageNum(), bookListModelPageRequest.getPageSize(),
                Sort.Direction.ASC, "createTime");
        return bookListModelRepository.findAll(requestCondition, pageable);
    }

    /**
     * condition
     * @param bookListModelPageRequest
     */
    private Specification<BookListModelDTO> packageWhere(BookListModelPageRequest bookListModelPageRequest) {
        Specification<BookListModelDTO> specificationWhere = new Specification<BookListModelDTO>() {
            @Override
            public Predicate toPredicate(Root<BookListModelDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> conditionList = new ArrayList<>();

                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), GoodsConstants.NORMAL));
                if (bookListModelPageRequest.getId() != null) {
                    conditionList.add(criteriaBuilder.equal(root.get("id"), bookListModelPageRequest.getId()));
                }

                if (!StringUtils.isEmpty(bookListModelPageRequest.getName())) {
                    conditionList.add(criteriaBuilder.like(root.get("name"), bookListModelPageRequest.getName() + "%"));
                }

                if (bookListModelPageRequest.getPublishState() != null) {
                    conditionList.add(criteriaBuilder.equal(root.get("publishState"), bookListModelPageRequest.getPublishState()));
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
            }
        };
        return specificationWhere;
    }
}
