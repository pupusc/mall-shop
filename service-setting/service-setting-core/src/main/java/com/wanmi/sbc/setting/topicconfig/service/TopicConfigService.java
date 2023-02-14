package com.wanmi.sbc.setting.topicconfig.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.setting.api.request.RankPageRequest;
import com.wanmi.sbc.setting.api.request.RankRequest;
import com.wanmi.sbc.setting.api.request.RankRequestListResponse;
import com.wanmi.sbc.setting.api.request.RankStoreyRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.*;
import com.wanmi.sbc.setting.api.response.RankPageResponse;
import com.wanmi.sbc.setting.api.response.TopicStoreyContentResponse;
import com.wanmi.sbc.setting.api.response.mixedcomponentV2.TopicStoreyMixedComponentResponse;
import com.wanmi.sbc.setting.api.response.TopicStoreySearchContentRequest;
import com.wanmi.sbc.setting.bean.dto.*;
import com.wanmi.sbc.setting.bean.enums.MixedComponentLevel;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyType;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyTypeV2;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
import com.wanmi.sbc.setting.topicconfig.model.root.*;
import com.wanmi.sbc.setting.topicconfig.repository.*;
import com.wanmi.sbc.setting.util.PartialUpdateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

import javax.persistence.criteria.CriteriaBuilder;
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
    private TopicStoreySearchRepository storeySearchRepository;

    @Autowired
    private TopicStoreyContentRepository contentRepository;

    @Autowired
    private TopicStoreyColumnRepository columnRepository;

    @Autowired
    private TopicStoreyColumnGoodsRepository columnGoodsRepository;

    @Autowired
    private PartialUpdateUtil updateUtil;

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

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

    /**
     * 首页榜单查询
     * @param storeyRequest
     * @return
     */
    public RankRequestListResponse rank(RankStoreyRequest storeyRequest) {


        Integer topicStoreyId=storeyRequest.getTopicStoreyId();
        String sql = "SELECT\n" +
                " *\n" +
                "FROM\n" +
                " (SELECT * from topic_storey_search_content where topic_storey_id=?) AS a \n" +
                "WHERE\n" +
                "( SELECT count(*) FROM topic_storey_search_content tt WHERE topic_storey_search_id = a.topic_storey_search_id AND num >= a.num )<= 3\n" +
                "ORDER BY\n" +
                " topic_storey_search_id asc,num DESC";
        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql,TopicStoreySearchContent.class);
        query.setParameter(1,topicStoreyId);
        List<TopicStoreySearch> list=getRankNameList(topicStoreyId,true);
        List<TopicStoreySearchContent> resultList = query.getResultList();
        List<TopicStoreySearchContentRequest> contentRequests=KsBeanUtil.convertList(resultList,TopicStoreySearchContentRequest.class);
        List<RankRequest> rankRequests=new ArrayList<>();
        RankRequestListResponse response=new RankRequestListResponse();
        List<String> idList=new ArrayList<>();
        list.forEach(ts->{
            RankRequest rankRequest=new RankRequest();
            rankRequest.setRankName(ts.getName());
            rankRequest.setId(ts.getId());
            List<Map> contentRequestList=new ArrayList<>();
            rankRequest.setRankList(contentRequestList);
            rankRequests.add(rankRequest);
        });
        rankRequests.forEach(r->{
            contentRequests.forEach(c->{
                if(c.getTopicStoreySearchId().equals(r.getId())){
                    Map map=new HashMap<>();
                    map.put("id",c.getId());
                    map.put("spuNo",c.getSpuNo());
                    map.put("skuNo",c.getSkuNo());
                    map.put("imageUrl",c.getImageUrl());
                    map.put("sorting",c.getSorting());
                    map.put("goodsName",c.getGoodsName());
                    map.put("num",c.getNum());
                    map.put("skuId",c.getSkuId());
                    map.put("spuId",c.getSpuId());
                    map.put("label","");
                    map.put("subName","");
                    r.getRankList().add(map);
                }
                if(!idList.contains(c.getSkuId())) {
                    idList.add(c.getSkuId());
                }
            });
        });
        response.setRankRequestList(rankRequests);
        response.setIdList(idList);
        return response;
    }

    /**
     * 榜单详情页分页
     * @param storeyRequest
     * @return
     */
    public RankPageResponse rankPage(RankStoreyRequest storeyRequest) {
        Integer topicStoreyId=0;
        storeyRequest.setTopicStoreyId(getRankId());
        List<TopicStoreySearch> list=getRankNameList(storeyRequest.getTopicStoreyId(),false);
        String sql = "SELECT * FROM topic_storey_search_content where topic_storey_search_id  ";
        if(null!=storeyRequest.getTopicStoreySearchId()){
             Integer fId=list.stream().filter(t->t.getLevel().equals(0)).collect(Collectors.toList()).get(0).getId();
            Optional<TopicStoreySearch> first = list.stream().filter(t -> fId.equals(t.getPId())).findFirst();
            if(first.isPresent()){
                topicStoreyId = first.get().getId();
            }
            sql+="= ?1 ";
        }else {
            sql+="in(SELECT DISTINCT id FROM topic_storey_search where topic_storey_id=?1) ";
            topicStoreyId=storeyRequest.getTopicStoreyId();
        }
        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query1 = entityManager.createNativeQuery(sql,TopicStoreySearchContent.class);
        query1.setParameter(1,topicStoreyId);
        RankPageRequest pageRequest=new RankPageRequest();
        Long total = Long.valueOf(query1.getResultList().size());
        pageRequest.setTotal(total);
        pageRequest.setPageNum(storeyRequest.getPageNum());
        pageRequest.setTotalPages((total/storeyRequest.getPageSize()));
        int start = (storeyRequest.getPageNum() - 1) * storeyRequest.getPageSize();
        sql+="ORDER BY sorting asc limit ?2,?3";
        Query query2 = entityManager.createNativeQuery(sql,TopicStoreySearchContent.class);
        query2.setParameter(1,topicStoreyId);
        query2.setParameter(2,start);
        query2.setParameter(3,storeyRequest.getPageSize());
        List<TopicStoreySearchContent> resultList = query2.getResultList();

        List<TopicStoreySearchContentRequest> contentRequests=KsBeanUtil.convertList(resultList,TopicStoreySearchContentRequest.class);
        List<RankRequest> requests=new ArrayList<>();
        list.stream().filter(ts->ts.getLevel().equals(0)||null==ts.getPId()).forEach(ts->{
            RankRequest rankRequest=new RankRequest();
            rankRequest.setRankName(ts.getName());
            rankRequest.setId(ts.getId());
            rankRequest.setLevel(ts.getLevel());
            List<RankRequest> contentRequestList=new ArrayList<>();
            rankRequest.setRankList(contentRequestList);
            requests.add(rankRequest);
        });
        List<String> idList=new ArrayList<>();
        list.stream().filter(ts->ts.getLevel()!=0||null!=ts.getPId()).forEach(ts->{
            requests.forEach(r->{
                if(ts.getPId().equals(r.getId())){
                    RankRequest rankRequest=new RankRequest();
                    rankRequest.setRankName(ts.getName());
                    rankRequest.setId(ts.getId());
                    rankRequest.setLevel(ts.getLevel());
                    rankRequest.setP_id(ts.getPId());
                    List<Map> contentRequestList=new ArrayList<>();
                    contentRequests.forEach(c->{
                        if(c.getTopicStoreySearchId().equals(ts.getId())){
                            Map map=new HashMap<>();
                            map.put("id",c.getId());
                            map.put("spuNo",c.getSpuNo());
                            map.put("skuNo",c.getSkuNo());
                            map.put("imageUrl",c.getImageUrl());
                            map.put("sorting",c.getSorting());
                            map.put("goodsName",c.getGoodsName());
                            map.put("num",c.getNum());
                            map.put("skuId",c.getSkuId());
                            map.put("spuId",c.getSpuId());
                            map.put("label","");
                            map.put("subName","");
                            contentRequestList.add(map);
                        }
                        if(!idList.contains(c.getSkuId())) {
                            idList.add(c.getSkuId());
                        }
                    });
                    rankRequest.setRankList(contentRequestList);
                    r.getRankList().add(rankRequest);
                }
            });
        });
        RankPageResponse response=new RankPageResponse();
        pageRequest.setContentList(requests);
        response.setPageRequest(pageRequest);
        response.setIdList(idList);
        return response;
    }

    public Integer getRankId(){
        String sql = "SELECT\n" +
                "\tss.relation_store_id AS r_id\n" +
                "FROM\n" +
                "\ttopic_storey_search AS ss\n" +
                "\tINNER JOIN\n" +
                "\ttopic_storey AS ts\n" +
                "\tON \n" +
                "\t\tss.topic_storey_id = ts.id\n" +
                "WHERE\n" +
                "\tts.storey_type = 21";
        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql);
        Integer id = Integer.parseInt(query.getSingleResult().toString());
        return id;
    }

    public List<TopicStoreySearch> getRankNameList(Integer topicStoreyId,Boolean isDitail){
        String sql = "SELECT * FROM topic_storey_search where topic_storey_id=? ";
        if(isDitail){
            sql+="and level!=0 ";
        }
        sql+="ORDER BY order_num asc";
        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql,TopicStoreySearch.class);
        query.setParameter(1,topicStoreyId);
        List<TopicStoreySearch> list=query.getResultList();
        return list;
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


    public List<TopicStoreyContentDTO> listTopicStoreyContentByPage(TopicStoreyContentRequest request){

        TopicStoreyContentResponse response = new TopicStoreyContentResponse();
        response.setStoreyId(request.getStoreyId());

        Sort sort = Sort.by(Sort.Direction.ASC, "sorting");

        Pageable pageable = PageRequest.of(request.getPageNum(), request.getPageSize(), sort);

        Page<TopicStoreyContent> topicStoreyContentPage = contentRepository.findAll(packageWhere(request), pageable);
        if(CollectionUtils.isEmpty(topicStoreyContentPage.getContent())){
            return null;
        }

        List<TopicStoreyContentDTO> collect = topicStoreyContentPage.stream().map(t -> {
            TopicStoreyContentDTO topicStoreyContentDTO = new TopicStoreyContentDTO();
            BeanUtils.copyProperties(t, topicStoreyContentDTO);
            return topicStoreyContentDTO;
        }).collect(Collectors.toList());

        return collect;
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

    public Specification<TopicStoreyContent> packageWhere(TopicStoreyContentRequest request) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getStoreyId() != null) {
                predicates.add(cbuild.equal(root.get("storeyId"), request.getStoreyId()));
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
        topicStoreySearch.setTopicStoreyId(request.getTopicStoreyId());
        topicStoreySearch.setCreateTime(request.getStartTime());
        topicStoreySearch.setEndTime(request.getEndTime());
        topicStoreySearch.setOrderNum(request.getSorting());
        topicStoreySearch.setName(request.getName());
        topicStoreySearch.setUpdateTime(LocalDateTime.now());
        topicStoreySearch.setDeleted(0);
        columnRepository.save(topicStoreySearch);
    }

    /**
     * 修改栏目
     * @param request
     */
    @SneakyThrows
    public void updateStoreyColumn(TopicStoreyColumnUpdateRequest request) {
        TopicStoreySearch topicStoreySearch = new TopicStoreySearch();
        topicStoreySearch.setId(request.getId());
        topicStoreySearch.setCreateTime(request.getStartTime());
        topicStoreySearch.setEndTime(request.getEndTime());
        topicStoreySearch.setOrderNum(request.getSorting());
        topicStoreySearch.setName(request.getName());
        updateUtil.partialUpdate(topicStoreySearch.getId(), topicStoreySearch, columnRepository);
    }

    /**
     * @Description 专栏调用
     * @Author zh
     * @Date  2023/2/8 09:54
     * @param: request
     */
    @Transactional
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
        topicStoreySearchContent.setStartTime(request.getStartTime());
        topicStoreySearchContent.setEndTime(request.getEndTime());
        topicStoreySearchContent.setSkuNo(request.getSkuNo());
        topicStoreySearchContent.setSorting(request.getSorting());
        topicStoreySearchContent.setGoodsName(request.getGoodsName());
        topicStoreySearchContent.setCreateTime(LocalDateTime.now());
        topicStoreySearchContent.setUpdateTime(LocalDateTime.now());
        topicStoreySearchContent.setDeleted(0);
        topicStoreySearchContent.setType(1);
        topicStoreySearchContent.setTopicStoreySearchTable("topic_storey_search");
        columnGoodsRepository.save(topicStoreySearchContent);
    }

    /**
     * 修改栏目商品
     * @param request
     */
    @SneakyThrows
    public void updateStoreyColumnGoods(TopicStoreyColumnGoodsUpdateRequest request) {
        TopicStoreySearchContent topicStoreySearchContent = new TopicStoreySearchContent();
        topicStoreySearchContent.setId(request.getId());
        topicStoreySearchContent.setImageUrl(request.getImageUrl());
        topicStoreySearchContent.setStartTime(request.getStartTime());
        topicStoreySearchContent.setEndTime(request.getEndTime());
        topicStoreySearchContent.setSkuNo(request.getSkuNo());
        topicStoreySearchContent.setSorting(request.getSorting());
        topicStoreySearchContent.setGoodsName(request.getGoodsName());
        updateUtil.partialUpdate(topicStoreySearchContent.getId(), topicStoreySearchContent, columnGoodsRepository);
    }

    /**
     * @Description 专栏调用商品
     * @Author zh
     * @Date  2023/2/8 09:54
     * @param: request
     */
    @Transactional
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

    //混合组件
    public TopicStoreyMixedComponentResponse getMixedComponent(MixedComponentQueryRequest request) {
        TopicStoreySearch topicStoreySearch = new TopicStoreySearch();
        topicStoreySearch.setTopicStoreyId(request.getTopicStoreyId());
        List<TopicStoreySearch> topicStoreySearches = columnRepository.findAll(Example.of(topicStoreySearch),
                Sort.by(Sort.Direction.ASC, "orderNum"));
        List<MixedComponentTabDto> mixedComponentTabs = getMixedComponentTabs(topicStoreySearches);
        MixedComponentKeyWordsDto mixedComponentKeyWord = getMixedComponentKeyWord(topicStoreySearches, mixedComponentTabs, request);
        getMixedComponentContentPage(request, mixedComponentKeyWord);
        TopicStoreyMixedComponentResponse topicStoreyMixedComponentResponse = new TopicStoreyMixedComponentResponse();
        topicStoreyMixedComponentResponse.setMixedComponentTabs(mixedComponentTabs);
        mixedComponentTabs.forEach(s -> {
            if(request.getId().equals(s.getId())){
                s.setMixedComponentKeyWord(mixedComponentKeyWord);
            }
        });
        return topicStoreyMixedComponentResponse;
    }

    private MicroServicePage<MixedComponentContentDto> getMixedComponentContentPage(MixedComponentQueryRequest request, MixedComponentKeyWordsDto mixedComponentKeyWord) {
        if (request.getKeywordId() == null) {
            request.setKeywordId(mixedComponentKeyWord.getKeyWord().stream().findFirst().get().getId());
        }
        List<Sort.Order> sortList = new ArrayList<>();
        sortList.add(Sort.Order.asc("sorting"));
        sortList.add(Sort.Order.asc("type"));
        Page<TopicStoreySearchContent> topicStoreySearchGoodsPage = columnGoodsRepository
                .findAll(columnGoodsRepository.mixedComponentContent(request), PageRequest.of(request.getPageNum(),
                        request.getPageSize(), Sort.by(sortList)));
        List<TopicStoreySearchContent> content = topicStoreySearchGoodsPage.getContent();
        MicroServicePage<MixedComponentContentDto> mixedComponentContentPage = new MicroServicePage<>();
        mixedComponentContentPage.setContent(content.stream().map(s -> {
            MixedComponentContentDto mixedComponentContentDto = new MixedComponentContentDto();
            BeanUtils.copyProperties(s, mixedComponentContentDto);
            return mixedComponentContentDto;
        }).collect(Collectors.toList()));
        mixedComponentContentPage.setTotal(topicStoreySearchGoodsPage.getTotalElements());
        List<KeyWordDto> keyWord = mixedComponentKeyWord.getKeyWord();
        keyWord.forEach(s -> {
            if(request.getKeywordId().equals(s.getId())){
                s.setMixedComponentContentPage(mixedComponentContentPage);
            }
        });
        return mixedComponentContentPage;
    }

    public List<MixedComponentTabDto> getMixedComponentTabs(List<TopicStoreySearch> topicStoreySearches) {
        return topicStoreySearches.stream().filter(s -> MixedComponentLevel.ONE.toValue().equals(s.getLevel())).map(s -> {
                MixedComponentTabDto mixedComponentTabDto = new MixedComponentTabDto();
                mixedComponentTabDto.setId(s.getId());
                mixedComponentTabDto.setName(s.getName());
                mixedComponentTabDto.setSubName(s.getSubName());
                if (StringUtils.isNotEmpty(s.getColor())) {
                    JSONObject color = JSON.parseObject(s.getColor());
                    mixedComponentTabDto.setSelectedColor(color.getString("selected"));
                    mixedComponentTabDto.setUnSelectedColor(color.getString("unselected"));
                }
                if (StringUtils.isNotEmpty(s.getImage())) {
                    JSONObject image = JSON.parseObject(s.getImage());
                    mixedComponentTabDto.setSelectedImage(image.getString("selected"));
                    mixedComponentTabDto.setUnSelectedImage(image.getString("unselected"));
                }

            return mixedComponentTabDto;
        }).collect(Collectors.toList());
    }

    public MixedComponentKeyWordsDto getMixedComponentKeyWord(List<TopicStoreySearch> topicStoreySearches, List<MixedComponentTabDto> mixedComponentTabs, MixedComponentQueryRequest request) {
        if (request.getId() == null) {
            request.setId(mixedComponentTabs.get(0).getId());
        }
        Integer finalId = request.getId();
        MixedComponentKeyWordsDto mixedComponentKeyWordsDto = new MixedComponentKeyWordsDto();
        List<KeyWordDto> keyWords = topicStoreySearches.stream()
                .filter(s -> finalId.equals(s.getPId()) && MixedComponentLevel.TWO.toValue().equals(s.getLevel()))
                .map(s -> {
                    KeyWordDto keyWordDto = new KeyWordDto();
                    keyWordDto.setId(s.getId());
                    keyWordDto.setName(s.getName());
            return keyWordDto;
        }).collect(Collectors.toList());
        mixedComponentKeyWordsDto.setKeyWord(keyWords);
        Optional<TopicStoreySearch> storeySearch = topicStoreySearches.stream().filter(s -> finalId.equals(s.getPId()) && MixedComponentLevel.TWO.toValue().equals(s.getLevel())).findFirst();
        if (storeySearch.get() != null && StringUtils.isNotEmpty(storeySearch.get().getColor())) {
            JSONObject colorObject = JSON.parseObject(storeySearch.get().getColor());
            mixedComponentKeyWordsDto.setKeyWordSelectedColor(colorObject.getString("selected"));
            mixedComponentKeyWordsDto.setKeyWordUnSelectedColor(colorObject.getString("unselected"));
        }
        return mixedComponentKeyWordsDto;
    }


    /**
     * @Description 根据主题类型获得主题id
     */
    public List<TopicStoreyDTO> listTopicStoreyIdByType(Integer storeyType){
        List<TopicStoreyDTO> collect = storeyRepository.getAvailByTopicType(storeyType).stream().map(t -> {
            TopicStoreyDTO topicStoreyDTO = new TopicStoreyDTO();
            BeanUtils.copyProperties(t, topicStoreyDTO);
            return topicStoreyDTO;
        }).collect(Collectors.toList());
        return collect;
    }
}
