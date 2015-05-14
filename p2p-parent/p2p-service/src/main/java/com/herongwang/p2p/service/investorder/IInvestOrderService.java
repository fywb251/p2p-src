package com.herongwang.p2p.service.investorder;

import java.util.List;

import com.herongwang.p2p.entity.investorder.InvestOrderEntity;
import com.herongwang.p2p.model.profit.ProfitModel;
import com.sxj.util.exception.ServiceException;

public interface IInvestOrderService
{
    /**
     * 新增投标订单
     * aa
     * @param order 
     */
    public InvestOrderEntity addOrder(String debtId, String amount)
            throws ServiceException;
    
    /**
     * 更新投标订单
     * 
     * @param order
     */
    public void updateOrder(InvestOrderEntity order) throws ServiceException;
    
    /**
     * 获取投标订单信息
     * 
     * @param id
     * @return
     */
    public InvestOrderEntity getInvestOrderEntity(String id)
            throws ServiceException;
    
    /**
     * 获取投标订单列表
     * 
     * @param query
     * @return
     */
    public List<InvestOrderEntity> queryorderList(InvestOrderEntity query)
            throws ServiceException;
    
    /**
     * 删除投标订单
     * 
     * @param id
     */
    public void delOrder(String id) throws ServiceException;
    
    void finishOrder(InvestOrderEntity io) throws ServiceException;
}
