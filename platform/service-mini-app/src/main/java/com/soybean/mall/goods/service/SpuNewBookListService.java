package com.soybean.mall.goods.service;
import com.soybean.mall.goods.dto.SpuRecomBookListDTO.Spu;
import com.google.common.collect.Lists;
import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.enums.SearchBookListSortTypeEnum;

import com.soybean.elastic.api.provider.booklistmodel.EsBookListModelProvider;
import com.soybean.elastic.api.req.EsBookListQueryProviderReq;
import com.soybean.elastic.api.resp.EsBookListModelResp;
import com.soybean.mall.goods.dto.SpuRecomBookListDTO;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Description: 商品书单信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/10 2:35 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class SpuNewBookListService {

    @Autowired
    private EsBookListModelProvider esBookListModelProvider;

    /**
     * 获取商品对应的书单信息
     * @param spuIdList
     * @return
     */
    public Map<String, SpuRecomBookListDTO> getSpuId2EsBookListModelResp(List<String> spuIdList) {

        Map<String, SpuRecomBookListDTO> spuId2BookListMap = new HashMap<>();
        if (CollectionUtils.isEmpty(spuIdList)) {
            return spuId2BookListMap;
        }
        int pageSize = 100; //每次查询数量


        List<String> tmpSpuIdList = new ArrayList<>(spuIdList);
        //便利商品，极端情况下是每个商品都遍历一遍
        for (String spuId : spuIdList){
            //如果数据为空的时候，则直接break掉
            if (CollectionUtils.isEmpty(tmpSpuIdList)){
                break;
            }

            EsBookListQueryProviderReq req = new EsBookListQueryProviderReq();
            req.setSpuIds(spuIdList);
            req.setBooklistSortType(SearchBookListSortTypeEnum.HAS_TOP_UPDATE_TIME.getCode());
            req.setPageSize(pageSize); //每次获取一些书单、如果获取的为空，则代表没有书单信息
            CommonPageResp<List<EsBookListModelResp>> context = esBookListModelProvider.listEsBookListModel(req).getContext();
            if (context.getTotal() <= 0L) {
                break;
            }
            //获取书单列表
            for (EsBookListModelResp esBookListModelResp : context.getContent()) {
                //此处控制内存使用，分页遍历的方式
                //根据书单获取书单下的商品列表信息
                for (EsBookListModelResp.Spu spuTmp : esBookListModelResp.getSpus()) {
                    SpuRecomBookListDTO spuRecomBookListDTOTmp = spuId2BookListMap.get(spuTmp.getSpuId());
                    if (spuRecomBookListDTOTmp != null) {
                        continue;
                    }
                    //如果存在商品id，则存入到map中，然后从list中remove掉,下次请求剩余的商品
                    if (tmpSpuIdList.contains(spuTmp.getSpuId())) {
                        SpuRecomBookListDTO spuRecomBookListDTO = new SpuRecomBookListDTO();


                        Spu spu = new Spu();
                        spu.setSpuId(spuTmp.getSpuId());
                        spu.setSortNum(spuTmp.getSortNum());
                        spuRecomBookListDTO.setSpu(spu);

                        spuRecomBookListDTO.setBookListId(esBookListModelResp.getBookListId());
                        spuRecomBookListDTO.setBookListBusinessType(esBookListModelResp.getBookListBusinessType());
                        spuRecomBookListDTO.setBookListName(esBookListModelResp.getBookListName());
                        if (Objects.equals(esBookListModelResp.getBookListBusinessType(), BusinessTypeEnum.RANKING_LIST.getCode())) {
                            spuRecomBookListDTO.setBookListNameShow(String.format("%s排行榜第%d名", esBookListModelResp.getBookListName(), spuTmp.getSortNum()));
                        } else {
                            spuRecomBookListDTO.setBookListNameShow(String.format("收录在「%s」中", esBookListModelResp.getBookListName()));
                        }
                        spuId2BookListMap.put(spuTmp.getSpuId(), spuRecomBookListDTO);
                        tmpSpuIdList.remove(spuTmp.getSpuId());
                    }
                }
            }
        }
        return spuId2BookListMap;
    }

    /**
     * 获取商品对应的书单信息
     * @param spuIdList
     * @return
     */
    public Map<String, SpuRecomBookListDTO> getSpuId2EsBookListModelRespV2(List<String> spuIdList) {

        Map<String, SpuRecomBookListDTO> spuId2BookListMap = new HashMap<>();
        if (CollectionUtils.isEmpty(spuIdList)) {
            return spuId2BookListMap;
        }
        int pageSize = 100; //每次查询数量


        List<String> tmpSpuIdList = new ArrayList<>(spuIdList);
        //便利商品，极端情况下是每个商品都遍历一遍
        for (String spuId : spuIdList){
            //如果数据为空的时候，则直接break掉
            if (CollectionUtils.isEmpty(tmpSpuIdList)){
                break;
            }

            EsBookListQueryProviderReq req = new EsBookListQueryProviderReq();
            req.setSpuIds(spuIdList);
            req.setBooklistSortType(SearchBookListSortTypeEnum.HAS_TOP_UPDATE_TIME.getCode());
            req.setPageSize(pageSize); //每次获取一些书单、如果获取的为空，则代表没有书单信息
            CommonPageResp<List<EsBookListModelResp>> context = esBookListModelProvider.listEsBookListModel(req).getContext();
            if (context.getTotal() <= 0L) {
                break;
            }
            //获取书单列表
            for (EsBookListModelResp esBookListModelResp : context.getContent()) {
                //此处控制内存使用，分页遍历的方式
                //根据书单获取书单下的商品列表信息
                for (EsBookListModelResp.Spu spuTmp : esBookListModelResp.getSpus()) {
                    SpuRecomBookListDTO spuRecomBookListDTOTmp = spuId2BookListMap.get(spuTmp.getSpuId());
                    if (spuRecomBookListDTOTmp != null) {
                        continue;
                    }
                    //如果存在商品id，则存入到map中，然后从list中remove掉,下次请求剩余的商品
                    if (tmpSpuIdList.contains(spuTmp.getSpuId())) {
                        SpuRecomBookListDTO spuRecomBookListDTO = new SpuRecomBookListDTO();


                        Spu spu = new Spu();
                        spu.setSpuId(spuTmp.getSpuId());
                        spu.setSortNum(spuTmp.getSortNum());
                        spuRecomBookListDTO.setSpu(spu);

                        spuRecomBookListDTO.setBookListId(esBookListModelResp.getBookListId());
                        spuRecomBookListDTO.setBookListBusinessType(esBookListModelResp.getBookListBusinessType());
                        spuRecomBookListDTO.setBookListName(esBookListModelResp.getBookListName());
                        if (Objects.equals(esBookListModelResp.getBookListBusinessType(), BusinessTypeEnum.RANKING_LIST.getCode())) {
                            spuRecomBookListDTO.setBookListNameShow(String.format("%s排行榜第%d名", esBookListModelResp.getBookListName(), spuTmp.getSortNum()));
                        } else {
                            spuRecomBookListDTO.setBookListNameShow(String.format("收录在「%s」中", esBookListModelResp.getBookListName()));
                        }
                        spuId2BookListMap.put(spuTmp.getSpuId(), spuRecomBookListDTO);
                        tmpSpuIdList.remove(spuTmp.getSpuId());
                    }
                }
            }
        }
        return spuId2BookListMap;
    }


}
