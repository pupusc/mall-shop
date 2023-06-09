package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaBookLabelQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookLabelBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookLabelReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookQueryByPageBo;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 标签(MetaBookLabel)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-23 22:54:32
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaBookLabelProvider")
public interface MetaBookLabelProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookLabel/queryById")
    BusinessResponse<MetaBookLabelBO> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaBookLabel/queryByPage")
    BusinessResponse<List<MetaBookLabelBO>> queryByPage(@RequestBody @Valid MetaBookLabelQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaBookLabel 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookLabel/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaBookLabelBO metaBookLabel);

    /**
     * 修改数据
     *
     * @param metaBookLabel 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookLabel/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaBookLabelBO metaBookLabel);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaBookLabel/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);


    /**
     * 新增数据
     *
     * @param metaBookLabel 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookLabel/importBookLabel")
    BusinessResponse<String> importBookLabel(@RequestBody @Valid MetaBookLabelBO metaBookLabel);

    @PostMapping("/goods/${application.goods.version}/metaBook/bookAllByPage")
    BusinessResponse<List<MetaBookLabelReqBO>> bookAllByPage(@RequestBody MetaBookQueryByPageBo metaBookQueryByPageBo);

    @PostMapping("/goods/${application.goods.version}/metaBook/bookLabelAllByPage")
    BusinessResponse<List<MetaBookLabelReqBO>> bookLabelAllByPage(@RequestBody MetaBookLabelQueryByPageReqBO metaBookQueryByPageBo);
    @PostMapping("/goods/${application.goods.version}/metaBook/addBookLabe")
    BusinessResponse<Integer> addBookLabel(@RequestBody MetaBookLabelQueryByPageReqBO metaBookQueryByPageBo);

}
