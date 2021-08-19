package com.wanmi.sbc.standard;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.OsUtil;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.standard.EsStandardProvider;
import com.wanmi.sbc.elastic.api.provider.standard.EsStandardQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardInitRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardPageRequest;
import com.wanmi.sbc.elastic.api.response.standard.EsStandardPageResponse;
import com.wanmi.sbc.goods.api.provider.ares.GoodsAresProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardExcelProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardGoodsProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.ares.DispatcherFunctionRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsCheckRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.standard.*;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListByGoodsRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsByIdResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsCheckResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardGoodsByIdResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardGoodsPageResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateListByGoodsResponse;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.bean.vo.StoreCateGoodsRelaVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.standard.request.StandardExcelImplGoodsRequest;
import com.wanmi.sbc.standard.service.StandardExcelService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品库服务
 * Created by daiyitian on 17/4/12.
 */
@RestController
@RequestMapping("/standard")
@Api(description = "商品库服务",tags ="StandardController")
@Slf4j
public class StandardController {

    @Autowired
    private StandardGoodsQueryProvider standardGoodsQueryProvider;

    @Autowired
    private StandardGoodsProvider standardGoodsProvider;

    @Autowired
    private StandardExcelProvider standardExcelProvider;

    @Autowired
    private StandardExcelService standardExcelService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private GoodsAresProvider goodsAresProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private OsUtil osUtil;

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    private EsStandardQueryProvider esStandardQueryProvider;

    @Autowired
    private EsStandardProvider esStandardProvider;

    @Autowired
    private RedisService redisService;

    /**
     * 查询商品
     *
     * @param pageRequest 商品 {@link StandardGoodsPageRequest}
     * @return 商品详情
     */
    @ApiOperation(value = "获取商品库商品分类详情信息")
    @RequestMapping(value = "/spus", method = RequestMethod.POST)
    public BaseResponse<EsStandardPageResponse> list(@RequestBody EsStandardPageRequest pageRequest) {
        pageRequest.setDelFlag(DeleteFlag.NO.toValue());
        //按创建时间倒序、ID升序
        pageRequest.putSort("createTime", SortType.DESC.toValue());
        return esStandardQueryProvider.page(pageRequest);
    }

