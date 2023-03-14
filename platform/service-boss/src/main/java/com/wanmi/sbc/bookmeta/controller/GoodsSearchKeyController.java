package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.GoodsBO;
import com.wanmi.sbc.bookmeta.bo.GoodsNameBySpuIdBO;
import com.wanmi.sbc.bookmeta.bo.GoodsSearchKeyAddBo;
import com.wanmi.sbc.bookmeta.provider.GoodsSearchKeyProvider;
import com.wanmi.sbc.bookmeta.vo.GoodsSearchBySpuIdReqVO;
import com.wanmi.sbc.bookmeta.vo.GoodsSearchBySpuIdRespVO;
import com.wanmi.sbc.bookmeta.vo.GoodsSearchKeyAddReqVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/03/16:43
 * @Description:
 */
@RestController
@RequestMapping("goodsSearchKey")
public class GoodsSearchKeyController {

    @Resource
    private GoodsSearchKeyProvider goodsSearchKeyProvider;
    @PostMapping("queryGoodsSearchKey")
    public BusinessResponse<List<GoodsSearchBySpuIdRespVO>> getGoodsSearch(@RequestBody GoodsSearchBySpuIdReqVO pageRequest) {
        List<GoodsNameBySpuIdBO> goodsNameBySpuId = goodsSearchKeyProvider.getGoodsNameBySpuId(pageRequest.getName());
        List<GoodsSearchBySpuIdRespVO> goodsSearchBySpuIdRespVOS = KsBeanUtil.convertList(goodsNameBySpuId, GoodsSearchBySpuIdRespVO.class);
        return BusinessResponse.success(goodsSearchBySpuIdRespVOS);
    }

    @PostMapping("getAllGoodsSearchKey")
    public BusinessResponse<List<GoodsSearchBySpuIdRespVO>> getAllGoodsSearch(@RequestBody GoodsSearchBySpuIdReqVO pageRequest) {
        GoodsNameBySpuIdBO convert = KsBeanUtil.convert(pageRequest, GoodsNameBySpuIdBO.class);
        BusinessResponse<List<GoodsNameBySpuIdBO>> goodsNameBySpuId = goodsSearchKeyProvider.getAllGoodsSearchKey(convert);
        List<GoodsSearchBySpuIdRespVO> goodsSearchBySpuIdRespVOS = KsBeanUtil.convertList(goodsNameBySpuId.getContext(), GoodsSearchBySpuIdRespVO.class);
        return BusinessResponse.success(goodsSearchBySpuIdRespVOS);
    }

    @PostMapping("addGoodsSearchKey")
    public BusinessResponse<Integer> addGoodsSearch(@RequestBody GoodsSearchKeyAddReqVO pageRequest) {
        GoodsSearchKeyAddBo convert = KsBeanUtil.convert(pageRequest, GoodsSearchKeyAddBo.class);
        int i = goodsSearchKeyProvider.addGoodsSearchKey(convert);
        return BusinessResponse.success(i);
    }
    @PostMapping("updateGoodsSearchKey")
    public BusinessResponse<Integer> updateGoodsSearch(@RequestBody GoodsSearchBySpuIdReqVO pageRequest) {
        GoodsSearchKeyAddBo convert = KsBeanUtil.convert(pageRequest, GoodsSearchKeyAddBo.class);
        int i = goodsSearchKeyProvider.updateGoodsSearchKey(convert);
        return BusinessResponse.success(i);
    }

    @PostMapping("deleteGoodsSearchKey")
    public BusinessResponse<Integer> deleteGoodsSearch(@RequestBody GoodsSearchBySpuIdReqVO pageRequest) {
        GoodsNameBySpuIdBO convert = KsBeanUtil.convert(pageRequest, GoodsNameBySpuIdBO.class);
        int i = goodsSearchKeyProvider.deleteGoodsSearchKey(convert);
        return BusinessResponse.success(i);
    }

    @PostMapping("goodsList")
    public BusinessResponse<List<GoodsBO>> getGoodsList(@RequestBody GoodsSearchBySpuIdReqVO pageRequest) {
        GoodsNameBySpuIdBO convert = KsBeanUtil.convert(pageRequest, GoodsNameBySpuIdBO.class);
        BusinessResponse<List<GoodsBO>> goodsList = goodsSearchKeyProvider.getGoodsList(convert);
        return goodsList;
    }

}
