package com.cy.pj.idempotent.example;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "基金赎回请求")
public class FundRedeemRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @NotBlank(message = "基金代码不能为空")
    @ApiModelProperty(value = "基金代码", required = true)
    private String fundCode;

    @NotNull(message = "赎回份额不能为空")
    @DecimalMin(value = "0.01", message = "赎回份额必须大于0")
    @ApiModelProperty(value = "赎回份额", required = true)
    private BigDecimal redeemShares;

    @NotBlank(message = "业务流水号不能为空")  // ⭐ 改为必填
    @ApiModelProperty(value = "业务流水号（由前端生成，保证幂等性）")
    private String businessNo;

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

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    @Override
    public String toString() {
        return "FundRedeemRequest{" +
                "userId=" + userId +
                ", fundCode='" + fundCode + '\'' +
                ", redeemShares=" + redeemShares +
                ", businessNo='" + businessNo + '\'' +
                '}';
    }
}
