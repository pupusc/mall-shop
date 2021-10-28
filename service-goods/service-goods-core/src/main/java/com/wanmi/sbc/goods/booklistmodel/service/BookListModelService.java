package com.wanmi.sbc.goods.booklistmodel.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.enums.HasTopEnum;
import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListMixProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelBySpuIdCollQueryRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.request.chooserulegoodslist.ChooseRuleGoodsListProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelGoodsIdProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelIdAndClassifyIdProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.ChooseRuleProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifySimpleProviderResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.service.BookListGoodsPublishService;
import com.wanmi.sbc.goods.booklistmodel.model.root.BookListModelDTO;
import com.wanmi.sbc.goods.booklistmodel.repository.BookListModelRepository;
import com.wanmi.sbc.goods.booklistmodel.request.BookListModelPageRequest;
import com.wanmi.sbc.goods.chooserulegoodslist.service.ChooseRuleGoodsListService;
import com.wanmi.sbc.goods.classify.model.root.BookListModelClassifyRelDTO;
import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import com.wanmi.sbc.goods.classify.request.BookListModelClassifyRelRequest;
import com.wanmi.sbc.goods.classify.service.BookListModelClassifyRelService;
import com.wanmi.sbc.goods.classify.service.ClassifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


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

    @Autowired
    private BookListModelClassifyRelService bookListModelClassifyRelService;

    @Autowired
    private BusinessTypeBookListModelFactory businessTypeBookListModelFactory;

    @Autowired
    private ClassifyService classifyService;



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
        bookListModelParam.setHasTop(HasTopEnum.NO.getCode());
        bookListModelParam.setPublishState(PublishStateEnum.UN_PUBLISH.getCode());
        bookListModelParam.setCreateTime(new Date());
        bookListModelParam.setUpdateTime(new Date());
        bookListModelParam.setDelFlag(DeleteFlagEnum.NORMAL.getCode());

        //新增书单模板
        BookListModelDTO bookListModelDTO = bookListModelRepository.save(bookListModelParam);
        log.info("operator：{} BookListModelService.add BookListModel complete result:{}",
                bookListMixProviderRequest.getOperator(), JSON.toJSONString(bookListModelDTO));

        //新增类目关系
        BookListModelClassifyRelRequest bookListModelClassifyRequest = new BookListModelClassifyRelRequest();
        bookListModelClassifyRequest.setBookListModelId(bookListModelDTO.getId());
        bookListModelClassifyRequest.setClassifyIdList(bookListModelRequest.getClassifyList());
        bookListModelClassifyRelService.change(bookListModelClassifyRequest);
        @Valid @NotNull ChooseRuleGoodsListProviderRequest chooseRuleGoodsListModel = bookListMixProviderRequest.getChooseRuleGoodsListModel();
        if (chooseRuleGoodsListModel == null) {
            log.info("operator：{} BookListModelService.add BookListModel complete result:{}",
                    bookListMixProviderRequest.getOperator(), JSON.toJSONString(bookListModelDTO));
            throw new SbcRuntimeException("新增书单异常，bookListMixProviderRequest.getChooseRuleGoodsListModel 为空");
        } else {
            chooseRuleGoodsListModel.setCategory(CategoryEnum.BOOK_LIST_MODEL.getCode());
            chooseRuleGoodsListModel.setBookListId(bookListModelDTO.getId());
            chooseRuleGoodsListService.add(chooseRuleGoodsListModel, bookListMixProviderRequest.getOperator());
        }


    }

    /**
     * 更新操作
     * @param bookListMixProviderRequest
     * @return
     */
    @org.springframework.transaction.annotation.Transactional
    public void update(BookListMixProviderRequest bookListMixProviderRequest) {
        log.info("operator：{} BookListModelService.update BookListModel beginning param:{}",
                bookListMixProviderRequest.getOperator(), JSON.toJSONString(bookListMixProviderRequest));
        BookListModelProviderRequest bookListModelRequest = bookListMixProviderRequest.getBookListModel();
        if (bookListModelRequest.getId() == null) {
            log.info("operator：{} BookListModelService.update chooseRuleGoodsListModel is null 控件信息不做修改", bookListMixProviderRequest.getOperator());
            return;
        }
        if (bookListModelRequest.getId() <= 0) {
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
        if (!StringUtils.isEmpty(bookListModelRequest.getFamousName())) {
            existBookListModelDtoUpdate.setFamousName(bookListModelRequest.getFamousName());
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
        if (!StringUtils.isEmpty(bookListModelRequest.getHeadSquareImgUrl())) {
            existBookListModelDtoUpdate.setHeadSquareImgUrl(bookListModelRequest.getHeadSquareImgUrl());
        }
        if (!StringUtils.isEmpty(bookListModelRequest.getHeadImgHref())) {
            existBookListModelDtoUpdate.setHeadImgHref(bookListModelRequest.getHeadImgHref());
        }
        if (!StringUtils.isEmpty(bookListModelRequest.getPageHref())) {
            existBookListModelDtoUpdate.setPageHref(bookListModelRequest.getPageHref());
        }
        if (bookListModelRequest.getHasTop() != null) {
            existBookListModelDtoUpdate.setHasTop(bookListModelRequest.getHasTop());
        }
        if (bookListModelRequest.getTagType() != null) {
            existBookListModelDtoUpdate.setTagType(bookListModelRequest.getTagType());
        }
        if (!StringUtils.isEmpty(bookListModelRequest.getTagName())) {
            existBookListModelDtoUpdate.setTagName(bookListModelRequest.getTagName());
        }
        if (bookListModelRequest.getTagValidBeginTime() != null) {
            existBookListModelDtoUpdate.setTagValidBeginTime(bookListModelRequest.getTagValidBeginTime());
        }
        if (bookListModelRequest.getTagValidEndTime() != null) {
            existBookListModelDtoUpdate.setTagValidEndTime(bookListModelRequest.getTagValidEndTime());
        }

        BookListModelDTO bookListModel = bookListModelRepository.save(existBookListModelDtoUpdate);
        log.info("operator：{} BookListModelService.update BookListModel complete result: {}",
                bookListMixProviderRequest.getOperator(), JSON.toJSONString(bookListModel));

        //修改类目
        if (!CollectionUtils.isEmpty(bookListModelRequest.getClassifyList())){
            BookListModelClassifyRelRequest bookListModelClassifyRequest = new BookListModelClassifyRelRequest();
            bookListModelClassifyRequest.setBookListModelId(bookListModelRequest.getId());
            bookListModelClassifyRequest.setClassifyIdList(bookListModelRequest.getClassifyList());
            bookListModelClassifyRelService.change(bookListModelClassifyRequest);
        }

        /**
         * 修改控件和商品列表
         */
        if (bookListMixProviderRequest.getChooseRuleGoodsListModel() != null) {
            bookListMixProviderRequest.getChooseRuleGoodsListModel().setBookListId(bookListModelRequest.getId());
            if (CategoryEnum.getByCode(bookListMixProviderRequest.getChooseRuleGoodsListModel().getCategory()) == null) {
                log.error("operator：{} BookListModelService.update chooseRuleGoodsListModel category {} not exists",
                        bookListMixProviderRequest.getOperator(), bookListMixProviderRequest.getChooseRuleGoodsListModel().getCategory());
                throw new SbcRuntimeException("修改空间和商品列表中 传递的 categoryId 不存在");
            }
            //修改控件和书单列表
            chooseRuleGoodsListService.update(bookListMixProviderRequest.getChooseRuleGoodsListModel(), bookListMixProviderRequest.getOperator());
        }



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
    @org.springframework.transaction.annotation.Transactional
    public void publish(Integer bookListModelId, String operator) {

        BookListModelDTO bookListModelObj = this.findSimpleById(bookListModelId);
        if (Objects.equals(bookListModelObj.getPublishState(), PublishStateEnum.PUBLISH.getCode())) {
            log.error("-------->> ChooseRuleGoodsListService.publish id:{} operator:{} publishState is already publish return",
                    bookListModelId, operator);
            return ;
        }
        bookListGoodsPublishService.publish(bookListModelId, CategoryEnum.BOOK_LIST_MODEL.getCode(), operator);
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
    public BookListModelDTO findSimpleById(Integer id){
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
     * 根据id获取 书单模版详细信息【这里是获取的书单不一定发布】
     * @param bookListModelId
     * @return
     */
    public BookListMixProviderResponse findById(Integer bookListModelId) {

        BookListModelDTO bookListModelDTO = this.findSimpleById(bookListModelId);
        BookListMixProviderResponse bookListMixProviderResponse = new BookListMixProviderResponse();

        BookListModelProviderResponse bookListModelProviderResponse = new BookListModelProviderResponse();
        BeanUtils.copyProperties(bookListModelDTO, bookListModelProviderResponse);
        bookListMixProviderResponse.setBookListModel(bookListModelProviderResponse);

        //获取商品分类信息
        List<BookListModelClassifyRelDTO> bookListModelClassifyRelList = bookListModelClassifyRelService.listNoPage(bookListModelId);
        if (CollectionUtils.isEmpty(bookListModelClassifyRelList)) {
            log.error("--->>> BookListModelService.findById bookListModelId: {} 店铺分类关系表为空", bookListModelId);
            throw new SbcRuntimeException(String.format(" 根据书单模版id %s 查询书单的商品分类为空", bookListModelId));
        }
        List<Integer> classifyIdList = bookListModelClassifyRelList.stream().map(BookListModelClassifyRelDTO::getClassifyId).collect(Collectors.toList());
        List<ClassifyDTO> classifyList = classifyService.listNoPage(classifyIdList);
        List<ClassifySimpleProviderResponse> classifyResponseList = new ArrayList<>();
        for (ClassifyDTO classifyParam : classifyList) {
            ClassifySimpleProviderResponse classifySimpleProviderResponse = new ClassifySimpleProviderResponse();
            BeanUtils.copyProperties(classifyParam, classifySimpleProviderResponse);
            classifyResponseList.add(classifySimpleProviderResponse);
        }
        bookListMixProviderResponse.setClassifyList(classifyResponseList);
        //获取控件信息和商品列表信息
        ChooseRuleProviderResponse chooseRuleProviderResponse =
                chooseRuleGoodsListService.findRuleAndGoodsByCondition(bookListModelDTO.getId(), CategoryEnum.BOOK_LIST_MODEL.getCode());
        bookListMixProviderResponse.setChooseRuleMode(chooseRuleProviderResponse);
        return bookListMixProviderResponse;
    }


    /**
     * 批量获取书单模版详细信息 【这里的书单列表都是已经发布的】
     * @param bookListModelIdCollection
     */
    public List<BookListMixProviderResponse> listPublishGoodsByModelIds(Collection<Integer> bookListModelIdCollection, CategoryEnum categoryEnum) {
        List<BookListMixProviderResponse> result = new ArrayList<>();

        //获取有效的书单
        BookListModelPageRequest bookListModelPageRequest = new BookListModelPageRequest();
        bookListModelPageRequest.setIdCollection(bookListModelIdCollection);
        List<BookListModelDTO> bookListModelList = bookListModelRepository.findAll(this.packageWhere(bookListModelPageRequest));
        if (CollectionUtils.isEmpty(bookListModelList)) {
            return result;
        }

        //封装书单信息
        for (BookListModelDTO bookListModelParam : bookListModelList) {
            BookListMixProviderResponse bookListMixProviderResponse = new BookListMixProviderResponse();
            BookListModelProviderResponse bookListModelProviderResponse = new BookListModelProviderResponse();
            BeanUtils.copyProperties(bookListModelParam, bookListModelProviderResponse);
            bookListMixProviderResponse.setBookListModel(bookListModelProviderResponse);
            result.add(bookListMixProviderResponse);
        }

        //获取有效书单列表
        Set<Integer> bookListModelIdSet = bookListModelList.stream().map(BookListModelDTO::getId).collect(Collectors.toSet());
        List<ChooseRuleProviderResponse> ruleAndPublishGoodsByConditionList =
                chooseRuleGoodsListService.findRuleAndPublishGoodsByCondition(bookListModelIdSet, categoryEnum.getCode());
        if (CollectionUtils.isEmpty(ruleAndPublishGoodsByConditionList)) {
            return result;
        }
        Map<Integer, ChooseRuleProviderResponse> collect =
                ruleAndPublishGoodsByConditionList.stream().collect(Collectors.toMap(ChooseRuleProviderResponse::getBookListId, Function.identity(), (k1, k2) -> k1));
        //书单和有效书列表匹配
        for (BookListMixProviderResponse bookListMixProviderParam : result) {
            ChooseRuleProviderResponse chooseRuleProviderModel = collect.get(bookListMixProviderParam.getBookListModel().getId());
            if (chooseRuleProviderModel == null) {
                log.error("---> BookListModelService.listPublishGoodsByIds param: {}, chooseRuleProviderModel is null error ",
                        JSON.toJSONString(bookListMixProviderParam));
                continue;
            }
            bookListMixProviderParam.setChooseRuleMode(chooseRuleProviderModel);
        }

        return result;
    }


    /**
     * 查询模板列表
     * @param bookListModelPageRequest
     * @return
     */
    public Page<BookListModelDTO> list(BookListModelPageRequest bookListModelPageRequest, int pageNum, int pageSize) {
        //查询数量
        Specification<BookListModelDTO> requestCondition = this.packageWhere(bookListModelPageRequest);
        Sort sort = Sort.by(Sort.Direction.DESC, "hasTop").and(Sort.by(Sort.Direction.DESC, "updateTime"));
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return bookListModelRepository.findAll(requestCondition, pageable);
    }


    /**
     * 获取书单对应不同业务类型的数据信息
     * @param spuId
     * @return
     */
    public List<BookListModelAndOrderNumProviderResponse> listBusinessTypeBookListModel(String spuId, Integer businessTypeId, Integer size){
        BusinessTypeBookListModelAbstract invoke = businessTypeBookListModelFactory.newInstance(BusinessTypeEnum.getByCode(businessTypeId));
        return invoke.listBookListModelAndOrderNum(spuId, size);
    }


    /**
     * 更多书单 榜单
     * @param bookListModelId
     * @param businessTypeId
     * @param size
     * @return
     */
    public List<BookListModelIdAndClassifyIdProviderResponse> listBookListModelMore(Integer bookListModelId, Integer businessTypeId, Integer size) {
        BusinessTypeBookListModelAbstract invoke = businessTypeBookListModelFactory.newInstance(BusinessTypeEnum.getByCode(businessTypeId));
        return invoke.listBookListModelMore(bookListModelId, size);
    }


    /**
     * 根据商品列表，获取商品列表对应的书单
     * @param bookListModelBySpuIdCollQueryRequest
     * @return
     */
    public List<BookListModelGoodsIdProviderResponse> listBookListModelNoPageBySpuIdColl(BookListModelBySpuIdCollQueryRequest bookListModelBySpuIdCollQueryRequest) {
        List<BookListModelGoodsIdProviderResponse> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(bookListModelBySpuIdCollQueryRequest.getBusinessTypeList())
                || CollectionUtils.isEmpty(bookListModelBySpuIdCollQueryRequest.getSpuIdCollection())) {
            return result;
        }

//        if (businessTypeList.isEmpty()) {
//            businessTypeList = Arrays.asList(BusinessTypeEnum.RANKING_LIST.getCode(), BusinessTypeEnum.BOOK_LIST.getCode(), BusinessTypeEnum.BOOK_RECOMMEND.getCode());
//        }
        //根据spuId 获取书单
        List<BookListGoodsPublishLinkModelResponse> bookListGoodsPublishLinkModelList = bookListGoodsPublishService.listPublishGoodsAndBookListModelBySpuId(
                bookListModelBySpuIdCollQueryRequest.getBusinessTypeList(), bookListModelBySpuIdCollQueryRequest.getSpuIdCollection());
        if (CollectionUtils.isEmpty(bookListGoodsPublishLinkModelList)) {
            return result;
        }
        bookListGoodsPublishLinkModelList.forEach(ex -> {
            BookListModelGoodsIdProviderResponse param = new BookListModelGoodsIdProviderResponse();
            param.setId(ex.getBookListModelId());
            param.setName(ex.getName());
            param.setDesc(ex.getDesc());
            param.setBusinessType(ex.getBusinessType());
            param.setHeadImgUrl(ex.getHeadImgUrl());
            param.setHeadImgHref(ex.getHeadImgUrl());
            param.setPageHref(ex.getPageHref());
            param.setPublishState(ex.getPublishState());
            param.setCreateTime(ex.getCreateTime());
            param.setUpdateTime(ex.getUpdateTime());
            param.setSpuId(ex.getSpuId());
            param.setSpuNo(ex.getSpuNo());
            param.setSkuId(ex.getSkuId());
            param.setSkuNo(ex.getSkuNo());
            param.setErpGoodsNo(ex.getErpGoodsNo());
            param.setErpGoodsInfoNo(ex.getErpGoodsInfoNo());
            param.setOrderNum(ex.getOrderNum());
            result.add(param);
        });
        return result;
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

//                if (bookListModelPageRequest.getPublishState() != null) {
//                    conditionList.add(criteriaBuilder.equal(root.get("publishState"), bookListModelPageRequest.getPublishState()));
//                }
//
//                if (bookListModelPageRequest.getBusinessType() != null) {
//                    conditionList.add(criteriaBuilder.equal(root.get("businessType"), bookListModelPageRequest.getBusinessType()));
//                }

                if (!CollectionUtils.isEmpty(bookListModelPageRequest.getBusinessTypeList())) {
                    conditionList.add(root.get("businessType").in(bookListModelPageRequest.getBusinessTypeList()));
                }

                if (!CollectionUtils.isEmpty(bookListModelPageRequest.getPublishStateList())) {
                    conditionList.add(root.get("publishState").in(bookListModelPageRequest.getPublishStateList()));
                }

                if (!CollectionUtils.isEmpty(bookListModelPageRequest.getIdCollection())) {
                    conditionList.add(root.get("id").in(bookListModelPageRequest.getIdCollection()));
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
            }
        };
    }


    /**
     * 置顶或取消 feature_d_v0.02
     * @param bookListModelId
     */
    @org.springframework.transaction.annotation.Transactional
    public BookListModelDTO top(Integer bookListModelId, Integer hasTop) {

        BookListModelDTO bookListModelObj = this.findSimpleById(bookListModelId);
        if (Objects.equals(bookListModelObj.getHasTop(), hasTop)) {
            return bookListModelObj;
        }
        bookListModelObj.setHasTop(hasTop);
        bookListModelObj.setUpdateTime(new Date());
        BookListModelDTO bookListModelDTO = bookListModelRepository.save(bookListModelObj);
        log.info("----->>>bookListModel.top bookListModelId:{} hasTop:{} complete", bookListModelId, hasTop);
        return bookListModelDTO;
    }

    /**
     * 获取已发布的书单简单信息
     * @return
     */
    public List<BookListModelDTO> findPublishBook(){
        List<BookListModelDTO> bookListModelDTOS = bookListModelRepository.findPublishBook();
        return bookListModelDTOS;
    }
}
