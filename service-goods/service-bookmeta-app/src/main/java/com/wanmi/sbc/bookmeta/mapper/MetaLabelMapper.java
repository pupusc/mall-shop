package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.bo.AuthorityBO;
import com.wanmi.sbc.bookmeta.bo.MetaLabelBO;
import com.wanmi.sbc.bookmeta.entity.*;
import com.wanmi.sbc.bookmeta.entity.*;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 标签(MetaLabel)表数据库访问层
 *
 * @author Liang Jun
 * @since 2022-05-17 11:38:03
 */
@Repository
public interface MetaLabelMapper extends Mapper<MetaLabel> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MetaLabel queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param metaLabel 查询条件
     * @param limitIndex 分页参数
     * @param limitSize 分页参数
     * @return 对象列表
     */
    List<MetaLabel> queryAllByLimit(@Param("metaLabel") MetaLabel metaLabel, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 统计总行数
     *
     * @param metaLabel 查询条件
     * @return 总行数
     */
    long count(@Param("metaLabel") MetaLabel metaLabel);

    /**
     * 新增数据
     *
     * @param metaLabel 实例对象
     * @return 影响行数
     */
    int insert(MetaLabel metaLabel);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaLabel> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MetaLabel> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MetaLabel> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MetaLabel> entities);

    /**
     * 修改数据
     *
     * @param metaLabel 实例对象
     * @return 影响行数
     */
    int update(MetaLabel metaLabel);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    int countExt(@Param("metaLabel") MetaLabel metaLabel);

    List<MetaLabelExt> queryAllByLimitExt(@Param("metaLabel") MetaLabel metaLabel, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    /**
     * 采集标签信息
     * @param beginTime
     * @param endTime
     * @param fromId
     * @param pageSize
     * @return
     */
    List<MetaLabel> collectMetaLabelByTime(
            @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime, @Param("fromId") Integer fromId, @Param("pageSize") int pageSize);

    /**
     * 采集标签信息
     * @param parentIds
     * @return
     */
    List<MetaLabel> collectMetaLabel(@Param("parentIds") List<Integer> parentIds);

    List<Map> getAllLabel();

    List<Map> getLabelCate(int parent_id);

    List<MetaLabelV2> getLabels(@Param("name")String name,@Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    List<MetaLabelV2> getLabelByGoodsIdOrLabelId(MetaLabelV2 metaLabelV2);

    int getLabelsCount(@Param("name")String name);

    int isExistGoods(@Param("goods_id") String id);

    int addGoodsLabelSpu(GoodsLabelSpu goodsLabelSpu);

    int updateGoodsLabelSpu(GoodsLabelSpu goodsLabelSpu);

    int deleteGoodsLabel(@Param("id") int id);

    Map<String,Object> getSkuIdBySpuId(@Param("goodsId") String goodsId);

    Map<String,Object> getSkuIdBySpuId1(@Param("goodsId") String goodsId);

    Map<String,Object> getSkuIdBySkuId(@Param("goodsInfoId") String goodsInfoId);

    String getScoreBySkuId(@Param("goodsId") String goodsId);

    String getIsbnBySkuId(@Param("goodsId") String goodsId);

    String getSaleNumSkuId(@Param("goodsId") String goodsId);
    GoodsOtherDetail getGoodsOtherDetail(@Param("goodsId") String goodsId);
    Integer updateGoodsOtherDetail(GoodsOtherDetail goodsOtherDetail);
    List<GoodsLabelSpu> getGoodsLabel();
    List<GoodsVO> getGoodsList();

    int existGoods(@Param("goodsId")String goodsId);

    List<GoodsLabelSpu> getExistGoodsLabel(@Param("goodsId") String goodsId,@Param("LabelId")int LabelId);

}