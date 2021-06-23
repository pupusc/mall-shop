package com.wanmi.sbc.account.company.service;

import com.wanmi.sbc.account.api.constant.AccountErrorCode;
import com.wanmi.sbc.account.api.constant.BaseBank;
import com.wanmi.sbc.account.api.constant.BaseBankConfiguration;
import com.wanmi.sbc.account.api.response.storeInformation.StoreAuditStateResponse;
import com.wanmi.sbc.account.company.model.root.CompanyAccount;
import com.wanmi.sbc.account.company.repository.CompanyAccountRepository;
import com.wanmi.sbc.account.company.request.CompanyAccountSaveRequest;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoProvider;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoAllModifyRequest;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreAuditStateModifyRequest;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoByIdResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * 线下账号服务
 * Created by sunkun on 2017/11/30.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class CompanyAccountService {

    /**
     * logger 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CompanyAccountService.class);

    @Resource
    private CompanyAccountRepository companyAccountRepository;

    @Resource
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Resource
    private CompanyInfoProvider companyInfoProvider;

    @Resource
    private StoreProvider storeProvider;

    /**
     * 新增线下账户
     *
     * @param saveRequest 新增线下账户信息
     * @return 线下账户
     */
    @Transactional
    public Optional<CompanyAccount> addOffLineAccount(CompanyAccountSaveRequest saveRequest) {
        CompanyAccount offlineAccount = new CompanyAccount();
        List<CompanyAccount> accounts =
                companyAccountRepository.getByCompanyInfoIdAndBankNoAndBankCodeAndDeleteFlag(saveRequest.getCompanyInfoId(), saveRequest.getBankNo(),
                        saveRequest.getBankCode(), DeleteFlag.NO.toValue());
        if (accounts.size() > 1) {
            throw new SbcRuntimeException(AccountErrorCode.BANK_ACCOUNT_EXIST, new Object[]{saveRequest.getBankNo()});
        }
        KsBeanUtil.copyPropertiesThird(saveRequest, offlineAccount);
        offlineAccount.setDeleteFlag(0);
        offlineAccount.setCreateTime(LocalDateTime.now());
        offlineAccount.setBankStatus(0);
        return Optional.ofNullable(companyAccountRepository.save(offlineAccount));
    }

    /**
     * 删除线下账户
     *
     * @param offlineAccountId 线下账户Id
     * @return 影响行数
     */
    @Transactional
    public int removeOfflineById(Long offlineAccountId) {
        return companyAccountRepository.removeOfflineAccountById(offlineAccountId, LocalDateTime.now());
    }

    public List<CompanyAccount> findOfflineAccounts(Long companyInfoId, DefaultFlag defaultFlag) {
        return companyAccountRepository.findOfflineAccounts(companyInfoId, defaultFlag.toValue());
    }

    /**
     * 修改线下账户
     *
     * @param saveRequest 修改参数
     * @return 修改账户Optional
     */
    @Transactional
    public Optional<CompanyAccount> modifyLineAccount(CompanyAccountSaveRequest saveRequest) {
        if (saveRequest.getAccountId() == null) {
            logger.debug("银行账号主键为空");
            throw new SbcRuntimeException(AccountErrorCode.MODIFY_ACCOUNT_FAILED);
        }
        //判断当前商家是否存在相同的银行账号
        List<CompanyAccount> accounts =
                companyAccountRepository.getByCompanyInfoIdAndBankNoAndBankCodeAndDeleteFlag(saveRequest.getCompanyInfoId(), saveRequest.getBankNo(),
                        saveRequest.getBankCode(), DeleteFlag.NO.toValue());
        if (accounts.stream().anyMatch(i -> !i.getAccountId().equals(saveRequest.getAccountId()))) {
            throw new SbcRuntimeException(AccountErrorCode.BANK_ACCOUNT_EXIST, new Object[]{saveRequest.getBankNo()});
        }
        CompanyAccount offlineAccount = companyAccountRepository.findById(saveRequest.getAccountId()).orElse(null);
        KsBeanUtil.copyProperties(saveRequest, offlineAccount);
        offlineAccount.setUpdate_time(LocalDateTime.now());
        return Optional.ofNullable(companyAccountRepository.save(offlineAccount));
    }

    /**
     * 统计商家财务信息
     *
     * @param companyInfoId 公司信息Id
     * @return 商家财务信息总数
     */
    public int countOffline(Long companyInfoId) {
        return companyAccountRepository.countByCompanyInfoIdAndDeleteFlag(companyInfoId, DefaultFlag.NO.toValue());
    }

    /**
     * 打款
     *
     * @param request 打款参数
     */
    @Transactional
    public void accountRemit(CompanyAccountSaveRequest request) {
        //参数错误
        if (isNull(request.getAccountId()) || isNull(request.getRemitPrice())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        CompanyAccount companyAccount = companyAccountRepository.findById(request.getAccountId()).orElse(null);
        //账号不存在
        if (isNull(companyAccount)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //已打过款，不能重复打款
        if (Objects.nonNull(companyAccount.getRemitPrice())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        companyAccount.setRemitPrice(request.getRemitPrice());
        companyAccountRepository.save(companyAccount);
    }

    /**
     * 更新商家财务信息(新增、修改、删除)
     *
     * @param offlineAccounts 批量商家账户更新信息
     * @param ids             删除的账户Id
     * @param companyInfoId   公司信息Id
     */
    @Transactional
    public void renewalOfflines(List<CompanyAccountSaveRequest> offlineAccounts, List<Long> ids, Long
            companyInfoId) {
        if (CollectionUtils.isNotEmpty(offlineAccounts)) {

            // 20200630 产品巩晓晓口头需求。银行、账户名、账号、支行信息一致的认为是重复数据。不允许添加
            List<CompanyAccount> companyAccounts = companyAccountRepository.findOfflineAccounts(companyInfoId,
                    DeleteFlag.NO.toValue());

            offlineAccounts.forEach(info -> {
                if (StringUtils.isBlank(info.getBankCode())) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
                List<BaseBank> baseBanks = BaseBankConfiguration.bankList.stream()
                        .filter(baseBank -> baseBank.getBankCode().equals(info.getBankCode())).collect(Collectors
                                .toList());
                if (CollectionUtils.isEmpty(baseBanks) || baseBanks.size() > 1) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
                info.setBankName(baseBanks.get(0).getBankName());
                if (isNull(info.getAccountId())) {
                    long i = companyAccounts.stream().filter(companyAccount -> {
                        boolean flag = false;
                        if (Objects.equals(companyAccount.getBankCode(), info.getBankCode())
                                && Objects.equals(companyAccount.getBankName(), info.getBankName())
                                && Objects.equals(companyAccount.getBankNo(), info.getBankNo())
                                && Objects.equals(companyAccount.getAccountName(), info.getAccountName())
                                && Objects.equals(companyAccount.getBankBranch(), info.getBankBranch())) {
                            flag = true;
                        }
                        return flag;
                    }).count();

                    if (i > 0){
                        throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "账户信息已存在");
                    }
                    //新增
                    if (countOffline(companyInfoId) >= 5) {
                        throw new SbcRuntimeException(AccountErrorCode.ACCOUNT_MAX_FAILED);
                    }
                    info.setCompanyInfoId(companyInfoId);
                    addOffLineAccount(info);
                } else {
                    //修改
                    info.setCompanyInfoId(companyInfoId);
                    modifyLineAccount(info);
                }
            });
        }
        //删除
        if (CollectionUtils.isNotEmpty(ids)) {
            ids.forEach(id -> {
                CompanyAccount offlineAccount = companyAccountRepository.findById(id).orElse(null);
                if (isNull(offlineAccount)) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
                if (!offlineAccount.getCompanyInfoId().equals(companyInfoId)) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
                removeOfflineById(id);
            });
        }
        //修改商家打款状态
        updateCompanyAffirm(companyInfoId);
        //修改商家店铺审核状态


        CompanyInfoByIdResponse companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder()
                        .companyInfoId(companyInfoId)
                        .build()).getContext();

        if (CollectionUtils.isNotEmpty(companyInfo.getStoreVOList())) {
            StoreVO store = companyInfo.getStoreVOList().get(0);

            StoreAuditStateModifyRequest request = new StoreAuditStateModifyRequest();
            request.setStoreId(store.getStoreId());
            if (store.getAuditState() == null) {

                request.setAuditState(CheckState.WAIT_CHECK);
                request.setApplyEnterTime(LocalDateTime.now());

                storeProvider.modifyAuditState(request);

            } else if (store.getAuditState() == CheckState.NOT_PASS) {
                request.setAuditState(CheckState.WAIT_CHECK);
                request.setAuditReason(null);

                storeProvider.modifyAuditState(request);
            }
        }
    }




    /**
     * 更新商家财务信息(新增、修改、删除) 返回店铺审核状态，重新刷入es
     *
     * @param offlineAccounts 批量商家账户更新信息
     * @param ids             删除的账户Id
     * @param companyInfoId   公司信息Id
     */
    @Transactional
    public StoreAuditStateResponse renewalOfflinesReturnState(List<CompanyAccountSaveRequest> offlineAccounts, List<Long> ids, Long
            companyInfoId) {
        StoreAuditStateResponse response = new StoreAuditStateResponse();
        if (CollectionUtils.isNotEmpty(offlineAccounts)) {

            // 20200630 产品巩晓晓口头需求。银行、账户名、账号、支行信息一致的认为是重复数据。不允许添加
            List<CompanyAccount> companyAccounts = companyAccountRepository.findOfflineAccounts(companyInfoId,
                    DeleteFlag.NO.toValue());

            offlineAccounts.forEach(info -> {
                if (StringUtils.isBlank(info.getBankCode())) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
                List<BaseBank> baseBanks = BaseBankConfiguration.bankList.stream()
                        .filter(baseBank -> baseBank.getBankCode().equals(info.getBankCode())).collect(Collectors
                                .toList());
                if (CollectionUtils.isEmpty(baseBanks) || baseBanks.size() > 1) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
                info.setBankName(baseBanks.get(0).getBankName());
                if (isNull(info.getAccountId())) {
                    long i = companyAccounts.stream().filter(companyAccount -> {
                        boolean flag = false;
                        if (Objects.equals(companyAccount.getBankCode(), info.getBankCode())
                                && Objects.equals(companyAccount.getBankName(), info.getBankName())
                                && Objects.equals(companyAccount.getBankNo(), info.getBankNo())
                                && Objects.equals(companyAccount.getAccountName(), info.getAccountName())
                                && Objects.equals(companyAccount.getBankBranch(), info.getBankBranch())) {
                            flag = true;
                        }
                        return flag;
                    }).count();

                    if (i > 0){
                        throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "账户信息已存在");
                    }
                    //新增
                    if (countOffline(companyInfoId) >= 5) {
                        throw new SbcRuntimeException(AccountErrorCode.ACCOUNT_MAX_FAILED);
                    }
                    info.setCompanyInfoId(companyInfoId);
                    addOffLineAccount(info);
                } else {
                    //修改
                    info.setCompanyInfoId(companyInfoId);
                    modifyLineAccount(info);
                }
            });
        }
        //删除
        if (CollectionUtils.isNotEmpty(ids)) {
            ids.forEach(id -> {
                CompanyAccount offlineAccount = companyAccountRepository.findById(id).orElse(null);
                if (isNull(offlineAccount)) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
                if (!offlineAccount.getCompanyInfoId().equals(companyInfoId)) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
                removeOfflineById(id);
            });
        }
        //修改商家打款状态
        BoolFlag flag = updateReturnCompanyAffirm(companyInfoId);
        if(Objects.nonNull(flag)){
            response.setRemitAffirm(flag.toValue());
        }

        //修改商家店铺审核状态


        CompanyInfoByIdResponse companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder()
                        .companyInfoId(companyInfoId)
                        .build()).getContext();

        if (CollectionUtils.isNotEmpty(companyInfo.getStoreVOList())) {
            StoreVO store = companyInfo.getStoreVOList().get(0);

            StoreAuditStateModifyRequest request = new StoreAuditStateModifyRequest();
            request.setStoreId(store.getStoreId());
            if (store.getAuditState() == null) {

                request.setAuditState(CheckState.WAIT_CHECK);
                request.setApplyEnterTime(LocalDateTime.now());

                storeProvider.modifyAuditState(request);
                response.setAuditState(com.wanmi.sbc.account.bean.enums.CheckState.CHECK);
            } else if (store.getAuditState() == CheckState.NOT_PASS) {
                request.setAuditState(CheckState.WAIT_CHECK);
                request.setAuditReason(null);
                storeProvider.modifyAuditState(request);
                response.setAuditState(com.wanmi.sbc.account.bean.enums.CheckState.CHECK);
            }
        }
        return response;
    }




    /**
     * 设为主账号
     *
     * @param companyInfoId 公司信息Id
     * @param accountId     账户Id
     */
    @Transactional
    public void setPrimary(Long companyInfoId, Long accountId) {
        List<CompanyAccount> companyAccountList = companyAccountRepository.findOfflineAccounts(companyInfoId,
                DefaultFlag.NO.toValue());
        List<CompanyAccount> accounts = companyAccountList.stream()
                .filter(companyAccount -> companyAccount.getAccountId().longValue() == accountId.longValue())
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(accounts)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        companyAccountList.forEach(companyAccount -> {
            if (companyAccount.getAccountId().longValue() == accountId.longValue()) {
                companyAccount.setIsDefaultAccount(DefaultFlag.YES);
            } else {
                companyAccount.setIsDefaultAccount(DefaultFlag.NO);
            }
            companyAccountRepository.save(companyAccount);
        });
    }

    /**
     * 确认收到打款
     *
     * @param companyInfoId 公司信息Id
     * @param accountId     账户Id
     */
    @Transactional
    @GlobalTransactional
    public void affirmRemit(Long companyInfoId, Long accountId) {
        CompanyAccount companyAccount = companyAccountRepository.findById(accountId).orElse(null);
        if (isNull(companyAccount) || !Objects.equals(companyAccount.getCompanyInfoId(), companyInfoId) ||
                isNull(companyAccount.getRemitPrice())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        companyAccount.setIsReceived(DefaultFlag.YES);
        companyAccountRepository.save(companyAccount);
        updateCompanyAffirm(companyInfoId);

    }

    /**
     * 确认收到打款 返回打款确认状态
     *
     * @param companyInfoId 公司信息Id
     * @param accountId     账户Id
     */
    @Transactional
    @GlobalTransactional
    public BoolFlag affirmRemitAndReturn(Long companyInfoId, Long accountId) {
        CompanyAccount companyAccount = companyAccountRepository.findById(accountId).orElse(null);
        if (isNull(companyAccount) || !Objects.equals(companyAccount.getCompanyInfoId(), companyInfoId) ||
                isNull(companyAccount.getRemitPrice())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        companyAccount.setIsReceived(DefaultFlag.YES);
        companyAccountRepository.save(companyAccount);
        return updateReturnCompanyAffirm(companyInfoId);

    }

    /**
     * 更改商家打款状态
     *
     * @param companyInfoId 公司信息Id
     */
    private void updateCompanyAffirm(Long companyInfoId) {
        CompanyInfoByIdResponse companyInfo = companyInfoQueryProvider
                .getCompanyInfoById(CompanyInfoByIdRequest.builder().companyInfoId(companyInfoId).build()).getContext();

        //商家不存在
        if (isNull(companyInfo)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //根据商家主键获取商家有效收款账号
        List<CompanyAccount> list = companyAccountRepository.findOfflineAccounts(companyInfoId, DefaultFlag.NO
                .toValue());
        if (CollectionUtils.isEmpty(list)) {
            //商家不存在有效收款账号修改商家打款确认状态
            if (companyInfo.getRemitAffirm() == BoolFlag.YES) {
                companyInfo.setRemitAffirm(BoolFlag.NO);
                this.modifyCompany(companyInfo);
            }
            return;
        }
        //筛选出未打款账户
        List<CompanyAccount> noList = list.stream().filter(companyAccount -> companyAccount.getIsReceived() ==
                DefaultFlag.NO).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(noList)) {
            //存在未打款账户修改商家打款确认状态为false
            if (companyInfo.getRemitAffirm() == BoolFlag.NO) {
                companyInfo.setRemitAffirm(BoolFlag.YES);
                this.modifyCompany(companyInfo);
            }
        } else {
            //不存在未打款账户修改商家打款确认状态为true
            if (companyInfo.getRemitAffirm() == BoolFlag.YES) {
                companyInfo.setRemitAffirm(BoolFlag.NO);
                this.modifyCompany(companyInfo);
            }
        }
    }



    /**
     * 更改商家打款状态 返回打款状态，刷新es
     *
     * @param companyInfoId 公司信息Id
     */
    private BoolFlag updateReturnCompanyAffirm(Long companyInfoId) {
        CompanyInfoByIdResponse companyInfo = companyInfoQueryProvider
                .getCompanyInfoById(CompanyInfoByIdRequest.builder().companyInfoId(companyInfoId).build()).getContext();

        //商家不存在
        if (isNull(companyInfo)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //根据商家主键获取商家有效收款账号
        List<CompanyAccount> list = companyAccountRepository.findOfflineAccounts(companyInfoId, DefaultFlag.NO
                .toValue());
        if (CollectionUtils.isEmpty(list)) {
            //商家不存在有效收款账号修改商家打款确认状态
            if (companyInfo.getRemitAffirm() == BoolFlag.YES) {
                companyInfo.setRemitAffirm(BoolFlag.NO);
                this.modifyCompany(companyInfo);
                return BoolFlag.NO;
            }
            return null;
        }
        //筛选出未打款账户
        List<CompanyAccount> noList = list.stream().filter(companyAccount -> companyAccount.getIsReceived() ==
                DefaultFlag.NO).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(noList)) {
            //存在未打款账户修改商家打款确认状态为false
            if (companyInfo.getRemitAffirm() == BoolFlag.NO) {
                companyInfo.setRemitAffirm(BoolFlag.YES);
                this.modifyCompany(companyInfo);
                return BoolFlag.YES;
            }
        } else {
            //不存在未打款账户修改商家打款确认状态为true
            if (companyInfo.getRemitAffirm() == BoolFlag.YES) {
                companyInfo.setRemitAffirm(BoolFlag.NO);
                this.modifyCompany(companyInfo);
                return BoolFlag.NO;
            }
        }
        return null;
    }
    /**
     * 更新商户信息
     *
     * @param companyInfoVO 商户信息
     */
    private void modifyCompany(CompanyInfoVO companyInfoVO) {
        CompanyInfoAllModifyRequest modifyRequest = new CompanyInfoAllModifyRequest();
        KsBeanUtil.copyPropertiesThird(companyInfoVO, modifyRequest);
        companyInfoProvider.modifyAllCompanyInfo(modifyRequest);
    }

    /**
     * 根据accountId获取账号详情信息
     *
     * @param accountId
     * @return
     */
    public Optional<CompanyAccount> findByAccountId(Long accountId) {
        return companyAccountRepository.findById(accountId);
    }
}
