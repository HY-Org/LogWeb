package org.hy.log.appInterfaces.restful;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hy.log.appInterfaces.ILogService;
import org.hy.log.common.BaseAppMessage;
import org.hy.log.msg.MsgLogInfoRequest;
import org.hy.log.msg.MsgLogRegisterRequest;
import org.hy.log.service.ILogInfoService;
import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.StringHelp;
import org.hy.common.xml.plugins.AppMessage;





/**
 * 日志信息的接口
 * 
 * 接口服务端: 我方
 * 接口客户端: 通用
 * 接口协议为: Restful + Json
 * 对方联调人: -
 * 相关文档有: -
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-12-12
 * @version     v1.0 
 */
@Path("log")
public class LogServiceImpl extends BaseAppMessage implements ILogService
{

    private ILogInfoService logInfoService = (ILogInfoService)this.getObject("LogInfoService");
    
    
    
    /**
     * 记录日志
     * 
     * 接口编号：Log.Create.A001
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-12
     * @version     v1.0
     *
     * @param i_AppMsg
     * @return
     */
    @Path("log")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public AppMessage<Object> log(AppMessage<MsgLogInfoRequest> i_AppMsg)
    {
        if ( i_AppMsg == null )
        {
            return error();
        }
        if ( !"1.0".equals(i_AppMsg.getSidv()) )
        {
            return errorVersion(i_AppMsg);
        }
        
        AppMessage<Object> v_Ret = i_AppMsg.clone();
        if ( i_AppMsg.getBody() == null )
        {
            return error("130101A001" ,v_Ret);
        }
        
        MsgLogInfoRequest v_MsgBody = null;
        try
        {
            v_MsgBody = i_AppMsg.getBody(); 
        }
        catch (Exception exce)
        {
            return error("130101A001" ,v_Ret);
        }
        
        if ( Help.isNull(v_Ret.getSysid()) )
        {
            return error("130101A011" ,v_Ret);
        }
        
        if ( v_Ret.getSysid().length() > 15 )
        {
            return error("130101A021" ,v_Ret);
        }
        
        if ( StringHelp.isChinese(v_Ret.getSysid()) )
        {
            return error("130101A031" ,v_Ret);
        }
        
        if ( !StringHelp.isABCNumber(v_Ret.getSysid()) )
        {
            return error("130101A041" ,v_Ret);
        }
        
        if ( v_MsgBody.getValidDataCount() <= 0 )
        {
            return error("130101A051" ,v_Ret);
        }
        
        if ( v_MsgBody.getOperationTime() != null )
        {
            Date v_MinDate = Date.getNowTime().getHours(-10);
            Date v_MaxDate = Date.getNowTime().getHours(+10);
            Date v_OptTime = v_MsgBody.getOperationTime();
            
            if ( v_OptTime.getTime() < v_MinDate.getTime()  )
            {
                return error("130101A061" ,v_Ret);
            }
            
            if ( v_OptTime.getTime() > v_MaxDate.getTime()  )
            {
                return error("130101A071" ,v_Ret);
            }
        }
        
        
        try
        {
            Return<Object> v_ExecRet = this.logInfoService.log(i_AppMsg);
            
            if ( v_ExecRet.booleanValue() )
            {
                v_Ret.setBody("");
                v_Ret.setResult(true);
            }
            else
            {
                return error(v_ExecRet.paramStr ,v_Ret);
            }
        }
        catch (Exception exce)
        {
            return error("130101A081" ,v_Ret);
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 1. 注册者的注册动作
     * 2. 注册者的变动注册配置的动作
     * 
     * 接口编号：Log.Register.A001
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @param i_AppMsg
     * @return
     */
    @Path("register")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public AppMessage<Object> register(AppMessage<MsgLogRegisterRequest> i_AppMsg)
    {
        if ( i_AppMsg == null )
        {
            return error();
        }
        if ( !"1.0".equals(i_AppMsg.getSidv()) )
        {
            return errorVersion(i_AppMsg);
        }
        
        AppMessage<Object> v_Ret = i_AppMsg.clone();
        if ( i_AppMsg.getBody() == null )
        {
            return error("130102A001" ,v_Ret);
        }
        
        MsgLogRegisterRequest v_MsgBody = null;
        try
        {
            v_MsgBody = i_AppMsg.getBody(); 
        }
        catch (Exception exce)
        {
            return error("130102A001" ,v_Ret);
        }
        
        if ( Help.isNull(v_Ret.getSysid()) )
        {
            return error("130102A011" ,v_Ret);
        }
        
        if ( v_Ret.getSysid().length() > 15 )
        {
            return error("130102A021" ,v_Ret);
        }
        
        if ( StringHelp.isChinese(v_Ret.getSysid()) )
        {
            return error("130102A031" ,v_Ret);
        }
        
        if ( !StringHelp.isABCNumber(v_Ret.getSysid()) )
        {
            return error("130102A041" ,v_Ret);
        }
        
        if ( v_MsgBody.getRegisterTypeEnum() == null )
        {
            return error("130102A051" ,v_Ret);
        }
        
        
        try
        {
            Return<Object> v_ExecRet = this.logInfoService.register(i_AppMsg);
            
            if ( v_ExecRet.booleanValue() )
            {
                v_Ret.setBody("");
                v_Ret.setResult(true);
            }
            else
            {
                return error(v_ExecRet.paramStr ,v_Ret);
            }
        }
        catch (Exception exce)
        {
            return error("130102B041" ,v_Ret);
        }
        
        return v_Ret;
    }
    
}
