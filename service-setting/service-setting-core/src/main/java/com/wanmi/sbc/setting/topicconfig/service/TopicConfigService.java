package com.wanmi.sbc.setting.topicconfig.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import javax.persistence.EntityManager;
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
import io.swagger.models.auth.In;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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

    @Autowired
    private PartialUpdateUtil updateUtil;
    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @Autowired
    private TopicRankRelationRepository relationRepository;

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
                " (SELECT * from topic_storey_column_content where topic_storey_id=?) AS a \n" +
                "WHERE\n" +
                "( SELECT count(*) FROM topic_storey_column_content tt WHERE topic_storey_column_id = a.topic_storey_column_id AND order_num >= a.order_num )<= 3\n" +
                "ORDER BY\n" +
                " topic_storey_column_id asc,order_num DESC";
        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql,TopicStoreySearchContent.class);
        query.setParameter(1,topicStoreyId);
        List<TopicStoreySearch> list=getRankNameList(topicStoreyId,true);
        List<TopicStoreySearchContent> resultList = query.getResultList();
        List<TopicStoreySearchContentRequest> contentRequests=KsBeanUtil.convertList(resultList,TopicStoreySearchContentRequest.class);
        entityManager.close();
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
                    if(r.getRankList().size()<3) {
                        Map map = new HashMap<>();
                        map.put("id", c.getId());
                        map.put("spuNo", c.getSpuNo());
                        map.put("skuNo", c.getSkuNo());
                        map.put("imageUrl", c.getImageUrl());
                        map.put("sorting", c.getSorting());
                        map.put("goodsName", c.getGoodsName());
                        if (StringUtils.isNotBlank(c.getNumTxt())) {
                            if (Integer.parseInt(c.getNumTxt()) >= 10000) {
                                String num = String.valueOf(Integer.parseInt(c.getNumTxt()) / 10000) + "万";
                                map.put("num", num);
                            } else {
                                map.put("num", String.valueOf(Integer.parseInt(c.getNumTxt())));
                            }
                        } else {
                            map.put("num", "");
                        }
                        map.put("skuId", c.getSkuId());
                        map.put("spuId", c.getSpuId());
                        map.put("label", "");
                        map.put("subName", "");
                        r.getRankList().add(map);
                    }
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

    public RankRequestListResponse rank2() {
        RankRequestListResponse response=new RankRequestListResponse();
        List<Integer> ids=new ArrayList<>();
        List<RankRequest> rankRequestList=new ArrayList<>();
        relationRepository.collectOrderByTopicRankSortingAsc().forEach(item->{
            RankRequest request=new RankRequest();
            request.setId(item.getCRankId());
            request.setRankName(item.getCRankName());
            rankRequestList.add(request);
            ids.add(item.getCRankId());

        });
        response.setRankIds(ids);
        response.setRankRequestList(rankRequestList);
        return response;
    }

    /**
     * 榜单详情页分页
     * @param storeyRequest
     * @return
     */
    public RankPageResponse rankPage2(RankStoreyRequest storeyRequest) {
        Integer rankIdByTopicStoreyId = getRankIdByTopicStoreyId(storeyRequest.getTopicStoreyId());
        storeyRequest.setTopicStoreyId(rankIdByTopicStoreyId);
        List<RankRequest> requests = getRankNameList2(storeyRequest.getTopicStoreyId(), false);
        String sql = "SELECT * FROM topic_storey_column_content where topic_storey_column_id = ?1 ";
        Integer topicStoreySearchId=0;
        if(null==storeyRequest.getTopicStoreySearchId()){
            RankRequest request = requests.get(0);
            Optional<RankRequest> first = request.getRankList().stream().findFirst();
            if(first.isPresent()){
                topicStoreySearchId = first.get().getId();
            }
        }else {
            if(isLevel0(storeyRequest.getTopicStoreySearchId(),requests)){
                Optional<RankRequest> first = requests.stream().filter(r -> r.getId().equals(storeyRequest.getTopicStoreySearchId())).findFirst();
                if(first.isPresent()){
                    RankRequest rankRequest = first.get();
                    List<RankRequest> rankList = (List<RankRequest>) rankRequest.getRankList();
                    Integer id = rankList.get(0).getId();
                    topicStoreySearchId=id;
                }
            }else {
                topicStoreySearchId = storeyRequest.getTopicStoreySearchId();
            }
        }
        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query1 = entityManager.createNativeQuery(sql,TopicStoreySearchContent.class);
        query1.setParameter(1,topicStoreySearchId);
        RankPageRequest pageRequest=new RankPageRequest();
        Long total = Long.valueOf(query1.getResultList().size());
        if(null==storeyRequest.getPageSize()){
            storeyRequest.setPageSize(10);
        }
        if(null==storeyRequest.getPageNum()){
            storeyRequest.setPageNum(0);
        }
        pageRequest.setTotal(total);
        pageRequest.setPageNum(storeyRequest.getPageNum());
        pageRequest.setTotalPages((long) (Math.ceil(total/storeyRequest.getPageSize())+1));
        int start = (storeyRequest.getPageNum()) * storeyRequest.getPageSize();
        sql+="ORDER BY sorting asc limit ?2,?3";
        Query query2 = entityManager.createNativeQuery(sql,TopicStoreySearchContent.class);
        query2.setParameter(1,topicStoreySearchId);
        query2.setParameter(2,start);
        query2.setParameter(3,storeyRequest.getPageSize());
        List<TopicStoreySearchContent> resultList = query2.getResultList();
        entityManager.close();
        List<TopicStoreySearchContentRequest> contentRequests=KsBeanUtil.convertList(resultList,TopicStoreySearchContentRequest.class);
        List<String> idList=new ArrayList<>();
        requests.forEach(r->{
            r.getRankList().forEach(q->{
                RankRequest rankRequest= (RankRequest) q;
                List<Map> contentRequestList= (List<Map>) rankRequest.getRankList();
                contentRequests.forEach(c->{
                    if(c.getTopicStoreySearchId().equals(rankRequest.getId())){
                        Map map=new HashMap<>();
                        map.put("id",c.getId());
                        map.put("spuNo",c.getSpuNo());
                        map.put("skuNo",c.getSkuNo());
                        map.put("imageUrl",c.getImageUrl());
                        map.put("sorting",c.getSorting());
                        map.put("goodsName",c.getGoodsName());
                        if(StringUtils.isNotBlank(c.getNumTxt())) {
                            if (Integer.parseInt(c.getNumTxt()) >= 10000) {
                                String num = String.valueOf(Integer.parseInt(c.getNumTxt()) / 10000) + "万";
                                map.put("num", num);
                            } else {
                                map.put("num", String.valueOf(Integer.parseInt(c.getNumTxt())));
                            }
                        }else {
                            map.put("num", "");
                        }
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
            });
        });
        RankPageResponse response=new RankPageResponse();
        pageRequest.setContentList(requests);
        response.setPageRequest(pageRequest);
        response.setIdList(idList);
        return response;
    }

    public RankPageResponse rankPageByBookList(RankStoreyRequest storeyRequest) {
        Integer rankIdByTopicStoreyId = getRankIdByTopicStoreyId(storeyRequest.getTopicStoreyId());
        storeyRequest.setTopicStoreyId(rankIdByTopicStoreyId);
        return getRankNameListRel(storeyRequest, false);
    }

    /**
     * 去除空榜单
     */
    public List<Integer> dislodgeBlankRank(){
        String sql = "select distinct topic_storey_column_id from topic_storey_column_content where topic_storey_id=?1";
        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1,"185");
        List<Integer> resultList = query.getResultList();
        entityManager.close();
        return resultList;
    }

    public Integer getTopicStoryId(RankStoreyRequest storeyRequest){
        String sql = "SELECT id from topic_story where storey_type = 21 and topic_id=?1";
        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1,storeyRequest.getTopicId());
        Integer id = Integer.parseInt(query.getSingleResult().toString());
        entityManager.close();
        return id;
    }

    public boolean isLevel0(Integer topicStoreySearchId, List<RankRequest> requests){
        AtomicBoolean flag= new AtomicBoolean(false);
        requests.forEach(r->{
            if(topicStoreySearchId.equals(r.getId())){
                flag.set(true);
            }
        });
        return flag.get();
    }

    public Integer getRankIdByTopicStoreyId(Integer topicStoreyId){
        String sql = "SELECT relation_store_id FROM topic_storey_column WHERE topic_storey_id = ?1";
        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1,topicStoreyId);
        Integer id = Integer.parseInt(query.getSingleResult().toString());
        entityManager.close();
        return id;
    }

    public List<TopicStoreySearch> getRankNameList(Integer topicStoreyId,Boolean isDitail){
        String sql = "SELECT * FROM topic_storey_column where topic_storey_id=? ";
        if(isDitail){
            sql+="and level!=0 ";
        }
        sql+="ORDER BY order_num asc";
        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql,TopicStoreySearch.class);
        query.setParameter(1,topicStoreyId);
        List<TopicStoreySearch> list=query.getResultList();
        entityManager.close();
        return list;
    }

    public List<RankRequest> getRankNameList2(Integer topicStoreyId,Boolean isDitail){
        String sql = "SELECT * FROM topic_storey_column where topic_storey_id=? ";
        if(isDitail){
            sql+="and level!=0 ";
        }
        sql+="ORDER BY order_num asc";
        EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query query = entityManager.createNativeQuery(sql,TopicStoreySearch.class);
        query.setParameter(1,topicStoreyId);
        List<TopicStoreySearch> list=query.getResultList();
        entityManager.close();
        List<RankRequest> rankRequests=new ArrayList<>();
        List<Integer> dislodgeBlankRankList = dislodgeBlankRank();
        list.forEach(l->{
            if(null==l.getPId()||l.getLevel().equals(0)){
                RankRequest rankRequest=new RankRequest();
                rankRequest.setRankName(l.getName());
                rankRequest.setId(l.getId());
                rankRequest.setLevel(l.getLevel());
                List<RankRequest> contentRequestList=new ArrayList<>();
                rankRequest.setRankList(contentRequestList);
                rankRequests.add(rankRequest);
            }
        });
        list.stream().filter(l->null!=l.getPId()&&!l.getLevel().equals(0)&&dislodgeBlankRankList.contains(l.getId())).forEach(l->{
            rankRequests.stream().filter(r->r.getId().equals(l.getPId())).forEach(r->{
                RankRequest rankRequest=new RankRequest();
                rankRequest.setRankName(l.getName());
                rankRequest.setId(l.getId());
                rankRequest.setLevel(l.getLevel());
                rankRequest.setP_id(l.getPId());
                List<Map> contentRequestList=new ArrayList<>();
                rankRequest.setRankList(contentRequestList);
                r.getRankList().add(rankRequest);
            });
        });
        Iterator<RankRequest> iterator= rankRequests.iterator();
        while (iterator.hasNext()){
            RankRequest next = iterator.next();
            if(CollectionUtils.isEmpty(next.getRankList())){
                iterator.remove();
            }
        }
        return rankRequests;
    }

    public RankPageResponse getRankNameListRel(RankStoreyRequest storeyRequest,Boolean isDitail){
        Integer topicStoreyId = storeyRequest.getTopicStoreyId();
        List<TopicStoreyColumn> list = columnRepository.getByTopicStoreyIdAndLevelOrderByOrderNumAsc(topicStoreyId, 0);
        List<Integer> idList=new ArrayList<>();
        if(null!=storeyRequest.getTopicStoreySearchId()){
            list.stream().filter(t->t.getId().equals(storeyRequest.getTopicStoreySearchId())).forEach(t->{
                idList.add(t.getId());
            });
        }else {
            idList.add(list.get(0).getId());
        }
        List<Integer> cIds=new ArrayList<>();
        List<TopicRankRelation> rankRelations = relationRepository.collectByPRankColumIdOrderByCRankSortingAsc(idList);
        if(CollectionUtils.isEmpty(rankRelations)){
            return null;
        }
        if(null!=storeyRequest.getRankId()){
            rankRelations.stream().filter(r->r.getPRankColumId().equals(storeyRequest.getRankId())).forEach(r->cIds.add(r.getCRankId()));
            cIds.add(storeyRequest.getRankId());
        }else {
            cIds.add(rankRelations.get(0).getCRankId());
        }
        if(CollectionUtils.isEmpty(cIds)){
            return null;
        }
        List<RankRequest> rankRequests=new ArrayList<>();
        list.forEach(l->{
            RankRequest rankRequest=new RankRequest();
            rankRequest.setRankName(l.getName());
            rankRequest.setId(l.getId());
            rankRequest.setLevel(l.getLevel());
            List<RankRequest> contentRequestList=new ArrayList<>();
            rankRequest.setRankList(contentRequestList);
            rankRelations.stream().filter(r->r.getPRankColumId().equals(l.getId())).forEach(r->{
                RankRequest rankRequest1=new RankRequest();
                rankRequest1.setRankName(r.getCRankName());
                rankRequest1.setId(r.getCRankId());
                rankRequest1.setLevel(1);
                rankRequest1.setP_id(l.getId());
                List<Map> requestList=new ArrayList<>();
                rankRequest1.setRankList(requestList);
                rankRequest.getRankList().add(rankRequest1);
            });
            rankRequests.add(rankRequest);
        });
        RankPageRequest pageRequest=new RankPageRequest();
        RankPageResponse response=new RankPageResponse();
        pageRequest.setContentList(rankRequests);
        response.setPageRequest(pageRequest);
        response.setRankIdList(cIds);
        return response;
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
            if (request.getType() != null) {
                predicates.add(cbuild.equal(root.get("type"), request.getType()));
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
        Page<TopicStoreyColumn> topicStoreySearchPage = columnRepository
                .findAll(columnRepository.topicStoreySearch(request), PageRequest.of(request.getPageNum(),
                request.getPageSize(), Sort.by(Sort.Direction.ASC, "orderNum")));
        List<TopicStoreyColumn> content = topicStoreySearchPage.getContent();
        MicroServicePage<TopicStoreyColumnDTO> microServicePage = new MicroServicePage<>();
        microServicePage.setTotal(topicStoreySearchPage.getTotalElements());
        microServicePage.setContent(changeTopicStoreyColumn(content));
        return microServicePage;
    }

    public MicroServicePage<RankListDTO> listRankList(TopicStoreyColumnQueryRequest request){
        Page<TopicStoreyColumn> topicStoreySearchPage = columnRepository
                .findAll(columnRepository.topicStoreyRankLevel0Search(request), PageRequest.of(request.getPageNum(),
                        request.getPageSize(), Sort.by(Sort.Direction.ASC, "orderNum")));
        List<TopicStoreyColumn> content = topicStoreySearchPage.getContent();
        MicroServicePage<RankListDTO> microServicePage = new MicroServicePage<>();
        microServicePage.setTotal(topicStoreySearchPage.getTotalElements());
        if(!CollectionUtils.isEmpty(content)) {
            microServicePage.setContent(getRankList(content, request.getTopicStoreyId()));
        }
        return microServicePage;
    }

    /**
     * 新增栏目
     * @param request
     */
    public void addStoreyColumn(TopicStoreyColumnAddRequest request) {
        TopicStoreyColumn topicStoreySearch = new TopicStoreyColumn();
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
     * 新增榜单分类
     * @param request
     */
    @Transactional
    public void addRankLevel(RankLevelAddRequest request) {
        TopicStoreyColumn topicStoreySearch = new TopicStoreyColumn();
        topicStoreySearch.setTopicStoreyId(request.getTopicStoreyId());
        topicStoreySearch.setCreateTime(request.getStartTime());
        topicStoreySearch.setEndTime(request.getEndTime());
        topicStoreySearch.setOrderNum(request.getSorting());
        topicStoreySearch.setName(request.getName());
        topicStoreySearch.setUpdateTime(LocalDateTime.now());
        topicStoreySearch.setDeleted(0);
        topicStoreySearch.setLevel(request.getLevel());
        columnRepository.save(topicStoreySearch);
    }

    /**
     * 新增二级榜单列表
     * @param request
     */
    public void addRankrelation(TopicRalationRequest request) {
        TopicRankRelation rankRelation =KsBeanUtil.convert(request, TopicRankRelation.class);
        relationRepository.save(rankRelation);
    }

    /**
     * 修改二级榜单列表
     * @param request
     */
    @SneakyThrows
    public void updateRankrelation(TopicRalationRequest request) {
        TopicRankRelation rankRelation =KsBeanUtil.convert(request, TopicRankRelation.class);
        updateUtil.partialUpdate(rankRelation.getId(),rankRelation,relationRepository);
    }

    /**
     * 删除二级榜单列表
     * @param request
     */
    @SneakyThrows
    public void deleteRankrelation(TopicRalationRequest request) {
        TopicRankRelation rankRelation =KsBeanUtil.convert(request, TopicRankRelation.class);
        relationRepository.deleteById(rankRelation.getId());
    }

    /**
     * 修改栏目
     * @param request
     */
    @SneakyThrows
    public void updateStoreyColumn(TopicStoreyColumnUpdateRequest request) {
        TopicStoreyColumn topicStoreySearch = new TopicStoreyColumn();
        topicStoreySearch.setId(request.getId());
        topicStoreySearch.setCreateTime(request.getStartTime());
        topicStoreySearch.setEndTime(request.getEndTime());
        topicStoreySearch.setOrderNum(request.getSorting());
        topicStoreySearch.setName(request.getName());
        updateUtil.partialUpdate(topicStoreySearch.getId(), topicStoreySearch, columnRepository);
    }

    /**
     * 榜单分类修改
     * @param request
     */
    @SneakyThrows
    public void updateRankLevel(RankLevelUpdateRequest request) {
        TopicStoreyColumn topicStoreySearch = new TopicStoreyColumn();
        topicStoreySearch.setId(request.getId());
        topicStoreySearch.setCreateTime(request.getStartTime());
        topicStoreySearch.setEndTime(request.getEndTime());
        topicStoreySearch.setOrderNum(request.getSorting());
        topicStoreySearch.setName(request.getName());
        topicStoreySearch.setPId(request.getPId());
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
        columnRepository.enable(request.getId(),request.getPublishState());
    }

    public MicroServicePage<TopicStoreyColumnGoodsDTO> listTopicStoreyColumnGoods(TopicStoreyColumnGoodsQueryRequest request) {
        Page<TopicStoreyColumnContent> topicStoreySearchGoodsPage = columnGoodsRepository
                .findAll(columnGoodsRepository.topicStoreySearchContent(request), PageRequest.of(request.getPageNum(),
                        request.getPageSize(), Sort.by(Sort.Direction.ASC, "sorting")));
        List<TopicStoreyColumnContent> content = topicStoreySearchGoodsPage.getContent();
        MicroServicePage<TopicStoreyColumnGoodsDTO> microServicePage = new MicroServicePage<>();
        microServicePage.setTotal(topicStoreySearchGoodsPage.getTotalElements());
        microServicePage.setContent(changeTopicStoreyColumnGoods(content));
        return microServicePage;
    }

    public List<TopicStoreyColumnGoodsDTO> listTopicStoreyColumnGoodsByIdAndSpu(TopicStoreyColumnGoodsQueryRequest request) {
        List<TopicStoreyColumnGoodsDTO> topicStoreyColumnGoodsDTOList=new ArrayList<>();
        columnGoodsRepository.getById(request.getTopicStoreyId(), request.getSpuNo()).stream().forEach(o->{
            TopicStoreyColumnGoodsDTO topicStoreyColumnGoodsDTO=new TopicStoreyColumnGoodsDTO();
            BeanUtils.copyProperties(o,topicStoreyColumnGoodsDTO);
            topicStoreyColumnGoodsDTOList.add(topicStoreyColumnGoodsDTO);
        });
        return topicStoreyColumnGoodsDTOList;
    }

    /**
     * 新增栏目商品
     * @param request
     */
    public void addStoreyColumnGoods(TopicStoreyColumnGoodsAddRequest request) {
        TopicStoreyColumnContent topicStoreySearchContent = new TopicStoreyColumnContent();
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
        topicStoreySearchContent.setSpuNo(request.getSpuNo());
        topicStoreySearchContent.setTopicStoreyId(request.getTopicStoreyId());
        topicStoreySearchContent.setShowLabeTxt(request.getShowLabelTxt());
        topicStoreySearchContent.setNumTxt(request.getNumTxt());
        columnGoodsRepository.save(topicStoreySearchContent);
    }

    /**
     * 修改栏目商品
     * @param request
     */
    @SneakyThrows
    public void updateStoreyColumnGoods(TopicStoreyColumnGoodsUpdateRequest request) {
        TopicStoreyColumnContent topicStoreySearchContent = new TopicStoreyColumnContent();
        topicStoreySearchContent.setId(request.getId());
        topicStoreySearchContent.setImageUrl(request.getImageUrl());
        topicStoreySearchContent.setStartTime(request.getStartTime());
        topicStoreySearchContent.setEndTime(request.getEndTime());
        topicStoreySearchContent.setSkuNo(request.getSkuNo());
        topicStoreySearchContent.setSorting(request.getSorting());
        topicStoreySearchContent.setGoodsName(request.getGoodsName());
        topicStoreySearchContent.setShowLabeTxt(request.getShowLabeTxt());
        topicStoreySearchContent.setNumTxt(request.getNumTxt());
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
        columnGoodsRepository.enable(request.getId(),request.getPublishState());
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

    private List<TopicStoreyColumnDTO> changeTopicStoreyColumn(List<TopicStoreyColumn> content) {
        LocalDateTime now = LocalDateTime.now();
        return content.stream().map(topicStoreySearch -> {
            TopicStoreyColumnDTO topicStoreyColumnDTO = new TopicStoreyColumnDTO();
            BeanUtils.copyProperties(topicStoreySearch, topicStoreyColumnDTO);
            topicStoreyColumnDTO.setStartTime(topicStoreySearch.getCreateTime());
            topicStoreyColumnDTO.setSorting(topicStoreySearch.getOrderNum());
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

    private List<RankListDTO> getRankList(List<TopicStoreyColumn> content,Integer topicStoryId) {
        List<RankListDTO> list=new ArrayList<>();
        List<Integer> ids=new ArrayList<>();
        content.forEach(c->{
            RankListDTO rankListDTO=new RankListDTO();
            BeanUtils.copyProperties(c, rankListDTO);
            List<TopicRankRelation> rankList=new ArrayList<>();
            rankListDTO.setRankList(rankList);
            rankListDTO.setSorting(c.getOrderNum());
            rankListDTO.setPublishState(c.getDeleted());
            list.add(rankListDTO);
            ids.add(c.getId());
        });
        List<TopicRankRelation> rankRelations = relationRepository.collectByPRankColumIdOrderByCRankSortingAsc(ids);
        list.forEach(l->{
            rankRelations.stream().filter(r->r.getPRankColumId().equals(l.getId())).forEach(r->{
                l.getRankList().add(r);
            });
        });
        return list;
    }

    private List<TopicStoreyColumnGoodsDTO> changeTopicStoreyColumnGoods(List<TopicStoreyColumnContent> content) {
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
//    public TopicStoreyMixedComponentResponse getMixedComponent(MixedComponentQueryRequest request) {
//        TopicStoreyColumn topicStoreySearch = new TopicStoreyColumn();
//        topicStoreySearch.setTopicStoreyId(request.getTopicStoreyId());
//        topicStoreySearch.setDeleted(0);
//        List<TopicStoreyColumn> topicStoreySearches = columnRepository.findAll(Example.of(topicStoreySearch),
//                Sort.by(Sort.Direction.ASC, "orderNum"));
//        List<MixedComponentTabDto> mixedComponentTabs = getMixedComponentTabs(topicStoreySearches);
//        MixedComponentKeyWordsDto mixedComponentKeyWord = getMixedComponentKeyWord(topicStoreySearches, mixedComponentTabs, request);
//        getMixedComponentContentPage(request, mixedComponentKeyWord);
//        TopicStoreyMixedComponentResponse topicStoreyMixedComponentResponse = new TopicStoreyMixedComponentResponse();
//        topicStoreyMixedComponentResponse.setMixedComponentTabs(mixedComponentTabs);
//        mixedComponentTabs.forEach(s -> {
//            if(request.getId().equals(s.getId())){
//                s.setMixedComponentKeyWord(mixedComponentKeyWord);
//            }
//        });
//        return topicStoreyMixedComponentResponse;
//    }
//
//    private void getMixedComponentContentPage(MixedComponentQueryRequest request, MixedComponentKeyWordsDto mixedComponentKeyWord) {
//        if (request.getKeywordId() == null) {
//            request.setKeywordId(mixedComponentKeyWord.getKeyWord().stream().findFirst().get().getId());
//        }
//        List<Sort.Order> sortList = new ArrayList<>();
//        sortList.add(Sort.Order.asc("sorting"));
//        sortList.add(Sort.Order.asc("type"));
//        Page<TopicStoreyColumnContent> topicStoreySearchGoodsPage = columnGoodsRepository
//                .findAll(columnGoodsRepository.mixedComponentContent(request), PageRequest.of(request.getPageNum(),
//                        request.getPageSize(), Sort.by(sortList)));
//        TopicStoreyColumnContent topicStoreySearchContent = new TopicStoreyColumnContent();
//        topicStoreySearchContent.setTopicStoreySearchId(request.getKeywordId());
//        List<TopicStoreyColumnContent> goods = columnGoodsRepository.findAll(Example.of(topicStoreySearchContent), Sort.by(Sort.Direction.ASC, "sorting"));
//        List<TopicStoreyColumnContent> content = topicStoreySearchGoodsPage.getContent();
//        MicroServicePage<MixedComponentContentDto> mixedComponentContentPage = new MicroServicePage<>();
//        mixedComponentContentPage.setContent(content.stream().map(s -> {
//            MixedComponentContentDto mixedComponentContentDto = new MixedComponentContentDto();
//            mixedComponentContentDto.setName(s.getTitle());
//            mixedComponentContentDto.setRecommend(s.getRecommend());
//            mixedComponentContentDto.setType(s.getType());
//            switch (s.getType()) {
//                case 1: //商品
//                    mixedComponentContentDto.setGoods(this.getGoods(s, null));
//                    break;
//                case 3: //视频
//                    mixedComponentContentDto.setGoods(this.getGoods(s, goods));
//                    mixedComponentContentDto.setImage(s.getImageUrl());
//                    mixedComponentContentDto.setVideo(s.getLinkUrl());
//                    break;
//                case 4: //广告
//                    mixedComponentContentDto.setGoods(this.getGoods(s, goods));
//                    mixedComponentContentDto.setImage(s.getImageUrl());
//                    mixedComponentContentDto.setUrl(s.getLinkUrl());
//                    break;
//                case 5: //指定内容
//                    mixedComponentContentDto.setGoods(this.getGoods(s, goods));
//                    mixedComponentContentDto.setTitleImage(s.getImageUrl());
//                    mixedComponentContentDto.setImage(s.getLinkUrl());
//                    break;
//                default:
//                    break;
//            }
//            BeanUtils.copyProperties(s, mixedComponentContentDto);
//            return mixedComponentContentDto;
//        }).collect(Collectors.toList()));
//        mixedComponentContentPage.setTotal(topicStoreySearchGoodsPage.getTotalElements());
//        List<KeyWordDto> keyWord = mixedComponentKeyWord.getKeyWord();
//        keyWord.forEach(s -> {
//            if(request.getKeywordId().equals(s.getId())){
//                s.setMixedComponentContentPage(mixedComponentContentPage);
//            }
//        });
//    }

    //获取商品信息
    private List<GoodsDto> getGoods(TopicStoreyColumnContent content, List<TopicStoreyColumnContent> goods ) {
        List<GoodsDto> goodsDtos = new ArrayList<>();
        if (StringUtils.isNotEmpty(content.getSpuId())) {
            goodsDtos.add(GoodsDto.builder().spuId(content.getSpuId()).goodsName(content.getGoodsName()).build());
            return goodsDtos;
        }
//         goodsDtos = goods.stream().filter(s -> content.getId().equals(s.getPId())).map(s -> {
//             return GoodsDto.builder().spuId(s.getSpuId()).goodsName(s.getGoodsName()).build();
//         }).collect(Collectors.toList());
        return goodsDtos;
    }

//    public List<MixedComponentTabDto> getMixedComponentTabs(List<TopicStoreyColumn> topicStoreySearches) {
//        return topicStoreySearches.stream().filter(s -> MixedComponentLevel.ONE.toValue().equals(s.getLevel())).map(s -> {
//                MixedComponentTabDto mixedComponentTabDto = new MixedComponentTabDto();
//                mixedComponentTabDto.setId(s.getId());
//                mixedComponentTabDto.setName(s.getName());
//                mixedComponentTabDto.setSubName(s.getSubName());
//                if (StringUtils.isNotEmpty(s.getColor())) {
//                    JSONObject color = JSON.parseObject(s.getColor());
//                    mixedComponentTabDto.setSelectedColor(color.getString("selected"));
//                    mixedComponentTabDto.setUnSelectedColor(color.getString("unselected"));
//                }
//                if (StringUtils.isNotEmpty(s.getImage())) {
//                    JSONObject image = JSON.parseObject(s.getImage());
//                    mixedComponentTabDto.setSelectedImage(image.getString("selected"));
//                    mixedComponentTabDto.setUnSelectedImage(image.getString("unselected"));
//                }
//
//            return mixedComponentTabDto;
//        }).collect(Collectors.toList());
//    }

//    public MixedComponentKeyWordsDto getMixedComponentKeyWord(List<TopicStoreyColumn> topicStoreySearches, List<MixedComponentTabDto> mixedComponentTabs, MixedComponentQueryRequest request) {
//        if (request.getId() == null) {
//            request.setId(mixedComponentTabs.get(0).getId());
//        }
//        Integer finalId = request.getId();
//        MixedComponentKeyWordsDto mixedComponentKeyWordsDto = new MixedComponentKeyWordsDto();
//        List<KeyWordDto> keyWords = topicStoreySearches.stream()
//                .filter(s -> finalId.equals(s.getPId()) && MixedComponentLevel.TWO.toValue().equals(s.getLevel()))
//                .map(s -> {
//                    KeyWordDto keyWordDto = new KeyWordDto();
//                    keyWordDto.setId(s.getId());
//                    keyWordDto.setName(s.getName());
//            return keyWordDto;
//        }).collect(Collectors.toList());
//        mixedComponentKeyWordsDto.setKeyWord(keyWords);
//        Optional<TopicStoreyColumn> storeySearch = topicStoreySearches.stream().filter(s -> finalId.equals(s.getPId()) && MixedComponentLevel.TWO.toValue().equals(s.getLevel())).findFirst();
//        if (storeySearch.get() != null && StringUtils.isNotEmpty(storeySearch.get().getColor())) {
//            JSONObject colorObject = JSON.parseObject(storeySearch.get().getColor());
//            mixedComponentKeyWordsDto.setKeyWordSelectedColor(colorObject.getString("selected"));
//            mixedComponentKeyWordsDto.setKeyWordUnSelectedColor(colorObject.getString("unselected"));
//        }
//        return mixedComponentKeyWordsDto;
//    }


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

    /**
     * @Description 混合标签tab列表
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    public MicroServicePage<MixedComponentTabDto> listMixedComponentTab(MixedComponentTabQueryRequest request) {
        ColumnQueryRequest columnQueryRequest = new ColumnQueryRequest();
        BeanUtils.copyProperties(request, columnQueryRequest);
        MicroServicePage<ColumnDTO> columnDTOS = listTopicStoreyColumn(columnQueryRequest);
        List<ColumnDTO> content = columnDTOS.getContent();
        List<MixedComponentTabDto> collect = content.stream().map(s -> {
            return new MixedComponentTabDto(s);
        }).collect(Collectors.toList());
        MicroServicePage<MixedComponentTabDto> mixedComponentTabDtos = new MicroServicePage<>();
        mixedComponentTabDtos.setContent(collect);
        mixedComponentTabDtos.setTotal(columnDTOS.getTotal());
        return mixedComponentTabDtos;
    }

    /**
     * @Description 混合标签tab添加
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    public void addMixedComponentTab(MixedComponentTabAddRequest request) {
//        ColumnAddRequest columnAddRequest = new ColumnAddRequest();
//        BeanUtils.copyProperties(request, columnAddRequest);
//        columnAddRequest.setCreateTime(request.getStartTime());
//        columnAddRequest.setOrderNum(request.getSorting());
        addTopicStoreyColumn(request.getColumnAddRequest());
    }

    /**
     * @Description topic_storey_column表list
     * @Author zh
     * @Date  2023/2/18 12:53
     */
    public MicroServicePage<ColumnDTO> listTopicStoreyColumn(ColumnQueryRequest request) {
        List<Sort.Order> sortList = new ArrayList<>();
        sortList.add(Sort.Order.asc("deleted"));
        sortList.add(Sort.Order.asc("orderNum"));
        Page<TopicStoreyColumn> topicStoreySearchPage = columnRepository
                .findAll(columnRepository.columnSearch(request), PageRequest.of(request.getPageNum(),
                        request.getPageSize(), Sort.by(sortList)));
        List<TopicStoreyColumn> content = topicStoreySearchPage.getContent();
        MicroServicePage<ColumnDTO> microServicePage = new MicroServicePage<>();
        microServicePage.setTotal(topicStoreySearchPage.getTotalElements());
        microServicePage.setContent(content.stream().map(topicStoreyColumn -> {
            ColumnDTO columnDTO = new ColumnDTO(topicStoreyColumn.getCreateTime(),
                    topicStoreyColumn.getEndTime(), topicStoreyColumn.getDeleted());
            BeanUtils.copyProperties(topicStoreyColumn, columnDTO);
            return columnDTO;
        }).collect(Collectors.toList()));
        return microServicePage;
    }

    /**
     * @Description topic_storey_column表add
     * @Author zh
     * @Date  2023/2/18 12:53
     */
    public TopicStoreyColumn addTopicStoreyColumn(ColumnAddRequest request) {
        TopicStoreyColumn topicStoreyColumn = new TopicStoreyColumn();
        BeanUtils.copyProperties(request, topicStoreyColumn);
        topicStoreyColumn.setUpdateTime(LocalDateTime.now());
        topicStoreyColumn.setColor(JSON.toJSONString(request.getColor()));
        topicStoreyColumn.setImage(JSON.toJSONString(request.getImage()));
        topicStoreyColumn.setDeleted(0);
        TopicStoreyColumn column = columnRepository.save(topicStoreyColumn);
        return column;
    }

    /**
     * @Description topic_storey_column表update
     * @Author zh
     * @Date  2023/2/18 12:53
     */
    @SneakyThrows
    public void updateTopicStoreyColumn(ColumnUpdateRequest request) {
        TopicStoreyColumn topicStoreyColumn = new TopicStoreyColumn();
        BeanUtils.copyProperties(request, topicStoreyColumn);
        topicStoreyColumn.setColor(request.getColor() != null ? JSON.toJSONString(request.getColor()) : null);
        topicStoreyColumn.setImage(request.getImage() != null ? JSON.toJSONString(request.getImage()) : null);
        updateUtil.partialUpdate(topicStoreyColumn.getId(), topicStoreyColumn, columnRepository);
    }

    /**
     * @Description topic_storey_column表状态修改
     * @Author zh
     * @Date  2023/2/18 12:53
     */
    @Transactional
    public void enableTopicStoreyColumn(ColumnEnableRequest request) {
        columnRepository.enable(request.getId(),request.getPublishState());
    }

    /**
     * @Description topic_storey_column表删除
     * @Author zh
     * @Date  2023/2/18 12:53
     */
    public void deleteTopicStoreyColumn(Integer id) {
        columnRepository.deleteById(id);
    }

    /**
     * @Description topic_storey_column表根据id获取
     * @Author zh
     * @Date  2023/2/18 12:53
     */
    public ColumnDTO getTopicStoreyColumnById(Integer id) {
        TopicStoreyColumn topicStoreyColumn = new TopicStoreyColumn();
        topicStoreyColumn.setId(id);
        topicStoreyColumn.setDeleted(0);
        ColumnDTO columnDTO = new ColumnDTO();
        BeanUtils.copyProperties(columnRepository.findOne(Example.of(topicStoreyColumn)), columnDTO);
        return columnDTO;
    }

    /**
     * @Description topic_storey_column_content表list
     * @Author zh
     * @Date  2023/2/18 12:53
     */
    public MicroServicePage<ColumnContentDTO> listTopicStoreyColumnContent(ColumnContentQueryRequest request) {
        List<Sort.Order> sortList = new ArrayList<>();
        sortList.add(Sort.Order.desc("deleted"));
        sortList.add(Sort.Order.asc("orderNum"));
        Page<TopicStoreyColumnContent> topicStoreySearchContentPage = columnGoodsRepository
                .findAll(columnGoodsRepository.topicStoreySearchContent(request), PageRequest.of(request.getPageNum(),
                        request.getPageSize(), Sort.by(sortList)));
        List<TopicStoreyColumnContent> content = topicStoreySearchContentPage.getContent();
        MicroServicePage<ColumnContentDTO> microServicePage = new MicroServicePage<>();
        microServicePage.setTotal(topicStoreySearchContentPage.getTotalElements());
        microServicePage.setContent(content.stream().map(topicStoreyColumnContent -> {
            ColumnContentDTO columnContentDTO = new ColumnContentDTO();
            BeanUtils.copyProperties(topicStoreyColumnContent, columnContentDTO);
            return columnContentDTO;
        }).collect(Collectors.toList()));
        return microServicePage;
    }

    /**
     * @Description topic_storey_column_content表add
     * @Author zh
     * @Date  2023/2/18 12:53
     */
    public void addTopicStoreyColumnContent(ColumnContentAddRequest request) {
        TopicStoreyColumnContent topicStoreyColumnContent = new TopicStoreyColumnContent();
        BeanUtils.copyProperties(request, topicStoreyColumnContent);
        topicStoreyColumnContent.setUpdateTime(LocalDateTime.now());
        topicStoreyColumnContent.setDeleted(0);
        columnGoodsRepository.save(topicStoreyColumnContent);
    }

    /**
     * @Description topic_storey_column_content表update
     * @Author zh
     * @Date  2023/2/18 12:53
     */
    @SneakyThrows
    public void updateTopicStoreyColumnContent(ColumnContentUpdateRequest request) {
        TopicStoreyColumnContent topicStoreyColumnContent = new TopicStoreyColumnContent();
        BeanUtils.copyProperties(request, topicStoreyColumnContent);
        updateUtil.partialUpdate(topicStoreyColumnContent.getId(), topicStoreyColumnContent, columnGoodsRepository);
    }

    /**
     * @Description topic_storey_column_content表状态修改
     * @Author zh
     * @Date  2023/2/18 12:53
     */
    @Transactional
    public void enableTopicStoreyColumnContent(ColumnContentEnableRequest request) {
        columnGoodsRepository.enable(request.getId(),request.getPublishState());
    }


    /**
     * @Description topic_storey_column_content表删除
     * @Author zh
     * @Date  2023/2/18 12:53
     */
    public void deleteTopicStoreyColumnContent(Integer id) {
        columnGoodsRepository.deleteById(id);
    }

    /**
     * @Description topic_storey_column_content表根据id获取
     * @Author zh
     * @Date  2023/2/18 12:53
     */
    public ColumnContentDTO getTopicStoreyColumnContentById(Integer id) {
        TopicStoreyColumnContent topicStoreyColumnContent = new TopicStoreyColumnContent();
        topicStoreyColumnContent.setId(id);
        topicStoreyColumnContent.setDeleted(0);
        ColumnContentDTO columnContentDTO = new ColumnContentDTO();
        BeanUtils.copyProperties(columnGoodsRepository.findOne(Example.of(topicStoreyColumnContent)), columnContentDTO);
        return columnContentDTO;
    }


}
