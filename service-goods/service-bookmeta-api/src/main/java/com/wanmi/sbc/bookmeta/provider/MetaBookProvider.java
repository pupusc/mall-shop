package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.*;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 书籍(MetaBook)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-23 22:54:32
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaBookProvider")
public interface MetaBookProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBook/queryById")
    BusinessResponse<MetaBookQueryByIdResBO> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaBook/queryByPage")
    BusinessResponse<List<MetaBookQueryByPageResBO>> queryByPage(@RequestBody @Valid MetaBookQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaBook 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBook/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaBookAddReqBO metaBook);

    /**
     * 修改数据
     *
     * @param metaBook 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBook/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaBookEditReqBO metaBook);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaBook/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

    @PostMapping("/goods/${application.goods.version}/metaBook/queryPublishInfo")
    BusinessResponse<MetaBookQueryPublishInfoResBO> queryPublishInfo(@RequestBody @NotNull Integer id);

    @PostMapping("/goods/${application.goods.version}/metaBook/updatePublishInfo")
    BusinessResponse<Boolean> updatePublishInfo(@RequestBody @Valid MetaBookEditPublishInfoReqBO metaBook);

    @PostMapping("/goods/${application.goods.version}/metaBook/queryBookLable")
    List<Map> queryBookLable();

    /**查询所有book
     */
    @PostMapping("/goods/${application.goods.version}/metaBook/queryBook")
    List<Map> queryAllBook();


    /**根据bookname或者labelname模糊分页查询bookLabel信息
     */
    @PostMapping("/goods/${application.goods.version}/metaBook/queryBookLabelBypage")
    MetaBookQueryByPageBo queryBookLabelBypage(@RequestBody MetaBookQueryByPageBo metaBookQueryByPageBo);

    /**查询所有book
     */
    @PostMapping("/goods/${application.goods.version}/metaBook/getRecommentKey")
    BusinessResponse<MetaBookRecommentKeyBo> getRecommentKey(@RequestParam(value = "spuId") String spuId);

}
