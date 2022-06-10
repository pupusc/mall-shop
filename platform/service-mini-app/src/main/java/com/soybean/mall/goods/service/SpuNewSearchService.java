package com.soybean.mall.goods.service;

import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.mall.goods.response.SpuNewBookListResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private BookListSearchService bookListSearchService;

    /**
     * 搜索商品书单信息
     * @param esSpuNewRespList
     * @return
     */
    public List<SpuNewBookListResp> listSpuNewSearch(List<EsSpuNewResp> esSpuNewRespList){

        //1、获取商品信息

        //2、获取书单信息

        return new ArrayList<>();
    }

    private List<SpuNewBookListResp> packageSpuNewBookListResp(){
        return null;
    }

}
