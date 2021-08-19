package com.wanmi.ares.source.service;

import com.wanmi.ares.report.customer.dao.CustomerMapper;
import com.wanmi.ares.request.CustomerQueryRequest;
import com.wanmi.ares.source.model.root.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品sku基础信息service
 * Created by sunkun on 2017/9/22.
 */
@Slf4j
@Service
public class CustomerService {

    @Resource
    private CustomerMapper customerMapper;

    /**
     * 新增客户
     * @param request
     * @return
     */
    public int insertCustomer(Customer request){
        return customerMapper.insert(request);
    }

    /**
     * 主键查询
     * @param customerId
     * @return
     */
    public Customer queryCustomerById(String customerId){
        return customerMapper.queryById(customerId);
    }

    /**
     * 更新客户信息
     * @param customer
     * @return
     */
    public int updateCustomerById(Customer customer){
        return customerMapper.updateById(customer);
    }

    /**
     * 解绑客户(将本来属于某个业务员的客户的业务员设置为null)
     * @param employeeId
     * @return
     */
    public int unbindCustomer(String employeeId){
        return customerMapper.unbindCustomer(employeeId);
    }

    /**
     * 客户Id批量删除
     * @param customerIds
     * @return
     */
    public int deleteByCustomerIds(List<String> customerIds){
        return customerMapper.deleteByIds(customerIds);
    }

    /**
     * 根据条件查询
     * @param request
     * @return
     */
    public List<Customer> queryByParams(CustomerQueryRequest request){
        return customerMapper.queryByParams(request);
    }

//    public void deleteLevel(String levelId) throws Exception{
//        if(StringUtils.isBlank(levelId)){
//            return;
//        }
//
//        List<CustomerLevel> defaultLevels = getDefaultLevel();
//        //默认等级为空，则不执行
//        if(CollectionUtils.isEmpty(defaultLevels)){
//            return;
//        }
//
//        basicDataElasticService.doDelete(Arrays.asList(levelId), CustomerLevel.class);
//
//        CustomerQueryRequest request = new CustomerQueryRequest();
//        request.setLevelId(NumberUtils.toLong(levelId));
//        List<Customer> customers = queryByParams(request);
//        if(CollectionUtils.isNotEmpty(customers)){
//            UpdateRequest updateRequest = new UpdateRequest();
//            updateRequest
//                    .doc(XContentFactory.jsonBuilder()
//                            .startObject()
//                            .field("levelId", NumberUtils.toLong(defaultLevels.get(0).getId()))
//                            .endObject());
//            List<UpdateQuery> updateQueries = customers.stream().map(customer -> new UpdateQueryBuilder()
//                    .withClass(Customer.class)
//                    .withId(customer.getId())
//                    .withUpdateRequest(updateRequest)
//                    .build()).collect(Collectors.toList());
//            elasticsearchTemplate.bulkUpdate(updateQueries);
//        }
//    }
//
//
//    /**
//     * 获取默认等级
//     * @return
//     */
//    private List<CustomerLevel> getDefaultLevel(){
//        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
//        builder.withIndices(EsConstants.ES_INDEX_BASIC);
//        builder.withTypes(EsConstants.ES_TYPE_CUSTOMER_LEVEL);
//        BoolQueryBuilder bq = QueryBuilders.boolQuery();
//        bq.must(termQuery("default", true));
//        bq.must(termQuery("delFlag", false));
//        builder.withQuery(bq);
//        return elasticsearchTemplate.queryForList(builder.build(), CustomerLevel.class);
//    }

//    /**
//     * 查询所有
//     * @return 所有sku
//     */
//    public List<Customer> queryAll(CustomerQueryRequest request){
//        List<Customer> infos = new ArrayList<>();
//        Page<Customer> infoPage = this.query(request);
//        infos.addAll(infoPage.getContent());
//        if(infoPage.getTotalPages() > 1){
//            for(long page = 1; page < infoPage.getTotalPages();page++){
//                request.setPageNum(page);
//                infos.addAll(this.query(request).getContent());
//            }
//        }
//        return infos;
//    }

//    /**
//     * 分页查询
//     * @return 所有sku
//     */
//    public Page<Customer> query(CustomerQueryRequest request){
//        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
//        builder.withIndices(EsConstants.ES_INDEX_BASIC);
//        builder.withTypes(EsConstants.ES_TYPE_CUSTOMER);
//        builder.withPageable(request.getPageable());
//        BoolQueryBuilder bq = QueryBuilders.boolQuery();
//        //编号查询
////        if (StringUtils.isNotEmpty(request.getCustomerId())){
////            bq.must(termQuery("id", request.getCustomerId()));
////        }
//
//        //员工编号查询
//        if (StringUtils.isNotEmpty(request.getEmployeeId())){
//            bq.must(termQuery("employeeId", request.getEmployeeId()));
//        }
//
//        //品牌精确查询
//        if (request.getLevelId() != null){
//            bq.must(termQuery("levelId", request.getLevelId()));
//        }
//
//        //客户名称或账号模糊查询
//        if (StringUtils.isNotBlank(request.getKeyWord())) {
//            BoolQueryBuilder bq1 = QueryBuilders.boolQuery();
//            bq1.should(matchPhrasePrefixQuery("name", request.getKeyWord().trim()));
//            bq1.should(matchPhrasePrefixQuery("account", request.getKeyWord().trim()));
//            bq.must(bq1);
//        }
//        builder.withQuery(bq);
//        return elasticsearchTemplate.queryForPage(builder.build(), Customer.class);
//    }

}
