package com.wanmi.sbc.account.invoice;

import com.wanmi.sbc.account.api.constant.AccountErrorCode;
import com.wanmi.sbc.account.invoice.request.InvoiceProjectSaveRequest;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by yuanlinling on 2017/4/25.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class InvoiceProjectService {

    @Autowired
    private InvoiceProjectRepository invoiceProjectRepository;

    @Autowired
    private InvoiceProjectSwitchRepository invoiceProjectSwitchRepository;

    /**
     * 根据开票项目名称查询该项目是否存在
     *
     * @param projectName
     * @return
     */
    public Boolean invoiceProjectIsExist(String projectName, Long companyInfoId){
        return invoiceProjectRepository.countByProjectNameAndDelFlagAndCompanyInfoId(projectName, DeleteFlag.NO, companyInfoId) > 0;
    }

    /**
     * 根据id查询开票项目
     *
     * @param projcetId
     * @return
     */
    public Optional<InvoiceProject> findInvoiceProjectById(String projcetId){
        return invoiceProjectRepository.findByProjectIdAndDelFlag(projcetId,DeleteFlag.NO);
    }

    /**
     * 保存新增开票项目
     *
     * @param invoiceProjectSaveRequest
     * @return
     */
    @Transactional
    public InvoiceProject saveInvoiceProject(InvoiceProjectSaveRequest invoiceProjectSaveRequest){
        String projectName = invoiceProjectSaveRequest.getProjectName().trim();
        //项目不能重复添加
        if(invoiceProjectIsExist(projectName, invoiceProjectSaveRequest.getCompanyInfoId())){
            throw new SbcRuntimeException(AccountErrorCode.INVOICE_PROJECT_EXIST);
        }
        InvoiceProject invoiceProject = new InvoiceProject();
        BeanUtils.copyProperties(invoiceProjectSaveRequest,invoiceProject);
        invoiceProject.setProjectName(projectName);
        invoiceProject.setDelFlag(DeleteFlag.NO);
        invoiceProject.setCreateTime(LocalDateTime.now());
        //保存开票项目
        return invoiceProjectRepository.save(invoiceProject);
    }

    /**
     * 编辑开票项目
     *
     * @param invoiceProjectSaveRequest
     * @return
     */
    @Transactional
    public InvoiceProject editInvoiceProject(InvoiceProjectSaveRequest invoiceProjectSaveRequest){
        String projectName = invoiceProjectSaveRequest.getProjectName().trim();
        //编辑的项目不能和已有的重名
        InvoiceProject project = invoiceProjectRepository.findByProjectNameAndDelFlagAndCompanyInfoId(projectName, DeleteFlag.NO, invoiceProjectSaveRequest.getCompanyInfoId());
        if(Objects.nonNull(project)){
            if (!project.getProjectId().equals(invoiceProjectSaveRequest.getProjectId())){
                throw new SbcRuntimeException(AccountErrorCode.INVOICE_PROJECT_EXIST);
            }
        }
        Optional<InvoiceProject> invoiceProject = findInvoiceProjectById(invoiceProjectSaveRequest.getProjectId());
        InvoiceProject invProject =invoiceProject.get();
        BeanUtils.copyProperties(invoiceProjectSaveRequest,invProject);
        invProject.setUpdateTime(LocalDateTime.now());
        invProject.setProjectName(projectName);
        return invoiceProjectRepository.save(invProject);
    }

    /**
     * 删除开票项目
     *
     * @param projectId projectId
     */
    @Transactional
    public void deleteInvoiceProject(String projectId) {
        if (StringUtils.isEmpty(projectId)) {
            return;
        }
        invoiceProjectRepository.deleteInvoiceProjectByIds(projectId);
    }

    /**
     * 分页查询
     *
     * @param baseQueryRequest
     * @return
     */
    public Page<InvoiceProject> findByPage(BaseQueryRequest baseQueryRequest, Long companyInfoId) {
        return invoiceProjectRepository.findByDelFlagAndCompanyInfoIdOrCompanyInfoIdIsNullOrderByCreateTimeDesc(DeleteFlag.NO, companyInfoId,  PageRequest.of(baseQueryRequest.getPageNum(), baseQueryRequest.getPageSize()));
    }

    /**
     * 查询所有开票项
     * @return List<InvoiceResponse>
     */
    public List<InvoiceResponse> findAllInoviceProject(Long companyInfoId) {
       List<InvoiceProject> invoiceProjects =  invoiceProjectRepository.
               findByDelFlagAndCompanyInfoIdOrCompanyInfoIdIsNull(DeleteFlag.NO, companyInfoId);
        if (CollectionUtils.isEmpty(invoiceProjects)) {
            return Collections.emptyList();
        }

       return invoiceProjects.parallelStream().map(invoiceProject -> {
           InvoiceResponse invoiceResponse = new InvoiceResponse();
           invoiceResponse.setProjectId(invoiceProject.getProjectId());
           invoiceResponse.setProjectName(invoiceProject.getProjectName());
           invoiceResponse.setProjectUpdateTime(invoiceProject.getUpdateTime());
           return invoiceResponse;
       }).collect(Collectors.toList());
    }
}
