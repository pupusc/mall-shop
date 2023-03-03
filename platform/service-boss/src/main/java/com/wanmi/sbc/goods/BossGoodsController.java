package com.wanmi.sbc.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.spu.EsSpuQueryProvider;
import com.wanmi.sbc.elastic.api.provider.standard.EsStandardProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInitProviderGoodsInfoRequest;
import com.wanmi.sbc.elastic.api.request.spu.EsSpuPageRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardInitRequest;
import com.wanmi.sbc.elastic.api.response.spu.EsSpuPageResponse;
import com.wanmi.sbc.goods.api.provider.ares.GoodsAresProvider;
import com.wanmi.sbc.goods.api.provider.common.RiskVerifyProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardImportProvider;
import com.wanmi.sbc.goods.api.request.SuspensionV2.SpuRequest;
import com.wanmi.sbc.goods.api.request.ares.DispatcherFunctionRequest;
import com.wanmi.sbc.goods.api.request.common.ImageVerifyRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsCheckRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsPageRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardGoodsGetUsedGoodsRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsByIdResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsCheckResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsPageResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardImportStandardRequest;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.bean.vo.GoodsSimpleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsPageSimpleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * S2B的BOSS商品服务
 * Created by daiyitian on 17/4/12.
 */
@RestController
@RequestMapping("/goods")
@Api(description = "S2B的BOSS商品服务", tags = "BossGoodsController")
public class BossGoodsController {

    @Autowired
    GoodsAresProvider goodsAresProvider;

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private StandardGoodsQueryProvider standardGoodsQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private StandardImportProvider standardImportProvider;

    @Autowired
    private EsSpuQueryProvider esSpuQueryProvider;

    @Autowired
    private EsStandardProvider esStandardProvider;

    @Autowired
    private RedisService redisService;

    public static int GOODS_SOURCE_PROVIDER = 0 ;

    @Autowired
    private RiskVerifyProvider riskVerifyProvider;


    /**
     * 审核/驳回商品
     *
     * @param checkRequest 审核参数
     * @return 商品详情
     */
    @ApiOperation(value = "审核/驳回商品")
    @RequestMapping(value = "/check", method = RequestMethod.PUT)
    @GlobalTransactional
    public BaseResponse check(@Valid @RequestBody GoodsCheckRequest checkRequest) {
        checkRequest.setChecker(commonUtil.getAccountName());
        GoodsCheckResponse response = goodsProvider.checkGoods(checkRequest).getContext();

        //操作日志记录
        GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
        goodsByIdRequest.setGoodsId(checkRequest.getGoodsIds().get(0));
        GoodsByIdResponse goods = goodsQueryProvider.getById(goodsByIdRequest).getContext();

        //初始化商家商品ES
        if (Integer.valueOf(GoodsSource.SELLER.toValue()).equals(goods.getGoodsSource())) {
            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().
                    goodsIds(checkRequest.getGoodsIds()).build());
        } else { //初始化供应商品ES
            esGoodsInfoElasticProvider.initProviderEsGoodsInfo(EsGoodsInitProviderGoodsInfoRequest.builder().
                    providerGoodsIds(checkRequest.getGoodsIds()).build());
        }

