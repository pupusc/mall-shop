package com.wanmi.sbc.goods.provider.impl.appointmentsalegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.appointmentsalegoods.AppointmentSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentGoodsInfoSimplePageRequest;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleQueryRequest;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsPageRequest;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsQueryRequest;
import com.wanmi.sbc.goods.api.response.appointmentsalegoods.*;
import com.wanmi.sbc.goods.appointmentsale.model.root.AppointmentSale;
import com.wanmi.sbc.goods.appointmentsale.service.AppointmentSaleService;
import com.wanmi.sbc.goods.appointmentsalegoods.model.root.AppointmentSaleGoods;
import com.wanmi.sbc.goods.appointmentsalegoods.service.AppointmentGoodsInfoSimpleCriterIaBuilder;
import com.wanmi.sbc.goods.appointmentsalegoods.service.AppointmentSaleGoodsService;
import com.wanmi.sbc.goods.bean.dto.AppointmentGoodsInfoSimplePageDTO;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.AppointmentVO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleGoodsVO;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.spec.service.GoodsInfoSpecDetailRelService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>预约抢购查询服务接口实现</p>
 *
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@RestController
@Validated
public class AppointmentSaleGoodsQueryController implements AppointmentSaleGoodsQueryProvider {
    @Autowired
    private AppointmentSaleGoodsService appointmentSaleGoodsService;

    @Autowired
    private AppointmentSaleService appointmentSaleService;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsInfoSpecDetailRelService goodsInfoSpecDetailRelService;

