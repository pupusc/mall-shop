package com.wanmi.sbc.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsLabelProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsLabelDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsLabelUpdateNameRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsLabelUpdateSortRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsLabelUpdateVisibleRequest;
import com.wanmi.sbc.goods.api.provider.goodslabel.GoodsLabelQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodslabel.GoodsLabelSaveProvider;
import com.wanmi.sbc.goods.api.request.goodslabel.*;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelAddResponse;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelByIdResponse;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelCacheListResponse;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelModifyResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsLabelVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Api(description = "商品标签管理API", tags = "GoodsLabelController")
@RestController
@RequestMapping(value = "/goodslabel")
public class GoodsLabelController {

    @Autowired
    private GoodsLabelQueryProvider goodsLabelQueryProvider;

    @Autowired
    private GoodsLabelSaveProvider goodsLabelSaveProvider;

    @Autowired
    private EsGoodsLabelProvider esGoodsLabelProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "根据店铺id列表查询商品标签")
    @PostMapping("/list")
    public BaseResponse<GoodsLabelCacheListResponse> list() {
        return goodsLabelQueryProvider.cacheList();
    }

    @ApiOperation(value = "根据id查询商品标签")
    @GetMapping("/{goodsLabelId}")
    public BaseResponse<GoodsLabelByIdResponse> getById(@PathVariable Long goodsLabelId) {
        if (goodsLabelId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        GoodsLabelByIdRequest idReq = new GoodsLabelByIdRequest();
        idReq.setGoodsLabelId(goodsLabelId);
        return goodsLabelQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增商品标签")
    @PostMapping("/add")
    public BaseResponse<GoodsLabelAddResponse> add(@RequestBody @Valid GoodsLabelAddRequest addReq) {
        addReq.setDelFlag(DeleteFlag.NO);
        addReq.setCreateTime(LocalDateTime.now());
        addReq.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        addReq.setLabelSort(1);
        return goodsLabelSaveProvider.add(addReq);
    }

    @ApiOperation(value = "修改商品标签")
    @PutMapping("/modify")
    public BaseResponse<GoodsLabelModifyResponse> modify(@RequestBody @Valid GoodsLabelModifyRequest modifyReq) {
        modifyReq.setStoreId(commonUtil.getStoreIdWithDefault());
        GoodsLabelModifyResponse response = goodsLabelSaveProvider.modify(modifyReq).getContext();
        //更新ES
        if (Objects.nonNull(response.getGoodsLabelVO())) {
            esGoodsLabelProvider.updateLabelName(EsGoodsLabelUpdateNameRequest.builder().
                    goodsLabelVO(response.getGoodsLabelVO()).build());
        }
        return BaseResponse.success(response);
    }

    @ApiOperation(value = "修改商品展示状态")
    @PutMapping("/modify-visible")
    public BaseResponse modifyVisible(@RequestBody @Valid GoodsLabelModifyVisibleRequest modifyReq) {
        modifyReq.setStoreId(commonUtil.getStoreIdWithDefault());
        goodsLabelSaveProvider.modifyVisible(modifyReq);
        //更新ES
        GoodsLabelVO vo = new GoodsLabelVO();
        vo.setGoodsLabelId(modifyReq.getGoodsLabelId());
        vo.setLabelVisible(modifyReq.getLabelVisible());
        esGoodsLabelProvider.updateLabelVisible(EsGoodsLabelUpdateVisibleRequest.builder().goodsLabelVO(vo).build());
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "标签拖拽排序")
    @PutMapping(value = "/editSort")
    public BaseResponse editSort(@RequestBody GoodsLabelSortRequest request) {
        if (CollectionUtils.isEmpty(request.getLabelIdList())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        request.setStoreId(commonUtil.getStoreIdWithDefault());
        List<GoodsLabelVO> list = goodsLabelSaveProvider.editSort(request).getContext().getList();
        esGoodsLabelProvider.updateGoodsLabelSort(EsGoodsLabelUpdateSortRequest.builder().goodsLabelVOList(list).build());
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "根据id删除商品标签")
    @DeleteMapping("/{goodsLabelId}")
    public BaseResponse deleteById(@PathVariable Long goodsLabelId) {
        if (goodsLabelId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        GoodsLabelDelByIdRequest delByIdReq = new GoodsLabelDelByIdRequest();
        delByIdReq.setGoodsLabelId(goodsLabelId);
        delByIdReq.setStoreId(commonUtil.getStoreIdWithDefault());
        goodsLabelSaveProvider.deleteById(delByIdReq);
        // 同步删除es对应关联的标签
        esGoodsLabelProvider.deleteSomeLabel(EsGoodsLabelDeleteByIdsRequest.builder().
                ids(Collections.singletonList(goodsLabelId)).build());
        return BaseResponse.SUCCESSFUL();
    }
}
