package com.wanmi.sbc.customer.livecompany.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.customer.api.request.company.CompanyPageRequest;
import com.wanmi.sbc.customer.api.response.company.CompanyReponse;
import com.wanmi.sbc.customer.api.response.livecompany.LiveCompanyPagePackResponse;
import com.wanmi.sbc.customer.api.response.livecompany.LiveCompanyPageResponse;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.EmployeeVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.customer.company.model.root.CompanyInfo;
import com.wanmi.sbc.customer.company.request.CompanyRequest;
import com.wanmi.sbc.customer.company.service.CompanyInfoService;
import com.wanmi.sbc.customer.employee.model.root.Employee;
import com.wanmi.sbc.customer.store.model.root.Store;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.customer.livecompany.repository.LiveCompanyRepository;
import com.wanmi.sbc.customer.livecompany.model.root.LiveCompany;
import com.wanmi.sbc.customer.api.request.livecompany.LiveCompanyQueryRequest;
import com.wanmi.sbc.customer.bean.vo.LiveCompanyVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.enums.DeleteFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * <p>直播商家业务逻辑</p>
 * @author zwb
 * @date 2020-06-06 18:06:59
 */
@Service("LiveCompanyService")
public class LiveCompanyService {
	@Autowired
	private LiveCompanyRepository liveCompanyRepository;

	@Autowired
	private CompanyInfoService companyInfoService;

	/**
	 * 新增直播商家
	 * @author zwb
	 */
	@Transactional
	public LiveCompany add(LiveCompany entity) {
		Optional<LiveCompany> liveCompany = liveCompanyRepository.findByStoreIdAndDelFlag(entity.getStoreId(), DeleteFlag.NO);
		//第一次审核
		if (!liveCompany.isPresent()){
			entity.setLiveBroadcastStatus(1);
			liveCompanyRepository.save(entity);
		}else {
			//再次审核
			entity.setLiveBroadcastStatus(1);
			liveCompanyRepository.updateLiveBroadcastStatusByStoreId(entity.getLiveBroadcastStatus(),"",entity.getStoreId());
		}
		return entity;
	}

	/**
	 * 直播商家开通审核
	 * @author zwb
	 */
	@Transactional
	public LiveCompany modify(LiveCompany entity) {

		String auditReason = entity.getAuditReason();
		if (auditReason==null){
			auditReason="";
		}
		liveCompanyRepository.updateLiveBroadcastStatusByStoreId(entity.getLiveBroadcastStatus(),auditReason,entity.getStoreId());

		return entity;
	}

	/**
	 * 单个删除直播商家
	 * @author zwb
	 */
	@Transactional
	public void deleteById(LiveCompany entity) {
		liveCompanyRepository.save(entity);
	}

	/**
	 * 批量删除直播商家
	 * @author zwb
	 */
	@Transactional
	public void deleteByIdList(List<LiveCompany> infos) {
		liveCompanyRepository.saveAll(infos);
	}

	/**
	 * 单个查询直播商家
	 * @author zwb
	 */
	public LiveCompany getOne(Long storeId){
		LiveCompany liveCompany = liveCompanyRepository.findByStoreIdAndDelFlag(  storeId,DeleteFlag.NO)
				.orElse(new LiveCompany());
		if (liveCompany.getLiveBroadcastStatus()==null){
			liveCompany.setLiveBroadcastStatus(0);
		}
		return liveCompany;
	}

	/**
	 * 分页查询直播商家
	 * @author zwb
	 */
	public Page<LiveCompany> page(LiveCompanyQueryRequest queryReq){
		return liveCompanyRepository.findAll(
				LiveCompanyWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询直播商家
	 * @author zwb
	 */
	public List<LiveCompany> list(LiveCompanyQueryRequest queryReq){
		return liveCompanyRepository.findAll(LiveCompanyWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author zwb
	 */
	public LiveCompanyVO wrapperVo(LiveCompany liveCompany) {
		if (liveCompany != null){
			LiveCompanyVO liveCompanyVO = KsBeanUtil.convert(liveCompany, LiveCompanyVO.class);
			return liveCompanyVO;
		}
		return null;
	}

	public LiveCompanyPagePackResponse pageNew(LiveCompanyQueryRequest pageReq){
		Page<LiveCompany> livePage = this.page(pageReq);
		if (CollectionUtils.isEmpty(livePage.getContent())){
			return new LiveCompanyPagePackResponse();
		}
		//根据直播商家companyInfoId 去查询商家详细信息
		Map<Long, String> collect = livePage.getContent().stream().collect(Collectors.toMap(LiveCompany::getCompanyInfoId, liveCompanyVO -> {
			String auditReason = liveCompanyVO.getAuditReason();
			if (auditReason == null) {
				auditReason = "";
			}
			return auditReason;
		}));
		if(CollectionUtils.isNotEmpty(collect.keySet())) {
			CompanyRequest request = new CompanyRequest();
			request.setCompanyInfoIds(new ArrayList<>(collect.keySet()));
			request.setDeleteFlag(DeleteFlag.NO);
			request.setAccountName(pageReq.getAccountName());
			request.setStoreName(pageReq.getStoreName());
			request.putSort("createTime", SortType.DESC.toValue());
			Page<CompanyInfo> page = companyInfoService.findAll(request);

			List<CompanyReponse> companyReponseList = new ArrayList<>();
			page.getContent().forEach(info -> {
				//组装返回结构
				CompanyReponse companyReponse = new CompanyReponse();
				companyReponse.setCompanyInfoId(info.getCompanyInfoId());
				companyReponse.setCompanyCode(info.getCompanyCode());
				companyReponse.setCompanyType(info.getCompanyType());
				companyReponse.setSupplierName(info.getSupplierName());
				companyReponse.setAuditReason(collect.get(info.getCompanyInfoId()));
				if (CollectionUtils.isNotEmpty(info.getEmployeeList())) {
					Employee employee = info.getEmployeeList().get(0);
					companyReponse.setAccountName(employee.getAccountName());
					companyReponse.setAccountState(employee.getAccountState());
					companyReponse.setAccountDisableReason(employee.getAccountDisableReason());
				}
				if (CollectionUtils.isNotEmpty(info.getStoreList())) {
					Store store = info.getStoreList().get(0);
					companyReponse.setStoreId(store.getStoreId());
					companyReponse.setStoreName(store.getStoreName());
					companyReponse.setContractStartDate(store.getContractStartDate());
					companyReponse.setContractEndDate(store.getContractEndDate());
					companyReponse.setAuditState(store.getAuditState());
					// companyReponse.setAuditReason(store.getAuditReason());
					companyReponse.setStoreState(store.getStoreState());
					companyReponse.setStoreClosedReason(store.getStoreClosedReason());
					companyReponse.setApplyEnterTime(store.getApplyEnterTime());
					companyReponse.setStoreType(store.getStoreType());
				}
				companyReponseList.add(companyReponse);
			});
			PageImpl<CompanyReponse> newPage = new PageImpl<>(companyReponseList, request.getPageable(), page.getTotalElements());
			MicroServicePage<CompanyReponse> microPage = new MicroServicePage<>(newPage, pageReq.getPageable());
			return new LiveCompanyPagePackResponse(microPage);
		}
		return new LiveCompanyPagePackResponse();
	}
}