    @Override
    public BaseResponse<AppointmentSaleGoodsPageResponse> page(@RequestBody @Valid AppointmentSaleGoodsPageRequest appointmentSaleGoodsPageReq) {
        AppointmentSaleGoodsQueryRequest queryReq = KsBeanUtil.convert(appointmentSaleGoodsPageReq, AppointmentSaleGoodsQueryRequest.class);
        Page<AppointmentSaleGoods> appointmentSaleGoodsPage = appointmentSaleGoodsService.page(queryReq);
        Page<AppointmentSaleGoodsVO> newPage = appointmentSaleGoodsPage.map(entity -> appointmentSaleGoodsService.wrapperVo(entity));
        MicroServicePage<AppointmentSaleGoodsVO> microPage = new MicroServicePage<>(newPage, appointmentSaleGoodsPageReq.getPageable());
        AppointmentSaleGoodsPageResponse finalRes = new AppointmentSaleGoodsPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<AppointmentSaleGoodsListResponse> list(@RequestBody @Valid AppointmentSaleGoodsListRequest appointmentSaleGoodsListReq) {
        AppointmentSaleGoodsQueryRequest queryReq = KsBeanUtil.convert(appointmentSaleGoodsListReq, AppointmentSaleGoodsQueryRequest.class);
        List<AppointmentSaleGoods> appointmentSaleGoodsList = appointmentSaleGoodsService.list(queryReq);
        List<AppointmentSaleGoodsVO> newList = appointmentSaleGoodsList.stream().map(entity -> appointmentSaleGoodsService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new AppointmentSaleGoodsListResponse(newList));
    }

    @Override
    public BaseResponse<AppointmentSaleGoodsByIdResponse> getById(@RequestBody @Valid AppointmentSaleGoodsByIdRequest appointmentSaleGoodsByIdRequest) {
        AppointmentSaleGoods appointmentSaleGoods =
                appointmentSaleGoodsService.getOne(appointmentSaleGoodsByIdRequest.getId(), appointmentSaleGoodsByIdRequest.getStoreId());
        return BaseResponse.success(new AppointmentSaleGoodsByIdResponse(appointmentSaleGoodsService.wrapperVo(appointmentSaleGoods)));
    }

    @Override
    public BaseResponse<AppointmentResponse> pageBoss(@RequestBody @Valid AppointmentGoodsInfoSimplePageRequest request) {
        AppointmentGoodsInfoSimplePageDTO query = KsBeanUtil.convert(request, AppointmentGoodsInfoSimplePageDTO.class);
        Page<AppointmentSaleGoods> page = appointmentSaleGoodsService.build(query);
        if (CollectionUtils.isEmpty(page.getContent())) {
            return BaseResponse.success(AppointmentResponse.builder().build());
        }
        List<AppointmentSale> appointmentSales = appointmentSaleService.list(AppointmentSaleQueryRequest.builder().idList
                (page.getContent().stream().map(AppointmentSaleGoods::getAppointmentSaleId).collect(Collectors.toList())).build());
        Map<Long, AppointmentSale> appointmentSaleMap = appointmentSales.stream().collect(Collectors.toMap(AppointmentSale::getId, v -> v));

        List<GoodsInfo> goodsInfoList = goodsInfoService.findByIds(page.getContent().stream().map(AppointmentSaleGoods::getGoodsInfoId).collect(Collectors.toList()));
        Map<String, GoodsInfo> goodsInfoMap = goodsInfoList.stream().collect(Collectors.toMap(GoodsInfo::getGoodsInfoId, v -> v));
        Page<AppointmentVO> newPage = page.map(entity -> appointmentSaleGoodsService.wrapperAppointmentVO(entity, appointmentSaleMap, goodsInfoMap));
        MicroServicePage<AppointmentVO> microPage = new MicroServicePage<>(newPage, page.getPageable());

        //填充spu主图片
        if(CollectionUtils.isNotEmpty(goodsInfoList)) {
            Map<String, String> spuMap = goodsService.findAll(GoodsQueryRequest.builder()
                    .goodsIds(goodsInfoList.stream().map(GoodsInfo::getGoodsId).collect(Collectors.toList())).build())
                    .stream().filter(g -> StringUtils.isNotBlank(g.getGoodsImg())).collect(Collectors.toMap(Goods::getGoodsId, Goods::getGoodsImg));
            if(MapUtils.isNotEmpty(spuMap)){
                microPage.getContent().forEach(s -> s.getAppointmentSaleGoods().setGoodsImg(spuMap.get(s.getAppointmentSaleGoods().getGoodsId())));
            }
        }

        Map<String, String> specMap = goodsInfoSpecDetailRelService.textByGoodsInfoIds(
                microPage.getContent().stream().map(a -> a.getAppointmentSaleGoods().getGoodsInfoId()).collect(Collectors.toList()));
        if (MapUtils.isNotEmpty(specMap)) {
            microPage.getContent().forEach(s -> s.getAppointmentSaleGoods().setSpecText(specMap.containsKey(s.getAppointmentSaleGoods().getGoodsInfoId()) ?
                    specMap.get(s.getAppointmentSaleGoods().getGoodsInfoId()) : ""));
        }
        return BaseResponse.success(AppointmentResponse.builder().appointmentVOPage(microPage).build());
    }

    @Override
    public BaseResponse<AppointmentGoodsResponse> pageAppointmentGoodsInfo(@RequestBody @Valid AppointmentGoodsInfoSimplePageRequest request) {
        AppointmentGoodsInfoSimpleCriterIaBuilder query = KsBeanUtil.convert(request, AppointmentGoodsInfoSimpleCriterIaBuilder.class);
        Page<AppointmentSaleGoodsVO> appointmentSaleGoodsVOS = appointmentSaleGoodsService.pageAppointmentGoodsInfo(query);
        MicroServicePage<AppointmentSaleGoodsVO> microServicePage = KsBeanUtil.convertPage(appointmentSaleGoodsVOS, AppointmentSaleGoodsVO.class);

        //填充sku图片
        if(CollectionUtils.isNotEmpty(microServicePage.getContent())) {
            Map<String, GoodsInfo> skuMap = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder()
                    .goodsInfoIds(microServicePage.getContent().stream().map(AppointmentSaleGoodsVO::getGoodsInfoId).collect(Collectors.toList())).build())
                    .stream().collect(Collectors.toMap(GoodsInfo::getGoodsInfoId, g -> g));
            if (MapUtils.isNotEmpty(skuMap)) {
                microServicePage.getContent().stream().filter(s -> skuMap.containsKey(s.getGoodsInfoId()))
                        .forEach(s -> {
                            GoodsInfo sku = skuMap.get(s.getGoodsInfoId());
                            s.setGoodsInfoImg(sku.getGoodsInfoImg());
                        });
            }
        }

        //填充规格
        if (Boolean.TRUE.equals(request.getHavSpecTextFlag())
                && CollectionUtils.isNotEmpty(microServicePage.getContent())) {
            Map<String, String> specMap = goodsInfoSpecDetailRelService.textByGoodsInfoIds(
                    microServicePage.getContent().stream().map(AppointmentSaleGoodsVO::getGoodsInfoId).collect(Collectors.toList()));
            if (MapUtils.isNotEmpty(specMap)) {
                microServicePage.getContent().forEach(s -> s.setSpecText(specMap.get(s.getGoodsInfoId())));
            }
        }
        return BaseResponse.success(AppointmentGoodsResponse.builder().page(microServicePage).build());
    }
}

