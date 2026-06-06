package com.cy.pj.idempotent.example;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FundRedeemRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流水ID")
    private Long id;

    @ApiModelProperty(value = "业务流水号")
    private String businessNo;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "基金代码")
    private String fundCode;

    @ApiModelProperty(value = "赎回份额")
    private BigDecimal redeemShares;

    @ApiModelProperty(value = "状态：PROCESSING/SUCCESS/FAILED")
    private String status;

    @ApiModelProperty(value = "错误信息")
    private String errorMsg;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public BigDecimal getRedeemShares() {
        return redeemShares;
    }

    public void setRedeemShares(BigDecimal redeemShares) {
        this.redeemShares = redeemShares;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "FundRedeemRecord{" +
                "id=" + id +
                ", businessNo='" + businessNo + '\'' +
                ", userId=" + userId +
                ", fundCode='" + fundCode + '\'' +
                ", redeemShares=" + redeemShares +
                ", status='" + status + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}

