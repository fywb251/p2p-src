package com.herongwang.p2p.website.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.sxj.util.exception.SystemException;
import com.sxj.util.logger.SxjLogger;

public class BaseController
{
    
    public static final String LOGIN = "site/member/login";
    
    // public static final String INDEX = "site/index";
    
    public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
    
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
    }
    
    protected String getBasePath(HttpServletRequest request)
    {
        return request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + request.getContextPath() + "/";
    }
    
    //    protected SupervisorPrincipal getLoginInfo(HttpSession session)
    //    {
    //        Object object = session.getAttribute("userinfo");
    //        if (object != null)
    //        {
    //            SupervisorPrincipal userBean = (SupervisorPrincipal) object;
    //            return userBean;
    //        }
    //        else
    //        {
    //            return null;
    //        }
    //        
    //    }
    
    protected void getValidError(BindingResult result) throws SystemException
    {
        if (result.hasErrors())
        {
            String message = "";
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors)
            {
                if (error == null)
                {
                    continue;
                }
                message = message + error.getDefaultMessage();
            }
            SxjLogger.error("Nested path [" + result.getNestedPath()
                    + "] has errors [" + message + "]", this.getClass());
            throw new SystemException(message);
        }
    }
    
}