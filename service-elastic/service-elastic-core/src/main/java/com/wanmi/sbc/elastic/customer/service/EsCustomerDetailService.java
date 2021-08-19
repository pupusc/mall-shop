package com.wanmi.sbc.elastic.customer.service;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailInitEsRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailListByCustomerIdsRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelPageRequest;
import com.wanmi.sbc.customer.bean.dto.CustomerDetailFromEsDTO;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailFromEsVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailInitEsVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.elastic.api.request.customer.*;
import com.wanmi.sbc.elastic.bean.constant.customer.CustomerDetailErrorCode;
import com.wanmi.sbc.elastic.bean.dto.customer.EsCustomerDetailDTO;
import com.wanmi.sbc.elastic.bean.dto.customer.EsEnterpriseInfoDTO;
import com.wanmi.sbc.elastic.bean.dto.customer.EsPaidCardDTO;
import com.wanmi.sbc.elastic.bean.dto.customer.EsStoreCustomerRelaDTO;
import com.wanmi.sbc.elastic.bean.vo.customer.EsCustomerDetailVO;
import com.wanmi.sbc.elastic.customer.mapper.EsCustomerDetailMapper;
import com.wanmi.sbc.elastic.customer.model.root.EsCustomerDetail;
import com.wanmi.sbc.elastic.customer.model.root.EsEnterpriseInfo;
import com.wanmi.sbc.elastic.customer.model.root.EsPaidCard;
import com.wanmi.sbc.elastic.customer.model.root.EsStoreCustomerRela;
import com.wanmi.sbc.elastic.customer.repository.EsCustomerDetailRepository;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressListRequest;
import com.wanmi.sbc.setting.bean.vo.PlatformAddressVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

/**
 * 会员详情Service
 */
@Slf4j
@Service
public class EsCustomerDetailService {

    @Autowired
    private EsCustomerDetailRepository esCustomerDetailRepository;

    @Autowired
    private EsCustomerDetailMapper esCustomerDetailMapper;

    @Autowired
    private PlatformAddressQueryProvider platformAddressQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 初始化ES数据
     */
    public void init(EsCustomerDetailInitRequest esCustomerDetailInitRequest){
        Boolean initCustomerDetail = Boolean.TRUE;
        int pageNum = esCustomerDetailInitRequest.getPageNum();
        Integer pageSize = Objects.equals(10,esCustomerDetailInitRequest.getPageSize().intValue()) ? 2000 : esCustomerDetailInitRequest.getPageSize();
        CustomerDetailInitEsRequest request = KsBeanUtil.convert(esCustomerDetailInitRequest,CustomerDetailInitEsRequest.class);
        try {
            //支持多个customerId刷ES
            if (CollectionUtils.isNotEmpty(esCustomerDetailInitRequest.getCustomerIds())) {
                log.info("==========ES初始化多个会员详情customerIds:{}==============",esCustomerDetailInitRequest.getCustomerIds());
                List<CustomerDetailInitEsVO> customerDetailInitEsVOList = customerQueryProvider.listByPaidMembersPage(CustomerDetailInitEsRequest.builder().customerIds(esCustomerDetailInitRequest.getCustomerIds()).build()).getContext().getCustomerDetailInitEsVOList();
                if (CollectionUtils.isNotEmpty(customerDetailInitEsVOList)) {
                    List<EsCustomerDetail> esCustomerDetailList = esCustomerDetailMapper.customerDetailInitEsVOToEsCustomerDetail(customerDetailInitEsVOList);
                    this.saveAll(esCustomerDetailList);
                    log.info("==========ES初始化多个会员详情结束==============");
                }
            }  else {
                while (initCustomerDetail) {
                    request.putSort("createTime", SortType.DESC.toValue());
                    request.setPageNum(pageNum);
                    request.setPageSize(pageSize);
                    List<CustomerDetailInitEsVO>  customerDetailInitEsVOList = customerQueryProvider.listByPage(request).getContext().getCustomerDetailInitEsVOList();
                    if (CollectionUtils.isEmpty(customerDetailInitEsVOList)){
                        initCustomerDetail = Boolean.FALSE;
                        log.info("==========ES初始化会员详情结束，结束pageNum:{}==============",pageNum);
                    }else {
                        List<EsCustomerDetail> esCustomerDetailList = esCustomerDetailMapper.customerDetailInitEsVOToEsCustomerDetail(customerDetailInitEsVOList);
                        this.saveAll(esCustomerDetailList);
                        log.info("==========ES初始化会员详情成功，当前pageNum:{}==============",pageNum);
                        pageNum++;
                    }
                }
            }
        }catch (Exception e){
            log.info("==========ES初始化会员详情异常，异常pageNum:{}==============",pageNum);
            throw new SbcRuntimeException(CustomerDetailErrorCode.INIT_CUSTOMER_DETAIL_FAIL,new Object[]{pageNum});
        }

    }

