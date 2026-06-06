package com.cy.pj.idempotent.example;


import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FundPosition implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "持仓ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "基金代码")
    private String fundCode;

    @ApiModelProperty(value = "基金名称")
    private String fundName;

    @ApiModelProperty(value = "持有份额")
    private BigDecimal shares;

    @ApiModelProperty(value = "可用份额")
    private BigDecimal availableShares;

    @ApiModelProperty(value = "冻结份额")
    private BigDecimal frozenShares;

    @ApiModelProperty(value = "版本号（乐观锁）")
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public BigDecimal getShares() {
        return shares;
    }

    public void setShares(BigDecimal shares) {
        this.shares = shares;
    }

    public BigDecimal getAvailableShares() {
        return availableShares;
    }

    public void setAvailableShares(BigDecimal availableShares) {
        this.availableShares = availableShares;
    }

    public BigDecimal getFrozenShares() {
        return frozenShares;
    }

    public void setFrozenShares(BigDecimal frozenShares) {
        this.frozenShares = frozenShares;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "FundPosition{" +
                "id=" + id +
                ", userId=" + userId +
                ", fundCode='" + fundCode + '\'' +
                ", fundName='" + fundName + '\'' +
                ", shares=" + shares +
                ", availableShares=" + availableShares +
                ", frozenShares=" + frozenShares +
                ", version=" + version +
                ", createdTime=" + createdTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
