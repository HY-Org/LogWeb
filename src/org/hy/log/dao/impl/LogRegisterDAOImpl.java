package org.hy.log.dao.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hy.log.common.BaseDAO;
import org.hy.log.dao.ILogRegisterDAO;
import org.hy.log.dao.IServerInfoDAO;
import org.hy.log.model.LogInfo;
import org.hy.log.model.LogRegister;
import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.StringHelp;
import org.hy.common.xml.XSQL;
import org.hy.common.xml.annotation.Xjava;





/**
 * 注册者操作日志DAO
 *
 * @author      ZhengWei
 * @createDate  2014-12-14
 * @version     v1.0
 */
@Xjava(id="LogRegisterDAO")
public class LogRegisterDAOImpl extends BaseDAO implements ILogRegisterDAO
{
    
    @Xjava
    private IServerInfoDAO serverInfoDAO;
    
    
    
    /**
     * 注册者的：业务操作日志添加
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @param io_LogInfo
     * @return           Return.paramStr 在成功时返回记录主键编号id的值
     *                   Return.paramObj 在异常时返回异常信息
     */
    @Override
    public Return<String> addLogInfo(LogInfo io_LogInfo)
    {
        Return<String> v_Ret = new Return<String>(false);
        
        if ( io_LogInfo == null )
        {
            return v_Ret;
        }
        
        io_LogInfo.setWaitTime((int)(Date.getNowTime().getTime() - io_LogInfo.getMsgRequestTime().getTime()));
        io_LogInfo.setLogContent(StringHelp.replaceAll(io_LogInfo.getLogContent() ,"'" ,"''"));
        io_LogInfo.setLogInfo(   StringHelp.replaceAll(io_LogInfo.getLogInfo()    ,"'" ,"''"));
        
        try
        {
            if ( Help.isNull(io_LogInfo.getId()) )
            {
                v_Ret.paramStr = super.makeID();
                io_LogInfo.setId(v_Ret.paramStr);
            }
            else
            {
                v_Ret.paramStr = io_LogInfo.getId();
            }
            
            v_Ret.set(1 == this.getXSQL("XSQL_LogInfo_Register_Add").executeUpdate(io_LogInfo));
        }
        catch (Exception e)
        {
            v_Ret.paramObj = e.getMessage();
            v_Ret.set(false);
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 注册者的：创建日志表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-13
     * @version     v1.0
     *
     * @param i_TableName
     * @return             Return.paramObj 在异常时返回异常信息
     *                     Return.paramInt 标记出是否有真正创建表。1:有创建表  0:表已存在无须创建
     */
    public Return<String> createTable(String i_TableName)
    {
        Return<String> v_Ret = new Return<String>(false);
        
        if ( this.serverInfoDAO.isExists("TLOG_" + i_TableName.trim().toUpperCase()) )
        {
            v_Ret.paramInt = 0;
            return v_Ret.set(true);
        }
        
        try
        {
            LogInfo v_Log = new LogInfo();
            v_Log.setSysID(i_TableName);
            
            List<LogInfo> v_Params = new ArrayList<LogInfo>(1);
            v_Params.add(v_Log);
            
            Map<XSQL ,List<?>> v_XSQLs = new LinkedHashMap<XSQL ,List<?>>();
            
            v_XSQLs.put(this.getXSQL("XSQL_LogInfo_Register_Create")                       ,v_Params);
            v_XSQLs.put(this.getXSQL("XSQL_LogInfo_Register_Create_CIndexByLogID")         ,v_Params);
            v_XSQLs.put(this.getXSQL("XSQL_LogInfo_Register_Create_CIndexByOperationTime") ,v_Params);
            
            XSQL.executeUpdates(v_XSQLs);
            v_Ret.paramInt = 1;
            v_Ret.set(true);
        }
        catch (Exception e)
        {
            v_Ret.paramObj = e.getMessage();
            v_Ret.set(false);
        }
        
        return v_Ret;
    }
    
    
    
    public String qq()
    {
        return "Hello World!";
    }
    
    
    
    /**
     * 注册者的：创建日志表任务
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-15
     * @version     v1.0
     *
     */
    @Override
    public void taskCreateTable()
    {
        if ( !this.serverInfoDAO.isExists("TLogRegister") )
        {
            return;
        }
                
        
        Date                     v_Now       = new Date();
        Map<String ,LogRegister> v_Registers = this.queryRegisters();
        
        if ( Help.isNull(v_Registers) )
        {
            return;
        }
        
        System.out.println("-- " + Date.getNowTime().getFull() + " Task Create Table Starting ... ...");
        
        for (LogRegister v_Register : v_Registers.values())
        {
            List<String> v_LRTNames = v_Register.makeLRTNameByNext(v_Now);
            
            java.util.Collections.sort(v_LRTNames);
            
            if ( !Help.isNull(v_LRTNames) )
            {
                for (String v_LRTName : v_LRTNames)
                {
                    Return<String> v_DataRet = this.createTable(v_LRTName);
                    
                    if ( !v_DataRet.booleanValue() )
                    {
                        System.out.println("-- " + Date.getNowTime().getFull() + " Task Create Table [" + v_LRTName + "] Error.");
                    }
                    else
                    {
                        if ( v_DataRet.paramInt == 1 )
                        {
                            System.out.println("-- " + Date.getNowTime().getFull() + " Task Create Table [" + v_LRTName + "] OK.");
                        }
                        else
                        {
                            System.out.println("-- " + Date.getNowTime().getFull() + " Task Create Table [" + v_LRTName + "] Ignore.");
                        }
                    }
                }
            }
        }
        
        System.out.println("-- " + Date.getNowTime().getFull() + " Task Create Table Finish.");
    }
    
    
    
    /**
     * 注册者的：注册配置信息
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @param io_LogRegister
     * @return           Return.paramObj 在异常时返回异常信息
     */
    @Override
    public Return<String> addRegister(LogRegister io_LogRegister)
    {
        Return<String> v_Ret = new Return<String>(false);
        
        if ( io_LogRegister == null )
        {
            return v_Ret;
        }
        
        if ( Help.isNull(io_LogRegister.getCreateTime()) )
        {
            io_LogRegister.setCreateTime(new Date());
        }
        
        try
        {
            v_Ret.set(1 == this.getXSQL("XSQL_LogInfo_Register_Config_Add").executeUpdate(io_LogRegister));
        }
        catch (Exception e)
        {
            v_Ret.paramObj = e.getMessage();
            v_Ret.set(false);
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 注册者的：更新配置信息
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @param io_LogRegister
     * @return           Return.paramObj 在异常时返回异常信息
     */
    @Override
    public Return<String> updateRegister(LogRegister io_LogRegister)
    {
        Return<String> v_Ret = new Return<String>(false);
        
        if ( io_LogRegister == null )
        {
            return v_Ret;
        }
        
        io_LogRegister.setUpdateTime(new Date());
        
        try
        {
            v_Ret.set(1 == this.getXSQL("XSQL_LogInfo_Register_Config_Update").executeUpdate(io_LogRegister));
        }
        catch (Exception e)
        {
            v_Ret.paramObj = e.getMessage();
            v_Ret.set(false);
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 注册者的：更新构建信息
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @param io_LogRegister
     * @return           Return.paramObj 在异常时返回异常信息
     */
    @Override
    public Return<String> updateBuild(LogRegister io_LogRegister)
    {
        Return<String> v_Ret = new Return<String>(false);
        
        if ( io_LogRegister == null )
        {
            return v_Ret;
        }
        
        if ( io_LogRegister.getDbBuildTime() == null )
        {
            io_LogRegister.setDbBuildTime(new Date());
        }
        
        try
        {
            v_Ret.set(1 == this.getXSQL("XSQL_LogInfo_Register_Build").executeUpdate(io_LogRegister));
        }
        catch (Exception e)
        {
            v_Ret.paramObj = e.getMessage();
            v_Ret.set(false);
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 注册者的：按系统编号，查询配置信息
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @param i_SysID
     * @return         异常时返回空null
     */
    @Override
    public LogRegister queryRegister(String i_SysID)
    {
        return this.queryRegister(new LogRegister(i_SysID));
    }
    
    
    
    /**
     * 注册者的：按系统编号，查询配置信息
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @param i_LogRegister
     * @return               异常时返回空null
     */
    @SuppressWarnings("unchecked")
    @Override
    public LogRegister queryRegister(LogRegister i_LogRegister)
    {
        if ( i_LogRegister == null )
        {
            return null;
        }
        
        if ( Help.isNull(i_LogRegister.getSysID()) )
        {
            return null;
        }
        
        try
        {
            List<LogRegister> v_Datas = (List<LogRegister>)this.getXSQL("XSQL_LogInfo_Register_QueryByID").query(i_LogRegister);
            
            if ( Help.isNull(v_Datas) )
            {
                return null;
            }
            else
            {
                return v_Datas.get(0);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    
    /**
     * 注册者的：查询所有配置信息
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @return               异常时返回空null
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String ,LogRegister> queryRegisters()
    {
        Map<String ,LogRegister> v_Ret = null;
        
        try
        {
            v_Ret = (Map<String ,LogRegister>)this.getXSQL("XSQL_LogInfo_Register_QueryAll").query();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        if ( v_Ret == null )
        {
            v_Ret = new Hashtable<String ,LogRegister>();
        }
        
        return v_Ret;
    }
    
    
    
    public IServerInfoDAO getServerInfoDAO()
    {
        return serverInfoDAO;
    }
    
    
    
    public void setServerInfoDAO(IServerInfoDAO serverInfoDAO)
    {
        this.serverInfoDAO = serverInfoDAO;
    }
    
}
