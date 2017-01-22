package org.hy.log.appInterfaces.restful;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hy.log.common.BaseAppMessage;
import org.hy.log.common.BaseEntity;
import org.hy.log.appInterfaces.IServerService;
import org.hy.log.appInterfaces.InterfaceInfo;
import org.hy.log.msg.MsgErrorResponse;
import org.hy.log.service.IServerInfoService;
import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.xml.XJSON;
import org.hy.common.xml.plugins.AppInterface;
import org.hy.common.xml.plugins.AppMessage;





/**
 * 服务运行信息的接口
 * 
 * 接口服务端: 我方
 * 接口客户端: 通用
 * 接口协议为: Restful + Json
 * 对方联调人: -
 * 相关文档有: -
 *
 * @author      ZhengWei(HY)、WangZhanBin
 * @createDate  2014-11-14
 * @version     v1.0
 */
@Path("server")
public class ServerServiceImpl extends BaseAppMessage implements IServerService
{
    
    private IServerInfoService serverInfoService = (IServerInfoService)this.getObject("ServerInfoService"); 
    
    
    
    /**
     * 查看服务信息及判断服务是否存活
     * 
     * 接口的编号: Order.Server.A001
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-11-14
     * @version     v1.0
     *
     * @return
     *
     * @see org.hy.log.appInterfaces.IServerService#isok()
     */
    @Path("info")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Override
    public String info()
    {
        StringBuffer v_Buffer = new StringBuffer();
        
        v_Buffer.append("App Web ID: ").append(this.getAppWebID());
        v_Buffer.append("<br><br>");
        v_Buffer.append("Startup time: ").append(this.getStartupTime().getFullMilli());
        v_Buffer.append("<br>");
        v_Buffer.append("System time: ").append(Date.getNowTime().getFullMilli());
        v_Buffer.append("<br>");
        v_Buffer.append("Cache Type: ").append(this.getCacheType());
        v_Buffer.append("<br><br>");
        v_Buffer.append("Version: v").append(this.getVersion());
        
        return v_Buffer.toString();
    }
    
    
    
    /**
     * 查看服务的接口错误列表
     * 
     * 接口的编号: Order.Server.A002
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-11-19
     * @version     v1.0
     *
     * @return
     * @throws UnsupportedEncodingException 
     *
     * @see org.hy.log.appInterfaces.IServerService#showError()
     */
    @Path("showError")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Override
    @SuppressWarnings("unchecked")
    public String showError()
    {
        Map<String ,MsgErrorResponse> v_AppErrors = (Map<String ,MsgErrorResponse>)this.getObject("AppInterfaces_Error");
        StringBuffer                  v_Buffer    = new StringBuffer();
        int                           v_Index     = 0;
        String                        v_Content   = this.getTemplateShowErrorContent();
        
        for (MsgErrorResponse v_MsgError : v_AppErrors.values())
        {
            v_Buffer.append(v_Content.replaceAll(":No"        ,String.valueOf(++v_Index))
                                     .replaceAll(":ErrorCode" ,v_MsgError.getErrorCode())
                                     .replaceAll(":ErrorInfo" ,v_MsgError.getErrorInfo()));
        }
        
        return this.getTemplateShowError().replaceAll(":Content" ,v_Buffer.toString());
    }
    
    
    
