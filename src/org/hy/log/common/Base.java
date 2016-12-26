package org.hy.log.common;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.hy.log.model.Version;
import org.hy.common.Busway;
import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.file.FileHelp;
import org.hy.common.xml.XJava;
import org.hy.common.xml.XSQL;
import org.hy.common.xml.plugins.AppMessage;





/**
 * 基础类的基础
 * 
 * 主要实现最顶级能用的方法。
 * 
 * 注：除BaseEntity、BaseModel、BaseMsg、BaseEnum表示值对象的不继承外，其它基础类均继承此类。
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-11-19
 * @version     v1.0
 */
public class Base
{
    
    /** 服务启动时间 */
    private final static Date                         $StartupTime    = new Date();
    
    /** 模板信息的缓存 */
    private final static Map<String ,String>          $TemplateCaches = new Hashtable<String ,String>();
    
    /** 所有错误的缓存缓存队列标识 */
    private static final String                       $ErrorAllID = "$ALL$";
    
    /** 
     * 记录错误信息的缓存
     * 
     * Map.key 为接口访问编号 
     */
    private static Map<String ,Busway<AppMessage<?>>> $ErrorCacheLogs = new Hashtable<String ,Busway<AppMessage<?>>>(); 
    
    
    
    /**
     * 将错误信息，记录在缓存中
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-07
     * @version     v1.0
     *
     * @param i_AppMsg
     */
    public synchronized void logErrorByCache(AppMessage<?> i_AppMsg)
    {
        if ( i_AppMsg == null )
        {
            return;
        }
        
        if ( !Help.isNull(i_AppMsg.getSid()) )
        {
            if ( $ErrorCacheLogs.containsKey($ErrorAllID) )
            {
                $ErrorCacheLogs.get($ErrorAllID).put(i_AppMsg);
            }
            else
            {
                int                   v_IFSize = ((Map<? ,?>)this.getObject("AppInterfaces")).size();
                Busway<AppMessage<?>> v_Busway = new Busway<AppMessage<?>>(this.getErrorLogCacheSize() * v_IFSize);
                
                v_Busway.put(i_AppMsg);
                
                $ErrorCacheLogs.put($ErrorAllID ,v_Busway);
            }
            
            if ( $ErrorCacheLogs.containsKey(i_AppMsg.getSid()) )
            {
                $ErrorCacheLogs.get(i_AppMsg.getSid()).put(i_AppMsg);
            }
            else
            {
                Busway<AppMessage<?>> v_Busway = new Busway<AppMessage<?>>(this.getErrorLogCacheSize());
                
                v_Busway.put(i_AppMsg);
                
                $ErrorCacheLogs.put(i_AppMsg.getSid() ,v_Busway);
            }
        }
    }
    
    
    
    /**
     * 获取所有接口相关的错误信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-08
     * @version     v1.0
     *
     * @return
     */
    public AppMessage<?> [] getLogErrorByCache()
    {
        return this.getLogErrorByCache($ErrorAllID);
    }
    
    
    
    /**
     * 获取接口相关的错误信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-07
     * @version     v1.0
     *
     * @param i_SID
     * @return
     */
    public AppMessage<?> [] getLogErrorByCache(String i_SID)
    {
        if ( Help.isNull(i_SID) )
        {
            return null;
        }
        
        if ( $ErrorCacheLogs.containsKey(i_SID) )
        {
            return $ErrorCacheLogs.get(i_SID).toArray(new AppMessage[]{});
        }
        else
        {
            return null;
        }
    }
    
    
    
    /**
     * 部署Web服务所在主机的标示
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-08
     * @version     v1.0
     *
     * @return
     */
    protected String getAppWebID()
    {
        return this.getParam("APPWEBID").toString();
    }
    
    
    
    /**
     * 记录错误信息的缓存大小
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-08
     * @version     v1.0
     *
     * @return
     */
    protected int getErrorLogCacheSize()
    {
        return Integer.parseInt(this.getParam("ErrorLogCacheSize").getValue());
    }
    
    
    
    /**
     * 数据库是什么类型
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-06
     * @version     v1.0
     *
     * @return
     */
    protected String getDataSourceType()
    {
        return this.getParam("DataSourceType").getValue();
    }
    
    
    
    /**
     * 性能类参数。当为false时，部分访问量大的接口启用异步访问机制
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-04
     * @version     v1.0
     *
     * @return
     */
    protected boolean IsSynchronized()
    {
        return Boolean.parseBoolean(this.getParam("IsSynchronized").getValue());
    }
    
    
    
