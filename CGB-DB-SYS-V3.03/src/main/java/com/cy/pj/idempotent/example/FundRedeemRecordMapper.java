package com.cy.pj.idempotent.example;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FundRedeemRecordMapper {

    /**
     * 插入赎回流水记录
     * 如果businessNo已存在，会抛出唯一索引冲突异常
     */
    int insertRecord(FundRedeemRecord record);

    /**
     * 根据业务流水号查询记录
     */
    FundRedeemRecord selectByBusinessNo(@Param("businessNo") String businessNo);

    /**
     * 更新流水状态为成功
     */
    int updateStatusToSuccess(@Param("businessNo") String businessNo);

    /**
     * 更新流水状态为失败
     */
    int updateStatusToFailed(@Param("businessNo") String businessNo,
                             @Param("errorMsg") String errorMsg);

    /**
     * 检查并更新状态（原子操作，用于防止重复处理）
     * 只有状态为PROCESSING时才能更新
     */
    int updateStatusFromProcessing(@Param("businessNo") String businessNo,
                                   @Param("newStatus") String newStatus,
                                   @Param("errorMsg") String errorMsg);
}
