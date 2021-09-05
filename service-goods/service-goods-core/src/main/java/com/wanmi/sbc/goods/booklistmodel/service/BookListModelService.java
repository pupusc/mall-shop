package com.wanmi.sbc.goods.booklistmodel.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListMixProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.ChooseRuleProviderResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.service.BookListGoodsPublishService;
import com.wanmi.sbc.goods.booklistmodel.model.root.BookListModelDTO;
import com.wanmi.sbc.goods.booklistmodel.repository.BookListModelRepository;
import com.wanmi.sbc.goods.booklistmodel.request.BookListModelPageRequest;
import com.wanmi.sbc.goods.chooserulegoodslist.service.ChooseRuleGoodsListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ChooseRuleGoodsListService chooseRuleGoodsListService;

    /**
     * 新增 书单模板
     * @return
     */
    @org.springframework.transaction.annotation.Transactional
    public void add(BookListMixProviderRequest bookListMixProviderRequest) {
        log.info("operator：{} BookListModelService.add BookListModel beginning", bookListMixProviderRequest.getOperator());
        BookListModelProviderRequest bookListModelRequest = bookListMixProviderRequest.getBookListModel();
        BookListModelDTO bookListModelParam = new BookListModelDTO();
        BeanUtils.copyProperties(bookListModelRequest, bookListModelParam);
        bookListModelParam.setId(null);
        bookListModelParam.setPublishState(PublishStateEnum.UN_PUBLISH.getCode());
        bookListModelParam.setCreateTime(new Date());
        bookListModelParam.setUpdateTime(new Date());
        bookListModelParam.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
        //新增对象信息
        BookListModelDTO bookListModelDTO = bookListModelRepository.save(bookListModelParam);
        log.info("operator：{} BookListModelService.add BookListModel complete result:{}",
                bookListMixProviderRequest.getOperator(), JSON.toJSONString(bookListModelDTO));

        chooseRuleGoodsListService.add(bookListMixProviderRequest.getChooseRuleGoodsListModel(), bookListMixProviderRequest.getOperator());
    }

    /**
     * 更新操作
     * @param bookListMixProviderRequest
     * @return
     */
    @org.springframework.transaction.annotation.Transactional
    public void update(BookListMixProviderRequest bookListMixProviderRequest) {
        log.info("operator：{} BookListModelService.update BookListModel beginning", bookListMixProviderRequest.getOperator());
        BookListModelProviderRequest bookListModelRequest = bookListMixProviderRequest.getBookListModel();
        if (bookListModelRequest.getId() == null || bookListModelRequest.getId() <= 0) {
            throw new SbcRuntimeException(String.format("书单:%s id有误", bookListModelRequest.getId()));
        }

        Optional<BookListModelDTO> bookListModelOptional = bookListModelRepository.findById(bookListModelRequest.getId());
        if (!bookListModelOptional.isPresent() || Objects.equals(bookListModelOptional.get().getDelFlag(), DeleteFlagEnum.DELETE.getCode())) {
            throw new SbcRuntimeException("书单" + bookListModelRequest.getId() + "不存在");
        }
        BookListModelDTO existBookListModelDtoUpdate = bookListModelOptional.get();
        existBookListModelDtoUpdate.setUpdateTime(new Date());
        existBookListModelDtoUpdate.setId(bookListModelRequest.getId());
        existBookListModelDtoUpdate.setPublishState(PublishStateEnum.EDIT_UN_PUBLISH.getCode());
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
        BookListModelDTO bookListModel = bookListModelRepository.save(existBookListModelDtoUpdate);
        log.info("operator：{} BookListModelService.update BookListModel complete result: {}",
                bookListMixProviderRequest.getOperator(), JSON.toJSONString(bookListModel));
        
        //修改控件和书单列表
        chooseRuleGoodsListService.update(bookListMixProviderRequest.getChooseRuleGoodsListModel(), bookListMixProviderRequest.getOperator());
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

        //todo  删除控件和商品列表


    }

    /**
     * 发布书单
     * @param bookListModelId
     * @param operator
     */
    public void publish(Integer bookListModelId, String operator) {

        BookListModelDTO bookListModelObj = this.findSimpleById(bookListModelId);
        if (Objects.equals(bookListModelObj.getPublishState(), PublishStateEnum.PUBLISH.getCode())) {
            log.error("-------->> ChooseRuleGoodsListService.publish id:{} operator:{} publishState is already publish return",
                    bookListModelId, operator);
            return ;
        }
        bookListGoodsPublishService.publish(bookListModelId, PublishStateEnum.PUBLISH.getCode(), operator);
        log.info("----->>>ChooseRuleGoodsListService.publish bookListModelId:{} operator:{} complete"
                , bookListModelId, operator);

        bookListModelObj.setPublishState(PublishStateEnum.PUBLISH.getCode());
        bookListModelObj.setUpdateTime(new Date());
        bookListModelRepository.save(bookListModelObj);
    }

    /**
     * 获取书单简单信息
     * @param id
     * @return
     */
    private BookListModelDTO findSimpleById(Integer id){
        Optional<BookListModelDTO> bookListModelDTOOptional = bookListModelRepository.findById(id);
        if (!bookListModelDTOOptional.isPresent()) {
            throw new SbcRuntimeException(String.format("bookListModel id: %s not exists", id));
        }
        BookListModelDTO bookListModelDTO = bookListModelDTOOptional.get();
        if (Objects.equals(bookListModelDTO.getDelFlag(), DeleteFlagEnum.DELETE.getCode())) {
            throw new SbcRuntimeException(String.format("bookListModel id: %s is delete", id));
        }
        return bookListModelDTO;
    }

    /**
     * 根据id获取 书单模版详细信息
     * @param id
     * @return
     */
    public BookListMixProviderResponse findById(Integer id) {

        BookListModelDTO bookListModelDTO = this.findSimpleById(id);
        BookListMixProviderResponse bookListMixProviderResponse = new BookListMixProviderResponse();

        BookListModelProviderResponse bookListModelProviderResponse = new BookListModelProviderResponse();
        BeanUtils.copyProperties(bookListModelDTO, bookListModelProviderResponse);
        bookListMixProviderResponse.setBookListModel(bookListModelProviderResponse);

        //获取控件信息和商品列表信息
        ChooseRuleProviderResponse chooseRuleProviderResponse =
                chooseRuleGoodsListService.findByCondition(bookListModelDTO.getId(), CategoryEnum.BOOK_LIST_MODEL.getCode());
        bookListMixProviderResponse.setChooseRuleMode(chooseRuleProviderResponse);
        return bookListMixProviderResponse;
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
