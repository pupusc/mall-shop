package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.GoodsEvaluateAnalyse;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/17/16:36
 * @Description:
 */
@Resource
public interface GoodsEvaluateAnalyseMapper {
    int delete(@Param("id") int id);
    List<GoodsEvaluateAnalyse> getByPage(@Param("name") String name,@Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    int getCount(@Param("name") String name);

}
