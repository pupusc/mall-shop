package com.wanmi.sbc.setting.provider.impl.onlineservice;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.constant.SettingErrorCode;
import com.wanmi.sbc.setting.bean.vo.OnlineServiceItemVO;
import com.wanmi.sbc.setting.bean.vo.OnlineServiceVO;
import com.wanmi.sbc.setting.onlineserviceitem.model.root.OnlineServiceItem;
import com.wanmi.sbc.setting.onlineserviceitem.service.OnlineServiceItemService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.onlineservice.OnlineServiceSaveProvider;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceModifyRequest;
import com.wanmi.sbc.setting.onlineservice.service.OnlineServiceService;
import com.wanmi.sbc.setting.onlineservice.model.root.OnlineService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>onlineService保存服务接口实现</p>
 *
 * @author lq
 * @date 2019-11-05 16:10:28
 */
@RestController
@Validated
public class OnlineServiceSaveController implements OnlineServiceSaveProvider {
    @Autowired
    private OnlineServiceService onlineServiceService;

    @Autowired
    private OnlineServiceItemService onlineServiceItemService;

    @Override
    public BaseResponse modify(@RequestBody @Valid OnlineServiceModifyRequest onlineServiceModifyRequest) {
        // 在线客服设置
        OnlineServiceVO onlineServerVo = onlineServiceModifyRequest.getQqOnlineServerRop();
        // 客服座席列表
        List<OnlineServiceItemVO> onlineServerItemVoList = onlineServiceModifyRequest.getQqOnlineServerItemRopList();

        if (Objects.isNull(onlineServerVo)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if(Objects.equals(onlineServerVo.getServerStatus(), DefaultFlag.YES)
                && CollectionUtils.isEmpty(onlineServerItemVoList)){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (onlineServerItemVoList.size() > 10){
            throw new SbcRuntimeException(SettingErrorCode.ONLINE_SERVER_MAX_ERROR);
        }

        //客服设置
        OnlineService onlineServer = onlineServiceService.getById(onlineServerVo.getOnlineServiceId());
        onlineServer.setServerStatus(onlineServerVo.getServerStatus());
        onlineServer.setServiceTitle(onlineServerVo.getServiceTitle());
        onlineServer.setEffectivePc(onlineServerVo.getEffectivePc());
        onlineServer.setEffectiveApp(onlineServerVo.getEffectiveApp());
        onlineServer.setEffectiveMobile(onlineServerVo.getEffectiveMobile());
        onlineServer.setUpdateTime(LocalDateTime.now());

        //删除客服座席
        onlineServiceItemService.deleteByOnlineServiceId(onlineServer.getOnlineServiceId());
        if (CollectionUtils.isNotEmpty(onlineServerItemVoList)) {
            List<String> serverAccount = onlineServerItemVoList.stream().map(vo -> vo.getCustomerServiceAccount()).collect(Collectors.toList());
            //查询重复的QQ号
            List<OnlineServiceItem> duplicateItem = onlineServiceItemService.checkDuplicateAccount(serverAccount);
            duplicateItem = duplicateItem.stream()
                    .filter(item -> item.getStoreId().equals(onlineServiceModifyRequest.getStoreId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(duplicateItem)) {
                List<String> dupAccounts = duplicateItem.stream().map(item -> item.getCustomerServiceAccount()).collect(Collectors.toList());
                String dupAccount = StringUtils.join(dupAccounts, ",");
                throw new SbcRuntimeException(SettingErrorCode.ONLINE_SERVER_ACCOUNT_ALREADY_EXIST, new Object[]{dupAccount});
            }

            List<OnlineServiceItem> onlineServerItemList = onlineServiceItemService.getOnlineServerItemList(onlineServerItemVoList);
            //保存客服座席
            onlineServiceItemService.save(onlineServerItemList);
        }

        //保存在线客服
        onlineServiceService.add(onlineServer);

        return BaseResponse.SUCCESSFUL();
    }

}

