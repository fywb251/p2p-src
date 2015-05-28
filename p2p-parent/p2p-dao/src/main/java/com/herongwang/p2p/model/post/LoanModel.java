package com.herongwang.p2p.model.post;

public class LoanModel
{
    private Double amount;//金额
    
    private String cardNoList;//当前绑定的银行卡号列表
    
    private String withdrawMoneymoremore;//提现人乾多多标识
    
    private Double fee;//手续费
    
    private String feePlatform;
    
    private String feeType;//手续费类型1.自付 2.代付 3.分开付)
    
    private Double feeMax;//用户承担的最高手续费
    
    private String loanNo;//钱多多流水号
    
    private String message;//返回信息
    
    private String orderNo;//订单号
    
    private String platformMoneymoremore;//平台标识
    
    private String randomTimeStamp;//随机时间戳
    
    private String rechargeMoneymoremore;//客户钱多多标识
    
    private String rechargeType;//充值类型(空.网银充值 1.代扣充值 2.快捷支付 3.汇款充值 4.企业网银)
    
    private String remark1;//
    
    private String remark2;//
    
    private String remark3;//
    
    private String resultCode;//返回代码
    
    private String signInfo;//签名信息
    
    private Double feeWithdraws;//用户实际承担的手续费金额
    
    private Double feePercent;//平台承担的手续费比例
    
    private Double freeLimit;//平台扣除的免费提现额
    
    private Double feeRate;//上浮费率
    
    private Double feeSplitting;//平台分润
    
    public Double getAmount()
    {
        return amount;
    }
    
    public void setAmount(Double amount)
    {
        this.amount = amount;
    }
    
    public String getCardNoList()
    {
        return cardNoList;
    }
    
    public void setCardNoList(String cardNoList)
    {
        this.cardNoList = cardNoList;
    }
    
    public Double getFee()
    {
        return fee;
    }
    
    public void setFee(Double fee)
    {
        this.fee = fee;
    }
    
    public String getFeePlatform()
    {
        return feePlatform;
    }
    
    public void setFeePlatform(String feePlatform)
    {
        this.feePlatform = feePlatform;
    }
    
    public String getFeeType()
    {
        return feeType;
    }
    
    public void setFeeType(String feeType)
    {
        this.feeType = feeType;
    }
    
    public String getLoanNo()
    {
        return loanNo;
    }
    
    public void setLoanNo(String loanNo)
    {
        this.loanNo = loanNo;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public String getOrderNo()
    {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }
    
    public String getPlatformMoneymoremore()
    {
        return platformMoneymoremore;
    }
    
    public void setPlatformMoneymoremore(String platformMoneymoremore)
    {
        this.platformMoneymoremore = platformMoneymoremore;
    }
    
    public String getRandomTimeStamp()
    {
        return randomTimeStamp;
    }
    
    public void setRandomTimeStamp(String randomTimeStamp)
    {
        this.randomTimeStamp = randomTimeStamp;
    }
    
    public String getRechargeMoneymoremore()
    {
        return rechargeMoneymoremore;
    }
    
    public void setRechargeMoneymoremore(String rechargeMoneymoremore)
    {
        this.rechargeMoneymoremore = rechargeMoneymoremore;
    }
    
    public String getRechargeType()
    {
        return rechargeType;
    }
    
    public void setRechargeType(String rechargeType)
    {
        this.rechargeType = rechargeType;
    }
    
    public String getRemark1()
    {
        return remark1;
    }
    
    public void setRemark1(String remark1)
    {
        this.remark1 = remark1;
    }
    
    public String getRemark2()
    {
        return remark2;
    }
    
    public void setRemark2(String remark2)
    {
        this.remark2 = remark2;
    }
    
    public String getRemark3()
    {
        return remark3;
    }
    
    public void setRemark3(String remark3)
    {
        this.remark3 = remark3;
    }
    
    public String getResultCode()
    {
        return resultCode;
    }
    
    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }
    
    public String getSignInfo()
    {
        return signInfo;
    }
    
    public void setSignInfo(String signInfo)
    {
        this.signInfo = signInfo;
    }
    
    public Double getFeeMax()
    {
        return feeMax;
    }
    
    public void setFeeMax(Double feeMax)
    {
        this.feeMax = feeMax;
    }
    
    public Double getFeeWithdraws()
    {
        return feeWithdraws;
    }
    
    public void setFeeWithdraws(Double feeWithdraws)
    {
        this.feeWithdraws = feeWithdraws;
    }
    
    public Double getFeePercent()
    {
        return feePercent;
    }
    
    public void setFeePercent(Double feePercent)
    {
        this.feePercent = feePercent;
    }
    
    public Double getFreeLimit()
    {
        return freeLimit;
    }
    
    public void setFreeLimit(Double freeLimit)
    {
        this.freeLimit = freeLimit;
    }
    
    public Double getFeeRate()
    {
        return feeRate;
    }
    
    public void setFeeRate(Double feeRate)
    {
        this.feeRate = feeRate;
    }
    
    public Double getFeeSplitting()
    {
        return feeSplitting;
    }
    
    public void setFeeSplitting(Double feeSplitting)
    {
        this.feeSplitting = feeSplitting;
    }
    
    public String getWithdrawMoneymoremore()
    {
        return withdrawMoneymoremore;
    }
    
    public void setWithdrawMoneymoremore(String withdrawMoneymoremore)
    {
        this.withdrawMoneymoremore = withdrawMoneymoremore;
    }
    
}
