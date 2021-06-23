package com.wanmi.sbc.elastic.employee.service;


import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.EmployeePageRequest;
import com.wanmi.sbc.customer.api.response.employee.EmployeeImportResponse;
import com.wanmi.sbc.customer.bean.dto.EmployeeDTO;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.vo.EmployeePageVO;
import com.wanmi.sbc.elastic.api.request.employee.*;
import com.wanmi.sbc.elastic.employee.mapper.EsEmployeeMapper;
import com.wanmi.sbc.elastic.employee.model.root.EsEmployee;
import com.wanmi.sbc.elastic.employee.repository.EsEmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class EsEmployeeService {

    @Autowired
    private EsEmployeeRepository esEmployeeRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;



    public void save(EsEmployee esEmployee) {
        esEmployeeRepository.save(esEmployee);
    }

    public Page<EsEmployee> page(SearchQuery searchQuery) {
        return esEmployeeRepository.search(searchQuery);
    }

    public void batchEnableByIds(List<String> employeeIds) {
        List<EsEmployee> EsEmployeeList = findByIds(employeeIds).map(esEmployee -> {
            esEmployee.setAccountState(AccountState.ENABLE);
            return esEmployee;
        }).collect(Collectors.toList());
        saveAll(EsEmployeeList);
    }

    public void  disableById(EsEmployeeDisableByIdRequest esEmployeeDisableByIdRequest) {
        Optional<EsEmployee> esEmployeeOptional = esEmployeeRepository.findById(esEmployeeDisableByIdRequest.getEmployeeId()).map(esEmployee -> {
            esEmployee.setAccountState(AccountState.DISABLE);
            esEmployee.setAccountDisableReason(esEmployeeDisableByIdRequest.getAccountDisableReason());
            return esEmployee;
        });
        esEmployeeOptional.ifPresent(this::save);
    }

    public void batchDisableByIds(EsEmployeeBatchDisableByIdsRequest esEmployeeBatchDisableByIdsRequest) {
        List<String> employeeIds = esEmployeeBatchDisableByIdsRequest.getEmployeeIds();
        List<EsEmployee> EsEmployeeList = findByIds(employeeIds).map(esEmployee -> {
            esEmployee.setAccountState(AccountState.DISABLE);
            esEmployee.setAccountDisableReason(esEmployeeBatchDisableByIdsRequest.getAccountDisableReason());
            return esEmployee;
        }).collect(Collectors.toList());
        saveAll(EsEmployeeList);
    }


    public Stream<EsEmployee> findByIds(List<String> employeeIds){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (CollectionUtils.isNotEmpty(employeeIds)) {
            boolQueryBuilder.must(QueryBuilders.idsQuery().addIds(employeeIds.stream().toArray(String[]::new)));
        }
        Iterable<EsEmployee> iterable = esEmployeeRepository.search(boolQueryBuilder);
        return  StreamSupport.stream(iterable.spliterator(), Boolean.TRUE);
    }

    public void batchDeleteByIds(List<String> employeeIds) {
        DeleteQuery deleteQuery = new DeleteQuery();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.idsQuery().addIds(employeeIds.stream().toArray(String[]::new)));
        deleteQuery.setQuery(boolQueryBuilder);
        elasticsearchTemplate.delete(deleteQuery,EsEmployee.class);
    }

    public void changeDepartment(EsEmployeeChangeDepartmentRequest eSEmployeeChangeDepartmentRequest) {
        List<String> employeeIds = eSEmployeeChangeDepartmentRequest.getEmployeeIds();
        List<String> departmentIds = eSEmployeeChangeDepartmentRequest.getDepartmentIds();
        List<EsEmployee> EsEmployeeList = findByIds(employeeIds).map(esEmployee -> {
            esEmployee.setDepartmentIds(departmentIds);
            return esEmployee;
        }).collect(Collectors.toList());
        saveAll(EsEmployeeList);
    }

    public void batchSetEmployeeByIds(List<String> employeeIds) {
        List<EsEmployee> EsEmployeeList = findByIds(employeeIds).map(esEmployee -> {
            esEmployee.setIsEmployee(BigDecimal.ROUND_UP);
            return esEmployee;
        }).collect(Collectors.toList());
        saveAll(EsEmployeeList);
    }

    public void batchDimissionByIds(EsEmployeeBatchDimissionByIdsRequest esEmployeeBatchDimissionByIdsRequest) {
        List<String> employeeIds = esEmployeeBatchDimissionByIdsRequest.getEmployeeIds();
        List<EsEmployee> EsEmployeeList = findByIds(employeeIds).map(esEmployee -> {
            esEmployee.setAccountState(AccountState.DIMISSION);
            esEmployee.setAccountDisableReason(esEmployeeBatchDimissionByIdsRequest.getAccountDimissionReason());
            esEmployee.setManageDepartmentIds(null);
            return esEmployee;
        }).collect(Collectors.toList());
        saveAll(EsEmployeeList);
    }

    public void activateAccount(EsEmployeeActivateAccountRequest esEsEmployeeActivateAccountRequest) {
        List<String> employeeIds = esEsEmployeeActivateAccountRequest.getEmployeeIds();
        List<EsEmployee> EsEmployeeList = findByIds(employeeIds).filter(esEmployee ->
                !AccountState.DIMISSION.equals(esEmployee.getAccountState()))
                .peek(esEmployee -> esEmployee.setBecomeMember(BigDecimal.ROUND_DOWN)).collect(Collectors.toList());
        saveAll(EsEmployeeList);
    }

    public void handoverEmployee(EsEmployeeHandoverRequest esEmployeeHandoverRequest) {
        List<String> employeeIds = esEmployeeHandoverRequest.getEmployeeIds();
        List<EsEmployee> EsEmployeeList = findByIds(employeeIds).filter(esEmployee ->
                !AccountState.DIMISSION.equals(esEmployee.getAccountState()))
                .filter(esEmployee ->
                        esEmployee.getIsEmployee() == BigDecimal.ROUND_UP)
                .peek(esEmployee -> esEmployee.setHeirEmployeeId(esEmployeeHandoverRequest.getNewEmployeeId())).collect(Collectors.toList());
        saveAll(EsEmployeeList);
    }

    public void saveAll(List<EsEmployee> employeeList) {
        esEmployeeRepository.saveAll(employeeList);
    }

    public void init(EmployeePageRequest pageRequest) {
        List<EmployeePageVO> employeePageVOS;
        try {
            employeePageVOS = employeeQueryProvider.pageList(pageRequest).getContext().getEmployeeList();
            List<EsEmployee> esEmployeeList = employeePageVOS.parallelStream().filter(employeePageVO -> StringUtils.isNotEmpty(employeePageVO.getEmployeeId())).map(employeePageVO -> {
                EsEmployee esEmployee = KsBeanUtil.convert(employeePageVO, EsEmployee.class);
                if (StringUtils.isNotEmpty(employeePageVO.getRoleIds())) {
                    esEmployee.setRoleIds(Arrays.asList(employeePageVO.getRoleIds().split(",")));
                }
                if (StringUtils.isNotEmpty(employeePageVO.getManageDepartmentIds())) {
                    esEmployee.setManageDepartmentIds(Arrays.asList(employeePageVO.getManageDepartmentIds().split(",")));
                }
                if (StringUtils.isNotEmpty(employeePageVO.getDepartmentIds())) {
                    esEmployee.setDepartmentIds(Arrays.asList(employeePageVO.getDepartmentIds().split(",")));
                }
                if (employeePageVO.getIsLeader() == null) {
                    esEmployee.setIsLeader(BigDecimal.ROUND_UP);
                }
                if (employeePageVO.getBecomeMember() == null) {
                    esEmployee.setBecomeMember(BigDecimal.ROUND_UP);
                }
                return esEmployee;
            }).collect(Collectors.toList());
            saveAll(esEmployeeList);
            //如果不是最后一页，继续执行
            if(CollectionUtils.isNotEmpty(employeePageVOS)) {
                Integer pageNum = pageRequest.getPageNum() + 1;
                pageRequest.setPageNum(pageNum);
                init(pageRequest);
            }else {
                log.info("==========ES初始化员工结束，结束pageNum:{}==============",pageRequest.getPageNum());
            }
        }catch (Exception e) {
            log.info("==========ES初始化员工异常，异常pageNum:{}==============",pageRequest.getPageNum());
            e.printStackTrace();
        }

    }
}