    /**
     * 初始化ES数据
     */
    public void initPaidMembers(PaidCardCustomerRelPageRequest paidCardCustomerRelPageRequest){
        Boolean initCustomerDetail = Boolean.TRUE;
        int pageNum = paidCardCustomerRelPageRequest.getPageNum();
        Integer pageSize = Objects.equals(10,paidCardCustomerRelPageRequest.getPageSize().intValue()) ? 2000 : paidCardCustomerRelPageRequest.getPageSize();
        try {
            while (initCustomerDetail) {
                paidCardCustomerRelPageRequest.setPageNum(pageNum);
                paidCardCustomerRelPageRequest.setPageSize(pageSize);
                List<PaidCardCustomerRelVO> paidCardCustomerRelVOS = paidCardCustomerRelQueryProvider.page(paidCardCustomerRelPageRequest).getContext().getPaidCardCustomerRelVOPage().getContent();
                if (CollectionUtils.isEmpty(paidCardCustomerRelVOS)){
                    initCustomerDetail = Boolean.FALSE;
                    log.info("==========ES初始化付费会员详情结束，结束pageNum:{}==============",pageNum);
                }else {
                    List<String> customerIds=paidCardCustomerRelVOS.stream().map(PaidCardCustomerRelVO::getCustomerId).distinct().collect(Collectors.toList());
                    List<CustomerDetailInitEsVO> customerDetailInitEsVOList = customerQueryProvider.listByPaidMembersPage(CustomerDetailInitEsRequest.builder().customerIds(customerIds).build()).getContext().getCustomerDetailInitEsVOList();
                    List<EsCustomerDetail> esCustomerDetailList = esCustomerDetailMapper.customerDetailInitEsVOToEsCustomerDetail(customerDetailInitEsVOList);
                    this.saveAll(esCustomerDetailList);
                    log.info("==========ES初始化付费会员详情成功，当前pageNum:{}==============",pageNum);
                    pageNum++;
                }
            }
        }catch (Exception e){
            log.info("==========ES初始化付费会员详情异常，异常pageNum:{}==============",pageNum);
            throw new SbcRuntimeException(CustomerDetailErrorCode.INIT_CUSTOMER_DETAIL_FAIL,new Object[]{pageNum});
        }

    }


    /**
     * 支持删除多个ES中的会员数据
     */
    public void deleteCustomer(EsCustomerDetailInitRequest esCustomerDetailInitRequest){
        try {
            if (CollectionUtils.isNotEmpty(esCustomerDetailInitRequest.getCustomerIds())) {
                esCustomerDetailInitRequest.getCustomerIds().forEach(customerId->{
                    esCustomerDetailRepository.deleteById(customerId);
                });
            }
        }catch (Exception e){
            log.info("==========删除ES初始化会员异常，异常会员ID:{}==============",esCustomerDetailInitRequest.getCustomerIds());
            throw new SbcRuntimeException(CustomerDetailErrorCode.INIT_CUSTOMER_DETAIL_FAIL,new Object[]{esCustomerDetailInitRequest.getCustomerIds()});
        }

    }




    /**
     * 保存会员详情ES数据
     * @param customerDetailList
     * @return
     */
    public Iterable<EsCustomerDetail> saveAll(List<EsCustomerDetail> customerDetailList){
        this.createIndexAddMapping();
        return esCustomerDetailRepository.saveAll(customerDetailList);
    }