    /**
     * 查看服务提供的接口列表
     * 
     * 接口的编号: Order.Server.A003
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-11-19
     * @version     v1.0
     *
     * @return
     *
     * @see org.hy.log.appInterfaces.IServerService#showInterface()
     */
    @Path("showInterface")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Override
    @SuppressWarnings("unchecked")
    public String showInterface()
    {
        Map<String ,InterfaceInfo> v_AppRestfuls = (Map<String ,InterfaceInfo>)this.getObject("AppRestfuls");
        StringBuffer               v_Buffer      = new StringBuffer();
        int                        v_Index       = 0;
        String                     v_Content     = this.getTemplateShowInterfaceContent();
        
        for (InterfaceInfo v_Interface : v_AppRestfuls.values())
        {
            if ( !Help.isNull(v_Interface.getInnerNo()) )
            {
                String v_ParamURL   = "";
                String v_ParamTitle = "";
                String v_ReturnURL   = "";
                String v_ReturnTitle = "";
                
                if ( "POST".equals(v_Interface.getHttpMethod()) && !Help.isNull(v_Interface.getRequestClass()) )
                {
                    v_ParamURL   = "showInterfaceParam?no=" + v_Interface.getName();
                    v_ParamTitle = "查看入参";
                }
                
                if ( "POST".equals(v_Interface.getHttpMethod())  && !Help.isNull(v_Interface.getResponseClass()) )
                {
                    v_ReturnURL   = "showInterfaceReturn?no=" + v_Interface.getName();
                    v_ReturnTitle = "查看出参";
                }
                
                v_Buffer.append(v_Content.replaceAll(":No"          ,String.valueOf(++v_Index))
                                         .replaceAll(":Name"        ,v_Interface.getName())
                                         .replaceAll(":Comment"     ,v_Interface.getComment())
                                         .replaceAll(":HttpMethod"  ,v_Interface.getHttpMethod())
                                         .replaceAll(":Version"     ,v_Interface.getVersion())
                                         .replaceAll(":InnerNo"     ,v_Interface.getInnerNo())
                                         .replaceAll(":ParamURL"    ,v_ParamURL)
                                         .replaceAll(":ParamTitle"  ,v_ParamTitle)
                                         .replaceAll(":ReturnURL"   ,v_ReturnURL)
                                         .replaceAll(":ReturnTitle" ,v_ReturnTitle)
                               );
            }
        }
        
        return this.getTemplateShowInterface().replaceAll(":Content" ,v_Buffer.toString());
    }
    
    
    
    /**
     * 初始化数据库对象
     * 
     * 接口的编号: Order.Server.A004
     * 
     * 这应当是个安全的接口。只有探测到表不存在时，才会创建表。
     * 由脚本编写者来保证没有Drop、Delete、Update语句，只应有Create、Insert语句
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-28
     * @version     v1.0
     *
     * @return
     */
    @Path("initDB")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Override
    public String initDB()
    {
        List<Return<String>> v_Rets    = this.serverInfoService.initDB();
        StringBuffer         v_Buffer  = new StringBuffer();
        String               v_Content = this.getTemplateInitDBContent();
        int                  v_Index   = 0;
        
        if ( Help.isNull(v_Rets) )
        {
            return "数据库配置或访问权限异常";
        }
        
        for (Return<String> v_Ret : v_Rets)
        {
            String v_Info = (v_Ret.paramInt == 0 ? "<font color='orange'>已存在,放弃执行：" : (v_Ret.get() ? "<font color='green'>成功执行：" : "<font color='red'>执行异常：")) + "</font>";
            
            v_Buffer.append(v_Content.replaceAll(":No"      ,String.valueOf(++v_Index))
                                     .replaceAll(":Object"  ,v_Ret.paramStr)
                                     .replaceAll(":Content" ,v_Info + v_Ret.paramObj)
                           );
        }
        
        return this.getTemplateInitDB().replaceAll(":Content" ,v_Buffer.toString());
    }
    
    
    