    /**
     * 数据安全及性能类参数。在IsSynchronized=false时才有效。
     * 
     *   当为true时，牺牲部分性能保证日志数据的安全性，即使在数据库断连时，也确保日志数据能被记录下来而不丢失。
     *   当为false时，性能损失将是最少的。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2016-12-22
     * @version     v1.0
     *
     * @return
     */
    protected boolean IsSafeData()
    {
        return Boolean.parseBoolean(this.getParam("IsSafeData").getValue());
    }
    
    
    
    /**
     * 系统级请求参数Json字符串样式
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-03
     * @version     v1.0
     *
     * @return
     */
    protected String getAppInterfaceSysParam()
    {
        return this.getParam("AppInterfaceSysHead").getValue();
    }
    
    
    
    /**
     * 系统级请求参数Json字符串样式(接口返回时用)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-24
     * @version     v1.0
     *
     * @return
     */
    protected String getAppInterfaceSysReturn()
    {
        return this.getParam("AppInterfaceSysHeadReturn").getValue();
    }
    
    
    
    /**
     * 获取系统的版本号
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-19
     * @version     v1.0
     *
     * @return
     */
    protected String getVersion()
    {
        List<Version> v_Versions = this.getVersions();
        return v_Versions.get(v_Versions.size() - 1).getName();
    }
    
    
    
    /**
     * 获取系统的版本号对应的数据库脚本的版本号
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-06
     * @version     v1.0
     *
     * @return
     */
    protected String getDBVersion()
    {
        List<Version> v_Versions = this.getVersions();
        return v_Versions.get(v_Versions.size() - 1).getDbVersion();
    }
    
    
    
    /**
     * 获取系统所有的版本信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-25
     * @version     v1.0
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    protected List<Version> getVersions()
    {
        return (List<Version>)this.getObject("Versions");
    }
    
    
    
    /**
     * 服务启动时间
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-19
     * @version     v1.0
     *
     * @return
     */
    protected Date getStartupTime()
    {
        return $StartupTime;
    }
    
    
    
    /**
     * 获取XJava对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-18
     * @version     v1.0
     *
     * @param i_XJavaID
     * @return
     */
    protected Object getObject(String i_XJavaID)
    {
        return XJava.getObject(i_XJavaID);
    }
    
    
    
    /**
     * 获取XJava对象集合
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-04
     * @version     v1.0
     *
     * @param i_XJavaIDPrefix
     * @return
     */
    protected Map<String ,Object> getObjects(String i_XJavaIDPrefix)
    {
        return XJava.getObjects(i_XJavaIDPrefix);
    }
    
    
    
    /**
     * 获取参数Param对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-18
     * @version     v1.0
     *
     * @param i_XJavaID
     * @return
     */
    protected Param getParam(String i_XJavaID)
    {
        return (Param)this.getObject(i_XJavaID);
    }
    
    
    
    /**
     * 获取XSQL对象，方便操作数据库
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-19
     * @version     v1.0
     *
     * @param i_XSQLID
     * @return
     */
    protected XSQL getXSQL(String i_XSQLID)
    {
        return (XSQL)this.getObject(i_XSQLID);
    }
    
    
    
    /**
     * 四舍五入保留的小数位数
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-18
     * @version     v1.0
     *
     * @return
     */
    protected int getDigit()
    {
        return Integer.parseInt(this.getParam("Digit").getValue());
    }
    
    
    
    /**
     * 是否启用缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-19
     * @version     v1.0
     *
     * @return
     */
    public boolean isEnableCache()
    {
        return !"NULL".equals(getCacheType());
    }
    
    
    
    /**
     * 获取缓存类型
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-03
     * @version     v1.0
     *
     * @return
     */
    public String getCacheType()
    {
        return this.getParam("CacheType").getValue();
    }
    
    
    
    /**
     * 获取模板内容（有缓存机制）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-19
     * @version     v1.0
     *
     * @param i_TemplateName  模板名称
     * @return
     */
    protected String getTemplateContent(String i_TemplateName)
    {
        if ( $TemplateCaches.containsKey(i_TemplateName) )
        {
            return $TemplateCaches.get(i_TemplateName);
        }
        
        String v_Content = "";
        
        try
        {
            v_Content = this.getFileContent("template/" + i_TemplateName);
            $TemplateCaches.put(i_TemplateName ,v_Content);
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }
        
        return v_Content;
    }
    
    
    
    /**
     * 获取文件内容
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-28
     * @version     v1.0
     *
     * @param i_FileName  文件名称(无须文件路径)。
     * @return
     * @throws Exception
     */
    protected String getFileContent(String i_FileName) throws Exception
    {
        return new FileHelp().getContent(Help.getWebINFPath() + i_FileName ,"UTF-8");
    }
    
}
