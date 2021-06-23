package com.wanmi.sbc.thirdgoodscate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.provider.thirdgoodscate.ThirdGoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.thirdgoodscate.ThirdGoodsCateProvider;
import com.wanmi.sbc.goods.api.request.thirdgoodscate.*;
import com.wanmi.sbc.goods.api.response.thirdgoodscate.*;

import javax.validation.Valid;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;

import java.util.List;

import com.wanmi.sbc.goods.bean.dto.ThirdGoodsCateRelDTO;
import com.wanmi.sbc.goods.bean.vo.ThirdGoodsCateRelVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(description = "第三方类目API", tags = "ThirdGoodsCateController")
@RestController
@RequestMapping(value = "/thirdgoodscate")
public class ThirdGoodsCateController {

    @Autowired
    private ThirdGoodsCateQueryProvider thirdGoodsCateQueryProvider;

    @Autowired
    private ThirdGoodsCateProvider thirdGoodsCateProvider;

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public BaseResponse<ThirdGoodsCatePageResponse> getPage(@RequestBody @Valid ThirdGoodsCatePageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("cateId", "desc");
        return thirdGoodsCateQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping("/list")
    public BaseResponse<ThirdGoodsCateListResponse> getList(@RequestBody @Valid ThirdGoodsCateListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        return thirdGoodsCateQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping("/{cateId}")
    public BaseResponse<ThirdGoodsCateByIdResponse> getById(@PathVariable Long cateId) {
        if (cateId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        ThirdGoodsCateByIdRequest idReq = new ThirdGoodsCateByIdRequest();
        idReq.setId(cateId);
        return thirdGoodsCateQueryProvider.getById(idReq);
    }

    /**
     * 根据三方类目父id关联查询平台类目
     *
     * @return
     */
    @ApiOperation(value = "根据三方类目父id关联查询平台类目")
    @PostMapping("/getRelByParentId")
    public BaseResponse<List<ThirdGoodsCateRelDTO>> getAllRel(@RequestBody CateRelByParentIdRequest cateRelByParentIdRequest) {
        return thirdGoodsCateQueryProvider.getRelByParentId(cateRelByParentIdRequest);
    }

    /**
     * 查询所有三方类目并关联平台类目
     *
     * @return
     */
    @ApiOperation(value = "查询所有三方类目并关联平台类目")
    @PostMapping("/list/rel")
    public BaseResponse<List<ThirdGoodsCateRelVO>> listRel(@RequestBody CateRelRequest request) {
        return thirdGoodsCateQueryProvider.listRel(request);
    }


}