    /**
     * 保存会员详情ES数据
     * @param esCustomerDetailDTO
     * @return
     */
    public EsCustomerDetail save(EsCustomerDetailDTO esCustomerDetailDTO){
        this.createIndexAddMapping();
        EsCustomerDetail esCustomerDetail = esCustomerDetailMapper.customerDetailDTOToEsCustomerDetail(esCustomerDetailDTO);
        return esCustomerDetailRepository.save(esCustomerDetail);
    }

    /**
     * 保存会员详情ES数据
     * @param customerId
     * @return
     */
    public void delEsCustomerById(String  customerId){
        esCustomerDetailRepository.deleteById(customerId);
    }

    /**
     * boss端修改会员信息
     * @param esCustomerDetailDTO
     * @return
     */
    public EsCustomerDetail modify(EsCustomerDetailDTO esCustomerDetailDTO){
        this.createIndexAddMapping();
        String customerId = esCustomerDetailDTO.getCustomerId();
        EsCustomerDetail esCustomerDetail = esCustomerDetailRepository.findById(customerId).orElse(null);
        if (Objects.isNull(esCustomerDetail)){
            return null;
        }
        esCustomerDetail.setCustomerAccount(esCustomerDetailDTO.getCustomerAccount());
        esCustomerDetail.setCustomerName(esCustomerDetailDTO.getCustomerName());
        esCustomerDetail.setProvinceId(esCustomerDetailDTO.getProvinceId());
        esCustomerDetail.setCityId(esCustomerDetailDTO.getCityId());
        esCustomerDetail.setAreaId(esCustomerDetailDTO.getAreaId());
        esCustomerDetail.setStreetId(esCustomerDetailDTO.getStreetId());
        esCustomerDetail.setCustomerAddress(esCustomerDetailDTO.getCustomerAddress());
        esCustomerDetail.setContactName(esCustomerDetailDTO.getContactName());
        esCustomerDetail.setContactPhone(esCustomerDetailDTO.getContactPhone());
        esCustomerDetail.setCustomerLevelId(esCustomerDetailDTO.getCustomerLevelId());
        esCustomerDetail.setEmployeeId(esCustomerDetailDTO.getEmployeeId());
        EsEnterpriseInfo enterpriseInfo = esCustomerDetail.getEnterpriseInfo();
        EsEnterpriseInfoDTO enterpriseInfoDTO = esCustomerDetailDTO.getEnterpriseInfo();
        if (!Objects.equals(EnterpriseCheckState.INIT, esCustomerDetail.getEnterpriseCheckState()) && Objects.nonNull(enterpriseInfo) && Objects.nonNull(enterpriseInfoDTO)) {
            enterpriseInfo.setEnterpriseName(enterpriseInfoDTO.getEnterpriseName());
            enterpriseInfo.setBusinessNatureType(enterpriseInfoDTO.getBusinessNatureType());
        }
        return esCustomerDetailRepository.save(esCustomerDetail);
    }

