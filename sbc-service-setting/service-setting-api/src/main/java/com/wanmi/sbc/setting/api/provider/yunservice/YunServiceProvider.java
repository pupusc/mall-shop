package com.wanmi.sbc.setting.api.provider.yunservice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.yunservice.*;
import com.wanmi.sbc.setting.api.response.yunservice.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>系统配置表查询服务Provider</p>
 *
 * @author yang
 * @date 2019-11-05 18:33:04
 */
@FeignClient(value = "${application.setting.name}", contextId = "YunServiceProvider")
public interface YunServiceProvider {

    /**
     * 列表查询云配置信息
     *
     * @param yunConfigListRequest 列表请求参数和筛选对象 {@link YunConfigListRequest}
     * @return 云系统配置表的列表信息 {@link YunConfigListResponse}
     * @author yang
     */
    @PostMapping("/setting/${application.setting.version}/yunconfig/list")
    BaseResponse<YunConfigListResponse> list(@RequestBody @Valid YunConfigListRequest yunConfigListRequest);

    /**
     * 根据id查询云配置信息
     *
     * @param yunConfigByIdRequest 请求参数和筛选对象 {@link YunConfigByIdRequest}
     * @return 云系统配置表的列表信息 {@link YunConfigResponse}
     * @author yang
     */
    @PostMapping("/setting/${application.setting.version}/yunconfig/get-by-id")
    BaseResponse<YunConfigResponse> getById(@RequestBody @Valid YunConfigByIdRequest yunConfigByIdRequest);

    /**
     * 查询可用云配置信息
     *
     * @return 云系统配置表的列表信息 {@link YunConfigResponse}
     * @author yang
     */
    @PostMapping("/setting/${application.setting.version}/yunconfig/get-available-yun")
    BaseResponse<YunAvailableConfigResponse> getAvailableYun();

    /**
     * 修改云配置信息
     *
     * @param yunConfigModifyRequest 修改云配置信息参数结构 {@link YunConfigModifyRequest}
     * @return 修改云配置信息 {@link YunConfigResponse}
     * @author yang
     */
    @PostMapping("/setting/${application.setting.version}/yunconfig/modify")
    BaseResponse<YunConfigResponse> modify(@RequestBody @Valid YunConfigModifyRequest yunConfigModifyRequest);

    /**
     * 上传文件
     *
     * @param yunUploadResourceRequest 上传文件 {@link YunUploadResourceRequest}
     * @return 上传文件信息 {@link String}
     * @author yang
     */
    @PostMapping("/setting/${application.setting.version}/yunconfig/upload-resource")
    BaseResponse<YunUploadResourceResponse> uploadFile(@RequestBody @Valid YunUploadResourceRequest yunUploadResourceRequest);

    /**
     * 获取文件
     *
     * @param yunGetResourceRequest 获取文件 {@link YunGetResourceRequest}
     * @return 获取文件 {@link YunGetResourceResponse}
     * @author yang
     */
    @PostMapping("/setting/${application.setting.version}/yunconfig/get-resource")
    BaseResponse<YunGetResourceResponse> getFile(@RequestBody @Valid YunGetResourceRequest yunGetResourceRequest);


    /**
     * 上传excel文件
     *
     * @param yunUploadResourceRequest 上传文件 {@link YunUploadResourceRequest}
     * @return 上传文件信息 {@link String}
     */
    @PostMapping("/setting/${application.setting.version}/yunconfig/upload-file-excel")
    BaseResponse<String> uploadFileExcel(@RequestBody @Valid YunUploadResourceRequest yunUploadResourceRequest);

    /**
     * 删除云文件（只单纯删除云文件，不做其他操作）
     * @param yunFileDeleteRequest
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/yunconfig/delete-file")
    BaseResponse deleteFile(@RequestBody @Valid YunFileDeleteRequest yunFileDeleteRequest);
}

