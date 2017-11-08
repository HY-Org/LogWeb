package org.hy.log.common;

import java.util.List;
import java.util.Map;

import org.hy.log.msg.MsgErrorResponse;
import org.hy.log.service.impl.LogInfoService;
import org.hy.common.Help;
import org.hy.common.I18N;
import org.hy.common.PartitionMap;
import org.hy.common.StringHelp;
import org.hy.common.TablePartition;
import org.hy.common.app.Param;
import org.hy.common.thread.ThreadPool;
import org.hy.common.xml.XJava;
import org.hy.common.xml.plugins.AppInitConfig;





/**
 * Web初始化信息
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-09-12
 * @version     v1.0  
 */
public final class InitConfig extends AppInitConfig
{
    
    private static boolean $Init = false;
    
    private String xmlRoot;
    
    
    
    public InitConfig()
    {
        this(Help.getWebINFPath());
    }
    
    
    
    public InitConfig(String i_XmlRoot)
    {
        this.xmlRoot = i_XmlRoot;
        init();
    }
    
    
    
    @SuppressWarnings("unchecked")
    private synchronized void init()
    {
        if ( !$Init )
        {
            $Init = true;
            
            try
            {
                this.initW("sys.Config.xml" ,this.xmlRoot);
                this.initW("startup.Config.xml" ,this.xmlRoot);
                this.initW((List<Param>)XJava.getObject("StartupConfig") ,this.xmlRoot);
                this.initW(((Param)XJava.getObject("RootPackageName")).getValue());
                init_TPool();
                this.initW("job.Config.xml" ,this.xmlRoot);
            }
            catch (Exception exce)
            {
                System.out.println(exce.getMessage());
                exce.printStackTrace();
            }
            
            
            init_I18NError();
            
            
            // 容灾机制：将本地缓存的所有日志消息全部入库。
            ((LogInfoService)XJava.getObject("LogInfoService")).executeLogs();
        }
    }
    
    
    
    @SuppressWarnings("unchecked")
    private void init_I18NError()
    {
        Map<String ,MsgErrorResponse>          v_AppErrors     = (Map<String ,MsgErrorResponse>)XJava.getObject("AppInterfaces_Error");
        PartitionMap<String ,Param>            v_Mappings      = (PartitionMap<String ,Param>)  XJava.getObject("AppInterface_MappingError");
        PartitionMap<String ,MsgErrorResponse> v_AppErrorsPart = new TablePartition<String ,MsgErrorResponse>();
        int                                    v_AppECount     = v_AppErrors.size();
        I18N                                   v_I18NError     = (I18N)XJava.getObject("I18N_Error");
        List<Param>                            v_Params        = v_I18NError.getITextInfos("cn");
        int                                    v_MappingLen    = v_Mappings.keySet().iterator().next().length();
        
        for (Param v_Param : v_Params)
        {
            MsgErrorResponse v_MsgError = new MsgErrorResponse(v_Param);
            String           v_Prefix   = v_MsgError.getErrorCode().substring(0 ,v_MappingLen);
            List<Param>      v_Prefixs = v_Mappings.get(v_Prefix);
            
            if ( !Help.isNull(v_Prefixs) )
            {
                for (Param v_Mapping : v_Prefixs)
                {
                    v_AppErrorsPart.putRow(v_Mapping.getValue() ,v_MsgError);
                }
            }
            
            v_AppErrors.put(v_MsgError.getErrorCode() ,v_MsgError);
        }
        
        XJava.putObject("AppInterfaces_ErrorPart" ,v_AppErrorsPart);
        int v_LPad = ("" + v_AppErrors.size()).length();
        System.out.println("Load App Interface Error：手工定义 " + StringHelp.lpad(v_AppECount                        ,v_LPad ," ") + " 个；\n"
                         + "Load App Interface Error：自动加载 " + StringHelp.lpad((v_AppErrors.size() - v_AppECount) ,v_LPad ," ") + " 个；\n"
                         + "Load App Interface Error：合计定义 " + StringHelp.lpad(v_AppErrors.size()                 ,v_LPad ," ") + " 个。");
    }
    
    
    
    private void init_TPool()
    {
        ThreadPool.setMaxThread(    this.getIntConfig("TPool_MaxThread"));
        ThreadPool.setMinThread(    this.getIntConfig("TPool_MinThread"));
        ThreadPool.setMinIdleThread(this.getIntConfig("TPool_MinIdleThread"));
        ThreadPool.setIntervalTime( this.getIntConfig("TPool_IntervalTime"));
        ThreadPool.setIdleTimeKill( this.getIntConfig("TPool_IdleTimeKill"));
    }
    
    
    
    private int getIntConfig(String i_XJavaID)
    {
        return Integer.parseInt(XJava.getParam(i_XJavaID).getValue());
    }
    
}
