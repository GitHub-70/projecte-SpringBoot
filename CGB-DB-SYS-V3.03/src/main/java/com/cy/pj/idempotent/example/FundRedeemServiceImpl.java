package com.cy.pj.idempotent.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FundRedeemServiceImpl implements FundRedeemService {

    private static final Logger log = LoggerFactory.getLogger(FundRedeemServiceImpl.class);

    @Autowired
    private FundPositionMapper fundPositionMapper;

    @Autowired
    private FundRedeemRecordMapper fundRedeemRecordMapper;

    /**
     * 方案3增强版：原子操作 + 幂等性控制（⭐最优方案）
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = Exception.class)
    @Override
    public void redeemWithAtomicOperation(FundRedeemRequest request) {
        log.info("【方案3-原子操作+幂等】开始处理赎回请求: {}", request);

        // ==================== 第一步：幂等性检查 ====================

        // 1.1 如果没有业务流水号，生成一个
//        if (request.getBusinessNo() == null || request.getBusinessNo().trim().isEmpty()) {
//            request.setBusinessNo(generateBusinessNo(request));
//            log.info("生成业务流水号: {}", request.getBusinessNo());
//        }

        String businessNo = request.getBusinessNo();

        // 1.2 尝试插入流水记录（利用唯一索引保证幂等性）
        FundRedeemRecord record = new FundRedeemRecord();
        record.setBusinessNo(businessNo);
        record.setUserId(request.getUserId());
        record.setFundCode(request.getFundCode());
        record.setRedeemShares(request.getRedeemShares());
        record.setStatus("PROCESSING");

        try {
            int insertRows = fundRedeemRecordMapper.insertRecord(record);
            log.info("插入流水记录成功: businessNo={}", businessNo);
        } catch (DuplicateKeyException e) {
            // 1.3 如果插入失败（唯一索引冲突），说明是重复请求
            log.warn("检测到重复请求，businessNo: {}", businessNo);

            // 1.4 查询之前的处理结果
            FundRedeemRecord existingRecord = fundRedeemRecordMapper.selectByBusinessNo(businessNo);

            if (existingRecord == null) {
                throw new RuntimeException("系统异常，请稍后重试");
            }

            // 1.5 根据之前的状态返回结果
            if ("SUCCESS".equals(existingRecord.getStatus())) {
                log.info("重复请求，之前已处理成功: businessNo={}", businessNo);
                return; // 直接返回，不抛异常，前端认为成功
            } else if ("FAILED".equals(existingRecord.getStatus())) {
                log.warn("重复请求，之前处理失败: businessNo={}, error={}",
                        businessNo, existingRecord.getErrorMsg());
                throw new RuntimeException("之前处理失败: " + existingRecord.getErrorMsg());
            } else if ("PROCESSING".equals(existingRecord.getStatus())) {
                log.warn("重复请求，之前请求正在处理中: businessNo={}", businessNo);
                throw new RuntimeException("请求正在处理中，请勿重复提交");
            }
        }

        // ==================== 第二步：执行赎回业务逻辑 ====================

        try {
            // 2.1 直接在数据库层面完成校验和扣减（原子操作）
            int rows = fundPositionMapper.redeemWithAtomicCheck(
                    request.getUserId(),
                    request.getFundCode(),
                    request.getRedeemShares()
            );

            // 2.2 检查执行结果
            if (rows == 0) {
                // 可能是持仓不存在或份额不足
                FundPosition position = fundPositionMapper.selectByUserAndFund(
                        request.getUserId(),
                        request.getFundCode()
                );

                String errorMsg;
                if (position == null) {
                    errorMsg = "持仓不存在";
                } else {
                    errorMsg = "可用份额不足，当前可用: " +
                            position.getAvailableShares() +
                            ", 请求赎回: " + request.getRedeemShares();
                }

                log.error("赎回失败: {}", errorMsg);

                // 2.3 更新流水状态为失败
                fundRedeemRecordMapper.updateStatusToFailed(businessNo, errorMsg);

                throw new RuntimeException(errorMsg);
            }

            // 2.4 更新流水状态为成功
            int updateRows = fundRedeemRecordMapper.updateStatusToSuccess(businessNo);
            if (updateRows == 0) {
                log.error("更新流水状态失败，businessNo: {}", businessNo);
                throw new RuntimeException("系统异常，请联系客服");
            }

            log.info("【方案3-原子操作+幂等】赎回成功，businessNo: {}, 影响行数: {}",
                    businessNo, rows);

        } catch (Exception e) {
            // 2.5 发生异常，更新流水状态为失败
            log.error("赎回处理异常，businessNo: {}", businessNo, e);
            fundRedeemRecordMapper.updateStatusToFailed(businessNo, e.getMessage());
            throw e;
        }
    }

    /**
     * 设计有问题
     * 生成业务流水号
     * 格式：REDEEM_{userId}_{fundCode}_{timestamp}_{random}
     */
    private String generateBusinessNo(FundRedeemRequest request) {
        long timestamp = System.currentTimeMillis();
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return String.format("REDEEM_%d_%s_%d_%d_%s",
                request.getUserId(),
                request.getFundCode(),
                timestamp,
                request.getRedeemShares(),
                random
        );
    }

    // ... 其他方法保持不变 ...
}
