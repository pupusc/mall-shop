package com.wanmi.sbc.setting.topicconfig.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.setting.api.request.topicconfig.*;
import com.wanmi.sbc.setting.api.response.TopicStoreyColumnGoodsResponse;
import com.wanmi.sbc.setting.api.response.TopicStoreyColumnResponse;
import com.wanmi.sbc.setting.api.response.TopicStoreyContentResponse;
import com.wanmi.sbc.setting.bean.dto.*;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyType;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyTypeV2;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
import com.wanmi.sbc.setting.topicconfig.model.root.*;
import com.wanmi.sbc.setting.topicconfig.repository.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TopicConfigService {
    @Autowired
    private TopicRepository topicSettingRepository;

    @Autowired
    private TopicHeadImageRepository topicHeadImageRepository;

    @Autowired
    private TopicStoreyRepository storeyRepository;

    @Autowired
    private TopicStoreyContentRepository contentRepository;

    @Autowired
    private TopicStoreyColumnRepository columnRepository;

    @Autowired
    private TopicStoreyColumnGoodsRepository columnGoodsRepository;

    public void addTopic(TopicConfigAddRequest request) {
        Topic topic = KsBeanUtil.convert(request, Topic.class);
        topic.setTopicKey(UUIDUtil.getUUID());
        topic.setTrackKey(topic.getTopicKey());
        topic.setCreateTime(LocalDateTime.now());
        topic.setUpdateTime(LocalDateTime.now());
        topic.setDeleted(DeleteFlag.NO.toValue());
        topic.setStatus(0);
        topicSettingRepository.save(topic);
    }

    public void modifyTopic(TopicConfigModifyRequest request) {
        Topic oldTopic = topicSettingRepository.getOne(request.getId());
        if(oldTopic == null){
            addTopic(request);
        }
        Topic topic = KsBeanUtil.convert(oldTopic, Topic.class);
        topic.setTopicName(request.getTopicName());
        topic.setTopicColor(request.getTopicColor());
        topic.setNavigationColor(request.getNavigationColor());
        topic.setUpdateTime(LocalDateTime.now());
        topicSettingRepository.save(topic);
    }

    public void enableTopic(EnableTopicRequest request) {
        topicSettingRepository.enable(request.getId(),request.getOptType());
    }

    public MicroServicePage<TopicConfigVO> listTopic(TopicQueryRequest request) {
        BaseQueryRequest pageQuery = new BaseQueryRequest();
        pageQuery.setPageNum(request.getPageNum());
        pageQuery.setPageSize(request.getPageSize());
        pageQuery.setSortColumn("createTime");
        pageQuery.setSortRole("desc");
        Page<Topic> page = topicSettingRepository.findAll(getTopicWhereCriteria(request), pageQuery.getPageRequest());
        if(page == null){
            return new MicroServicePage<>();
        }
        return KsBeanUtil.convertPage(page,TopicConfigVO.class);
    }


    public void addHeadImage(HeadImageConfigAddRequest request){
        TopicHeadImage headImage = KsBeanUtil.convert(request, TopicHeadImage.class);
        headImage.setCreateTime(LocalDateTime.now());
        headImage.setUpdateTime(LocalDateTime.now());
        headImage.setDeleted(DeleteFlag.NO.toValue());
        topicHeadImageRepository.save(headImage);
    }

    public void modifyHeadImage(TopicHeadImageModifyRequest request){
        TopicHeadImage headImage = KsBeanUtil.convert(request, TopicHeadImage.class);
        headImage.setCreateTime(LocalDateTime.now());
        headImage.setUpdateTime(LocalDateTime.now());
        headImage.setDeleted(DeleteFlag.NO.toValue());
        topicHeadImageRepository.save(headImage);
    }

    public List<TopicHeadImageDTO> listHeadImage(TopicHeadImageQueryRequest request){
        List<TopicHeadImage> list = topicHeadImageRepository.getByTopicId(request.getTopicId());
        if(CollectionUtils.isEmpty(list)){
            return Collections.EMPTY_LIST;
        }
        return KsBeanUtil.convertList(list,TopicHeadImageDTO.class);
    }

    public void deleteHeadImage(Integer id){
        topicHeadImageRepository.delById(id);
    }

    public void addStorey(TopicStoreyAddRequest request){
        TopicStorey storey = KsBeanUtil.convert(request, TopicStorey.class);
        storey.setCreateTime(LocalDateTime.now());
        storey.setUpdateTime(LocalDateTime.now());
        storey.setDeleted(DeleteFlag.NO.toValue());
        storey.setStatus(1);
        storeyRepository.save(storey);
    }

    public void modifyStorey(TopicStoreyModifyRequest request){
        TopicStorey oldStorey = storeyRepository.getOne(request.getId());
        if(oldStorey == null){
            addStorey(request);
        }
        TopicStorey storey = KsBeanUtil.convert(request, TopicStorey.class);
        storey.setCreateTime(oldStorey.getCreateTime());
        storey.setUpdateTime(LocalDateTime.now());
        storey.setDeleted(DeleteFlag.NO.toValue());
        storey.setStatus(1);
        storeyRepository.save(storey);
    }

    public List<TopicStoreyDTO> listStorey(TopicHeadImageQueryRequest request){
        List<TopicStorey> list = storeyRepository.getByTopicId(request.getTopicId());
        if(CollectionUtils.isEmpty(list)){
            return Collections.EMPTY_LIST;
        }
        return KsBeanUtil.convertList(list, TopicStoreyDTO.class);
    }
    
    public TopicActivityVO detail(String id){
        List<Topic> list = topicSettingRepository.getByKey(id);
        if(CollectionUtils.isEmpty(list)){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        Topic topic = list.get(0);
        if(Objects.equals(topic.getStatus(),0)){
            return null;
        }
        TopicActivityVO topicVO = new TopicActivityVO();
        topicVO.setTopicColor(topic.getTopicColor());
        topicVO.setNavigationColor(topic.getNavigationColor());
        topicVO.setTopicName(topic.getTopicName());
        List<TopicHeadImage> images = topicHeadImageRepository.getByTopicId(topic.getId());
        topicVO.setHeadImageList(KsBeanUtil.convertList(images, TopicHeadImageDTO.class));
        List<TopicStorey> storeys = storeyRepository.getAvailByTopicId(topic.getId());
        if(CollectionUtils.isEmpty(storeys)){
            return topicVO;
        }
        List<TopicStorey> sortStoreys = storeys.stream().sorted(Comparator.comparing(TopicStorey::getSorting)).collect(Collectors.toList());
        topicVO.setStoreyList(KsBeanUtil.convertList(sortStoreys, TopicStoreyDTO.class));
        List<TopicStoreyContent> contents = contentRepository.getByTopicId(topic.getId());
        if(CollectionUtils.isEmpty(contents)){
            return topicVO;
        }
        List<TopicStoreyContentDTO>  contentDTOS = KsBeanUtil.convertList(contents, TopicStoreyContentDTO.class);
        topicVO.getStoreyList().forEach(p -> {
            List<TopicStoreyContentDTO> items = contentDTOS.stream().filter(g -> g.getStoreyId().equals(p.getId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(items)) {
                return;
            }
            List<TopicStoreyContentDTO> itemList = new ArrayList<>(items.size());
            if (Arrays.asList(TopicStoreyType.HETERSCROLLIMAGE.getId(),TopicStoreyType.COUPON.getId()).contains(p.getStoreyType())) {
                //轮播类型或优惠券根据时间过滤
                itemList.addAll(items.stream().filter(i->i.getStartTime() == null ||  i.getEndTime() == null).collect(Collectors.toList()));
                itemList.addAll(items.stream().filter(i->i.getStartTime() != null && i.getEndTime() != null && i.getStartTime().compareTo(LocalDateTime.now()) <= 0 && i.getEndTime().compareTo(LocalDateTime.now()) >=0).collect(Collectors.toList()));
            }else{
                itemList = items;
            }

            initAttr(itemList,p.getStoreyType());
            //排序
            List<TopicStoreyContentDTO> sortContents = itemList.stream().sorted(Comparator.comparing(TopicStoreyContentDTO::getType).thenComparing(TopicStoreyContentDTO::getSorting)).collect(Collectors.toList());
            p.setContents(sortContents);

        });
        return topicVO;
    }

    private void initAttr(List<TopicStoreyContentDTO> contents,Integer storeyType){
        if(Objects.equals(TopicStoreyType.COUPON.getId(),storeyType) || Objects.equals(TopicStoreyTypeV2.VOUCHER.getId(),storeyType)){
            contents.forEach(item->{
                TopicStoreyCouponDTO couponDTO = JSONObject.parseObject(item.getAttributeInfo(),TopicStoreyCouponDTO.class);
                item.setActivityId(couponDTO.getActivityId());
                item.setCouponId(couponDTO.getCouponId());
                item.setReceiveImageUrl(couponDTO.getReceiveImageUrl());
                item.setUseImageUrl(couponDTO.getUseImageUrl());
                item.setDoneImageUrl(couponDTO.getDoneImageUrl());
                item.setActivityName(couponDTO.getActivityName());
                item.setDenomination(couponDTO.getDenomination());
                item.setFullBuyType(couponDTO.getFullBuyType());
                item.setFullBuyPrice(couponDTO.getFullBuyPrice());
                item.setActivityConfigId(couponDTO.getActivityConfigId());

            });
        }else if(Objects.equals(TopicStoreyType.NAVIGATION.getId(),storeyType)){
            contents.forEach(item->{
                JSONObject configJson = JSONObject.parseObject(item.getAttributeInfo());
                item.setRelStoreyId(configJson.getInteger("relStoreyId"));
            });
        }

    }


    @Transactional
    public void enableStorey(EnableTopicStoreyRequest request){
        storeyRepository.enable(request.getStoreyId(),request.getOptType());
    }

    @Transactional
    public void addStoreyContents(TopicStoreyContentAddRequest request){
        if(request == null || CollectionUtils.isEmpty(request.getContents())){
            throw  new  SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //删除原数据
        contentRepository.deleteBySid(request.getStoreyId());
        //设置属性
        if(Objects.equals(request.getStoreyType(),TopicStoreyType.COUPON.getId()) || Objects.equals(TopicStoreyTypeV2.VOUCHER.getId(), request.getStoreyType())){
            request.getContents().forEach(c->{
                TopicStoreyCouponDTO couponDTO = (TopicStoreyCouponDTO)c;
                c.setAttributeInfo(JSON.toJSONString(couponDTO));
            });
        }else if(Objects.equals(request.getStoreyType(),TopicStoreyType.NAVIGATION.getId())){
            request.getContents().forEach(c->{
                Map<String,Integer> map = new HashMap<>();
                map.put("relStoreyId",c.getRelStoreyId());
                c.setAttributeInfo(JSON.toJSONString(map));
            });
        }
        List<TopicStoreyContent> contents = KsBeanUtil.convertList(request.getContents(),TopicStoreyContent.class);

        contents.forEach(c->{
            c.setCreateTime(LocalDateTime.now());
            c.setUpdateTime(LocalDateTime.now());
            c.setDeleted(DeleteFlag.NO.toValue());
            c.setStoreyId(request.getStoreyId());
            c.setTopicId(request.getTopicId());
        });
        contentRepository.saveAll(contents);
    }

    public TopicStoreyContentResponse listTopicStoreyContent(TopicStoreyContentQueryRequest request){
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC,"type"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"sorting"));
         TopicStorey topicStorey = storeyRepository.getOne(request.getStoreyId());
         if(topicStorey == null){
             return null;
         }
         TopicStoreyContentResponse response = new TopicStoreyContentResponse();
         response.setStoreyId(request.getStoreyId());
         response.setStoreyType(topicStorey.getStoreyType());
         List<TopicStoreyContent> list = contentRepository.findAll(getTopicStoreyContentWhereCriteria(request),Sort.by(orders));
         if(CollectionUtils.isEmpty(list)){
            return response;
         }
         response.setContents(KsBeanUtil.convertList(list,TopicStoreyContentDTO.class));
        //设置属性
        initAttr(response.getContents(),topicStorey.getStoreyType());
        return response;
    }


    public Specification<Topic> getTopicWhereCriteria(TopicQueryRequest request) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getTopicKey() != null) {
                predicates.add(cbuild.equal(root.get("topicKey"), request.getTopicKey()));
            }
            if (StringUtils.isNotEmpty(request.getTopicName())) {
                predicates.add(cbuild.like(root.get("topicName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(request.getTopicName().trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    public Specification<TopicStoreyContent> getTopicStoreyContentWhereCriteria(TopicStoreyContentQueryRequest request) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getStoreyId() != null) {
                predicates.add(cbuild.equal(root.get("storeyId"), request.getStoreyId()));
            }
            if (request.getTopicId() != null) {
                predicates.add(cbuild.equal(root.get("topicId"), request.getTopicId()));
            }
            predicates.add(cbuild.equal(root.get("deleted"), 0));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }


    @Transactional
    public void deleteStorey(Integer storeyId){
        storeyRepository.delete(storeyId);
        contentRepository.deleteBySid(storeyId);
    }

    /**
     * @Description 楼层栏目
     * @Author zh
     * @Date  2023/2/7 17:32
     * @param: request
     * @return: com.wanmi.sbc.setting.api.response.TopicStoreyColumnResponse
     */
    public MicroServicePage<TopicStoreyColumnDTO> listTopicStoreyColumn(TopicStoreyColumnQueryRequest request){
        Page<TopicStoreySearch> topicStoreySearchPage = columnRepository
                .findAll(columnRepository.topicStoreySearch(request), PageRequest.of(request.getPageNum(),
                request.getPageSize(), Sort.by(Sort.Direction.ASC, "orderNum")));
        List<TopicStoreySearch> content = topicStoreySearchPage.getContent();
        MicroServicePage<TopicStoreyColumnDTO> microServicePage = new MicroServicePage<>();
        microServicePage.setTotal(topicStoreySearchPage.getTotalElements());
        microServicePage.setContent(changeTopicStoreyColumn(content));
        return microServicePage;
    }

    /**
     * 新增栏目
     * @param request
     */
    public void addStoreyColumn(TopicStoreyColumnAddRequest request) {
        TopicStoreySearch topicStoreySearch = new TopicStoreySearch();
        topicStoreySearch.setTopicStoreId(request.getTopicStoreId());
        topicStoreySearch.setCreateTime(request.getCreateTime());
        topicStoreySearch.setEndTime(request.getEndTime());
        topicStoreySearch.setOrderNum(request.getOrderNum());
        topicStoreySearch.setName(request.getName());
        columnRepository.save(topicStoreySearch);
    }

    /**
     * 修改栏目
     * @param request
     */
    public void updateStoreyColumn(TopicStoreyColumnUpdateRequest request) {
        TopicStoreySearch topicStoreySearch = new TopicStoreySearch();
        topicStoreySearch.setId(request.getId());
        topicStoreySearch.setCreateTime(request.getCreateTime());
        topicStoreySearch.setEndTime(request.getEndTime());
        topicStoreySearch.setOrderNum(request.getOrderNum());
        topicStoreySearch.setName(request.getName());
        columnRepository.save(topicStoreySearch);
    }

    /**
     * @Description 专栏调用
     * @Author zh
     * @Date  2023/2/8 09:54
     * @param: request
     */
    public void enableStoreyColumn(EnableTopicStoreyColumnRequest request) {
        columnRepository.enable(request.getId(),request.getDeleted());
    }

    public MicroServicePage<TopicStoreyColumnGoodsDTO> listTopicStoreyColumnGoods(TopicStoreyColumnGoodsQueryRequest request) {
        Page<TopicStoreySearchContent> topicStoreySearchGoodsPage = columnGoodsRepository
                .findAll(columnGoodsRepository.topicStoreySearchContent(request), PageRequest.of(request.getPageNum(),
                        request.getPageSize(), Sort.by(Sort.Direction.ASC, "sorting")));
        List<TopicStoreySearchContent> content = topicStoreySearchGoodsPage.getContent();
        MicroServicePage<TopicStoreyColumnGoodsDTO> microServicePage = new MicroServicePage<>();
        microServicePage.setTotal(topicStoreySearchGoodsPage.getTotalElements());
        microServicePage.setContent(changeTopicStoreyColumnGoods(content));
        return microServicePage;
    }

    /**
     * 新增栏目商品
     * @param request
     */
    public void addStoreyColumnGoods(TopicStoreyColumnGoodsAddRequest request) {
        TopicStoreySearchContent topicStoreySearchContent = new TopicStoreySearchContent();
        topicStoreySearchContent.setTopicStoreySearchId(request.getTopicStoreySearchId());
        topicStoreySearchContent.setImageUrl(request.getImageUrl());
        topicStoreySearchContent.setCreateTime(request.getCreateTime());
        topicStoreySearchContent.setEndTime(request.getEndTime());
        topicStoreySearchContent.setSkuNo(request.getSkuNo());
        topicStoreySearchContent.setSorting(request.getSorting());
        topicStoreySearchContent.setGoodsName(request.getGoodsName());
        columnGoodsRepository.save(topicStoreySearchContent);
    }

    /**
     * 修改栏目商品
     * @param request
     */
    public void updateStoreyColumnGoods(TopicStoreyColumnGoodsUpdateRequest request) {
        TopicStoreySearchContent topicStoreySearchContent = new TopicStoreySearchContent();
        topicStoreySearchContent.setId(request.getId());
        topicStoreySearchContent.setImageUrl(request.getImageUrl());
        topicStoreySearchContent.setCreateTime(request.getCreateTime());
        topicStoreySearchContent.setEndTime(request.getEndTime());
        topicStoreySearchContent.setSkuNo(request.getSkuNo());
        topicStoreySearchContent.setSorting(request.getSorting());
        topicStoreySearchContent.setGoodsName(request.getGoodsName());
        columnGoodsRepository.save(topicStoreySearchContent);
    }

    /**
     * @Description 专栏调用商品
     * @Author zh
     * @Date  2023/2/8 09:54
     * @param: request
     */
    public void enableStoreyColumnGoods(EnableTopicStoreyColumnGoodsRequest request) {
        columnGoodsRepository.enable(request.getId(),request.getDeleted());
    }

    /**
     * @Description 专栏调用商品
     * @Author zh
     * @Date  2023/2/8 09:54
     * @param: request
     */
    public void deleteStoreyColumnGoods(EnableTopicStoreyColumnGoodsRequest request) {
        columnGoodsRepository.deleteById(request.getId());
    }

    private List<TopicStoreyColumnDTO> changeTopicStoreyColumn(List<TopicStoreySearch> content) {
        LocalDateTime now = LocalDateTime.now();
        return content.stream().map(topicStoreySearch -> {
            TopicStoreyColumnDTO topicStoreyColumnDTO = new TopicStoreyColumnDTO();
            BeanUtils.copyProperties(topicStoreySearch, topicStoreyColumnDTO);
            if (now.isBefore(topicStoreySearch.getCreateTime())) {
                //未开始
                topicStoreyColumnDTO.setState(0);
            } else if (now.isAfter(topicStoreySearch.getEndTime())) {
                //已结束
                topicStoreyColumnDTO.setState(2);
            } else {
                //进行中
                topicStoreyColumnDTO.setState(1);
            }
            topicStoreyColumnDTO.setPublishState(topicStoreySearch.getDeleted());
            return topicStoreyColumnDTO;
        }).collect(Collectors.toList());
    }

    private List<TopicStoreyColumnGoodsDTO> changeTopicStoreyColumnGoods(List<TopicStoreySearchContent> content) {
        LocalDateTime now = LocalDateTime.now();
        return content.stream().map(topicStoreySearchContent -> {
            TopicStoreyColumnGoodsDTO topicStoreyColumnGoodsDTO = new TopicStoreyColumnGoodsDTO();
            BeanUtils.copyProperties(topicStoreySearchContent, topicStoreyColumnGoodsDTO);
            if (now.isBefore(topicStoreySearchContent.getCreateTime())) {
                //未开始
                topicStoreyColumnGoodsDTO.setState(0);
            } else if (now.isAfter(topicStoreySearchContent.getEndTime())) {
                //已结束
                topicStoreyColumnGoodsDTO.setState(2);
            } else {
                //进行中
                topicStoreyColumnGoodsDTO.setState(1);
            }
            topicStoreyColumnGoodsDTO.setPublishState(topicStoreySearchContent.getDeleted());
            return topicStoreyColumnGoodsDTO;
        }).collect(Collectors.toList());
    }
}
