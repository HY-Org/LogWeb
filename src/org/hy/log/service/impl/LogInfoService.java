package org.hy.log.service.impl;

import java.util.List;
import java.util.Map;

import org.hy.log.common.BaseService;
import org.hy.log.dao.ILogInfoDAO;
import org.hy.log.dao.ILogRegisterDAO;
import org.hy.log.model.LogRegister;
import org.hy.log.msg.MsgLogInfoRequest;
import org.hy.log.msg.MsgLogRegisterRequest;
import org.hy.log.service.ILogInfoService;
import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.berkeley.Berkeley;
import org.hy.common.thread.Task;
import org.hy.common.thread.TaskPool;
import org.hy.common.xml.XJSON;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.plugins.AppMessage;





/**
 * 日志操作的服务类
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-12-12
 * @version     v1.0  
 *              v2.0  2016-02-18  添加：容灾机制。可防止突然停电等原因，造成消息队列中正在等待入库的日志消息的丢失。
 */
@Xjava
public class LogInfoService extends BaseService implements ILogInfoService
{
    private static final String                   $TaskType = "LogInfoTask";
    
    private static       int                      $SerialNo = 0;
    
    private static       Map<String ,LogRegister> $CacheLogRegisters;
    
    
    @Xjava
    private ILogInfoDAO                           logInfoDAO;
    
    @Xjava
    private ILogRegisterDAO                       logRegisterDAO;
    
    @Xjava
    private Berkeley                              berkeley;
    
    
    
    /**
     * 从缓存中获取注册者的注册信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @param i_SysID
     * @return         无返回 null
     */
    private synchronized LogRegister getCacheLogRegister(String i_SysID)
    {
        try
        {
            if ( $CacheLogRegisters == null )
            {
                $CacheLogRegisters = this.logRegisterDAO.queryRegisters();
            }
            
            return $CacheLogRegisters.get(i_SysID);
        }
        catch (Exception exce)
        {
            return null;
        }
    }
    
    
    
    /**
     * 更新缓存中的注册者的注册信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @param i_LogRegister
     */
    private synchronized void setCacheLogRegister(LogRegister i_LogRegister)
    {
        LogRegister v_Old = getCacheLogRegister(i_LogRegister.getSysID());
        
        if ( v_Old == null )
        {
            $CacheLogRegisters.put(i_LogRegister.getSysID() ,i_LogRegister);
        }
        else
        {
            v_Old.init(i_LogRegister);
        }
    }
    
    
    
