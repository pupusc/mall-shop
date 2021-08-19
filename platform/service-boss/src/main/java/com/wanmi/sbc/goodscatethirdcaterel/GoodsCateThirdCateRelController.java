package com.wanmi.sbc.goodscatethirdcaterel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.goods.api.provider.goodscatethirdcaterel.GoodsCateThirdCateRelQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodscatethirdcaterel.GoodsCateThirdCateRelProvider;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.*;
import com.wanmi.sbc.goods.api.response.goodscatethirdcaterel.*;

import javax.validation.Valid;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;

import java.time.LocalDateTime;
import java.util.ArrayList;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(description = "平台类目和第三方平台类目映射管理API", tags = "GoodsCateThirdCateRelController")
@RestController
@RequestMapping(value = "/goodscatethirdcaterel")
public class GoodsCateThirdCateRelController {

    @Autowired
    private GoodsCateThirdCateRelQueryProvider goodsCateThirdCateRelQueryProvider;

    @Autowired
    private GoodsCateThirdCateRelProvider goodsCateThirdCateRelProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @ApiOperation(value = "分页查询平台类目和第三方平台类目映射")
    @PostMapping("/page")
    public BaseResponse<GoodsCateThirdCateRelPageResponse> getPage(@RequestBody @Valid GoodsCateThirdCateRelPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("id", "desc");
        return goodsCateThirdCateRelQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询平台类目和第三方平台类目映射")
    @PostMapping("/list")
    public BaseResponse<GoodsCateThirdCateRelListResponse> getList(@RequestBody @Valid GoodsCateThirdCateRelListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        return goodsCateThirdCateRelQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询平台类目和第三方平台类目映射")
    @GetMapping("/{id}")
    public BaseResponse<GoodsCateThirdCateRelByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        GoodsCateThirdCateRelByIdRequest idReq = new GoodsCateThirdCateRelByIdRequest();
        idReq.setId(id);
        return goodsCateThirdCateRelQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "批量新增平台类目和第三方平台类目映射")
    @PostMapping("/addBatch")
    public BaseResponse addBatch(@RequestBody @Valid GoodsCateThirdCateRelAddRequest addReq) {
        GoodsCateThirdCateRelAddResponse response = goodsCateThirdCateRelProvider.addBatch(addReq).getContext();
        if (response != null) {
            ArrayList<String> delEsGoodsIds = response.getDelEsGoodsIds();
            if (delEsGoodsIds != null && delEsGoodsIds.size() > 0) {
                esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().deleteIds(delEsGoodsIds).build());
            }
            ArrayList<String> updateEsGoodsIds = response.getUpdateEsGoodsIds();
            if (updateEsGoodsIds != null && updateEsGoodsIds.size() > 0) {
                esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsIds(updateEsGoodsIds).build());
            }
        }
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "修改平台类目和第三方平台类目映射")
    @PutMapping("/modify")
    public BaseResponse<GoodsCateThirdCateRelModifyResponse> modify(@RequestBody @Valid GoodsCateThirdCateRelModifyRequest modifyReq) {
        modifyReq.setUpdateTime(LocalDateTime.now());
        return goodsCateThirdCateRelProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除平台类目和第三方平台类目映射")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        GoodsCateThirdCateRelDelByIdRequest delByIdReq = new GoodsCateThirdCateRelDelByIdRequest();
        delByIdReq.setId(id);
        return goodsCateThirdCateRelProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除平台类目和第三方平台类目映射")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid GoodsCateThirdCateRelDelByIdListRequest delByIdListReq) {
        return goodsCateThirdCateRelProvider.deleteByIdList(delByIdListReq);
    }


}
