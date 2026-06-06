package com.cy.pj.idempotent.example;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 基金持仓 Mapper 接口
 */
@Mapper
public interface FundPositionMapper {

    /**
     * 原子操作：在数据库层面完成份额校验和扣减
     * SQL 示例：
     *   UPDATE fund_position
     *   SET available_shares = available_shares - #{redeemShares},
     *       frozen_shares = frozen_shares + #{redeemShares},
     *       version = version + 1
     *   WHERE user_id = #{userId} AND fund_code = #{fundCode}
     *         AND available_shares >= #{redeemShares}
     *   只有 available_shares >= redeemShares 时才会更新成功（返回行数 > 0）
     */
    int redeemWithAtomicCheck(@Param("userId") Long userId,
                              @Param("fundCode") String fundCode,
                              @Param("redeemShares") BigDecimal redeemShares);

    /**
     * 根据用户ID和基金代码查询持仓
     * 用于在赎回失败时获取详细错误信息（如可用份额余额）
     */
    FundPosition selectByUserAndFund(@Param("userId") Long userId,
                                     @Param("fundCode") String fundCode);
}