    /**
     * 记录日志
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-12
     * @version     v1.0
     *
     * @param io_AppMsg
     * @return            Return.paramStr  异常时返回错误编号
     */
    public Return<Object> log(AppMessage<MsgLogInfoRequest> io_AppMsg)
    {
        Return<Object> v_Ret = new Return<Object>(true);
        
        io_AppMsg.getBody().setId(            io_AppMsg.getSerialNo());
        io_AppMsg.getBody().setSysID(         io_AppMsg.getSysid());
        io_AppMsg.getBody().setMsgRequestTime(io_AppMsg.gatCreateTime());
        
        if ( this.IsSynchronized() )
        {
            Return<String> v_ExecRet = this.executeLog(io_AppMsg);
            
            v_Ret.paramStr(v_ExecRet.paramStr);
            v_Ret.set(     v_ExecRet.booleanValue());
        }
        else
        {
            if ( this.IsSafeData() )
            {
                this.berkeley.put(io_AppMsg.getSerialNo() ,io_AppMsg.toString());
            }
            TaskPool.putTask(new LogInfoTask(io_AppMsg));
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 执行记录日志动作
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-12
     * @version     v1.0
     *
     * @param io_AppMsg
     * @return            Return.paramStr  异常时返回错误编号
     */
    private Return<String> executeLog(AppMessage<MsgLogInfoRequest> io_AppMsg)
    {
        Return<String> v_Ret = null;
        
        try
        {
            LogRegister v_LogRegister = this.getCacheLogRegister(io_AppMsg.getSysid());
            
            if ( Help.isNull(io_AppMsg.getBody().getOperationTime()) )
            {
                io_AppMsg.getBody().setOperationTime(new Date());
            }
            
            if ( v_LogRegister == null )
            {
                v_Ret = this.logInfoDAO.addLogInfo(io_AppMsg.getBody());
            }
            else
            {
                io_AppMsg.getBody().setSysID(v_LogRegister.makeLRTName(io_AppMsg.getBody().getOperationTime()));
                v_Ret = this.logRegisterDAO.addLogInfo(io_AppMsg.getBody());
            }
            
            if ( !v_Ret.booleanValue() )
            {
                v_Ret.paramStr = "130101B011";
                v_Ret.set(false);
            }
            else
            {
                if ( !this.IsSynchronized() && this.IsSafeData() )
                {
                    this.berkeley.delete(io_AppMsg.getSerialNo());
                }
            }
        }
        catch (Exception exce)
        {
            v_Ret.paramStr = "130101B011";
            v_Ret.set(false);
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 容灾机制：将消息列队中的所有消息入库。
     * 
     * 可防止突然停电等原因，造成消息队列中正在等待入库的日志消息的丢失。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2016-02-17
     * @version     v1.0
     *
     */
    @SuppressWarnings("unchecked")
    public void executeLogs()
    {
        Map<String ,String> v_CacheLogs = (Map<String ,String>)(this.berkeley.gets());
        XJSON               v_XJSON     = new XJSON();
        
        if ( !Help.isNull(v_CacheLogs) )
        {
            System.out.println("-- " + Date.getNowTime().getFullMilli() + " 容灾机制：将消息列队中的所有消息入库，共有消息" + v_CacheLogs.size() + "条。Starting... ...");
            int v_Succeed = 0;
            
            for (String v_CacheLog : v_CacheLogs.values())
            {
                try
                {
                    AppMessage<MsgLogInfoRequest> v_AppMsg = (AppMessage<MsgLogInfoRequest>)v_XJSON.parser(v_CacheLog , AppMessage.class);
                    v_AppMsg.setBody((MsgLogInfoRequest)v_XJSON.parser(v_CacheLog ,"body" ,MsgLogInfoRequest.class));
                    
                    this.executeLog(v_AppMsg);
                    v_Succeed++;
                }
                catch (Exception exec)
                {
                    exec.printStackTrace();
                }
            }
            
            System.out.println("-- " + Date.getNowTime().getFullMilli() + " 容灾机制：将消息列队中的所有消息入库，成功入库" + v_Succeed + "/" + v_CacheLogs.size() + "条。Finish.");
        }
    }
    
    
    
    /**
     * 1. 注册者的注册动作
     * 2. 注册者的变动注册配置的动作
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @param io_AppMsg
     * @return            Return.paramStr  异常时返回错误编号
     */
    public Return<Object> register(AppMessage<MsgLogRegisterRequest> io_AppMsg)
    {
        Return<Object> v_Ret         = new Return<Object>(false);
        LogRegister    v_RegisterNew = io_AppMsg.getBody();
        LogRegister    v_RegisterOld = this.logRegisterDAO.queryRegister(io_AppMsg.getSysid());
        
        v_RegisterNew.setSysID(io_AppMsg.getSysid());
        
        if ( v_RegisterOld == null )
        {
            v_Ret.set(this.buildLogTable(v_RegisterNew ,new Date()));
            
            if ( v_Ret.booleanValue() )
            {
                Return<String> v_DataRet = this.logRegisterDAO.addRegister(v_RegisterNew);
                
                if ( v_DataRet.booleanValue() )
                {
                    this.setCacheLogRegister(v_RegisterNew);
                }
                else
                {
                    v_Ret.paramStr("130102B011");
                }
            }
            else
            {
                v_Ret.paramStr("130102B041");
            }
            
        }
        else if ( v_RegisterOld.getRegisterTypeEnum() == v_RegisterNew.getRegisterTypeEnum() )
        {
            v_Ret.paramStr("130102B021");
        }
        else
        {
            Return<String> v_DataRet = this.logRegisterDAO.updateRegister(v_RegisterNew);
            
            if ( v_DataRet.booleanValue() )
            {
                v_Ret.set(this.buildLogTable(v_RegisterNew ,new Date()));
                
                if ( v_Ret.booleanValue() )
                {
                    this.setCacheLogRegister(v_RegisterNew);
                }
                else
                {
                    v_Ret.paramStr("130102B041");
                }
            }
            else
            {
                v_Ret.paramStr("130102B031");
            }
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 构建日志表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @param i_LR
     * @param i_Now
     * @return
     */
    public boolean buildLogTable(LogRegister i_LR ,Date i_Now)
    {
        Return<String> v_Ret      = null;
        List<String>   v_LRTNames = i_LR.makeLRTNameByInit(i_Now);
        
        for (String v_LRTName : v_LRTNames)
        {
            v_Ret = this.logRegisterDAO.createTable(v_LRTName);
            
            if ( !v_Ret.booleanValue() )
            {
                return v_Ret.booleanValue();
            }
        }
        
        if ( v_Ret.booleanValue() )
        {
            this.logRegisterDAO.updateBuild(i_LR);
        }
        
        return v_Ret.booleanValue();
    }
    
    
    
    public ILogInfoDAO getLogInfoDAO()
    {
        return logInfoDAO;
    }

    

    public void setLogInfoDAO(ILogInfoDAO logInfoDAO)
    {
        this.logInfoDAO = logInfoDAO;
    }


    
    public ILogRegisterDAO getLogRegisterDAO()
    {
        return logRegisterDAO;
    }


    
    public void setLogRegisterDAO(ILogRegisterDAO logRegisterDAO)
    {
        this.logRegisterDAO = logRegisterDAO;
    }



    public Berkeley getBerkeley()
    {
        return berkeley;
    }


    
    public void setBerkeley(Berkeley berkeley)
    {
        this.berkeley = berkeley;
    }





    class LogInfoTask extends Task<Object>
    {
        private AppMessage<MsgLogInfoRequest> appMsg;
        
        
        
        public LogInfoTask(AppMessage<MsgLogInfoRequest> io_AppMsg)
        {
            super($TaskType);
            
            this.appMsg = io_AppMsg;
        }


        private synchronized int GetSerialNo()
        {
            return ++$SerialNo;
        }
        
        
        @Override
        public void execute()
        {
            Return<String> v_Ret = executeLog(this.appMsg);
            
            if ( !v_Ret.booleanValue() )
            {
                try
                {
                    this.appMsg.setResult(false);
                    this.appMsg.setRc(v_Ret.paramStr);
                    this.appMsg.setBody(null);
                    
                    logErrorByCache(this.appMsg);
                }
                catch (Exception exce)
                {
                    exce.printStackTrace();
                }
            }
            
            this.finishTask();
        }

        
        @Override
        public int getSerialNo()
        {
            return GetSerialNo();
        }
        

        @Override
        public String getTaskDesc()
        {
            return "" + this.getTaskNo();
        }
        
    }

}
