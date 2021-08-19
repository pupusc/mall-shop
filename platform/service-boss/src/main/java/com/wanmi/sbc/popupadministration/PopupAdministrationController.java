package com.wanmi.sbc.popupadministration;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.provider.popupadministration.PopupAdministrationProvider;
import com.wanmi.sbc.setting.api.request.popupadministration.*;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationDeleteResponse;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationPauseResponse;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationResponse;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationStartResponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * <p>弹窗管理服务API</p>
 * @author weiwenhao
 * @date 2020-04-23
 */
@Api(description = "弹窗增删改管理服务API", tags = "PopupAdministrationController")
@RestController
@RequestMapping("/popup_administration")
public class PopupAdministrationController {


    @Autowired
    private PopupAdministrationProvider popupAdministrationProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;


    /**
     * 新增弹窗
     * @param request
     * @return
     */
    @ApiOperation(value = "新增弹窗")
    @PostMapping("/add")
    BaseResponse<PopupAdministrationResponse> add(@RequestBody @Valid PopupAdministrationRequest request){
        request.setCreatePerson(commonUtil.getOperatorId());
        operateLogMQUtil.convertAndSend("新增弹窗", "新增弹窗", "新增弹窗:"+request.getPopupName());
        return popupAdministrationProvider.add(request);
    }

    /**
     * 编辑弹窗
     * @param request
     * @return
     */
    @ApiOperation(value = "编辑弹窗")
    @PostMapping("/modify")
    BaseResponse<PopupAdministrationResponse> modify(@RequestBody @Valid PopupAdministrationModifyRequest request){
        request.setUpdatePerson(commonUtil.getOperatorId());
        operateLogMQUtil.convertAndSend("编辑弹窗", "编辑弹窗", "编辑弹窗:"+request.getPopupName());
        return popupAdministrationProvider.modify(request);
    }

    /**
     * 删除弹窗
     * @param request
     * @return
     */
    @ApiOperation(value = "删除弹窗")
    @PostMapping("/delete")
    BaseResponse<PopupAdministrationDeleteResponse> delete(@RequestBody @Valid PopupAdministrationDeleteRequest request){
        operateLogMQUtil.convertAndSend("删除弹窗", "删除弹窗", "删除弹窗:"+request.getPopupId());
        return popupAdministrationProvider.delete(request);
    }

    /**
     * 暂停弹窗
     * @param popupAdministrationId
     * @return
     */
     @ApiOperation(value = "暂停弹窗")
     @GetMapping("/pause_popup_administration/{popupAdministrationId}")
     BaseResponse<PopupAdministrationPauseResponse> pausePopupAdministration(@PathVariable("popupAdministrationId") Long popupAdministrationId){
         if(Objects.isNull(popupAdministrationId)){
             throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
         }
         PopupAdministrationPauseRequest popupAdministrationPuase=
                 new PopupAdministrationPauseRequest(popupAdministrationId);
         operateLogMQUtil.convertAndSend("暂停弹窗", "暂停弹窗", "暂停弹窗:"+popupAdministrationId);
         return popupAdministrationProvider.pausePopupAdministration(popupAdministrationPuase);
     }


    /**
     * 启动弹窗
     * @param popupAdministrationId
     * @return
     */
    @ApiOperation(value = "启动弹窗")
    @GetMapping("/start_popup_administration/{popupAdministrationId}")
    BaseResponse<PopupAdministrationStartResponse> startPopupAdministration(@PathVariable("popupAdministrationId") Long popupAdministrationId){
        if(Objects.isNull(popupAdministrationId)){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PopupAdministrationStartRequest popupAdministrationStartRequest=new PopupAdministrationStartRequest(popupAdministrationId);
        operateLogMQUtil.convertAndSend("启动弹窗", "启动弹窗", "启动弹窗:"+popupAdministrationId);
        return popupAdministrationProvider.startPopupAdministration(popupAdministrationStartRequest);
    }

    /**
     * 弹窗在应用页面的排序
     * @param request
     * @return
     */
    @ApiOperation(value = "弹窗在应用页面的排序")
    @PostMapping("/sort_popup_administration")
    BaseResponse sortPopupAdministration(@RequestBody @Valid List<PopupAdministrationSortRequest> request){
        operateLogMQUtil.convertAndSend("弹窗在应用页面的排序", "弹窗在应用页面的排序", "弹窗在应用页面的排序:"+request);
        return popupAdministrationProvider.sortPopupAdministration(request);
    }

}
