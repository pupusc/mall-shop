package com.wanmi.ares.export.dao;

import com.wanmi.ares.export.model.entity.ExportDataEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 导出数据请求Dao
 * Created by bail on 2017/11/01.
 */
@Repository
public interface ExportDataMapper {

    /**
     * 根据条件分页查询导出任务
     * @param exportDataEntity
     * @return
     */
    List<ExportDataEntity> queryExportDataRequest(ExportDataEntity exportDataEntity);

    /**
     * 根据条件查询导出任务总条数
     * @param exportDataEntity
     * @return
     */
    Integer countExportDataRequest(ExportDataEntity exportDataEntity);

    /**
     * 根据条件查询导出任务List
     * @param exportDataEntity
     * @return
     */
    List<ExportDataEntity> queryExportDataRequestList(ExportDataEntity exportDataEntity);

    /**
     * 根据条件查询需要清理的导出任务List
     * @param exportDataEntity
     * @return
     */
    List<ExportDataEntity> queryExportDataRequestListForClear(ExportDataEntity exportDataEntity);

    /**
     * 根据条件查询单个导出任务要求
     * @param exportDataEntity
     * @return
     */
    ExportDataEntity queryOneExportDataRequest(ExportDataEntity exportDataEntity);

    /**
     * 插入要求不一样的导出任务要求
     * (即:同一个用户,不可以对同一个报表同一个时间段进行多次下载)
     * @param exportDataEntity
     * @return
     */
    int insertExportDataRequest(ExportDataEntity exportDataEntity);

    /**
     * 根据条件删除导出任务要求
     * @param exportDataEntity
     *
     * @return
     */
    int deleteExportDataRequest(ExportDataEntity exportDataEntity);

    /**
     * 根据条件清理导出任务要求
     * @param exportDataEntity
     * @return
     */
    int clearExportDataRequest(ExportDataEntity exportDataEntity);

    /**
     * 根据条件修改导出任务要求的状态
     * @param exportDataEntity
     * @return
     */
    int updateExportDataRequest(ExportDataEntity exportDataEntity);

}
