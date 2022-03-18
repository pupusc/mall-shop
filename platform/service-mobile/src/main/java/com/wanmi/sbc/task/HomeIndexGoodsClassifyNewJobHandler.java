package com.wanmi.sbc.task;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsCustomQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.api.request.goods.SortCustomBuilder;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.request.classify.BookListModelClassifyLinkPageProviderRequest;
import com.wanmi.sbc.goods.api.request.classify.ClassifyCollectionProviderRequest;
import com.wanmi.sbc.goods.api.response.classify.BookListModelClassifyLinkProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.redis.RedisListService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.RedisKeyUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/10 2:56 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@JobHandler(value = "homeIndexGoodsClassifyNewJobHandler")
@Component
public class HomeIndexGoodsClassifyNewJobHandler  extends IJobHandler {

    @Autowired
    private ClassifyProvider classifyProvider;

    @Autowired
    private EsGoodsCustomQueryProvider esGoodsCustomQueryProvider;

    @Autowired
    private RedisListService redisService;

    @Autowired
    private RedisService redis;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        long beginTime = System.currentTimeMillis();
        log.info("HomeIndexGoodsClassifyNewJobHandler job beginTime ");
        List<String> channelNameList = new ArrayList<>();
        if (!StringUtils.isEmpty(param)) {
            Collections.addAll(channelNameList, param.split(","));
        }
        Long refreshCount = redis.incrKey(RedisKeyUtil.KEY_LIST_PREFIX_INDEX_REFRESH_COUNT);
        ClassifyCollectionProviderRequest classifyCollectionParent = new ClassifyCollectionProviderRequest();
        classifyCollectionParent.setParentIdColl(Collections.singleton(0));
        BaseResponse<List<ClassifyProviderResponse>> listParentBaseResponse = classifyProvider.listClassifyNoChildByParentId(classifyCollectionParent);
        for (ClassifyProviderResponse classifyProviderResponseParam : listParentBaseResponse.getContext()) {
            log.info("HomeIndexGoodsClassifyNewJobHandler execute classifyId: {} classifyName: {} begin",
                    classifyProviderResponseParam.getId(), classifyProviderResponseParam.getClassifyName());
            //获取当前分类下的所有子分类
            ClassifyCollectionProviderRequest classifyCollectionProviderRequest = new ClassifyCollectionProviderRequest();
            classifyCollectionProviderRequest.setParentIdColl(Collections.singleton(classifyProviderResponseParam.getId()));
            BaseResponse<List<ClassifyProviderResponse>> listBaseResponse = classifyProvider.listClassifyNoChildByParentId(classifyCollectionProviderRequest);
            if (CollectionUtils.isEmpty(listBaseResponse.getContext())) {
                log.info("HomeIndexGoodsClassifyNewJobHandler classifyId: {} child is empity", classifyProviderResponseParam.getId());
                continue;
            }
            Set<Integer> childClassifySet = listBaseResponse.getContext().stream().map(ClassifyProviderResponse::getId).collect(Collectors.toSet());
            classifyGoods(childClassifySet, classifyProviderResponseParam.getId(), refreshCount, channelNameList); //默认300
            classifyBookListModel(childClassifySet, classifyProviderResponseParam.getId(), refreshCount); //默认60
            log.info("HomeIndexGoodsClassifyNewJobHandler execute classifyId: {} classifyName: {} end",
                    classifyProviderResponseParam.getId(), classifyProviderResponseParam.getClassifyName());
        }
        if (refreshCount >= 1000) {
            redis.setString(RedisKeyUtil.KEY_LIST_PREFIX_INDEX_REFRESH_COUNT, "1");
        }
        log.info("HomeIndexGoodsClassifyNewJobHandler job end cost {}", System.currentTimeMillis() - beginTime);
        return SUCCESS;
    }

    /**
     * 获取分类下的商品列表
     * @param childClassifySet
     * @param classifyId
     */
    private void classifyGoods(Set<Integer> childClassifySet, Integer classifyId, Long refreshCount, List<String> channelNameList) {

        for (String channelName : channelNameList) {
            log.info("HomeIndexGoodsClassifyNewJobHandler job classifyGoods channelName:{} begin", channelName);
            //根据分类id 获取销量前300的商品列表
            EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
            esGoodsCustomRequest.setPageNum(0);
            esGoodsCustomRequest.setPageSize(300);
            esGoodsCustomRequest.setClassifyIdList(childClassifySet);
            List<SortCustomBuilder> sortBuilderList = new ArrayList<>();
            //按照销售数量排序
            sortBuilderList.add(new SortCustomBuilder("goodsSalesNum", SortOrder.DESC));
            esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
            esGoodsCustomRequest.setGoodsChannelTypeSet(Collections.singletonList(TerminalSource.getTerminalSource(channelName).getCode()));
            //获取分类下的商品列表
            BaseResponse<MicroServicePage<EsGoodsVO>> esGoodsVOMicroServiceResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
            MicroServicePage<EsGoodsVO> esGoodsVOMicroServicePage = esGoodsVOMicroServiceResponse.getContext();
            List<EsGoodsVO> content = esGoodsVOMicroServicePage.getContent();
            if (CollectionUtils.isEmpty(content)) {
                log.info("HomeIndexGoodsClassifyNewJobHandler classifyGoods channelName:{} content is empty", channelName);
                continue;
            }
            //简化goodsVo
            List<String> goodsIdList = content.stream().map(EsGoodsVO::getId).collect(Collectors.toList());
            Collections.shuffle(goodsIdList);
            String key = RedisKeyUtil.KEY_LIST_PREFIX_INDEX_CLASSIFY_GOODS + "_" + channelName + ":" + refreshCount + ":" + classifyId;
            redisService.putAllStr(key, goodsIdList, 40);
            log.info("HomeIndexGoodsClassifyNewJobHandler job classifyGoods channelName:{} complete", channelName);
        }
    }

    /**
     * 获取书单
     * @param childClassifySet
     * @param classifyId
     */
    private void classifyBookListModel(Set<Integer> childClassifySet, Integer classifyId, Long refreshCount) {
        //获取书单列表
        BookListModelClassifyLinkPageProviderRequest bookListModelClassifyLinkPageProviderRequest = new BookListModelClassifyLinkPageProviderRequest();
        bookListModelClassifyLinkPageProviderRequest.setClassifyIdColl(childClassifySet);
        bookListModelClassifyLinkPageProviderRequest.setBusinessTypeList(Arrays.asList(BusinessTypeEnum.BOOK_LIST.getCode(), BusinessTypeEnum.BOOK_RECOMMEND.getCode(), BusinessTypeEnum.FAMOUS_RECOMMEND.getCode()));
        bookListModelClassifyLinkPageProviderRequest.setPageNum(0);
        bookListModelClassifyLinkPageProviderRequest.setPageSize(60);  //当前是一共300个商品，5个商品随机一个 书单，则300 / 5 为最大60个书单随机
        BaseResponse<List<BookListModelClassifyLinkProviderResponse>> bookListModelClassifyLinkProviderResponses = classifyProvider.listBookListModelByClassifyIdColl(bookListModelClassifyLinkPageProviderRequest);
        List<BookListModelClassifyLinkProviderResponse> context = bookListModelClassifyLinkProviderResponses.getContext();
        if (CollectionUtils.isEmpty(context)) {
            return;
        }
        List<Integer> goodsIdList = context.stream().map(BookListModelClassifyLinkProviderResponse::getBookListModelId).collect(Collectors.toList());
        Collections.shuffle(goodsIdList);
        //context 已经乱序
        String key = RedisKeyUtil.KEY_LIST_PREFIX_INDEX_BOOK_LIST_MODEL + ":" + refreshCount + ":" + classifyId;

        redisService.putAll(key, goodsIdList, 40);
    }

}
