package com.soybean.mall.goods.service;

import com.soybean.elastic.api.resp.EsBookListModelResp;
import com.soybean.mall.goods.response.BookListSpuResp;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Description: 获取书单业务信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 10:51 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class BookListSearchService {


    /**
     * 过滤spu
     * @param bookList2SpuMap
     * @param spuNum
     * @return
     */
    private Map<Integer, List<EsBookListModelResp.Spu>> filterSpu(Map<Integer, Set<EsBookListModelResp.Spu>> bookList2SpuMap, int spuNum) {

        Map<Integer, List<EsBookListModelResp.Spu>> result = new HashMap<>();

        //获取商品id集合
        for (Map.Entry<Integer, Set<EsBookListModelResp.Spu>> integerSetEntry : bookList2SpuMap.entrySet()) {
            int index = 0;
            List<EsBookListModelResp.Spu> spuList = new ArrayList<>();
            for (EsBookListModelResp.Spu spu : integerSetEntry.getValue()) {
                if (index < spuNum) {
                    spuList.add(spu);
                } else {
                    break;
                }
                index ++;
            }

            result.put(integerSetEntry.getKey(), spuList);
        }
        return result;
    }

    /**
     * 搜索书单商品信息
     * @param esBookListModelRespList
     * @param spuNum
     * @return
     */
    public List<BookListSpuResp> listBookListSearch(List<EsBookListModelResp> esBookListModelRespList, int spuNum) {
        Map<Integer, Set<EsBookListModelResp.Spu>> bookList2SpuMap = new HashMap<>();
        for (EsBookListModelResp esBookListModelParam : esBookListModelRespList) {
            if (CollectionUtils.isEmpty(esBookListModelParam.getSpus())) {
                continue;
            }

            Set<EsBookListModelResp.Spu> spuSet = new TreeSet<>(new Comparator<EsBookListModelResp.Spu>() {
                @Override
                public int compare(EsBookListModelResp.Spu o1, EsBookListModelResp.Spu o2) {
                    return o1.getSortNum() - o2.getSortNum();
                }
            });
            spuSet.addAll(esBookListModelParam.getSpus());
            bookList2SpuMap.put(esBookListModelParam.getBookListId().intValue(), spuSet);
        }

        //获取书单商品信息
        return this.packBookListSpuResp(esBookListModelRespList, this.filterSpu(bookList2SpuMap, spuNum));
    }

    /**
     * 打包书单信息
     * @param esBookListModelRespList
     * @return
     */
    private List<BookListSpuResp> packBookListSpuResp(List<EsBookListModelResp> esBookListModelRespList, Map<Integer, List<EsBookListModelResp.Spu>> bookListId2SpuMap) {
        List<BookListSpuResp> result = new ArrayList<>();
        for (EsBookListModelResp esBookListModelResp : esBookListModelRespList) {
            BookListSpuResp bookListSpuResp = new BookListSpuResp();
            BeanUtils.copyProperties(esBookListModelResp, bookListSpuResp);
//            bookListSpuResp.setBookListId(esBookListModelResp.getBookListId());
//            bookListSpuResp.setBookListBusinessType(esBookListModelResp.getBookListBusinessType());
//            bookListSpuResp.setBookListName(esBookListModelResp.getBookListName());
//            bookListSpuResp.setBookListDesc(esBookListModelResp.getBookListDesc());

            List<BookListSpuResp.Spu> spuList = new ArrayList<>();
            List<EsBookListModelResp.Spu> esBookListSpuList = bookListId2SpuMap.getOrDefault(esBookListModelResp.getBookListId().intValue(), new ArrayList<>());
            for (EsBookListModelResp.Spu spuParam : esBookListSpuList) {
                BookListSpuResp.Spu spu = new BookListSpuResp.Spu();
                BeanUtils.copyProperties(spuParam, spu);
//                spu.setSpuId(spuParam.getSpuId());
//                spu.setPic(spuParam.getPic());
//                spu.setUnBackgroundPic(spuParam.getUnBackgroundPic());
//                spu.setSortNum(spuParam.getSortNum());
//                spuList.add(spu);
            }
            bookListSpuResp.setSpus(spuList);
            result.add(bookListSpuResp);
        }
        return result;
    }
}
