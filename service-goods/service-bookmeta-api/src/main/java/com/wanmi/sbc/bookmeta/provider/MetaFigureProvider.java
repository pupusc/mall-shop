package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaFigureAddReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaFigureEditReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaFigureQueryByIdResBO;
import com.wanmi.sbc.bookmeta.bo.MetaFigureQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaFigureBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 人物(MetaFigure)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-23 22:54:32
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaFigureProvider")
public interface MetaFigureProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaFigure/queryById")
    BusinessResponse<MetaFigureQueryByIdResBO> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaFigure/queryByPage")
    BusinessResponse<List<MetaFigureBO>> queryByPage(@RequestBody @Valid MetaFigureQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaFigure 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaFigure/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaFigureAddReqBO metaFigure);

    /**
     * 修改数据
     *
     * @param metaFigure 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaFigure/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaFigureEditReqBO metaFigure);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaFigure/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
