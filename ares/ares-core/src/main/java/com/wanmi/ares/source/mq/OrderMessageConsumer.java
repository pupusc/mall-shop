package com.wanmi.ares.source.mq;

import com.wanmi.ares.report.customer.dao.StoreCateMapper;
import com.wanmi.ares.source.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消息接收处理Bean
 * <p>
 *      为了对具体消息的生产消费情况做更好地监控，消息以业务中最小粒度的操作区分
 * </p>
 * Created by of628-wenzhi on 2017-10-10-下午2:19.
 */
@Slf4j
@Component
@EnableBinding(OrderMessageSink.class)
public class OrderMessageConsumer {

//    @Resource
//    private BasicDataElasticService basicDataElasticService;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerAndLevelService customerAndLevelService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    GoodsCateService goodsCateService;

    @Autowired
    private StoreCateMapper storeCateMapper;

//    @StreamListener(MQConstant.Q_ARES_ORDER_CREATE)
//    public void createOrder(String json) {
//        try {
//            OrderDataRequest request = JSONObject.parseObject(json, OrderDataRequest.class);
//            request.setType(DataSourceType.CREATE);
//            this.save(request);
//        } catch (Exception e) {
//            log.error("Activemq order execute method [order.create] error, param={}", json, e);
//        }
//    }
//
//    @StreamListener(MQConstant.Q_ARES_ORDER_LIST_CREATE)
//    public void createOrderList(String json) {
//        List<OrderDataRequest> orderRequests = JSONObject.parseArray(json, OrderDataRequest.class);
//        orderRequests.stream().forEach(request -> {
//            try {
//                request.setType(DataSourceType.CREATE);
//                this.save(request);
//            } catch (Exception e) {
//                log.error("Activemq order.list execute method [order.list.create] error, param={}", json, e);
//            }
//        });
//    }
//
//    @StreamListener(MQConstant.Q_ARES_ORDER_PAY)
//    public void payOrder(String json) {
//        try {
//            OrderDataRequest request = JSONObject.parseObject(json, OrderDataRequest.class);
//            request.setType(DataSourceType.PAY);
//            this.save(request);
//        } catch (Exception e) {
//            log.error("Activemq order execute method [order.modify] error, param={}", json, e);
//        }
//    }
//
//    @StreamListener(MQConstant.Q_ARES_ORDER_RETURN)
//    public void returnOrder(String json) {
//        try {
//            OrderDataRequest request = JSONObject.parseObject(json, OrderDataRequest.class);
//            request.setType(DataSourceType.RETURN);
//            this.save(request);
//        } catch (Exception e) {
//            log.error("Activemq order execute method [order.delete] error, param={}", json, e);
//        }
//    }