    /**
     * 获取接口的输入参数信息
     * 
     * 接口的编号: Order.Server.A005
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-01
     * @version     v1.0
     *
     * @param i_InterfaceNo  接口对外的访问编号
     * @return
     */
    @Path("showInterfaceParam")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Override
    @SuppressWarnings("unchecked")
    public String showInterfaceParam(@QueryParam("no") String i_InterfaceNo)
    {
        if ( Help.isNull(i_InterfaceNo) )
        {
            return "";
        }
        
        try
        {
            Map<String ,AppInterface>  v_Interfaces   = (Map<String ,AppInterface>) this.getObject("AppInterfaces");
            Map<String ,InterfaceInfo> v_AppRestfuls  = (Map<String ,InterfaceInfo>)this.getObject("AppRestfuls");
            AppInterface               v_AppInterface = v_Interfaces.get(i_InterfaceNo.trim());
            InterfaceInfo              v_Interface    = v_AppRestfuls.get(i_InterfaceNo.trim());
            List<MsgErrorResponse>     v_MsgErrors    = this.getErrors(v_Interface.getInnerNo());
            BaseEntity                 v_Msg          = (BaseEntity)Class.forName(v_AppInterface.getClassName()).newInstance();
            XJSON                      v_XJSON        = new XJSON();
            String                     v_ErrorContent = this.getTemplateShowErrorContent();
            StringBuffer               v_Buffer       = new StringBuffer();
            int                        v_ErrorIndex   = 0;
            
            v_XJSON.setReturnNVL(true);
            v_XJSON.setAccuracy(true);
            
            String v_Content = v_XJSON.parser(v_Msg.gatDocsInfoForever(v_Interface.getInnerNo())).toJSONString();
            String v_Title   = v_Interface.getName() + " - " + v_Interface.getInnerNo() + " - " + v_Interface.getComment();
            
            for (MsgErrorResponse v_MsgError : v_MsgErrors)
            {
                v_Buffer.append(v_ErrorContent.replaceAll(":No"        ,String.valueOf(++v_ErrorIndex))
                                              .replaceAll(":ErrorCode" ,v_MsgError.getErrorCode())
                                              .replaceAll(":ErrorInfo" ,v_MsgError.getErrorInfo()));
            }
            
            return this.getTemplateShowInterfaceParam().replaceAll(":Content"        ,getAppInterfaceSysParam().replaceAll(":BODY" ,v_Content))
                                                       .replaceAll(":InterfaceTitle" ,v_Title)
                                                       .replaceAll(":ErrorContent"   ,v_Buffer.toString())
                                                       .replaceAll(":IFType"         ,"请求");
        }
        catch (Exception exce)
        {
            return this.getTemplateShowInterfaceParam().replaceAll(":Content" ,"开发人员太忙，还没了得急整理，请稍后几日再来查看。感谢您的支持。");
        }
    }
    
    
    
    /**
     * 获取接口的返回输出参数信息
     * 
     * 接口的编号: Order.Server.A006
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-24
     * @version     v1.0
     *
     * @param i_InterfaceNo  接口对外的访问编号
     * @return
     */
    @Path("showInterfaceReturn")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Override
    @SuppressWarnings("unchecked")
    public String showInterfaceReturn(@QueryParam("no") String i_InterfaceNo)
    {
        if ( Help.isNull(i_InterfaceNo) )
        {
            return "";
        }
        
        try
        {
            Map<String ,Param>         v_IFsReturn    = (Map<String ,Param>)this.getObject("AppInterfacesRetrun");
            Map<String ,InterfaceInfo> v_AppRestfuls  = (Map<String ,InterfaceInfo>)this.getObject("AppRestfuls");
            InterfaceInfo              v_Interface    = v_AppRestfuls.get(i_InterfaceNo.trim());
            Param                      v_IFReturn     = v_IFsReturn.get(i_InterfaceNo.trim());
            XJSON                      v_XJSON        = new XJSON();
            Map<String ,Object>        v_Docs         = null;
            List<MsgErrorResponse>     v_MsgErrors    = this.getErrors(v_Interface.getInnerNo());
            String                     v_ErrorContent = this.getTemplateShowErrorContent();
            StringBuffer               v_Buffer       = new StringBuffer();
            int                        v_ErrorIndex   = 0;
            
            v_XJSON.setReturnNVL(true);
            v_XJSON.setAccuracy(true);
            
            String v_ReturnC = "";
            String v_Title   = v_Interface.getName() + " - " + v_Interface.getInnerNo() + " - " + v_Interface.getComment();
            
            if ( v_IFReturn != null )
            {
                if ( !Help.isNull(v_IFReturn.getValue()) )
                {
                    BaseEntity v_MsgReturn = (BaseEntity)Class.forName(v_IFReturn.getValue()).newInstance();
                    v_Docs    = v_MsgReturn.gatDocsInfoForever(v_Interface.getInnerNo());
                    if ( !Help.isNull(v_Docs) )
                    {
                        v_ReturnC = v_XJSON.parser(v_MsgReturn.gatDocsInfoForever(v_Interface.getInnerNo())).toJSONString();
                        v_ReturnC = getAppInterfaceSysReturn().replaceAll(":BODY" ,v_ReturnC);
                    }
                    else
                    {
                        v_ReturnC = getAppInterfaceSysReturn().replaceAll(":BODY" ,"\"无消息体\"");
                    }
                }
                else
                {
                    v_ReturnC = getAppInterfaceSysReturn().replaceAll(":BODY" ,"\"无消息体\"");
                }
            }
            else
            {
                v_ReturnC = getAppInterfaceSysReturn().replaceAll(":BODY" ,"\"无消息体\"");
            }
            
            for (MsgErrorResponse v_MsgError : v_MsgErrors)
            {
                v_Buffer.append(v_ErrorContent.replaceAll(":No"        ,String.valueOf(++v_ErrorIndex))
                                              .replaceAll(":ErrorCode" ,v_MsgError.getErrorCode())
                                              .replaceAll(":ErrorInfo" ,v_MsgError.getErrorInfo()));
            }
            
            return this.getTemplateShowInterfaceParam().replaceAll(":Content"        ,v_ReturnC)
                                                       .replaceAll(":InterfaceTitle" ,v_Title)
                                                       .replaceAll(":ErrorContent"   ,v_Buffer.toString())
                                                       .replaceAll(":IFType"         ,"返回");
        }
        catch (Exception exce)
        {
            return this.getTemplateShowInterfaceParam().replaceAll(":Content" ,"开发人员太忙，还没了得急整理，请稍后几日再来查看。感谢您的支持。");
        }
    }
    
    
    