    /**
     * 修改会员基础信息，同步ES
     * @param esCustomerDetailDTO
     * @return
     */
    public EsCustomerDetail modifyBaseInfo(EsCustomerDetailDTO esCustomerDetailDTO){
        this.createIndexAddMapping();
        String customerId = esCustomerDetailDTO.getCustomerId();
        EsCustomerDetail esCustomerDetail = esCustomerDetailRepository.findById(customerId).orElse(null);
        if (Objects.isNull(esCustomerDetail)){
            return null;
        }
        if(Objects.nonNull(esCustomerDetailDTO.getAreaId())){
            esCustomerDetail.setAreaId(esCustomerDetailDTO.getAreaId());
        }

        if(Objects.nonNull(esCustomerDetailDTO.getCityId())){
            esCustomerDetail.setCityId(esCustomerDetailDTO.getCityId());
        }

        if(Objects.nonNull(esCustomerDetailDTO.getContactName())){
            esCustomerDetail.setContactName(esCustomerDetailDTO.getContactName());
        }

        if(Objects.nonNull(esCustomerDetailDTO.getContactPhone())){
            esCustomerDetail.setContactPhone(esCustomerDetailDTO.getContactPhone());
        }

        if(Objects.nonNull(esCustomerDetailDTO.getCustomerAddress())){
            esCustomerDetail.setCustomerAddress(esCustomerDetailDTO.getCustomerAddress());
        }

        if(Objects.nonNull(esCustomerDetailDTO.getCustomerName())){
            esCustomerDetail.setCustomerName(esCustomerDetailDTO.getCustomerName());
        }

        if(Objects.nonNull(esCustomerDetailDTO.getProvinceId())){
            esCustomerDetail.setProvinceId(esCustomerDetailDTO.getProvinceId());
        }

        if(Objects.nonNull(esCustomerDetailDTO.getStreetId())){
            esCustomerDetail.setStreetId(esCustomerDetailDTO.getStreetId());
        }
        List<EsPaidCardDTO> esPaidCardListParam = esCustomerDetailDTO.getEsPaidCardList();
        if(CollectionUtils.isNotEmpty(esPaidCardListParam)){
            List<EsPaidCard> esPaidCardList = esCustomerDetail.getEsPaidCardList();
            if(CollectionUtils.isEmpty(esPaidCardList)){
                esPaidCardList = new ArrayList<>();
            }
            if("delete".equals(esCustomerDetailDTO.getOperType())){
                // 说明是删除操作
                EsPaidCardDTO esPaidCardDTODelete = esPaidCardListParam.get(0);
                esPaidCardList = esPaidCardList.stream().filter(x->!x.getPaidCardId().equals(esPaidCardDTODelete.getPaidCardId())).collect(Collectors.toList());
            }else{
                List<EsPaidCard> esPaidCards = KsBeanUtil.convertList(esCustomerDetailDTO.getEsPaidCardList(), EsPaidCard.class);
                esPaidCardList.addAll(esPaidCards);
            }

            esPaidCardList = esPaidCardList.stream().filter(IteratorUtils.distinctByKey(EsPaidCard::getPaidCardId)).collect(Collectors.toList());

            esCustomerDetail.setEsPaidCardList(CollectionUtils.isEmpty(esPaidCardList)? new ArrayList<>() :esPaidCardList);
        }

        return esCustomerDetailRepository.save(esCustomerDetail);
    }

    /**
     * 修改会员账号，同步ES
     * @param esCustomerDetailDTO
     * @return
     */
    public EsCustomerDetail modifyCustomerAccount(EsCustomerDetailDTO esCustomerDetailDTO){
        String customerId = esCustomerDetailDTO.getCustomerId();
        EsCustomerDetail esCustomerDetail = esCustomerDetailRepository.findById(customerId).orElse(null);
        if (Objects.isNull(esCustomerDetail)){
            return null;
        }
        esCustomerDetail.setCustomerAccount(esCustomerDetailDTO.getCustomerAccount());
        return esCustomerDetailRepository.save(esCustomerDetail);
    }

    /**
     * 修改会员是否分销员，同步ES
     * @param esCustomerDetailDTO
     * @return
     */
    public EsCustomerDetail updateCustomerToDistributor(EsCustomerDetailDTO esCustomerDetailDTO){
        String customerId = esCustomerDetailDTO.getCustomerId();
        EsCustomerDetail esCustomerDetail = esCustomerDetailRepository.findById(customerId).orElse(null);
        if (Objects.isNull(esCustomerDetail)){
            return null;
        }
        esCustomerDetail.setIsDistributor(esCustomerDetailDTO.getIsDistributor());
        return esCustomerDetailRepository.save(esCustomerDetail);
    }

    /**
     * 修改账号状态
     * @param request
     * @return
     */
    public Iterable<EsCustomerDetail> modifyCustomerStateByCustomerId(EsCustomerStateBatchModifyRequest request){
        List<String> customerIds = request.getCustomerIds();
        CustomerStatus customerStatus = request.getCustomerStatus();
        String forbidReason = request.getForbidReason();
        Iterable<EsCustomerDetail> esCustomerDetails =  esCustomerDetailRepository.findAllById(customerIds);
        esCustomerDetails.iterator().forEachRemaining(esCustomerDetail -> {
            esCustomerDetail.setCustomerStatus(customerStatus);
            esCustomerDetail.setForbidReason(forbidReason);
        });
        return esCustomerDetailRepository.saveAll(esCustomerDetails);
    }