    /**
     * 调用
     * @param request
     * @throws Exception
     */
//    public void save(OrderDataRequest request) throws Exception {
//        DataPool data = new DataPool();
//        BeanUtils.copyProperties(data, request);
//        data.setTime(request.getOperationDate());
//        data.setOrderNo(data.getId());
//        data.setMurCustomerId(data.getCustomerId());
//        //填充客户信息
//        Customer customer = customerService.queryCustomerById(request.getCustomerId());
//        CustomerAndLevel level = customerAndLevelService.selectCustomerStoreLevel(request.getCustomerId(),request.getCompanyId());
//        if(Objects.nonNull(level) && Objects.nonNull(level.getCustomerLevelId())){
//            //设置客户等级(对应供应商的)
//            customer.setLevelId(level.getCustomerLevelId().toString());
//        }
//        if(customer == null){
//            log.error("Activemq order execute method customer is empty error, order param={}", request);
//        }else{
//            data.setBuyer(customer);
//            //填充冗余的业务员供应商id
//            if(customer.getEmployeeId() != null){
//                Employee employee = employeeService.queryByEmployeeId(customer.getEmployeeId());
//                if(employee != null){
//                    data.setEmployeeCompanyId(employee.getCompanyId());
//                }
//            }
//        }
//
//        //查询商品信息
//        GoodsInfoQueryRequest request1 = new GoodsInfoQueryRequest();
//        request1.setGoodsInfoIds(request.getItemRequestList().stream().map(TradeItemRequest::getId).collect(Collectors.toList()));
//        List<GoodsInfo> skus = goodsInfoService.queryAll(request1);
//        //为空，表示本地ES的商品不存在
//        if(CollectionUtils.isEmpty(skus)){
//            log.error("Activemq order execute method sku list is empty error, order param={}", request);
//            throw new Exception("sku list is empty");
//        }
//
//        Map<String,GoodsInfo> goodsInfoMap = skus.stream().collect(Collectors.toMap(GoodsInfo::getId, c -> c));
//        //传递的商品，ES本地不存在的情况
//        if(request.getItemRequestList().stream().filter(tradeItemRequest -> goodsInfoMap.get(tradeItemRequest.getId()) == null).count() > 0){
//            log.error("Activemq order execute method sku is not exist error, order param={}", request);
//        }
//
//        //获取商品的分类是否是含有子类
//        GoodsCateQueryRequest queryRequest = new GoodsCateQueryRequest();
//        queryRequest.setDelFlag(Boolean.FALSE);
//        queryRequest.setParentIds(skus.stream().map(GoodsInfo::getLeafCateId).filter(aLong -> aLong != null).collect(Collectors.toList()));
//        queryRequest.setPageSize((long)(skus.size()));
//        Set<Long> parentIds = new HashSet<>();
//        List<GoodsCate> goodsCates = goodsCateService.query(queryRequest);
//        if(CollectionUtils.isNotEmpty(goodsCates)){
//            parentIds.addAll(goodsCates.stream().map(GoodsCate::getParentId).collect(Collectors.toSet()));
//        }
//
//        //查询所有店铺父分类数据
//        List<String> storeCateIds = skus.stream().filter(goodsInfo -> CollectionUtils.isNotEmpty(goodsInfo.getStoreCateIds()))
//                    .map(GoodsInfo::getStoreCateIds)
//                    .flatMap(Collection::stream)
//                    .filter(Objects::nonNull)
//                    .map(String::valueOf)
//                    .distinct()
//                    .collect(Collectors.toList());
//        Set<Long> storeParentIds = new HashSet<>();
//        if(CollectionUtils.isNotEmpty(storeCateIds)) {
//            storeParentIds.addAll(storeCateMapper.queryByParentId(storeCateIds).stream().map(storeCate -> NumberUtils.toLong(storeCate.getCateParentId())).distinct().collect(Collectors.toSet()));
//        }
//
//        List<GoodsInfoPool> infoPoolList = request.getItemRequestList().stream().map(itemRequest -> {
//            GoodsInfo info = goodsInfoMap.get(itemRequest.getId());
//            GoodsInfoPool infoPool = new GoodsInfoPool();
//            infoPool.setId(itemRequest.getId());
//            if(info != null) {
//                infoPool.setBrandId(info.getBrandId());
//                infoPool.setCateIds(info.getCateIds());
//
//                //如果没有子类
//                if(!parentIds.contains(info.getLeafCateId())) {
//                    infoPool.setLeafCateId(info.getLeafCateId());
//                }
//
//                //店铺分类
//                if(CollectionUtils.isNotEmpty(info.getStoreCateIds())) {
//                    infoPool.setStoreCateIds(info.getStoreCateIds());
//                    //提取叶子节点
//                    infoPool.setLeafStoreCateIds(info.getStoreCateIds().stream().filter(id -> !storeParentIds.contains(id)).collect(Collectors.toList()));
//                }
//            }
//
//            infoPool.setPrice(itemRequest.getPrice());
//            infoPool.setNum(itemRequest.getNum());
//            infoPool.setType(request.getType());
//            return infoPool;
//        }).collect(Collectors.toList());
//        data.setSkus(infoPoolList);
//        if(!template.indexExists(EsConstants.ES_INDEX)){
//            template.createIndex(EsConstants.ES_INDEX);
//        }
//        template.putMapping(DataPool.class);
//        basicDataElasticService.index(data);
//    }
}
