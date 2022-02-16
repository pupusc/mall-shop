package com.wanmi.sbc.open;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.elastic.api.provider.sku.EsSkuQueryProvider;
import com.wanmi.sbc.elastic.api.request.sku.EsSkuPageRequest;
import com.wanmi.sbc.elastic.api.response.sku.EsSkuPageResponse;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.open.vo.GoodsListReqVO;
import com.wanmi.sbc.open.vo.GoodsListResVO;
import com.wanmi.sbc.open.vo.OrderCreateReqVO;
import com.wanmi.sbc.open.vo.OrderCreateResVO;
import com.wanmi.sbc.open.vo.OrderInfoReqVO;
import com.wanmi.sbc.open.vo.OrderInfoResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @desc 实物履约
 * @date 2022-02-15 13:56:00
 */
@RestController
@RequestMapping("open/substance")
public class OpenSubstanceController {
    @Autowired
    private EsSkuQueryProvider esSkuQueryProvider;

    // TODO: 2022/2/16 增加token验证，防止恶意下单
    // TODO: 2022/2/16 增加赠品标记

    /**
     * 商品查询
     */
    @PostMapping(value = "/goods/list")
    public BusinessResponse<List<GoodsListResVO>> goodsList(@RequestBody GoodsListReqVO param) {
        if (Objects.isNull(param)) {
            return BusinessResponse.error("参数错误");
        }
//        if (!OpenSubstanceSign.verify(param)) { // TODO: 2022/2/16
//            return BusinessResponse.error("签名错误");
//        }

        Integer pageNo = Objects.nonNull(param.getPage()) && Objects.nonNull(param.getPage().getPageNo()) ? param.getPage().getPageNo() : 1;
        Integer pageSize = Objects.nonNull(param.getPage()) && Objects.nonNull(param.getPage().getPageSize()) ? param.getPage().getPageSize() : 20;

        EsSkuPageRequest queryRequest = new EsSkuPageRequest();
        queryRequest.setGoodsInfoNos(Arrays.asList(param.getSkuNo()));
        queryRequest.setLikeGoodsName(param.getGoodsName());
        queryRequest.setPageNum(pageNo < 1 ? 1 : pageNo);
        queryRequest.setPageSize(pageSize > 100 ? 100 : pageSize);
//        queryRequest.setIsGift(true); //赠品标记 TODO: 2022/2/16

        //按创建时间倒序、ID升序
        queryRequest.putSort("addedTime", SortType.DESC.toValue());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
        queryRequest.setAuditStatus(CheckStatus.CHECKED);//已审核
        queryRequest.setVendibility(Constants.yes);
        queryRequest.setShowPointFlag(Boolean.TRUE);
        queryRequest.setShowProviderInfoFlag(Boolean.TRUE);
        queryRequest.setFillLmInfoFlag(Boolean.TRUE);
        EsSkuPageResponse response = esSkuQueryProvider.page(queryRequest).getContext();

        MicroServicePage<GoodsInfoVO> goodsInfoPage = response.getGoodsInfoPage();
        List<GoodsInfoVO> goodsInfoVOList = response.getGoodsInfoPage().getContent();

        Page retPage = new Page(goodsInfoPage.getNumber(), goodsInfoPage.getSize(), (int) goodsInfoPage.getTotal());
        if (CollectionUtils.isEmpty(goodsInfoVOList)) {
            return BusinessResponse.success(Lists.newArrayList(), retPage);
        }

        List<GoodsListResVO> result = goodsInfoVOList.stream().map(item -> {
            GoodsListResVO vo = new GoodsListResVO();
            vo.setSkuId(item.getGoodsInfoId());
            vo.setSkuNo(item.getGoodsInfoNo());
            vo.setSkuName(item.getGoodsInfoName());
            vo.setSalePrice(item.getSalePrice());
            vo.setCostPrice(item.getCostPrice());
            vo.setShelfStatus(item.getAddedFlag());
            return vo;
        }).collect(Collectors.toList());

        return BusinessResponse.success(result, retPage);
    }

    /**
     * 订单创建
     */
    @PostMapping(value = "/order/create")
    public BusinessResponse<OrderCreateResVO> orderCreate(@RequestBody List<OrderCreateReqVO> param) {
        if (Objects.isNull(param)) {
            return BusinessResponse.error("参数错误");
        }
        return BusinessResponse.success(null);
    }

    /**
     * 订单状态
     */
    @PostMapping(value = "/order/info")
    public BusinessResponse<OrderInfoResVO> orderInfo(@RequestBody OrderInfoReqVO param) {
        if (Objects.isNull(param)) {
            return BusinessResponse.error("参数错误");
        }
        return BusinessResponse.success(null);
    }
}
