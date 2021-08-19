package com.wanmi.sbc.xsite;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewPageRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewPageResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 魔方建站接口
 */
@Api(tags = "XsiteController", description = "魔方建站 API")
@RestController
@RequestMapping("")
public class XsiteController {

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    /**
     * 魔方分页显示商品
     * @param
     * @return 商品详情
     */
    @ApiOperation(value = "魔方分页显示商品")
    @RequestMapping(value = "/xsite/skusForXsite", method = RequestMethod.POST)
    public Map<String,Object> skuList(@RequestBody GoodsInfoRequest request) {
         GoodsInfoViewPageRequest queryRequest = new GoodsInfoViewPageRequest();
        queryRequest.setPageNum(request.getPageNum());
        queryRequest.setPageSize(request.getPageSize());
        queryRequest.setLikeGoodsName(request.getQ());
        queryRequest.setCateId(request.getCatName());
        //按创建时间倒序、ID升序
        queryRequest.putSort("addedTime", SortType.DESC.toValue());
        queryRequest.putSort("goodsInfoId", SortType.ASC.toValue());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
        GoodsInfoViewPageResponse response = goodsInfoQueryProvider.pageView(queryRequest).getContext();
        Map<String,Object> resMap = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        List<GoodsInfoVO> goodsInfos = response.getGoodsInfoPage().getContent();
        List<Map<String,Object>> skus = new ArrayList<>();
        goodsInfos.stream().forEach(goodsInfo -> {
            Map<String,Object> sku = new HashMap<>();
            sku.put("id",goodsInfo.getGoodsInfoId());
            sku.put("pid",goodsInfo.getGoodsId());
            sku.put("name",goodsInfo.getGoodsInfoName());
            sku.put("img",goodsInfo.getGoodsInfoImg());
            sku.put("price",goodsInfo.getSalePrice());
            sku.put("stock",goodsInfo.getStock());
            sku.put("enable",true);
            skus.add(sku);
        });
        data.put("dataList",skus);
        data.put("pageNum",request.getPageNum());
        data.put("pageSize",request.getPageSize());
        data.put("totalCount",response.getGoodsInfoPage().getTotalElements());
        resMap.put("data",data);
        resMap.put("status",1);
        resMap.put("message","操作成功");
        resMap.put("code",null);
        return resMap;
    }
}
