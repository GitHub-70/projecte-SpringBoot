package com.cy.pj.idempotent;

import com.cy.pj.idempotent.example.FundRedeemRequest;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 幂等性控制
 * <p>
 * 前端幂等设计：
 * class RedeemService {
 * constructor() {
 * // 缓存当前业务编号
 * this.currentBusinessNo = null;
 * // 防止并发提交(网络延迟波动，用户快速点击)
 * this.isProcessing = false;
 * }
 * <p>
 * // 方式1：生成 businessNo（只在需要时调用一次）
 * generateBusinessNo() {
 * const timestamp = Date.now();
 * const random = Math.random().toString(36).substring(2, 10);
 * return `REDEEM_${timestamp}_${random}`;
 * }
 * <p>
 * // 方式2：使用全局计数器（同一页面多次提交）
 * let requestCounter = 0;
 * function generateBusinessNoV2() {
 * requestCounter++;
 * return `REDEEM_${Date.now()}_${requestCounter}`;
 * }
 * <p>
 * // 发起赎回
 * async redeem(userId, fundCode, shares) {
 * // 防止并发提交
 * if (this.isProcessing) {
 * throw new Error('请求处理中，请勿重复提交');
 * }
 * <p>
 * this.isProcessing = true;
 * <p>
 * // ⭐ 生成 businessNo（整个请求周期不变）
 * this.currentBusinessNo = this.generateBusinessNo();
 * <p>
 * try {
 * const result = await this.sendRequest({
 * userId,
 * fundCode,
 * redeemShares: shares,
 * businessNo: this.currentBusinessNo
 * });
 * <p>
 * return result;
 * } catch (error) {
 * // 网络错误时可以重试，businessNo 不变
 * if (error.type === 'TIMEOUT') {
 * console.log('超时重试，businessNo:', this.currentBusinessNo);
 * return this.retry(this.currentBusinessNo);
 * }
 * throw error;
 * } finally {
 * this.isProcessing = false;
 * this.currentBusinessNo = null;
 * }
 * }
 * <p>
 * // 重试（使用相同的 businessNo）
 * async retry(businessNo) {
 * return this.sendRequest({
 * userId: 1001,
 * fundCode: 'FUND001',
 * redeemShares: 100,
 * businessNo: businessNo  // ⭐ 关键：使用相同的值
 * });
 * }
 * <p>
 * sendRequest(data) {
 * return fetch('/fund/redeem/atomic', {
 * method: 'POST',
 * headers: { 'Content-Type': 'application/json' },
 * body: JSON.stringify(data)
 * }).then(res => res.json());
 * }
 * }
 * <p>
 * // 使用
 * const redeemService = new RedeemService();
 * <p>
 * document.getElementById('redeemBtn').addEventListener('click', () => {
 * redeemService.redeem(1001, 'FUND001', 100)
 * .then(result => {
 * console.log('成功:', result);
 * })
 * .catch(error => {
 * console.error('失败:', error);
 * });
 * });
 * <p>
 * 后端校验幂等业务流水号：
 * 1️⃣ 创建幂等性控制表
 * -- 基金赎回流水表（用于幂等性控制）
 * CREATE TABLE fund_redeem_record (
 * id NUMBER PRIMARY KEY,
 * business_no VARCHAR2(64) NOT NULL UNIQUE,  -- 唯一索引，核心！
 * user_id NUMBER NOT NULL,
 * fund_code VARCHAR2(20) NOT NULL,
 * redeem_shares NUMBER(18, 2) NOT NULL,
 * status VARCHAR2(20) DEFAULT 'PROCESSING',  -- PROCESSING/SUCCESS/FAILED
 * error_msg VARCHAR2(500),
 * created_time DATE DEFAULT SYSDATE,
 * updated_time DATE DEFAULT SYSDATE,
 * CONSTRAINT uk_business_no UNIQUE (business_no)
 * );
 * <p>
 * -- 创建索引
 * CREATE INDEX idx_user_id ON fund_redeem_record(user_id);
 * CREATE INDEX idx_status ON fund_redeem_record(status);
 * <p>
 * COMMENT ON TABLE fund_redeem_record IS '基金赎回流水表';
 * COMMENT ON COLUMN fund_redeem_record.business_no IS '业务流水号（唯一，用于幂等性控制）';
 * COMMENT ON COLUMN fund_redeem_record.status IS '状态：PROCESSING-处理中，SUCCESS-成功，FAILED-失败';
 * <p>
 * 2️⃣ 幂等性控制逻辑
 * 第一步：幂等性检查
 * 1.2 尝试插入流水记录（利用唯一索引保证幂等性）
 * 1.3 如果插入失败（唯一索引冲突），说明是重复请求
 * <p>
 * 🔄 幂等性控制流程图
 * 用户发起赎回请求
 * ↓
 * 是否有businessNo？
 * ├─ 否 → 生成唯一的businessNo
 * └─ 是 → 使用传入的businessNo
 * ↓
 * 尝试插入流水记录
 * ↓
 * 插入成功？
 * ├─ 是 → 继续执行业务逻辑
 * │        ↓
 * │     执行赎回操作
 * │        ↓
 * │     成功？
 * │        ├─ 是 → 更新状态为SUCCESS
 * │        └─ 否 → 更新状态为FAILED
 * │
 * └─ 否（唯一索引冲突）→ 查询已有记录
 * ↓
 * 状态是什么？
 * ├─ SUCCESS → 直接返回成功（幂等）
 * ├─ FAILED  → 返回失败原因
 * └─ PROCESSING → 提示"处理中，勿重复提交"
 */
public class CommitIdempotent {


    public static void main(String[] args) throws InterruptedException {

        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executor.submit(() -> {
                try {
                    FundRedeemRequest request = new FundRedeemRequest();
                    request.setUserId(1001L);
                    request.setFundCode("006021");
                    request.setRedeemShares(new BigDecimal("100"));
                    String businessNo = generateBusinessNo(request);
                    System.out.println("businessNo==" + businessNo);
                } catch (Exception e) {
                    System.err.println("线程 " + threadNum + " 赎回失败: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

    }

    private static String generateBusinessNo(FundRedeemRequest request) {
        long timestamp = System.currentTimeMillis();
//        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String businessNo = String.format("REDEEM_%d_%s_%d_%f",
                request.getUserId(),
                request.getFundCode(),
                timestamp,
                request.getRedeemShares()
        );

        // 模拟其他业务
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        return businessNo;
    }
}
