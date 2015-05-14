package com.herongwang.p2p.model.profit;

import java.math.BigDecimal;
import java.util.List;

public class ProfitModel
{
    private List<MonthProfit> monthProfit;
    
    /**
     * 总利息
     */
    private BigDecimal totalInterest;
    
    /**
     * 总额
     */
    private BigDecimal amount;
    
    /**
     * 投资金额
     */
    private BigDecimal investment;
    
    public List<MonthProfit> getMonthProfit()
    {
        return monthProfit;
    }
    
    public void setMonthProfit(List<MonthProfit> monthProfit)
    {
        this.monthProfit = monthProfit;
    }
    
    public BigDecimal getTotalInterest()
    {
        return totalInterest;
    }
    
    public void setTotalInterest(BigDecimal totalInterest)
    {
        this.totalInterest = totalInterest;
    }
    
    public BigDecimal getAmount()
    {
        return amount;
    }
    
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
    
    public BigDecimal getInvestment()
    {
        return investment;
    }
    
    public void setInvestment(BigDecimal investment)
    {
        this.investment = investment;
    }
    
}
