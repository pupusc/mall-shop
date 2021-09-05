package com.wanmi.sbc.goods.booklistmodel.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.booklistgoodspublish.service.BookListGoodsPublishService;
import com.wanmi.sbc.goods.booklistmodel.model.root.BookListModelDTO;
import com.wanmi.sbc.goods.booklistmodel.repository.BookListModelRepository;
import com.wanmi.sbc.goods.booklistmodel.request.BookListModelPageRequest;
import com.wanmi.sbc.goods.booklistmodel.request.BookListModelRequest;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
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

    @Resource
    private BookListGoodsPublishService bookListGoodsPublishService;

    /**
     * 新增 书单模板
     * @return
     */
    public BookListModelDTO add(BookListModelRequest bookListModelRequest, String operator) {
        log.info("bookListModel.add operator: {} ", operator);
        BookListModelDTO bookListModelDTO = new BookListModelDTO();
        BeanUtils.copyProperties(bookListModelRequest, bookListModelDTO);
        bookListModelDTO.setId(null);
        bookListModelDTO.setCreateTime(new Date());
        bookListModelDTO.setUpdateTime(new Date());
        bookListModelDTO.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
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
        if (!bookListModelOptional.isPresent() || Objects.equals(bookListModelOptional.get().getDelFlag(), DeleteFlagEnum.DELETE.getCode())) {
            throw new SbcRuntimeException("书单" + bookListModelRequest.getId() + "不存在");
        }
        BookListModelDTO existBookListModelDtoUpdate = bookListModelOptional.get();
        existBookListModelDtoUpdate.setUpdateTime(new Date());
        existBookListModelDtoUpdate.setId(bookListModelRequest.getId());
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
    public void delete(Integer bookListModelId, String operator) {
        log.info("bookListModel.delete id {} by user: {}", bookListModelId, operator);
        Optional<BookListModelDTO> bookListModelOptional =
                bookListModelRepository.findById(bookListModelId);
        if (!bookListModelOptional.isPresent()) {
            log.error("bookListModel.delete id {} not exists ", bookListModelId);
            throw new SbcRuntimeException("书单" + bookListModelId + "不存在");
        }
        if (!Objects.equals(bookListModelOptional.get().getPublishState(), PublishStateEnum.UN_PUBLISH.getCode())) {
            throw new SbcRuntimeException("书单" + bookListModelId + "状态不是草稿，不能删除");
        }
        if (Objects.equals(bookListModelOptional.get().getDelFlag(), DeleteFlagEnum.DELETE.getCode())) {
            log.info("bookListModel.delete id {} already delete ", bookListModelId);
            return;
        }
        log.info("bookListModel.delete id{} result{}", bookListModelId, JSON.toJSONString(bookListModelOptional.get()));
        int result = bookListModelRepository.deleteBookListModelByCustomer(bookListModelId, bookListModelOptional.get().getVersion());
        if (result <= 0) {
            throw new SbcRuntimeException("书单" + bookListModelId + "删除中异常");
        }
    }

    /**
     * 发布书单
     * @param bookListModelId
     * @param operator
     */
    public void publish(Integer bookListModelId, String operator) {

        BookListModelDTO bookListModelObj = this.findById(bookListModelId);
        if (Objects.equals(bookListModelObj.getPublishState(), PublishStateEnum.PUBLISH.getCode())) {
            log.error("-------->> ChooseRuleGoodsListService.publish id:{} operator:{} publishState is already publish return",
                    bookListModelId, operator);
            return ;
        }
        bookListGoodsPublishService.publish(bookListModelId, PublishStateEnum.PUBLISH.getCode(), operator);
        log.info("----->>>ChooseRuleGoodsListService.publish bookListModelId:{} operator:{} complete"
                , bookListModelId, operator);

    }


    /**
     * 查询模板列表
     * @param bookListModelPageRequest
     * @return
     */
    public Page<BookListModelDTO> list(BookListModelPageRequest bookListModelPageRequest, int pageNum, int pageSize) {
        //查询数量
        Specification<BookListModelDTO> requestCondition = this.packageWhere(bookListModelPageRequest);
        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.Direction.DESC, "createTime");
        return bookListModelRepository.findAll(requestCondition, pageable);
    }


    /**
     * 根据id获取 书单模版
     * @param id
     * @return
     */
    public BookListModelDTO findById(Integer id) {
        Optional<BookListModelDTO> bookListModelDTOOptional = bookListModelRepository.findById(id);
        if (!bookListModelDTOOptional.isPresent()) {
            throw new SbcRuntimeException(String.format("bookListModel id: %s not exists", id));
        }
        BookListModelDTO bookListModelDTO = bookListModelDTOOptional.get();
        if (Objects.equals(bookListModelDTO.getDelFlag(), DeleteFlagEnum.DELETE.getCode())) {
            throw new SbcRuntimeException(String.format("bookListModel id: %s is delete", id));
        }
        log.info("bookListModel.findById id: {} is :{}", id, JSON.toJSONString(bookListModelDTO));
        return bookListModelDTO;
    }

    /**
     * condition
     * @param bookListModelPageRequest
     */
    private Specification<BookListModelDTO> packageWhere(BookListModelPageRequest bookListModelPageRequest) {
        return new Specification<BookListModelDTO>() {
            @Override
            public Predicate toPredicate(Root<BookListModelDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();

                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
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
    }
}
