package com.herongwang.p2p.manage.controller.repayplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.herongwang.p2p.entity.financing.FinancingOrdersEntity;
import com.herongwang.p2p.entity.repayPlan.RepayPlanEntity;
import com.herongwang.p2p.loan.util.Common;
import com.herongwang.p2p.loan.util.RsaHelper;
import com.herongwang.p2p.manage.controller.BaseController;
import com.herongwang.p2p.model.loan.LoanInfoBean;
import com.herongwang.p2p.model.loan.LoanTransferReturnBean;
import com.herongwang.p2p.model.post.TransferModel;
import com.herongwang.p2p.service.financing.IFinancingOrdersService;
import com.herongwang.p2p.service.impl.post.PostServiceImpl;
import com.herongwang.p2p.service.loan.ILoanService;
import com.herongwang.p2p.service.repayplan.IRepayPlanService;
import com.sxj.util.exception.WebException;
import com.sxj.util.logger.SxjLogger;

@Controller
@RequestMapping("/repayPlan")
public class RepayPlanController extends BaseController
{
    
    @Autowired
    private IRepayPlanService repayPlanService;
    
    @Autowired
    IFinancingOrdersService financingOrdersService;
    
    @Autowired
    private PostServiceImpl postService;
    
    @Autowired
    private ILoanService loanService;
    
    /**
     * 还款计划
     * @param entity
     * @return
     */
    @RequestMapping("/queryRepayPlan")
    public String queryRepayPlan(String debtId, ModelMap map)
            throws WebException
    {
        try
        {
            FinancingOrdersEntity order = financingOrdersService.getOrderByDebtId(debtId);
            List<RepayPlanEntity> list = repayPlanService.queryRepayPlan(order);
            map.put("repayPlan", list);
            //map.put("orderId", entity.getOrderId());
            map.put("orderId", order.getOrderId());
            map.put("debtId", debtId);
            return "manage/repayPlan/repayPlan";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error("查询还款计划信息错误", e, this.getClass());
            throw new WebException("查询还款计划信息错误");
        }
    }
    
