package com.soybean.mall.goods.service;

import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.req.EsSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.goods.response.SpuNewBookListResp;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Description: 查询Es信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/13 5:54 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class EsSpuNewSearchService {

    @Autowired
    private EsSpuNewProvider esSpuNewProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CustomerProvider customerProvider;

    @Autowired
    private SpuComponentService spuComponentService;

    @Autowired
    private SpuNewSearchService spuNewSearchService;

    @Autowired
    private CommonUtil commonUtil;


    /**
     * 内部查询spu信息
     * @param esSpuNewQueryProviderReq
     * @param filterBlackList
     * @param filterCpsSpecial
     * @return
     */
    public CommonPageResp<List<EsSpuNewResp>> listNormalEsSpuNew(EsSpuNewQueryProviderReq esSpuNewQueryProviderReq, boolean filterBlackList, boolean filterCpsSpecial) {
        esSpuNewQueryProviderReq.setChannelTypes(Collections.singletonList(commonUtil.getTerminal().getCode()));
        if (filterBlackList){
            //获取搜索黑名单
            List<String> unSpuIds = spuComponentService.listSearchBlackList(
                    Arrays.asList(GoodsBlackListCategoryEnum.GOODS_SESRCH_H5_AT_INDEX.getCode(), GoodsBlackListCategoryEnum.GOODS_SESRCH_AT_INDEX.getCode()));
            if (!CollectionUtils.isEmpty(esSpuNewQueryProviderReq.getUnSpuIds())) {
                esSpuNewQueryProviderReq.getUnSpuIds().addAll(unSpuIds);
            } else {
                esSpuNewQueryProviderReq.setUnSpuIds(unSpuIds);
            }
        }
        //获取客户信息
        CustomerGetByIdResponse customer = null;
        if (filterCpsSpecial) {
            //获取是否知识顾问用户
            String userId = commonUtil.getOperatorId();
            if (!StringUtils.isEmpty(userId)) {
                customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(userId)).getContext();
                String isCounselor = customerProvider.isCounselorCache(Integer.valueOf(customer.getFanDengUserNo())).getContext();
                //非知识顾问用户
                if (!Objects.isNull(isCounselor) && "true".equals(isCounselor)) {
                    esSpuNewQueryProviderReq.setCpsSpecial(1);// 表示知识顾问，显示所有商品
                }
            }
        }
        return esSpuNewProvider.listNormalEsSpuNew(esSpuNewQueryProviderReq).getContext();
    }


    /**
     * 默认内部搜索、包含搜索黑名单、知识顾问过滤
     * @param esSpuNewQueryProviderReq
     * @return
     */
    public CommonPageResp<List<EsSpuNewResp>> listNormalEsSpuNew(EsSpuNewQueryProviderReq esSpuNewQueryProviderReq) {
        return this.listNormalEsSpuNew(esSpuNewQueryProviderReq, true, true);
    }

}
