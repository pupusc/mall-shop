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

    @PostMapping("/goods/${application.goods.version}/metaLabel/queryAuthority")
    BusinessResponse<List<AuthorityBO>> getAuthorityByUrl(@RequestBody @Valid AuthorityQueryByPageReqBO pageReqBO);


    @PostMapping("/goods/${application.goods.version}/metaTrade/tree")
    List<MetaTradeBO> getMetaTadeTree(@RequestBody @NotNull int parentId);


    @PostMapping("/goods/${application.goods.version}/metaTrade/")
    List<GoodsNameBySpuIdBO> getGoodsNameBySpuId(@RequestBody @NotNull String name);
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

}
