package com.wanmi.sbc.account.offline;

import com.wanmi.sbc.account.api.constant.AccountErrorCode;
import com.wanmi.sbc.account.api.request.offline.OfflineAccountAddRequest;
import com.wanmi.sbc.account.api.request.offline.OfflineAccountModifyRequest;
import com.wanmi.sbc.account.bean.dto.OfflineAccountSaveDTO;
import com.wanmi.sbc.account.bean.enums.AccountStatus;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 线下账号服务
 * Created by zhangjin on 2017/3/19.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class OfflineService {

    /**
     * logger 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(OfflineService.class);

    /**
     * 线下账户数据源
     */
    @Autowired
    private OfflineRepository offlineRepository;

    /**
     * 新增线下账户
     *
     * @param saveRequest
     * @return OfflineAccount
     */
    @Transactional
//    public Optional<OfflineAccount> addOffLineAccount(OfflineAccountSaveRequest saveRequest) {
    public Optional<OfflineAccount> addOffLineAccount(OfflineAccountAddRequest saveRequest) {
        OfflineAccount offlineAccount = new OfflineAccount();
        if (offlineRepository.countByBankNoAndDeleteFlag(saveRequest.getBankNo(), DeleteFlag.NO.toValue()) > 0) {
            throw new SbcRuntimeException(AccountErrorCode.BANK_ACCOUNT_EXIST, new Object[]{saveRequest.getBankNo()});
        }
        if (offlineRepository.countByDeleteFlag(DeleteFlag.NO.toValue()) >= 50) {
            throw new SbcRuntimeException(AccountErrorCode.ACCOUNT_MAX_LIMIT);
        }

        BeanUtils.copyProperties(saveRequest, offlineAccount);
        offlineAccount.setDeleteFlag(0);
        offlineAccount.setCreateTime(LocalDateTime.now());
        offlineAccount.setBankStatus(0);
        return Optional.ofNullable(offlineRepository.save(offlineAccount));
    }


    /**
     * 修改线下账户
     *
     * @param saveRequest
     * @return 修改账户
     */
    @Transactional
//    public Optional<OfflineAccount> modifyLineAccount(OfflineAccountSaveRequest saveRequest) {
    public Optional<OfflineAccount> modifyLineAccount(OfflineAccountModifyRequest saveRequest) {
        if (saveRequest.getAccountId() == null) {
            logger.debug("银行账号主键为空");
            throw new SbcRuntimeException(AccountErrorCode.MODIFY_ACCOUNT_FAILED);
        }

        OfflineAccount offlineAccount = offlineRepository.findById(saveRequest.getAccountId()).orElse(new OfflineAccount());
        if (offlineRepository.countByBankNoAndDeleteFlag(saveRequest.getBankNo(), DeleteFlag.NO.toValue()) > 0 &&
                !offlineAccount.getBankNo().equals(saveRequest.getBankNo())
                ) {
            throw new SbcRuntimeException(AccountErrorCode.BANK_ACCOUNT_EXIST, new Object[]{saveRequest.getBankNo()});
        }

        KsBeanUtil.copyProperties(saveRequest, offlineAccount);
        offlineAccount.setUpdate_time(LocalDateTime.now());
        return Optional.ofNullable(offlineRepository.save(offlineAccount));
    }

    /**
     * 查询线上账户
     *
     * @param offlineAccountId offlineAccountId
     * @return OfflineAccount
     */
    public Optional<OfflineAccount> findOfflineAccountById(Long offlineAccountId) {
        return offlineRepository.findById(offlineAccountId);
    }


    /**
     * 删除线下账户
     *
     * @param offlineAccountId offlineAccountId
     * @return rows
     */
    @Transactional
    public int removeOfflineById(Long offlineAccountId) {
        return offlineRepository.removeOfflineAccountById(offlineAccountId, LocalDateTime.now());
    }

    /**
     * 账号管理查询
     *
     * @return
     */
    public List<OfflineAccount> findManagerAccounts() {
        return offlineRepository.findManagerAccounts();
    }

    public List<OfflineAccount> findOfflineAccounts(Long companyInfoId, DefaultFlag defaultFlag) {
        return offlineRepository.findOfflineAccounts(companyInfoId, defaultFlag.toValue());
    }

    /**
     * 查询所有线下账户
     *
     * @return 线下账户列表
     */
    public List<OfflineAccount> findAll() {
        return offlineRepository.findAll();
    }

    /**
     * 查询所有有效线下账户
     *
     * @return 线下账户列表
     */
    public List<OfflineAccount> findValidAccounts() {
        return offlineRepository.findValidAccounts();
    }

    /**
     * 禁用银行账号
     *
     * @param offlineAccountId offlineAccountId
     * @return rows
     */
    @Transactional
    public int disableOfflineById(Long offlineAccountId) {
        return offlineRepository.modifyAccountStatus(offlineAccountId, AccountStatus.DISABLE.toValue());
    }

    /**
     * 启用银行账号
     *
     * @param offlineAccountId offlineAccountId
     * @return rows
     */
    @Transactional
    public int enableOfflineById(Long offlineAccountId) {
        return offlineRepository.modifyAccountStatus(offlineAccountId, AccountStatus.ENABLE.toValue());
    }

    /**
     * 统计供应商财务信息
     *
     * @param companyInfoId
     * @return
     */
    public int countOffline(Long companyInfoId) {
        return offlineRepository.countByCompanyInfoIdAndDeleteFlag(companyInfoId, DefaultFlag.NO.toValue());
    }

    /**
     * 更新供应商财务信息(新增、修改、删除)
     *
     * @param offlineAccounts
     * @param ids
     * @param companyInfoId
     */
    @Transactional
    public void renewalOfflines(List<OfflineAccountSaveDTO> offlineAccounts, List<Long> ids, Long companyInfoId) {
        if (CollectionUtils.isNotEmpty(offlineAccounts)) {
            offlineAccounts.forEach(info -> {
                if (Objects.isNull(info.getAccountId())) {
                    //新增
                    if (countOffline(companyInfoId) >= 5) {
                        throw new SbcRuntimeException(AccountErrorCode.ACCOUNT_MAX_FAILED);
                    }
                    info.setCompanyInfoId(companyInfoId);

                    OfflineAccountAddRequest request = new OfflineAccountAddRequest();

                    KsBeanUtil.copyPropertiesThird(info, request);

                    addOffLineAccount(request);
                } else {
                    OfflineAccountModifyRequest request = new OfflineAccountModifyRequest();

                    KsBeanUtil.copyPropertiesThird(info, request);

                    //修改
                    modifyLineAccount(request);
                }
            });
        }
        //删除
        if (CollectionUtils.isNotEmpty(ids)) {
            ids.forEach(id -> {
                OfflineAccount offlineAccount = offlineRepository.findById(id).orElse(new OfflineAccount());
                if (offlineAccount.getCompanyInfoId() != companyInfoId) {
                    throw new SbcRuntimeException(CommonErrorCode.FAILED);
                }
                removeOfflineById(id);
            });
        }
    }

    public List<OfflineAccount> listOfflineAccount(String bankNo) {
        return offlineRepository.findOfflineAccounts(bankNo);
    }

    public List<OfflineAccount> listOfflineAccountWithOutDeleteFlag(String bankNo) {
        return offlineRepository.findOfflineAccountsWithOutDeleteFlag(bankNo);
    }
}