    /**
     * 获取接口访问量的概要统计数据 
     * 
     * 接口的编号: Order.Server.A007
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-04
     * @version     v1.0
     *
     * @return
     */
    @Path("showTotal")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Override
    @SuppressWarnings("unchecked")
    public String showTotal()
    {
        Map<String ,AppInterface>  v_Interfaces  = (Map<String ,AppInterface>)this.getObject("AppInterfaces");
        Map<String ,InterfaceInfo> v_AppRestfuls = (Map<String ,InterfaceInfo>)this.getObject("AppRestfuls");
        StringBuffer               v_Buffer      = new StringBuffer();
        int                        v_Index       = 0;
        String                     v_Content     = this.getTemplateShowTotalContent();
        long                       v_RequestCount = 0;
        long                       v_SuccessCount = 0;
        long                       v_TotalTimeLen = 0;
        double                     v_AvgTimeLen   = 0;
        
        for (InterfaceInfo v_Interface : v_AppRestfuls.values())
        {
            if ( v_Interfaces.containsKey(v_Interface.getName()) )
            {
                AppInterface v_AppIF = v_Interfaces.get(v_Interface.getName());
                
                v_AvgTimeLen = Help.round(Help.division(v_AppIF.getSuccessTimeLen().getSumValue() ,v_AppIF.getSuccessCount().getSumValue()) ,2);
                
                v_Buffer.append(v_Content.replaceAll(":No"           ,String.valueOf(++v_Index))
                                         .replaceAll(":Name"         ,v_AppIF.getName())
                                         .replaceAll(":RequestCount" ,String.valueOf(v_AppIF.getRequestCount().getSumValue()))
                                         .replaceAll(":SuccessCount" ,String.valueOf(v_AppIF.getSuccessCount().getSumValue()))
                                         .replaceAll(":FailCount"    ,String.valueOf(v_AppIF.getRequestCount().getSumValue() - v_AppIF.getSuccessCount().getSumValue()))
                                         .replaceAll(":ParamURL"     ,"showErrorLog?no=" + v_AppIF.getName())
                                         .replaceAll(":SumTime"      ,Date.toTimeLen(v_AppIF.getSuccessTimeLen().getSumValue()))
                                         .replaceAll(":AvgTime"      ,String.valueOf(v_AvgTimeLen))
                               );
                
                v_RequestCount += v_AppIF.getRequestCount().getSumValue();
                v_SuccessCount += v_AppIF.getSuccessCount().getSumValue();
                v_TotalTimeLen += v_AppIF.getSuccessTimeLen().getSumValue();
            }
        }
        
        v_AvgTimeLen = Help.round(Help.division(v_TotalTimeLen ,v_SuccessCount) ,2);
        
        v_Buffer.append(v_Content.replaceAll(":No"           ,String.valueOf(++v_Index))
                                 .replaceAll(":Name"         ,"合计")
                                 .replaceAll(":RequestCount" ,String.valueOf(v_RequestCount))
                                 .replaceAll(":SuccessCount" ,String.valueOf(v_SuccessCount))
                                 .replaceAll(":FailCount"    ,String.valueOf(v_RequestCount - v_SuccessCount))
                                 .replaceAll(":ParamURL"     ,"showErrorLog")
                                 .replaceAll(":SumTime"      ,Date.toTimeLen(v_TotalTimeLen))
                                 .replaceAll(":AvgTime"      ,String.valueOf(v_AvgTimeLen))
                       );
        
        return this.getTemplateShowTotal().replaceAll(":Content"   ,v_Buffer.toString())
                                          .replaceAll(":NameTitle" ,"接口访问标识")
                                          .replaceAll(":Title"     ,"接口访问量的概要统计");
    }
    
    
    
