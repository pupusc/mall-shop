package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.*;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 标签(MetaLabel)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-23 22:54:32
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaLabelProvider")
public interface MetaLabelProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/queryById")
    BusinessResponse<MetaLabelBO> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/queryByPage")
    BusinessResponse<List<MetaLabelBO>> queryByPage(@RequestBody @Valid MetaLabelQueryByPageReqBO pageRequest);

    /**查询所有label分类
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/queryCate")
    List<Map> getLabelCate(@RequestBody @NotNull int parent_id);

    /**查询所有label分类
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/queryCate2")
    List<Map> getLabelCate2(@RequestBody @NotNull String parent_id);


    /**
     * 新增数据
     *
     * @param metaLabel 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaLabelBO metaLabel);

    /**
     * 修改数据
     *
     * @param metaLabel 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaLabelBO metaLabel);


    @PostMapping("/goods/${application.goods.version}/metaLabel/updateName")
    BusinessResponse<Boolean> updateName(@RequestBody @Valid MetaLabelBO metaLabel);

    /**
     * 修改数据
     *
     * @param metaLabel 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/updateStatus")
    BusinessResponse<Boolean> updateStatus(@RequestBody @Valid MetaLabelUpdateStatusReqBO metaLabel);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

    /**查询所有label
     */
    @PostMapping("/goods/${application.goods.version}/metaLabel/queryLabel")
    List<Map> queryAllLabel();

    @PostMapping("/goods/${application.goods.version}/metaLabel/queryAllGoodsLabel")
    List<GoodsLabelSpuReqBO> queryAllGoodsLabel();

    @PostMapping("/goods/${application.goods.version}/metaLabel/queryAllGoods")
    List<GoodsBO> queryAllGoods();

    @PostMapping("/goods/${application.goods.version}/metaLabel/importGoodsLabel")
    BusinessResponse<String> importGoodsLabel(@RequestBody GoodsLabelSpuReqBO goodsLabelSpuReqBO);
    @PostMapping("/goods/${application.goods.version}/metaLabel/getAllLabel")
    BusinessResponse<List<MetaLabelBO>> getLabels(@RequestBody @NotNull MetaLabelQueryByPageReqBO metaLabelQueryByPageReqBO);

    @PostMapping("/goods/${application.goods.version}/metaLabel/getLabelByGoods")
    BusinessResponse<List<MetaLabelBO>> getLabelByGoodsId(@RequestBody @NotNull MetaLabelQueryByPageReqBO metaLabelQueryByPageReqBO);

    @PostMapping("/goods/${application.goods.version}/metaLabel/insertGoodsLabel")
    BusinessResponse<String> insertGoodsLabel(@RequestBody @NotNull GoodsLabelSpuReqBO bo);

    @PostMapping("/goods/${application.goods.version}/metaLabel/updateGoodsLabel")
    BusinessResponse<Integer> updateGoodsLabel(@RequestBody @NotNull GoodsLabelSpuReqBO bo);

    @PostMapping("/goods/${application.goods.version}/metaLabel/deleteGoodsLabel")
    Integer deleteGoodsLabel(@RequestBody @NotNull GoodsLabelSpuReqBO bo);

    @PostMapping("/goods/${application.goods.version}/metaLabel/getGoodsInfoBySpuId")
    SkuDetailBO getGoodsInfoBySpuId(@RequestBody @NotNull String id);

    @PostMapping("/goods/${application.goods.version}/metaLabel/getGoodsInfoBySkuId")
    SkuDetailBO getGoodsInfoBySkuId(@RequestBody @NotNull String id);

    @PostMapping("/goods/${application.goods.version}/metaLabel/getGoodsDetailOther")
    BusinessResponse<List<GoodDetailOtherRespBO>> getGoodsDetailAndOther(@RequestBody @NotNull GoodsOtherDetailBO bo);

    @PostMapping("/goods/${application.goods.version}/metaLabel/updateGoodsDetailOther")
    int updateGoodsDetailAndOther(@RequestBody @NotNull GoodDetailOtherRespBO goodDetailOtherRespBO);

    @PostMapping("/goods/${application.goods.version}/metaLabel/getList")
    List<MetaLabelBO> getType2Label(@RequestBody @NotNull LabelListReqVO reqVO);
}