        //初始化标品库ES
        if(CollectionUtils.isNotEmpty(response.getStandardIds())){
            esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(response.getStandardIds()).build());
        }

        //删除标品库ES
        if(CollectionUtils.isNotEmpty(response.getDeleteStandardIds())){
            esStandardProvider.deleteByIds(EsStandardDeleteByIdsRequest.builder().goodsIds(response.getDeleteStandardIds()).build());
        }

        //ares埋点-商品-后台审核/驳回商品
        goodsAresProvider.dispatchFunction(new DispatcherFunctionRequest("editGoodsSpus", checkRequest.getGoodsIds().toArray()));

        if (checkRequest.getAuditStatus() == CheckStatus.CHECKED) {
            operateLogMQUtil.convertAndSend("商品", "审核商品",
                    "审核商品：SPU编码" + goods.getGoodsNo());
        } else if (checkRequest.getAuditStatus() == CheckStatus.NOT_PASS) {
            operateLogMQUtil.convertAndSend("商品", "驳回商品",
                    "驳回商品：SPU编码" + goods.getGoodsNo());
        }

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 禁售商品
     *
     * @param checkRequest 禁售参数
     * @return 商品详情
     */
    @ApiOperation(value = "禁售商品")
    @RequestMapping(value = "/forbid", method = RequestMethod.PUT)
    @GlobalTransactional
    public BaseResponse forbid(@Valid @RequestBody GoodsCheckRequest checkRequest) {
        checkRequest.setChecker(commonUtil.getAccountName());
        GoodsCheckResponse checkResponse = goodsProvider.checkGoods(checkRequest).getContext();
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().auditStatus(checkRequest.getAuditStatus()).goodsIds(checkRequest.getGoodsIds()).build());

        //更新redis商品基本数据
        String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + checkRequest.getGoodsIds());
        if (StringUtils.isNotBlank(goodsDetailInfo)) {
            redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + checkRequest.getGoodsIds());
        }

        //删除标品库ES
        if(CollectionUtils.isNotEmpty(checkResponse.getDeleteStandardIds())){
            esStandardProvider.deleteByIds(EsStandardDeleteByIdsRequest.builder().goodsIds(checkResponse.getDeleteStandardIds()).build());
        }

        //ares埋点-商品-后台禁售商品
        goodsAresProvider.dispatchFunction(new DispatcherFunctionRequest("editGoodsSpus", checkRequest.getGoodsIds().toArray()));

        //操作日志记录
        GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
        goodsByIdRequest.setGoodsId(checkRequest.getGoodsIds().get(0));
        GoodsByIdResponse response = goodsQueryProvider.getById(goodsByIdRequest).getContext();
        operateLogMQUtil.convertAndSend("商品", "禁售商品",
                "禁售商品：SPU编码" + response.getGoodsNo());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询商品
     *
     * @param pageRequest 商品 {@link GoodsPageRequest}
     * @return 商品详情
     */
    @ApiOperation(value = "查询商品")
    @RequestMapping(value = "/spus", method = RequestMethod.POST)
    public BaseResponse<EsSpuPageResponse> list(@RequestBody EsSpuPageRequest pageRequest) {
        pageRequest.setDelFlag(DeleteFlag.NO.toValue());
        //按创建时间倒序、ID升序
//        if(Objects.nonNull(pageRequest.getGoodsSortType()) && StringUtils.isNotBlank(pageRequest.getSortRole())) {
//            switch (pageRequest.getGoodsSortType()) {
//                case MARKET_PRICE:pageRequest.putSort("skuMinMarketPrice", pageRequest.getSortRole());break;
//                case STOCK:pageRequest.putSort("stock", pageRequest.getSortRole());break;
//                case SALES_NUM:pageRequest.putSort("goodsSalesNum", pageRequest.getSortRole());break;
//                default:pageRequest.putSort("createTime", SortType.DESC.toValue());break;//按创建时间倒序、ID升序
//            }
//        }else{
//            pageRequest.putSort("createTime", SortType.DESC.toValue());
//        }
        pageRequest.setShowVendibilityFlag(Boolean.TRUE);//显示可售性
        EsSpuPageRequest request = new EsSpuPageRequest();
        KsBeanUtil.copyPropertiesThird(pageRequest, request);
        BaseResponse<EsSpuPageResponse> pageResponse = esSpuQueryProvider.page(request);
//        BaseResponse<GoodsPageResponse> pageResponse = goodsQueryProvider.page(pageRequest);
        EsSpuPageResponse goodsPageResponse = pageResponse.getContext();
        List<GoodsPageSimpleVO> goodses = goodsPageResponse.getGoodsPage().getContent();
        if (CollectionUtils.isNotEmpty(goodses)) {
            //列出已导入商品库的商品编号
            StandardGoodsGetUsedGoodsRequest standardGoodsGetUsedGoodsRequest = new StandardGoodsGetUsedGoodsRequest();
            standardGoodsGetUsedGoodsRequest.setGoodsIds(goodses.stream().filter(g-> Objects.nonNull(g) && Objects.nonNull(g.getGoodsId())).map(GoodsPageSimpleVO::getGoodsId).collect(Collectors.toList()));
            if(CollectionUtils.isNotEmpty(standardGoodsGetUsedGoodsRequest.getGoodsIds())) {
                goodsPageResponse.setImportStandard(standardGoodsQueryProvider.getUsedGoods(standardGoodsGetUsedGoodsRequest).getContext().getGoodsIds());
            }
        }
        return pageResponse;
    }


    /**
     * 查询商品
     *
     * @param pageRequest 商品 {@link GoodsPageRequest}
     * @return 商品详情
     */
    @ApiOperation(value = "查询商品")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<List<GoodsSimpleVO>> page(@RequestBody GoodsPageRequest pageRequest) {
        pageRequest.setDelFlag(DeleteFlag.NO.toValue());
        pageRequest.setAddedFlag(AddedFlag.YES.toValue());
        pageRequest.setAuditStatus(CheckStatus.CHECKED);
        //按创建时间倒序、ID升序
        pageRequest.putSort("createTime", SortType.DESC.toValue());
        BaseResponse<GoodsPageResponse> pageResponse = goodsQueryProvider.page(pageRequest);
        List<GoodsSimpleVO> list = null;
        GoodsPageResponse goodsPageResponse = pageResponse.getContext();
        List<GoodsVO> goodses = goodsPageResponse.getGoodsPage().getContent();
        if (CollectionUtils.isNotEmpty(goodses)) {
            list = KsBeanUtil.convert(goodses, GoodsSimpleVO.class);
        }
        return BaseResponse.success(ListUtils.emptyIfNull(list));
    }

    /**
     * 加入商品库
     *
     * @param request 导入参数
     * @return 成功结果
     */
    @ApiOperation(value = "加入商品库")
    @RequestMapping(value = "/standard", method = RequestMethod.POST)
    public BaseResponse importGoods(@RequestBody StandardImportStandardRequest request) {
        if (CollectionUtils.isEmpty(request.getGoodsIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<String> standardIds = standardImportProvider.importStandard(request).getContext().getStandardIds();
        //初始化商品库ES
        if(CollectionUtils.isNotEmpty(standardIds)){
            esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(standardIds).build());
        }

        //操作日志记录
        GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
        goodsByIdRequest.setGoodsId(request.getGoodsIds().get(0));
        GoodsByIdResponse response = goodsQueryProvider.getById(goodsByIdRequest).getContext();
        operateLogMQUtil.convertAndSend("商品", "加入商品库",
                "加入商品库：SPU编码" + response.getGoodsNo());

        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "同盾审核")
    @RequestMapping(value = "/image/verify/callback", method = RequestMethod.POST)
    public BaseResponse verifyCallBack(@RequestBody  ImageVerifyRequest imageVerifyRequest){
        return  riskVerifyProvider.verifyImageCallBack(imageVerifyRequest);
    }

    @RequestMapping(value = "/refreshBook", method = RequestMethod.POST)
    public BaseResponse refreshBook(@RequestBody SpuRequest spuRequest){
        return riskVerifyProvider.refreshBook(spuRequest);
    }


}