    /**
     * 修改审核状态
     * @param request
     * @return
     */
    public EsCustomerDetail modifyCustomerCheckState(EsCustomerCheckStateModifyRequest request){
        String customerId = request.getCustomerId();
        Integer checkState = request.getCheckState();
        String rejectReason = request.getRejectReason();
        EsCustomerDetail esCustomerDetail = esCustomerDetailRepository.findById(customerId).orElse(null);
        if (Objects.isNull(esCustomerDetail)){
            return null;
        }
        esCustomerDetail.setRejectReason(rejectReason);
        esCustomerDetail.setCheckState(checkState.equals(CheckState.CHECKED.toValue()) ? CheckState.CHECKED : CheckState.NOT_PASS);
        return esCustomerDetailRepository.save(esCustomerDetail);
    }

    /**
     * 添加平台客户
     * @param request
     * @return
     */
    public EsCustomerDetail addPlatformRelated(EsStoreCustomerRelaAddRequest request){
        EsStoreCustomerRelaDTO storeCustomerRelaDTO = request.getStoreCustomerRelaDTO();
        String customerId = storeCustomerRelaDTO.getCustomerId();
        EsCustomerDetail esCustomerDetail = esCustomerDetailRepository.findById(customerId).orElse(null);
        if (Objects.isNull(esCustomerDetail)){
            return null;
        }
        List<EsStoreCustomerRela> esStoreCustomerRelaList = esCustomerDetail.getEsStoreCustomerRelaList();
        EsStoreCustomerRela storeCustomerRela = KsBeanUtil.convert(storeCustomerRelaDTO,EsStoreCustomerRela.class);
        esStoreCustomerRelaList.add(storeCustomerRela);
        esCustomerDetail.setEsStoreCustomerRelaList(esStoreCustomerRelaList);
        return esCustomerDetailRepository.save(esCustomerDetail);
    }

    /**
     * 删除平台客户和商家之间的关联
     * @param request
     * @return
     */
    public EsCustomerDetail deletePlatformRelated(EsStoreCustomerRelaDeleteRequest request){
        String customerId = request.getCustomerId();
        EsCustomerDetail esCustomerDetail = esCustomerDetailRepository.findById(customerId).orElse(null);
        if (Objects.isNull(esCustomerDetail)){
            return null;
        }
        List<EsStoreCustomerRela> esStoreCustomerRelaList = esCustomerDetail.getEsStoreCustomerRelaList();
        Long companyInfoId = request.getCompanyInfoId();
        esStoreCustomerRelaList = esStoreCustomerRelaList.stream().filter(e -> !e.getCompanyInfoId().equals(companyInfoId)).collect(Collectors.toList());
        esCustomerDetail.setEsStoreCustomerRelaList(esStoreCustomerRelaList);
        return esCustomerDetailRepository.save(esCustomerDetail);
    }

    /**
     * 修改平台客户，只能修改等级
     *
     * @param request
     * @return
     */
    public EsCustomerDetail modifyByCustomerId(EsStoreCustomerRelaUpdateRequest request) {
        String customerId = request.getCustomerId();
        EsCustomerDetail esCustomerDetail = esCustomerDetailRepository.findById(customerId).orElse(null);
        if (Objects.isNull(esCustomerDetail)){
            return null;
        }
        if (esCustomerDetail.getCheckState() == CheckState.CHECKED ) {
            List<EsStoreCustomerRela> esStoreCustomerRelaList = esCustomerDetail.getEsStoreCustomerRelaList();
            Long companyInfoId = request.getCompanyInfoId();
            esStoreCustomerRelaList = esStoreCustomerRelaList.stream().filter(e -> e.getCompanyInfoId().equals(companyInfoId)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(esStoreCustomerRelaList)){
                EsStoreCustomerRela esStoreCustomerRela = esStoreCustomerRelaList.get(0);
                esStoreCustomerRela.setStoreLevelId(request.getStoreLevelId());
                String employeeId = request.getEmployeeId();
                if (StringUtils.isNotBlank(employeeId)){
                    esCustomerDetail.setEmployeeId(employeeId);
                    esStoreCustomerRela.setEmployeeId(employeeId);
                }
            }
            return esCustomerDetailRepository.save(esCustomerDetail);
        }
        return null;
    }


