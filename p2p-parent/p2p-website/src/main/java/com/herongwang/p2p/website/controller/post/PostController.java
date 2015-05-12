package com.herongwang.p2p.website.controller.post;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herongwang.p2p.entity.orders.OrdersEntity;
import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.herongwang.p2p.model.order.OrderModel;
import com.herongwang.p2p.model.order.ResultsModel;
import com.herongwang.p2p.service.orders.IOrdersService;
import com.herongwang.p2p.service.parameters.IParametersService;
import com.herongwang.p2p.service.post.IPostService;
import com.sxj.util.exception.WebException;

@Controller
@RequestMapping("/post")
public class PostController
{
    @Autowired
    IParametersService parametersService;
    
    @Autowired
    IOrdersService ordersService;
    
    @Autowired
    IPostService postService;
    
    @RequestMapping("/recharge")
    public String recharge(ModelMap map) throws WebException
    {
        return "site/post/recharge";
    }
    
    @RequestMapping("/rechargeList")
    public String rechargeList(HttpSession session, ModelMap map,
            OrderModel order) throws WebException
    {
        try
        {
            BigDecimal amount = new BigDecimal(order.getOrderAmount());
            BigDecimal b2 = new BigDecimal(100);
            BigDecimal b = amount.multiply(b2);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String customerId = "1";
            
            //生成充值订单
            OrdersEntity orders = new OrdersEntity();
            orders.setCustomerId(customerId);
            orders.setAmount(b);
            orders.setCreateTime(new Date());
            orders.setStatus(0);
            orders.setOrderType(1);
            ordersService.addOrders(orders);
            //返回到页面的参数
            ParametersEntity entity = new ParametersEntity();
            entity.setType("postType");
            List<ParametersEntity> postList = parametersService.queryParameters(entity);
            for (int i = 0; i < postList.size(); i++)
            {
                ParametersEntity p = postList.get(i);
                if (p.getValue().equals("serverip"))
                {
                    map.put("serverip", p.getText());
                }
                if (p.getValue().equals("pickupUrl"))
                {
                    map.put("pickupUrl", p.getText());
                }
                if (p.getValue().equals("receiveUrl"))
                {
                    map.put("receiveUrl", p.getText());
                }
                if (p.getValue().equals("merchantId"))
                {
                    map.put("merchantId", p.getText());
                }
                if (p.getValue().equals("orderExpireDatetime"))
                {
                    map.put("orderExpireDatetime", p.getText());
                }
                if (p.getValue().equals("productName"))
                {
                    map.put("productName", p.getText());
                }
                if (p.getValue().equals("payType"))
                {
                    map.put("payType", p.getText());
                }
            }
            
            //生成签名
            OrderModel orderMember = new OrderModel();
            orderMember.setInputCharset("1");
            orderMember.setPickupUrl(String.valueOf(map.get("pickupUrl")));
            orderMember.setReceiveUrl(String.valueOf(map.get("receiveUrl")));
            orderMember.setVersion("v1.0");
            orderMember.setLanguage("1");
            orderMember.setSignType("1");
            orderMember.setMerchantId(String.valueOf(map.get("merchantId")));
            orderMember.setOrderNo(orders.getOrdersNo());
            orderMember.setOrderAmount(b.toString());
            orderMember.setOrderCurrency("0");
            orderMember.setOrderDatetime(sf.format(orders.getCreateTime()));
            orderMember.setOrderExpireDatetime(String.valueOf(map.get("orderExpireDatetime")));
            orderMember.setProductName(String.valueOf(map.get("productName")));
            orderMember.setPayType(String.valueOf(map.get("payType")));
            
            String strSignMsg = postService.getSignMsg(orderMember);
            orders.setStrSignMsg(strSignMsg);
            //添加签名到订单表
            ordersService.updateOrders(orders);
            map.put("amount", amount);
            map.put("orderName", "充值");
            map.put("balance", "100");
            map.put("orderNo", orders.getOrdersNo());
            map.put("createTime", orders.getCreateTime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return "site/post/recharge-list";
    }
    
    @RequestMapping("/pickup")
    public String pickup(ModelMap map, ResultsModel result) throws WebException
    {
        map.put("signMsg", result.getSignMsg());
        return "site/post/results";
    }
    
    @RequestMapping("/receive")
    public void receive(ModelMap map, ResultsModel result) throws WebException
    {
        System.out.println(result.getMerchantId() + "===="
                + result.getOrderNo());
    }
}