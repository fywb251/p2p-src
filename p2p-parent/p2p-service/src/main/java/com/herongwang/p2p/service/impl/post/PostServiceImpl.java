package com.herongwang.p2p.service.impl.post;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Service;

import com.allinpay.ets.client.PaymentResult;
import com.allinpay.ets.client.RequestOrder;
import com.allinpay.ets.client.SecurityUtil;
import com.allinpay.ets.client.StringUtil;
import com.herongwang.p2p.entity.tl.TLBillEntity;
import com.herongwang.p2p.model.order.OrderModel;
import com.herongwang.p2p.service.post.IPostService;

@Service
public class PostServiceImpl implements IPostService
{
    
    @Override
    public void Post(String deal) throws Exception
    {
        
    }
    
    @Override
    public String getSignMsg(OrderModel orderModel) throws Exception
    {
        String strSignMsg = "";
        try
        {
            RequestOrder requestOrder = new RequestOrder();
            if (null != orderModel.getInputCharset()
                    && !"".equals(orderModel.getInputCharset()))
            {
                requestOrder.setInputCharset(Integer.parseInt(orderModel.getInputCharset()));
            }
            requestOrder.setPickupUrl(orderModel.getPickupUrl());
            requestOrder.setReceiveUrl(orderModel.getReceiveUrl());
            requestOrder.setVersion(orderModel.getVersion());
            if (null != orderModel.getLanguage()
                    && !"".equals(orderModel.getLanguage()))
            {
                requestOrder.setLanguage(Integer.parseInt(orderModel.getLanguage()));
            }
            requestOrder.setSignType(Integer.parseInt(orderModel.getSignType()));
            requestOrder.setPayType(Integer.parseInt(orderModel.getPayType()));
            requestOrder.setIssuerId(orderModel.getIssuerId());
            requestOrder.setMerchantId(orderModel.getMerchantId());
            requestOrder.setPayerName(orderModel.getPayerName());
            requestOrder.setPayerEmail(orderModel.getPayerEmail());
            requestOrder.setPayerTelephone(orderModel.getPayerTelephone());
            requestOrder.setPayerIDCard(orderModel.getPayerIDCard());
            requestOrder.setPid(orderModel.getPid());
            requestOrder.setOrderNo(orderModel.getOrderNo());
            requestOrder.setOrderAmount(Long.parseLong(orderModel.getOrderAmount()));
            requestOrder.setOrderCurrency(orderModel.getOrderCurrency());
            requestOrder.setOrderDatetime(orderModel.getOrderDatetime()
                    .replace("-", "")
                    .replace(" ", "")
                    .replace(":", ""));
            requestOrder.setOrderExpireDatetime(orderModel.getOrderExpireDatetime());
            requestOrder.setProductName(orderModel.getProductName());
            if (null != orderModel.getProductPrice()
                    && !"".equals(orderModel.getProductPrice()))
            {
                requestOrder.setProductPrice(Long.parseLong(orderModel.getProductPrice()));
            }
            if (null != orderModel.getProductNum()
                    && !"".equals(orderModel.getProductNum()))
            {
                requestOrder.setProductNum(Integer.parseInt(orderModel.getProductNum()));
            }
            requestOrder.setProductId(orderModel.getProductId());
            requestOrder.setProductDesc(orderModel.getProductDesc());
            requestOrder.setExt1(orderModel.getExt1());
            requestOrder.setExt2(orderModel.getExt2());
            requestOrder.setExtTL(orderModel.getExtTL());//通联商户拓展业务字段，在v2.2.0版本之后才使用到的，用于开通分账等业务
            requestOrder.setPan(orderModel.getPan());
            requestOrder.setTradeNature(orderModel.getTradeNature());
            requestOrder.setKey(orderModel.getKey()); //key为MD5密钥，密钥是在通联支付网关会员服务网站上设置。
            
            //            String strSrcMsg = requestOrder.getSrc(); // 此方法用于debug，测试通过后可注释。
            strSignMsg = requestOrder.doSign(); // 签名，设为signMsg字段值。
            //            System.out.println(strSrcMsg + "======" + strSignMsg);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return strSignMsg;
    }
    
    @Override
    public String getQuerySignMsg(OrderModel orderMember) throws Exception
    {
        StringBuffer bufSignSrc = new StringBuffer();
        StringUtil.appendSignPara(bufSignSrc,
                "merchantId",
                orderMember.getMerchantId());
        StringUtil.appendSignPara(bufSignSrc, "version", "v1.5");
        StringUtil.appendSignPara(bufSignSrc,
                "signType",
                orderMember.getSignType());
        StringUtil.appendSignPara(bufSignSrc,
                "orderNo",
                orderMember.getOrderNo());
        StringUtil.appendSignPara(bufSignSrc,
                "orderDatetime",
                orderMember.getOrderDatetime());
        StringUtil.appendSignPara(bufSignSrc,
                "queryDatetime",
                orderMember.getQueryTime());
        StringUtil.appendLastSignPara(bufSignSrc, "key", orderMember.getKey());
        String signSrc = bufSignSrc.toString();
        String sign = SecurityUtil.MD5Encode(bufSignSrc.toString());
        return sign;
    }
    
    @Override
    public TLBillEntity getBIll(OrderModel orderMember) throws Exception
    {
        TLBillEntity tl = new TLBillEntity();
        HttpClient httpclient = new HttpClient();
        PostMethod postmethod = new PostMethod(orderMember.getServerip());
        NameValuePair[] date = {
                new NameValuePair("merchantId", orderMember.getMerchantId()),
                new NameValuePair("version", "v1.5"),
                new NameValuePair("signType", orderMember.getSignType()),
                new NameValuePair("orderNo", orderMember.getOrderNo()),
                new NameValuePair("orderDatetime",
                        orderMember.getOrderDatetime()),
                new NameValuePair("queryDatetime", orderMember.getQueryTime()),
                new NameValuePair("signMsg", orderMember.getSignMsg()) };
        
        postmethod.setRequestBody(date);
        int responseCode = httpclient.executeMethod(postmethod);
        
        Map<String, String> results = new HashMap<String, String>();
        String resultMsg = "";
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            String strResponse = postmethod.getResponseBodyAsString();
            
            // 解析查询返回结果
            strResponse = URLDecoder.decode(strResponse);
            //  Map<String, String> result = new HashMap<String, String>();
            String[] parameters = strResponse.split("&");
            for (int i = 0; i < parameters.length; i++)
            {
                String msg = parameters[i];
                int index = msg.indexOf('=');
                if (index > 0)
                {
                    String name = msg.substring(0, index);
                    String value = msg.substring(index + 1);
                    results.put(name, value);
                }
            }
            
            // 查询结果会以Server方式通知商户(同支付返回)；
            // 若无法取得Server通知结果，可以通过解析查询返回结果，更新订单状态(参考如下).
            if (null != results.get("ERRORCODE"))
            {
                // 未查询到订单
                System.out.println("ERRORCODE=" + results.get("ERRORCODE"));
                System.out.println("ERRORMSG=" + results.get("ERRORMSG"));
                resultMsg = results.get("ERRORMSG");
                
            }
            else
            {
                // 查询到订单
                String payResult = results.get("payResult");
                if (payResult.equals("1"))
                {
                    System.out.println("订单付款成功！");
                    
                    // 支付成功，验证签名
                    PaymentResult paymentResult = new PaymentResult();
                    paymentResult.setMerchantId(results.get("merchantId"));
                    paymentResult.setVersion(results.get("version"));
                    paymentResult.setLanguage(results.get("language"));
                    paymentResult.setSignType(results.get("signType"));
                    paymentResult.setPayType(results.get("payType"));
                    paymentResult.setIssuerId(results.get("issuerId"));
                    paymentResult.setPaymentOrderId(results.get("paymentOrderId"));
                    paymentResult.setOrderNo(results.get("orderNo"));
                    paymentResult.setOrderDatetime(results.get("orderDatetime"));
                    paymentResult.setOrderAmount(results.get("orderAmount"));
                    paymentResult.setPayAmount(results.get("payAmount"));
                    paymentResult.setPayDatetime(results.get("payDatetime"));
                    paymentResult.setExt1(results.get("ext1"));
                    paymentResult.setExt2(results.get("ext2"));
                    paymentResult.setPayResult(results.get("payResult"));
                    paymentResult.setErrorCode(results.get("errorCode"));
                    paymentResult.setReturnDatetime(results.get("returnDatetime"));
                    paymentResult.setKey(orderMember.getKey());
                    paymentResult.setSignMsg(results.get("signMsg"));
                    paymentResult.setCertPath("d:\\cert\\TLCert-test.cer");
                    
                    tl.setMerchantBillNo(results.get("orderNo"));
                    tl.setMerchantNo(results.get("merchantId"));
                    tl.setTlBillNo(results.get("paymentOrderId"));
                    tl.setSubmitTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(results.get("orderDatetime")));
                    tl.setBillMoney(new BigDecimal(results.get("orderAmount")));
                    tl.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(results.get("payDatetime")));
                    tl.setActualMoney(new BigDecimal(results.get("payAmount")));
                    tl.setRemark1(results.get("ext1"));
                    tl.setRemark2(results.get("ext2"));
                    tl.setStarus(Integer.valueOf(results.get("payResult")));
                    
                    boolean verifyResult = paymentResult.verify();
                    
                    if (verifyResult)
                    {
                        System.out.println("验签成功！商户更新订单状态。");
                        resultMsg = "订单支付成功，验签成功！商户更新订单状态。";
                    }
                    else
                    {
                        System.out.println("验签失败！");
                        resultMsg = "订单支付成功，验签失败！";
                    }
                    
                }
                else
                {
                    System.out.println("订单尚未付款！");
                    resultMsg = "订单尚未付款！";
                }
            }
            
        }
        return tl;
    }
    
    @Override
    public String PostWithdraw(String url, boolean isFront) throws Exception
    {
        return null;
    }
    
}
