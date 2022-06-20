package com.soybean.mall.goods.service;
import java.math.BigDecimal;

import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.common.RedisService;
import com.soybean.mall.goods.dto.SpuRecomBookListDTO;
import com.soybean.mall.goods.response.SpuNewBookListResp.BookList;
import com.soybean.mall.goods.response.SpuNewBookListResp.Book;
import com.soybean.mall.goods.response.SpuNewBookListResp.Atmosphere;

import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.mall.goods.response.SpuNewBookListResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByConditionResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.info.GoodsInfoListByGoodsInfoResponse;
import com.wanmi.sbc.setting.api.provider.AtmosphereProvider;
import com.wanmi.sbc.setting.api.request.AtmosphereQueryRequest;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description: sp
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 1:00 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class SpuNewSearchService {

    @Autowired
    private SpuNewBookListService spuNewBookListService;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private AtmosphereProvider atmosphereProvider;

    @Autowired
    private CommonUtil commonUtil;


    /**
     * 搜索商品书单信息
     * @param esSpuNewRespList
     * @return
     */
    public List<SpuNewBookListResp> listSpuNewSearch(List<EsSpuNewResp> esSpuNewRespList){
        return this.packageSpuNewBookListResp(esSpuNewRespList);
    }


    /**
     * 封装商品信息
     * @param esSpuNewRespList
     * @return
     */
    private List<SpuNewBookListResp> packageSpuNewBookListResp(List<EsSpuNewResp> esSpuNewRespList){
        if (CollectionUtils.isEmpty(esSpuNewRespList)) {
            return new ArrayList<>();
        }
        //获取商品对应的书单信息
        List<String> spuIdList = esSpuNewRespList.stream().map(EsSpuNewResp::getSpuId).collect(Collectors.toList());
        Map<String, SpuRecomBookListDTO> spuId2SpuRecomBookListDTOMap = spuNewBookListService.getSpuId2EsBookListModelResp(spuIdList);


        //获取客户信息
        CustomerGetByIdResponse customer = null;
        String userId = commonUtil.getOperatorId();
        if (!StringUtils.isEmpty(userId)) {
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(userId)).getContext();
        }

        //获取goodsInfo信息
//        BigDecimal salePrice = new BigDecimal("9999");
        Map<String, GoodsInfoVO> spuId2HasStockGoodsInfoVoMap = new HashMap<>();
        Map<String, GoodsInfoVO> spuId2UnHasStockGoodsInfoVoMap = new HashMap<>();

        GoodsInfoListByConditionRequest request = new GoodsInfoListByConditionRequest();
        request.setGoodsIds(spuIdList);
        request.setAuditStatus(CheckStatus.CHECKED);
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setAddedFlag(AddedFlag.YES.toValue());
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(request).getContext().getGoodsInfos();
        if (!CollectionUtils.isEmpty(goodsInfos)) {
            MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
            filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class));
            if (Objects.nonNull(customer)) {
                filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
            }
            goodsInfos = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();

            for (GoodsInfoVO goodsInfoParam : goodsInfos) {
                if (goodsInfoParam.getSalePrice() == null) {
                    goodsInfoParam.setSalePrice(goodsInfoParam.getMarketPrice());
                }
                 if (goodsInfoParam.getStock() <= RedisKeyConstant.GOODS_INFO_MIN_STOCK_SIZE) {
                     goodsInfoParam.setStock(0L); //设置库存为0
                 }

                 //存在库存 规格：如果有库存则取库存里面价格最低的商品，如果没有商品则获取 库存最低里面价格最低的商品 skuInfo 设置skuid
                 if (goodsInfoParam.getStock() > 0) {
                     GoodsInfoVO goodsInfoHasStock = spuId2HasStockGoodsInfoVoMap.get(goodsInfoParam.getGoodsId());
                     //如果map中没有，则添加到
                     if (goodsInfoHasStock == null) {
                         spuId2HasStockGoodsInfoVoMap.put(goodsInfoParam.getGoodsId(), goodsInfoParam);
                         spuId2UnHasStockGoodsInfoVoMap.remove(goodsInfoParam.getGoodsId()); //如果无库存中存在，则移除掉数据
                     } else {
                        if (goodsInfoParam.getSalePrice().compareTo(goodsInfoHasStock.getSalePrice()) < 0) {
                            spuId2HasStockGoodsInfoVoMap.put(goodsInfoParam.getGoodsId(), goodsInfoParam);
                            spuId2UnHasStockGoodsInfoVoMap.remove(goodsInfoParam.getGoodsId()); //如果无库存中存在，则移除掉数据
                        }
                     }
                 } else {
                    //如果有库存，则继续
                     GoodsInfoVO goodsInfoHasStock = spuId2HasStockGoodsInfoVoMap.get(goodsInfoParam.getGoodsId());
                     if (goodsInfoHasStock != null) {
                         continue;
                     }

                     GoodsInfoVO goodsInfoUnHasStock = spuId2UnHasStockGoodsInfoVoMap.get(goodsInfoParam.getGoodsId());
                     if (goodsInfoUnHasStock == null) {
                         spuId2UnHasStockGoodsInfoVoMap.put(goodsInfoParam.getGoodsId(), goodsInfoParam);
                     } else {
                         if (goodsInfoParam.getSalePrice().compareTo(goodsInfoUnHasStock.getSalePrice()) < 0) {
                             spuId2UnHasStockGoodsInfoVoMap.put(goodsInfoParam.getGoodsId(), goodsInfoParam);
                         }
                     }
                 }
            }
        }


        //获取氛围图信息 atmosphere
        Map<String, AtmosphereDTO> skuId2AtomsphereMap = new HashMap<>();
        if (!spuId2HasStockGoodsInfoVoMap.isEmpty() || !spuId2UnHasStockGoodsInfoVoMap.isEmpty()) {
            List<String> skuIdList = new ArrayList<>();
            skuIdList.addAll(spuId2HasStockGoodsInfoVoMap.values().stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()));
            skuIdList.addAll(spuId2UnHasStockGoodsInfoVoMap.values().stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()));
            AtmosphereQueryRequest atmosphereQueryRequest = new AtmosphereQueryRequest();
            atmosphereQueryRequest.setSkuId(skuIdList);
            List<AtmosphereDTO> context = atmosphereProvider.listAtmosphere(atmosphereQueryRequest).getContext();
            for (AtmosphereDTO atmosphereDTO : context) {
                skuId2AtomsphereMap.put(atmosphereDTO.getSkuId(), atmosphereDTO);
            }
        }



        List<SpuNewBookListResp> result = new ArrayList<>();

        for (EsSpuNewResp esSpuNewRespParam : esSpuNewRespList) {
            GoodsInfoVO goodsInfoVOTmp = spuId2HasStockGoodsInfoVoMap.get(esSpuNewRespParam.getSpuId());
            GoodsInfoVO goodsInfoVO = goodsInfoVOTmp != null ? goodsInfoVOTmp
                    : spuId2UnHasStockGoodsInfoVoMap.get(esSpuNewRespParam.getSpuId()) == null
                    ? null : spuId2UnHasStockGoodsInfoVoMap.get(esSpuNewRespParam.getSpuId());
            if (goodsInfoVO == null) {
                continue;
            }

            SpuNewBookListResp spuNewBookListResp = new SpuNewBookListResp();
            spuNewBookListResp.setSpuId(esSpuNewRespParam.getSpuId());
            spuNewBookListResp.setSkuId(goodsInfoVO.getGoodsInfoId());
            spuNewBookListResp.setSpuName(esSpuNewRespParam.getSpuName());
            spuNewBookListResp.setSpuSubName(esSpuNewRespParam.getSpuSubName());
            spuNewBookListResp.setSpuCategory(esSpuNewRespParam.getSpuCategory());

            if (esSpuNewRespParam.getBook() != null) {
                EsSpuNewResp.Book book = esSpuNewRespParam.getBook();
                Book resultBook = new Book();
                resultBook.setAuthorNames(book.getAuthorNames());
                resultBook.setScore(book.getScore());
                resultBook.setPublisher(book.getPublisher());
                resultBook.setFixPrice(book.getFixPrice());

                List<Book.BookTag> resultTag = new ArrayList<>();
                if (!CollectionUtils.isEmpty(book.getTags())) {
                    for (EsSpuNewResp.Book.SubBookLabel tagParam : book.getTags()) {
                        Book.BookTag resultBookTag = new Book.BookTag();
                        resultBookTag.setTageId(tagParam.getTagId());
                        resultBookTag.setTagName(tagParam.getTagName());
                        resultTag.add(resultBookTag);
                    }
                }
                resultBook.setTags(resultTag);
                spuNewBookListResp.setBook(resultBook);
            }

            //主播推荐标签
            List<EsSpuNewResp.SubAnchorRecom> anchorRecoms = esSpuNewRespParam.getAnchorRecoms();
            if (!CollectionUtils.isEmpty(anchorRecoms)) {
                //排序获取
                anchorRecoms.sort((o1, o2) -> o1.getRecomId() - o2.getRecomId());
                spuNewBookListResp.setAnchorRecomName(anchorRecoms.get(0).getRecomName());
            }

            //获取书单排行榜信息
            SpuRecomBookListDTO spuRecomBookListDTO = spuId2SpuRecomBookListDTOMap.get(esSpuNewRespParam.getSpuId());
            BookList bookList = spuRecomBookListDTO != null ?
                    new BookList(spuRecomBookListDTO.getBookListName(), spuRecomBookListDTO.getSpu() == null ? 1
                            : spuRecomBookListDTO.getSpu().getSortNum()) : null;
            spuNewBookListResp.setBookList(bookList);

            //获取图书信息
            AtmosphereDTO atmosphereDTO = skuId2AtomsphereMap.get(goodsInfoVO.getGoodsInfoImg());
            if (atmosphereDTO != null) {
                Atmosphere atmosphere = new Atmosphere();
                atmosphere.setImageUrl(atmosphereDTO.getImageUrl());
                atmosphere.setAtmosType(atmosphereDTO.getAtmosType());
                atmosphere.setElementOne(atmosphereDTO.getElementOne());
                atmosphere.setElementTwo(atmosphereDTO.getElementTwo());
                atmosphere.setElementThree(atmosphereDTO.getElementThree());
                atmosphere.setElementFour(atmosphereDTO.getElementFour());
                spuNewBookListResp.setAtmosphere(atmosphere);
            }


            spuNewBookListResp.setStock(goodsInfoVO.getStock());
            spuNewBookListResp.setSalesPrice(goodsInfoVO.getSalePrice());
            spuNewBookListResp.setMarketPrice(goodsInfoVO.getMarketPrice());
            spuNewBookListResp.setPic(esSpuNewRespParam.getPic());
            result.add(spuNewBookListResp);
        }
        return result;
    }

}