    /**
     * 分页查询会员列表
     * @param request
     * @return
     */
    public Page<EsCustomerDetail> page(EsCustomerDetailPageRequest request){
        return esCustomerDetailRepository.search(request.getSearchQuery());
    }

    /**
     * 分页查询会员列表
     * @param request
     * @return
     */
    public Page<EsCustomerDetail> pageForEnterpriseCustomer(EsCustomerDetailPageTwoRequest request){
        return esCustomerDetailRepository.search(request.getSearchQuery());
    }

    /**
     * 包装会员信息-会员等级名称、成长值、业务员名称
     * @param esCustomerDetailVOList
     * @param companyInfoId
     */
    public void wrapperEsCustomerDetailVO(List<EsCustomerDetailVO> esCustomerDetailVOList,Long companyInfoId){
        List<CustomerDetailFromEsDTO> dtoList = esCustomerDetailVOList.stream().map(c -> {
            CustomerDetailFromEsDTO vo = new CustomerDetailFromEsDTO();
            vo.setCustomerId(c.getCustomerId());
            vo.setCustomerLevelId(c.getCustomerLevelId());
            vo.setEmployeeId(c.getEmployeeId());
            return vo;
        }).collect(Collectors.toList());
        List<CustomerDetailFromEsVO> customerDetailFromEsVOS = customerQueryProvider.listByCustomerIds(new CustomerDetailListByCustomerIdsRequest(dtoList,companyInfoId))
                .getContext().getCustomerDetailFromEsVOS();
        if (CollectionUtils.isEmpty(customerDetailFromEsVOS)){
            return;
        }
        Map<String, CustomerDetailFromEsVO> map = customerDetailFromEsVOS.stream().collect(Collectors.toMap(CustomerDetailFromEsVO::getCustomerId, Function.identity()));
        esCustomerDetailVOList.stream().forEach(c -> {
            CustomerDetailFromEsVO esVO =  map.get(c.getCustomerId());
            c.setCustomerLevelName(esVO.getCustomerLevelName());
            c.setGrowthValue(esVO.getGrowthValue());
            c.setEmployeeName(esVO.getEmployeeName());
            c.setMyCustomer(esVO.getMyCustomer());
            c.setCustomerType(esVO.getCustomerType());
        });
    }

    /**
     * 包装会员信息-会员等级名称、成长值、业务员名称
     *
     * @param esCustomerDetailList
     * @param request
     */
    public List<EsCustomerDetailVO> wrapperEsCustomerDetailVO(List<EsCustomerDetail> esCustomerDetailList, EsCustomerDetailPageTwoRequest request) {
        Long companyInfoId = request.getCompanyInfoId();
        Map<String, EsCustomerDetail> esCustomerDetailMap = esCustomerDetailList.stream().collect(Collectors.toMap(EsCustomerDetail::getCustomerId, Function.identity()));
        List<EsCustomerDetailVO> esCustomerDetailVOList = esCustomerDetailMapper.customerDetailToEsCustomerDetailVO(esCustomerDetailList);
        if (MapUtils.isNotEmpty(esCustomerDetailMap)) {
            esCustomerDetailVOList.forEach(esCustomerDetailVO -> {
                String customerId = esCustomerDetailVO.getCustomerId();
                EsCustomerDetail esCustomerDetail = esCustomerDetailMap.get(customerId);
                if (Objects.nonNull(esCustomerDetail) && Objects.nonNull(esCustomerDetail.getEnterpriseInfo())) {
                    EsEnterpriseInfo enterpriseInfo = esCustomerDetail.getEnterpriseInfo();
                    esCustomerDetailVO.setEnterpriseName(enterpriseInfo.getEnterpriseName());
                    esCustomerDetailVO.setBusinessNatureType(enterpriseInfo.getBusinessNatureType());
                }
            });
        }
        List<CustomerDetailFromEsDTO> dtoList = esCustomerDetailVOList.stream().map(c -> {
            CustomerDetailFromEsDTO vo = new CustomerDetailFromEsDTO();
            vo.setCustomerId(c.getCustomerId());
            vo.setCustomerLevelId(c.getCustomerLevelId());
            vo.setEmployeeId(c.getEmployeeId());
            return vo;
        }).collect(Collectors.toList());
        List<CustomerDetailFromEsVO> customerDetailFromEsVOS = customerQueryProvider.listByCustomerIds(new CustomerDetailListByCustomerIdsRequest(dtoList, companyInfoId))
                .getContext().getCustomerDetailFromEsVOS();
        if (CollectionUtils.isEmpty(customerDetailFromEsVOS)) {
            return esCustomerDetailVOList;
        }
        Map<String, CustomerDetailFromEsVO> map = customerDetailFromEsVOS.stream().collect(Collectors.toMap(CustomerDetailFromEsVO::getCustomerId, Function.identity()));
        esCustomerDetailVOList.forEach(c -> {
            CustomerDetailFromEsVO esVO = map.get(c.getCustomerId());
            c.setCustomerLevelId(esVO.getCustomerLevelId());
            c.setCustomerLevelName(esVO.getCustomerLevelName());
            c.setGrowthValue(esVO.getGrowthValue());
            c.setEmployeeName(esVO.getEmployeeName());
            c.setMyCustomer(esVO.getMyCustomer());
            c.setCustomerType(esVO.getCustomerType());
        });
        return esCustomerDetailVOList;
    }

