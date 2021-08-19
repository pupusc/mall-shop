package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.provider.storeresourcecate.StoreResourceCateQueryProvider;
import com.wanmi.sbc.setting.api.provider.storeresourcecate.StoreResourceCateSaveProvider;
import com.wanmi.sbc.setting.api.request.storeresourcecate.*;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCateAddResponse;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCateByIdResponse;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCateListResponse;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCateModifyResponse;
import com.wanmi.sbc.setting.bean.vo.StoreResourceCateVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 店铺素材分类服务
 * 完全参考平台素材分类服务
 * Created by yinxianzhi on 18/10/18.
 */
@Api(tags = "StoreResourceCateController", description = "店铺素材分类服务 API")
@RestController
public class StoreResourceCateController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private StoreResourceCateSaveProvider storeResourceCateSaveProvider;

    @Autowired
    private StoreResourceCateQueryProvider storeResourceCateQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;


    /**
     * 查询店铺素材分类
     */
    @ApiOperation(value = "查询店铺素材分类")
    @RequestMapping(value = {"/store/resourceCates"}, method = RequestMethod.GET)
    public ResponseEntity<List> list() {
        StoreResourceCateListRequest queryRequest = StoreResourceCateListRequest.builder()
                .storeId(commonUtil.getStoreId()).build();
        BaseResponse<StoreResourceCateListResponse> response = storeResourceCateQueryProvider.list
                (queryRequest);
        return ResponseEntity.ok(response.getContext().getStoreResourceCateVOList());
    }


    /**
     * 新增店铺素材分类
     */
    @ApiOperation(value = "新增店铺素材分类")
    @RequestMapping(value = "/store/resourceCate", method = RequestMethod.POST)
    public BaseResponse add(@RequestBody StoreResourceCateAddRequest addReq) {
        if (addReq == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        addReq.setStoreId(commonUtil.getStoreId());
        addReq.setCompanyInfoId(commonUtil.getCompanyInfoId());

        BaseResponse<StoreResourceCateAddResponse> response = storeResourceCateSaveProvider.add(addReq);
        //记录操作日志
        if (Objects.nonNull(addReq.getCateParentId())) {
            operateLogMQUtil.convertAndSend("设置", "新增子分类", "新增子分类：" + addReq.getCateName());
        } else {
            operateLogMQUtil.convertAndSend("设置", "新增一级分类", "新增一级分类：" + addReq.getCateName());
        }

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 编辑店铺素材分类
     */
    @ApiOperation(value = "编辑店铺素材分类")
    @RequestMapping(value = "/store/resourceCate", method = RequestMethod.PUT)
    public BaseResponse edit(@RequestBody StoreResourceCateModifyRequest modifyReq) {
        if (modifyReq == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        //查询对应分类名称
        Optional<StoreResourceCateVO> cateInfoOptional = this.findCateInfoByCateId(modifyReq.getCateId());

        modifyReq.setStoreId(commonUtil.getStoreId());
        modifyReq.setCompanyInfoId(commonUtil.getCompanyInfoId());

        BaseResponse<StoreResourceCateModifyResponse> response = storeResourceCateSaveProvider.modify(modifyReq);

        //记录操作日志
        cateInfoOptional.ifPresent(map -> operateLogMQUtil.convertAndSend("设置", "编辑分类",
                "分类名称：" + map.getCateName() + " 改成 " + modifyReq.getCateName()));
        return response;
    }



    /**
     * 检测店铺素材分类是否有子类
     */
    @ApiOperation(value = "检测店铺素材分类是否有子类")
    @RequestMapping(value = "/store/resourceCate/child", method = RequestMethod.POST)
    public BaseResponse checkChild(@RequestBody StoreResourceCateCheckChildRequest request) {
        if (request == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        request.setStoreId(commonUtil.getStoreId());
        BaseResponse<Integer> response =storeResourceCateQueryProvider.checkChild(request);
        return response;
    }




    /**
     * 检测店铺素材分类是否已关联素材
     */
    @ApiOperation(value = "检测店铺素材分类是否已关联素材")
    @RequestMapping(value = "/store/resourceCate/resource", method = RequestMethod.POST)
    public BaseResponse checkResource(@RequestBody StoreResourceCateCheckResourceRequest request) {
        if (request == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        request.setStoreId(commonUtil.getStoreId());
        BaseResponse<Integer> response =storeResourceCateQueryProvider.checkResource(request);
        return BaseResponse.success(response);
    }


    /**
     * 删除店铺素材分类
     */
    @ApiOperation(value = "删除店铺素材分类")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "cateId", value = "分类Id", required = true)
    @RequestMapping(value = "/store/resourceCate/{cateId}", method = RequestMethod.DELETE)
    public BaseResponse delete(@PathVariable Long cateId) {
        if (cateId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        //查询对应分类名称
        Optional<StoreResourceCateVO> cateInfoOptional = this.findCateInfoByCateId(cateId);

        StoreResourceCateDelByIdRequest delByIdReq = new StoreResourceCateDelByIdRequest();
        delByIdReq.setCateId(cateId);
        delByIdReq.setStoreId(commonUtil.getStoreId());
        BaseResponse response = storeResourceCateSaveProvider.delete(delByIdReq);

        //记录操作人日志
        cateInfoOptional.ifPresent(map -> operateLogMQUtil.convertAndSend("设置", "删除分类",
                "删除分类：" + map.getCateName()));
        return response;
    }



    /**
     * 根据cateId查询CateInfo
     *
     * @param cateId
     * @return
     */
    private Optional<StoreResourceCateVO> findCateInfoByCateId(Long cateId) {
        if (cateId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreResourceCateByIdRequest queryRequest = new StoreResourceCateByIdRequest();
        queryRequest.setCateId(cateId);
        BaseResponse<StoreResourceCateByIdResponse> response = storeResourceCateQueryProvider.getById(queryRequest);
        StoreResourceCateVO storeResourceCateVO = response.getContext().getStoreResourceCateVO();
        if (Objects.isNull(storeResourceCateVO)) {
            return Optional.empty();
        }
        return Optional.of(storeResourceCateVO);
    }
}