    /**
     * 获取数据库访问量的概要统计数据 
     * 
     * 接口的编号: Order.Server.A008
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-04
     * @version     v1.0
     *
     * @return
     */
    @Path("showTotalDB")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Override
    public String showTotalDB()
    {
        return StringHelp.replaceAll(getTemplateGoto() ,":Goto" ,"analyses/analyseDB");
    }
    
    
    
    /**
     * 获取错误日志
     * 
     * 接口的编号: Order.Server.A009
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-08
     * @version     v1.0
     *
     * @return
     */
    @Path("showErrorLog")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Override
    public String showErrorLog(@QueryParam("no") String i_InterfaceNo)
    {
        AppMessage<?> [] v_AppMsgs = null;
        StringBuffer     v_Buffer  = new StringBuffer();
        String           v_Content = this.getTemplateShowErrorLogContent();
        int              v_Index   = 0;
        
        if ( Help.isNull(i_InterfaceNo) )
        {
            v_AppMsgs = this.getLogErrorByCache();
        }
        else
        {
            v_AppMsgs = this.getLogErrorByCache(i_InterfaceNo);
        }
        
        if ( !Help.isNull(v_AppMsgs) )
        {
            for (AppMessage<?> v_AppMsg : v_AppMsgs)
            {
                if ( v_AppMsg != null )
                {
                    MsgErrorResponse v_ErrResp = this.error(v_AppMsg.getRc());
                    v_Buffer.append(v_Content.replaceAll(":No"          ,String.valueOf(++v_Index))
                                             .replaceAll(":Name"        ,v_AppMsg.getSid())
                                             .replaceAll(":ErrorCode"   ,v_AppMsg.getRc())
                                             .replaceAll(":ErrorInfo"   ,v_ErrResp.getErrorInfo())
                                             .replaceAll(":CreateTime"  ,v_AppMsg.gatCreateTime().getFullMilli())
                                             .replaceAll(":SerialNo"    ,v_AppMsg.getSerialNo())
                                             .replaceAll(":RequestInfo" ,v_AppMsg.gatMsg().replaceAll("(\\t)|(\\n)" ,""))
                                   );
                }
            }
        }
        return this.getTemplateShowErrorLog().replaceAll(":Content" ,v_Buffer.toString());
    }
    
    
    
    private String getTemplateShowError()
    {
        return this.getTemplateContent("template.showError.html");
    }
    
    
    
    private String getTemplateShowErrorContent()
    {
        return this.getTemplateContent("template.showErrorContent.html");
    }
    
    
    
    private String getTemplateShowInterface()
    {
        return this.getTemplateContent("template.showInterface.html");
    }
    
    
    
    private String getTemplateShowInterfaceContent()
    {
        return this.getTemplateContent("template.showInterfaceContent.html");
    }
    
    
    
    private String getTemplateInitDB()
    {
        return this.getTemplateContent("template.initDB.html");
    }
    
    
    
    private String getTemplateInitDBContent()
    {
        return this.getTemplateContent("template.initDBContent.html");
    }
    
    
    
    private String getTemplateShowInterfaceParam()
    {
        return this.getTemplateContent("template.showInterfaceParam.html");
    }
    
    
    
    private String getTemplateShowTotal()
    {
        return this.getTemplateContent("template.showTotal.html");
    }
    
    
    
    private String getTemplateShowTotalContent()
    {
        return this.getTemplateContent("template.showTotalContent.html");
    }
    
    
    
    private String getTemplateShowErrorLog()
    {
        return this.getTemplateContent("template.showErrorLog.html");
    }
    
    
    
    private String getTemplateShowErrorLogContent()
    {
        return this.getTemplateContent("template.showErrorLogContent.html");
    }
    
    
    
    private String getTemplateGoto()
    {
        return this.getTemplateContent("template.goto.html");
    }
    
}