    /**
     * 填充省市区
     * @param details
     */
    public void fillArea(List<EsCustomerDetailVO> details){
        if(CollectionUtils.isNotEmpty(details)){
            List<String> addrIds = new ArrayList<>();
            details.forEach(detail -> {
                addrIds.add(Objects.toString(detail.getProvinceId()));
                addrIds.add(Objects.toString(detail.getCityId()));
                addrIds.add(Objects.toString(detail.getAreaId()));
                addrIds.add(Objects.toString(detail.getStreetId()));
            });
            List<PlatformAddressVO> voList = platformAddressQueryProvider.list(PlatformAddressListRequest.builder().addrIdList(addrIds).build()).getContext().getPlatformAddressVOList();
            if(CollectionUtils.isNotEmpty(voList)){
                Map<String, String> addrMap = voList.stream().collect(Collectors.toMap(PlatformAddressVO::getAddrId, PlatformAddressVO::getAddrName));
                details.forEach(detail -> {
                    detail.setProvinceName(addrMap.get(Objects.toString(detail.getProvinceId())));
                    detail.setCityName(addrMap.get(Objects.toString(detail.getCityId())));
                    detail.setAreaName(addrMap.get(Objects.toString(detail.getAreaId())));
                    detail.setStreetName(addrMap.get(Objects.toString(detail.getStreetId())));
                });
            }
        }
    }

    public void resetEs(BaseQueryRequest req) {
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        bool.must(nestedQuery("esPaidCardList",existsQuery("esPaidCardList"), ScoreMode.None));
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(bool);
        builder.withPageable(PageRequest.of(req.getPageNum(), req.getPageSize()));
        Page<EsCustomerDetail> page = esCustomerDetailRepository.search(builder.build());
        List<EsCustomerDetail> content = page.getContent();
        log.info("查询到数据条数 {} ",content.size());
        log.info("总条数 {} ",page.getTotalElements());
        content = content.stream().filter(x->CollectionUtils.isNotEmpty(x.getEsPaidCardList())).collect(Collectors.toList());
        log.info("过滤之后的结果 {} ",content);
        if(CollectionUtils.isNotEmpty(content)){
            content.forEach(d-> d.setEsPaidCardList(null));
            log.info("设置之后 {} ",content);
            this.esCustomerDetailRepository.saveAll(content);
            log.info("执行结束 ");
        }
    }

    /**
     * 创建索引以及mapping
     */
    private void createIndexAddMapping() {
        if(!elasticsearchTemplate.indexExists(EsCustomerDetail.class)) {
            elasticsearchTemplate.createIndex(EsCustomerDetail.class);
            elasticsearchTemplate.putMapping(EsCustomerDetail.class);
        }
    }
}