    /**
     * @param addRequest 商品添加信息 {@link StandardGoodsAddRequest}
     * @return
     */
    @ApiOperation(value = "商品库商品添加信息")
    @RequestMapping(value = "/spu", method = RequestMethod.POST)
    public BaseResponse<String> add(@Valid @RequestBody StandardGoodsAddRequest addRequest) {

        //操作日志记录
        operateLogMQUtil.convertAndSend("商品", "新增商品库商品",
                "新增商品库商品：" + addRequest.getGoods().getGoodsName());
        BaseResponse<String> response = standardGoodsProvider.add(addRequest);

        //初始化ES
        if(StringUtils.isNotBlank(response.getContext())) {
            esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(Collections.singletonList(response.getContext())).build());
        }
        return response;
    }

    /**
     * @param modifyRequest 商品修改信息 {@link StandardGoodsModifyRequest}
     * @return
     */
    @ApiOperation(value = "商品库商品修改信息")
    @RequestMapping(value = "/spu", method = RequestMethod.PUT)
    public BaseResponse edit(@Valid @RequestBody StandardGoodsModifyRequest modifyRequest) {

        if (modifyRequest.getGoods().getGoodsId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        //操作日志记录
        operateLogMQUtil.convertAndSend("商品", "编辑商品",
                "编辑商品：" + modifyRequest.getGoods().getGoodsName());

        BaseResponse response = standardGoodsProvider.modify(modifyRequest);
        esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(Collections.singletonList(modifyRequest.getGoods().getGoodsId())).build());
        return response;
    }

    /**
     * 获取商品库详情信息
     *
     * @param goodsId 商品库编号 {@link String}
     * @return 商品库详情
     */
    @ApiOperation(value = "获取商品库详情信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "goodsId",
            value = "商品库商品Id", required = true)
    @RequestMapping(value = "/spu/{goodsId}", method = RequestMethod.GET)
    public BaseResponse<StandardGoodsByIdResponse> info(@PathVariable String goodsId) {
        StandardGoodsByIdRequest standardGoodsByIdRequest = new StandardGoodsByIdRequest();
        standardGoodsByIdRequest.setGoodsId(goodsId);
        BaseResponse<StandardGoodsByIdResponse> response =  standardGoodsQueryProvider.getById(standardGoodsByIdRequest);
        //获取商品店铺分类
        if (osUtil.isS2b()) {
            goodsId = response.getContext().getGoods().getProviderGoodsId();
            if(goodsId !=null){
                StoreCateListByGoodsRequest storeCateListByGoodsRequest = new StoreCateListByGoodsRequest(Collections.singletonList(goodsId));
                BaseResponse<StoreCateListByGoodsResponse> baseResponse = storeCateQueryProvider.listByGoods(storeCateListByGoodsRequest);
                StoreCateListByGoodsResponse storeCateListByGoodsResponse = baseResponse.getContext();
                if (Objects.nonNull(storeCateListByGoodsResponse)) {
                    List<StoreCateGoodsRelaVO> storeCateGoodsRelaVOList = storeCateListByGoodsResponse.getStoreCateGoodsRelaVOList();
                    response.getContext().getGoods().setStoreCateIds(storeCateGoodsRelaVOList.stream()
                            .filter(rela -> rela.getStoreCateId() != null)
                            .map(StoreCateGoodsRelaVO::getStoreCateId)
                            .collect(Collectors.toList()));
                }
            }
        }
        return response;
    }

    /**
     * 删除商品库
     *
     * @param deleteByGoodsIdsRequest 商品id封装 {@link StandardGoodsDeleteByGoodsIdsRequest}
     * @return
     */
    @ApiOperation(value = "删除商品库商品")
    @RequestMapping(value = "/spu", method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody StandardGoodsDeleteByGoodsIdsRequest deleteByGoodsIdsRequest) {
        if (CollectionUtils.isEmpty(deleteByGoodsIdsRequest.getGoodsIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StandardGoodsByIdRequest request = new StandardGoodsByIdRequest();
        request.setGoodsId(deleteByGoodsIdsRequest.getGoodsIds().get(0));
        BaseResponse<StandardGoodsByIdResponse> baseResponse = standardGoodsQueryProvider.getById(request);
        StandardGoodsByIdResponse response = baseResponse.getContext();
        if (Objects.nonNull(response)) {
            //操作日志记录
            operateLogMQUtil.convertAndSend("商品", "删除商品", "删除商品：SPU编码" + response.getGoods().getGoodsName());
        }
        standardGoodsProvider.delete(deleteByGoodsIdsRequest);
        return esStandardProvider.deleteByIds(EsStandardDeleteByIdsRequest.builder().goodsIds(deleteByGoodsIdsRequest.getGoodsIds()).build());
    }



    /**
     * 删除商品库
     *
     * @param deleteByGoodsIdsRequest 商品id封装 {@link StandardGoodsDeleteProviderByGoodsIdsRequest}
     * @return
     */
    @ApiOperation(value = "删除供应商商品库商品")
    @RequestMapping(value = "/spu/provider", method = RequestMethod.DELETE)
    public BaseResponse deleteProvider(@RequestBody StandardGoodsDeleteProviderByGoodsIdsRequest deleteByGoodsIdsRequest) {
        if (CollectionUtils.isEmpty(deleteByGoodsIdsRequest.getGoodsIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StandardGoodsByIdRequest request = new StandardGoodsByIdRequest();
        request.setGoodsId(deleteByGoodsIdsRequest.getGoodsIds().get(0));
        BaseResponse<StandardGoodsByIdResponse> baseResponse = standardGoodsQueryProvider.getById(request);
        StandardGoodsByIdResponse response = baseResponse.getContext();
        if (Objects.nonNull(response)) {
            //操作日志记录
            operateLogMQUtil.convertAndSend("商品", "删除商品", "删除商品：SPU编码" + response.getGoods().getGoodsName());
        }

        standardGoodsProvider.deleteProvider(deleteByGoodsIdsRequest);
        return esStandardProvider.deleteByIds(EsStandardDeleteByIdsRequest.builder().goodsIds(deleteByGoodsIdsRequest.getGoodsIds()).build());
    }

    /**
     * 删除商品库
     *
     * @param deleteByGoodsIdsRequest 商品id封装 {@link StandardGoodsDeleteProviderByGoodsIdsRequest}
     * @return
     */
    @ApiOperation(value = "删除供应商商品库商品")
    @RequestMapping(value = "/spu/provider/addReason", method = RequestMethod.DELETE)
    public BaseResponse deleteProviderAddReason(@RequestBody StandardGoodsDeleteProviderByGoodsIdsRequest deleteByGoodsIdsRequest) {
        if (CollectionUtils.isEmpty(deleteByGoodsIdsRequest.getGoodsIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StandardGoodsByIdRequest request = new StandardGoodsByIdRequest();
        request.setGoodsId(deleteByGoodsIdsRequest.getGoodsIds().get(0));
        BaseResponse<StandardGoodsByIdResponse> baseResponse = standardGoodsQueryProvider.getById(request);
        StandardGoodsByIdResponse response = baseResponse.getContext();
        if (Objects.nonNull(response)) {
            //操作日志记录
            operateLogMQUtil.convertAndSend("商品", "删除商品", "删除商品：SPU编码" + response.getGoods().getGoodsName());
        }
        List<String> supplierGoodsIds = standardGoodsProvider.deleteProviderAddReason(deleteByGoodsIdsRequest).getContext().getGoodsIds();
        if(CollectionUtils.isNotEmpty(supplierGoodsIds)){
            esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().deleteIds(supplierGoodsIds).build());

            supplierGoodsIds.forEach(goodsId->{
                //更新redis商品基本数据
                String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId);
                if (StringUtils.isNotBlank(goodsDetailInfo)) {
                    redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId);
                }

            });
        }
        esStandardProvider.deleteByIds(EsStandardDeleteByIdsRequest.builder().goodsIds(deleteByGoodsIdsRequest.getGoodsIds()).build());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 禁售商品库
     *
     * @param deleteByGoodsIdsRequest 商品id封装 {@link StandardGoodsDeleteProviderByGoodsIdsRequest}
     * @return
     */
    @ApiOperation(value = "禁售供应商或linkedmall商品库商品")
    @RequestMapping(value = "/spu/provider/forbid", method = RequestMethod.DELETE)
    public BaseResponse forbidProviderAddReason(@RequestBody StandardGoodsDeleteProviderByGoodsIdsRequest deleteByGoodsIdsRequest) {

        if (CollectionUtils.isEmpty(deleteByGoodsIdsRequest.getGoodsIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        StandardGoodsByIdRequest request = new StandardGoodsByIdRequest();
        request.setGoodsId(deleteByGoodsIdsRequest.getGoodsIds().get(0));
        BaseResponse<List<String>> baseResponse = standardGoodsQueryProvider.getGoodsIdByStandardId(request);
        if(CollectionUtils.isEmpty(baseResponse.getContext())){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        GoodsListByIdsResponse goodsListByIdsResponse = goodsQueryProvider.listByIds(GoodsListByIdsRequest.builder().goodsIds(baseResponse.getContext()).build()).getContext();
        if(goodsListByIdsResponse!=null && CollectionUtils.isNotEmpty(goodsListByIdsResponse.getGoodsVOList())){
            List<GoodsVO> goods = goodsListByIdsResponse.getGoodsVOList();
            List<String> providerGoodsIdList = goods.stream().filter(goodsVO -> goodsVO.getGoodsSource() == GoodsSource.PROVIDER.toValue()).map(GoodsVO::getGoodsId).collect(Collectors.toList());
            List<String> linkedMallGoodsIdList = goods.stream().filter(goodsVO -> goodsVO.getGoodsSource() == GoodsSource.LINKED_MALL.toValue()).map(GoodsVO::getGoodsId).collect(Collectors.toList());
            List<String> storeGoodsIdList = goods.stream().filter(goodsVO -> goodsVO.getGoodsSource() == GoodsSource.SELLER.toValue()).map(GoodsVO::getGoodsId).collect(Collectors.toList());
            log.info("供应商商品>>>{}linkedmall商品>>>{}商家商品>>>{}",JSON.toJSONString(providerGoodsIdList),JSON.toJSON(storeGoodsIdList));
            GoodsCheckRequest checkRequest = new GoodsCheckRequest();
            checkRequest.setGoodsIds(providerGoodsIdList.size()>0?providerGoodsIdList:linkedMallGoodsIdList);
            checkRequest.setAuditStatus(CheckStatus.FORBADE);
            checkRequest.setChecker(commonUtil.getAccountName());
            checkRequest.setAuditReason(deleteByGoodsIdsRequest.getDeleteReason());
            checkRequest.setDealStandardGoodsFlag(Boolean.TRUE);
            GoodsCheckResponse checkResponse = goodsProvider.checkGoods(checkRequest).getContext();


            //初始化供应商、linkedMall、商家商品
            if(CollectionUtils.isNotEmpty(baseResponse.getContext())){
                esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsIds(baseResponse.getContext()).build());
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

        }
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 下载模板
     */
    @ApiOperation(value = "下载商品库模板")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted",
            value = "加密", required = true)
    @RequestMapping(value = "/excel/template/{encrypted}", method = RequestMethod.GET)
    public void template(@PathVariable String encrypted) {
        String file = standardExcelProvider.exportTemplate().getContext().getFile();
        if (StringUtils.isNotBlank(file)) {
            try {
                String fileName = URLEncoder.encode("商品库导入模板.xls", "UTF-8");
                HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";" +
                        "filename*=\"utf-8''%s\"", fileName, fileName));
                HttpUtil.getResponse().getOutputStream().write(new BASE64Decoder().decodeBuffer(file));
            } catch (Exception e) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
        }
    }

    /**
     * 确认导入商品
     *
     * @param ext 文件格式 {@link String}
     * @return
     */
    @ApiOperation(value = "确认导入商品库商品")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "ext",
            value = "文件名后缀", required = true)
    @RequestMapping(value = "/import/{ext}", method = RequestMethod.GET)
    public BaseResponse<Boolean> implGoods(@PathVariable String ext) {
        if (!("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        StandardExcelImplGoodsRequest standardExcelImplGoodsRequest = new StandardExcelImplGoodsRequest();
        standardExcelImplGoodsRequest.setExt(ext);
        standardExcelImplGoodsRequest.setUserId(commonUtil.getOperatorId());
        List<String> ids = standardExcelService.implGoods(standardExcelImplGoodsRequest);

        if (CollectionUtils.isNotEmpty(ids)) {
            esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(ids).build());
        }
        //操作日志记录
        operateLogMQUtil.convertAndSend("商品", "批量导入", "批量导入");
        return BaseResponse.success(Boolean.TRUE);
    }
}
