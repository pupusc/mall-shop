package com.wanmi.sbc.account;

import com.wanmi.sbc.account.api.constant.BaseBank;
import com.wanmi.sbc.account.api.constant.BaseBankConfiguration;
import com.wanmi.sbc.account.api.provider.offline.OfflineProvider;
import com.wanmi.sbc.account.api.provider.offline.OfflineQueryProvider;
import com.wanmi.sbc.account.api.request.offline.*;
import com.wanmi.sbc.account.api.response.offline.OfflineAccountGetByIdResponse;
import com.wanmi.sbc.account.bean.vo.OfflineAccountVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 收款账户 线下支付
 * Created by CHENLI on 2017/4/27.
 */
@RestController
@RequestMapping("/account")
@Api(tags = "BaseOfflineAccountController", description = "S2B 公用-商家结算银行账户API")
public class BaseOfflineAccountController {
    @Autowired
    private OfflineQueryProvider offlineQueryProvider;

    @Autowired
    private OfflineProvider offlineProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 查询所有有效的线下账户
     * test
     *
     * @return
     */
    @ApiOperation(value = "查询所有有效的线下结算银行账户")
    @RequestMapping(value = "/offlineAccounts", method = RequestMethod.GET)
    public ResponseEntity<List<OfflineAccountVO>> findAllOfflineAccounts(){
        return ResponseEntity.ok(offlineQueryProvider.listValidAccount().getContext().getVoList());
    }

    /**
     * 查询所有的线下账户
     *
     * @return
     */
    @ApiOperation(value = "查询所有的（包括删除的）线下结算银行账户")
    @RequestMapping(value = "/allOfflineAccounts", method = RequestMethod.GET)
    public ResponseEntity<List<OfflineAccountVO>> findAllOfflineAccountsWithDelete(){
        return ResponseEntity.ok(offlineQueryProvider.list().getContext().getVoList());
    }

    /**
     * 查询所有有效的线下账户
     *
     * @return
     */
    @ApiOperation(value = "查询所有有效的线下结算银行账户")
    @RequestMapping(value = "/offlineValidAccounts", method = RequestMethod.GET)
    public BaseResponse<List<OfflineAccountVO>> findValidOfflineAccounts(){
        return BaseResponse.success(offlineQueryProvider.listValidAccount().getContext().getVoList());
    }

    @ApiOperation(value = "查询所有的（未被删除的）线下结算银行账户")
    @RequestMapping(value = "/managerOfflineAccounts", method = RequestMethod.GET)
    public ResponseEntity<List<OfflineAccountVO>> findManagerAccounts(){
        return ResponseEntity.ok(offlineQueryProvider.listManagerAccount().getContext().getVoList());
    }

    /**
     * 新增线下账户
     *
     * @param saveRequest
     * @return
     */
    @ApiOperation(value = "新增线下结算银行账户")
    @RequestMapping(value = "/offlineAccount", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> addOfflineAccount(@RequestBody OfflineAccountAddRequest saveRequest){
        offlineProvider.add(saveRequest);
        //操作日志记录
        operateLogMQUtil.convertAndSend("财务", "新增线下支付账号", "新增线下支付账号：" + saveRequest.getAccountName());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 修改线下账户
     *
     * @param saveRequest
     * @return
     */
    @ApiOperation(value = "修改线下结算银行账户")
    @RequestMapping(value = "/offlineAccount", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> modifyLineAccount(@RequestBody OfflineAccountModifyRequest saveRequest){
        offlineProvider.modify(saveRequest);
        //操作日志记录
        operateLogMQUtil.convertAndSend("财务", "编辑线下支付账号", "编辑线下支付账号：" + saveRequest.getAccountName());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 查询线上账户
     *
     * @param accountId
     * @return
     */
    @ApiOperation(value = "根据结算银行账户ID查询线下结算银行账户")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "accountId", value = "结算银行账户ID", required = true)
    @RequestMapping(value = "/offlineAccount/{accountId}", method = RequestMethod.GET)
    public ResponseEntity<OfflineAccountGetByIdResponse> findOfflineAccountById(@PathVariable("accountId") Long accountId){
        OfflineAccountGetByIdResponse offlineAccount = offlineQueryProvider.getById(new OfflineAccountGetByIdRequest
                (accountId)).getContext();

        if (offlineAccount.getAccountId() != null) {
            return ResponseEntity.ok(offlineAccount);
        } else {
            throw new SbcRuntimeException("K-020005");
        }
    }

    /**
     * 删除线下账户
     *
     * @param accountId
     * @return
     */
    @ApiOperation(value = "删除线下结算银行账户")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "accountId", value = "结算银行账户ID", required = true)
    @RequestMapping(value = "/offlineAccount/{accountId}", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponse> removeOfflineById(@PathVariable("accountId") Long accountId){
        OfflineAccountGetByIdResponse accountOptional =
                offlineQueryProvider.getById(new OfflineAccountGetByIdRequest(accountId)).getContext();

        offlineProvider.deleteById(new OfflineAccountDeleteByIdRequest(accountId));
        //操作日志记录
        operateLogMQUtil.convertAndSend("财务", "删除线下支付账号",
            "删除线下支付账号：" + (Objects.nonNull(accountOptional) ? accountOptional.getAccountName() : ""));

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 禁用银行账号
     *
     * @param accountId
     * @return
     */
    @ApiOperation(value = "禁用线下结算银行账户")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "accountId", value = "结算银行账户ID", required = true)
    @RequestMapping(value = "/offline/disable/{accountId}", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> disableOfflineById(@PathVariable("accountId") Long accountId){
        OfflineAccountGetByIdResponse accountOptional =
                offlineQueryProvider.getById(new OfflineAccountGetByIdRequest(accountId)).getContext();

        offlineProvider.disableById(new OfflineDisableByIdRequest(accountId));
        //操作日志记录
        operateLogMQUtil.convertAndSend("财务", "禁用线下支付账号",
            "禁用线下支付账号：" + (Objects.nonNull(accountOptional) ? accountOptional.getAccountName() : ""));

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 启用银行账号
     *
     * @param accountId
     * @return
     */
    @ApiOperation(value = "启用线下结算银行账户")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "accountId", value = "结算银行账户ID", required = true)
    @RequestMapping(value = "/offline/enable/{accountId}", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> enableOfflineById(@PathVariable("accountId") Long accountId){
        OfflineAccountGetByIdResponse accountOptional =
                offlineQueryProvider.getById(new OfflineAccountGetByIdRequest(accountId)).getContext();

        offlineProvider.enableById(new OfflineEnableByIdRequest(accountId));
        //操作日志记录
        operateLogMQUtil.convertAndSend("财务", "启用线下支付账号",
            "启用线下支付账号：" + (Objects.nonNull(accountOptional) ? accountOptional.getAccountName() : ""));

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 获取配置银行列表
     *
     * @return
     */
    @ApiOperation(value = "获取配置银行列表")
    @RequestMapping(value = "/base/bank", method = RequestMethod.GET)
    public BaseResponse<List<BaseBank>> getBaseBank() {
        List<BaseBank> list = BaseBankConfiguration.bankList;
        return BaseResponse.success(list);
    }
}
