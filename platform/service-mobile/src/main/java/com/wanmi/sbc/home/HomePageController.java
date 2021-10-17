package com.wanmi.sbc.home;

import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsCustomQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.api.request.goods.SortCustomBuilder;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.home.request.HomeNewBookRequest;
import com.wanmi.sbc.util.RandomUtil;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/18 1:26 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/mobile/home")
@RestController
public class HomePageController {


    @Autowired
    private EsGoodsCustomQueryProvider esGoodsCustomQueryProvider;

    /**
     * 获取新上书籍
     * @param homeNewBookRequest
     * @return
     */
    public BaseResponse<List<GoodsCustomResponse>> newBookList(@RequestBody HomeNewBookRequest homeNewBookRequest){

        //根据书单模版获取商品列表
        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
        esGoodsCustomRequest.setPageNum(0);
        esGoodsCustomRequest.setPageSize(200);
        List<SortCustomBuilder> sortBuilderList = new ArrayList<>();
        //按照销售数量排序
        sortBuilderList.add(new SortCustomBuilder("updateTime", SortOrder.DESC));
        esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
        BaseResponse<MicroServicePage<EsGoodsVO>> esGoodsVOMicroServiceResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
        MicroServicePage<EsGoodsVO> esGoodsVOMicroServicePage = esGoodsVOMicroServiceResponse.getContext();
        List<EsGoodsVO> content = esGoodsVOMicroServicePage.getContent();
        if (CollectionUtils.isEmpty(content)) {
            return null;
        }

        //获取随机书籍
        Collection<Integer> randomIndex = RandomUtil.getRandom(content.size(), homeNewBookRequest.getPageSize());
        List<EsGoodsVO> result = new ArrayList<>();
        for (Integer index : randomIndex) {
            result.add(content.get(index));
        }

        //书籍存入到redis中

        return null;
    }
}
