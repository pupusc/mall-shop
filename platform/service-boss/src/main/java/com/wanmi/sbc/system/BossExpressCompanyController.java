package com.wanmi.sbc.system;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyByIdRequest;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyDelByIdRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyByIdResponse;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyAddResponse;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyAddRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyListResponse;
import com.wanmi.sbc.util.OperateLogMQUtil;
import com.wanmi.sbc.setting.api.provider.expresscompany.ExpressCompanyQueryProvider;
import com.wanmi.sbc.setting.api.provider.expresscompany.ExpressCompanySaveProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * S2B 平台端-物流公司管理
 * Created by bail on 2017/11/21.
 */
@Api(tags = "BossExpressCompanyController", description = "S2B 平台端-物流公司管理API")
@RestController
@RequestMapping("/boss/expressCompany")
public class BossExpressCompanyController {

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private ExpressCompanyQueryProvider expressCompanyQueryProvider;

    @Autowired
    private ExpressCompanySaveProvider expressCompanySaveProvider;

    /**
     * S2B平台端 查询所有有效的物流公司列表
     *
     * @author bail
     */
    @ApiOperation(value = "S2B平台端 查询所有有效的物流公司列表")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<List> allExpressCompanyList() {
        ExpressCompanyListResponse response = expressCompanyQueryProvider.list().getContext();
        return BaseResponse.success(response.getExpressCompanyVOList());
//        CompositeResponse<List> response = sdkClient.buildClientRequest().post(List.class, "platformExpressCom.list", "1.0.0");
//        return BaseResponse.success(response.getSuccessResponse());
    }

    /**
     * S2B平台端 新增物流公司
     *
     */
    @ApiOperation(value = "S2B平台端 新增物流公司")
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse addExpressCompany(@Valid @RequestBody ExpressCompanyAddRequest addRequest) {
        addRequest.setDelFlag(DeleteFlag.NO);
        ExpressCompanyAddResponse response = expressCompanySaveProvider.add(addRequest).getContext();
        operateLogMQUtil.convertAndSend("设置", "新增物流公司", "新增物流公司：" + response.getExpressCompanyVO());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * S2B平台端 删除物流公司
     *
     * @param expressCompanyId
     */
    @ApiOperation(value = "S2B平台端 删除物流公司")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "expressCompanyId", value = "物流公司id", required = true)
    @RequestMapping(value = "/{expressCompanyId}", method = RequestMethod.DELETE)
    public BaseResponse delExpressCompany(@PathVariable Long expressCompanyId) {
         if(StringUtils.isBlank(expressCompanyId.toString())){
             throw new SbcRuntimeException("K-000009");
         }

         ExpressCompanyByIdResponse expressCompanyByIdResponse =  expressCompanyQueryProvider.getById(
                new ExpressCompanyByIdRequest().builder()
                .expressCompanyId(expressCompanyId)
                .build()).getContext();
         if(Objects.isNull(expressCompanyByIdResponse.getExpressCompanyVO())){
            return BaseResponse.error("该物流公司不存在，检查是否已删除");
         }
         expressCompanySaveProvider.deleteById(new ExpressCompanyDelByIdRequest()
                .builder().expressCompanyId(expressCompanyId).build());

         //操作日志记录
         operateLogMQUtil.convertAndSend("设置", "删除物流公司", "删除物流公司：" + expressCompanyId);
         return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取所有的Cate
     *
     * @return
     */
//    private Optional<Map<String, Object>> queryCateInfoByCateId(Long expressCompanyId) {
//        CompositeResponse<List<Map<String, Object>>> response = sdkClient.buildClientRequest().post(List.class,
//                "platformExpressCom.list", "1.0.0");
//
//        if (!response.isSuccessful() || CollectionUtils.isEmpty(response.getSuccessResponse())) {
//            return Optional.empty();
//        }
//        return response.getSuccessResponse().stream()
//                .filter(map -> expressCompanyId.equals(Long.valueOf(map.get("expressCompanyId").toString()))).findFirst();
//    }
}