    /**
     * 验证余额
     * @param session
     * @param ids
     * @return
     * @throws WebException
     */
    @RequestMapping("getBalance")
    public @ResponseBody Map<String, Object> getBalance(String ids,
            String orderId, String debtId, HttpServletRequest request)
            throws WebException
    {
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            String flag = repayPlanService.getBalance(ids,
                    orderId,
                    debtId,
                    getBasePath(request));
            map.put("flag", flag);
            return map;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("验证余额错误", e);
        }
        
    }
    
    /**
     * 还款
     * @param session
     * @param ids
     * @return
     * @throws WebException
     */
    @RequestMapping("saveRepayPlan")
    public String saveRepayPlan(String ids, String orderId, String debtId,
            RedirectAttributes ra) throws WebException
    {
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            /*List<Object> rdslist = Common.JSONDecodeList(ids, String.class);
            String[] rids = new String[rdslist.size()];
            for (int i = 0; i < rdslist.size(); i++)
            {
                rids[i] = (String) rdslist.get(i);
            }*/
            ids = Common.UrlDecoder(ids, "utf-8");
            String[] rids = ids.split(",");
            FinancingOrdersEntity fo = new FinancingOrdersEntity();
            fo.setDebtId(debtId);
            fo.setOrderId(orderId);
            List<RepayPlanEntity> list = repayPlanService.queryRepayPlan(fo);
            String[] listIds = new String[rids.length];
            int i = 0;
            for (RepayPlanEntity rp : list)
            {
                for (String xh : rids)
                {
                    if (xh.equals(rp.getSequence()))
                    {
                        listIds[i] = rp.getPlanId();
                        i++;
                    }
                }
            }
            String flag = repayPlanService.saveRepayPlan(listIds,
                    orderId,
                    debtId);
            map.put("flag", flag);
            ra.addAttribute("orderId", orderId);
            ra.addAttribute("debtId", debtId);
            return "redirect:/repayPlan/queryRepayPlan.htm";
            
        }
        catch (Exception e)
        {
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("还款错误", e);
        }
        
    }
    
    /**
     * 转账
     */
    @RequestMapping("transfer")
    public String transfer(ModelMap map, String ids, String orderId,
            String debtId, String payManId, HttpServletRequest request)
            throws WebException
    {
        try
        {
            String privatekey = Common.privateKeyPKCS8;
            List<Map<String, String>> list = repayPlanService.getTransferList(ids,
                    orderId,
                    debtId);
            List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
            if (list.size() > 0)
            {
                for (Map<String, String> maplist : list)
                {
                    LoanInfoBean mlib = new LoanInfoBean();
                    mlib.setLoanOutMoneymoremore(payManId);//付款人
                    mlib.setLoanInMoneymoremore(maplist.get("moneymoremoreId"));//收款人
                    mlib.setOrderNo(maplist.get("orderNo"));//订单号,投资人收益明细的ID
                    mlib.setBatchNo(maplist.get("debtNo"));//标号
                    mlib.setAmount(maplist.get("amount"));
                    mlib.setTransferName("还款");
                    listmlib.add(mlib);
                }
            }
            String LoanJsonList = Common.JSONEncode(listmlib);
            TransferModel tf = new TransferModel();
            tf.setPlatformMoneymoremore("p1190");
            tf.setTransferAction("2");
            tf.setAction("1");
            tf.setTransferType("2");
            tf.setNeedAudit("1");
            tf.setReturnURL(getBasePath(request)
                    + "repayPlan/transferReturn.htm");
            tf.setNotifyURL(getBasePath(request)
                    + "repayPlan/transferNotify.htm");
            tf.setRemark1(Common.UrlEncoder(ids, "utf-8"));//还款单的ID
            tf.setRemark2(orderId);//投资订单号
            tf.setRemark3(debtId);//标的ID
            String dataStr = LoanJsonList + tf.getPlatformMoneymoremore()
                    + tf.getTransferAction() + tf.getAction()
                    + tf.getTransferType() + tf.getNeedAudit()
                    + tf.getRandomTimeStamp() + tf.getRemark1()
                    + tf.getRemark2() + tf.getRemark3() + tf.getReturnURL()
                    + tf.getNotifyURL();
            RsaHelper rsa = RsaHelper.getInstance();
            String SignInfo = rsa.signData(dataStr, privatekey);
            LoanJsonList = Common.UrlEncoder(LoanJsonList, "utf-8");
            tf.setLoanJsonList(LoanJsonList);
            tf.setSignInfo(SignInfo);
            map.put("model", tf);
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("投资转账失败", e);
        }
        return "manage/repayPlan/loantransfer";
    }
    
    /**
     * 转账返回页面
     */
    @RequestMapping("transferReturn")
    public String transferReturn(LoanTransferReturnBean lr, ModelMap map,
            RedirectAttributes ra) throws WebException
    {
        try
        {
            loanService.addOrder(Common.JSONEncode(lr),
                    "LoanTransferReturnBean",
                    "转账页面返回Model");
            if ("88".equals(lr.getResultCode()))
            {
                ra.addAttribute("ids", lr.getRemark1());
                ra.addAttribute("orderId", lr.getRemark2());
                ra.addAttribute("debtId", lr.getRemark3());
                return "redirect:/repayPlan/saveRepayPlan.htm";
            }
            map.put("model", lr);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("还款失败", e);
        }
        return "manage/repayPlan/transferreturn";
    }
    
    /**
     * 转账后台通知
     */
    @RequestMapping("transferNotify")
    public @ResponseBody String transferNotify(LoanTransferReturnBean lr)
            throws WebException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            SxjLogger.error(e.getMessage(), e, this.getClass());
            throw new WebException("后台通知还款失败", e);
        }
        return "";
    }
}
