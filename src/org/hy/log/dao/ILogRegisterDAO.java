package org.hy.log.dao;

import java.util.Map;

import org.hy.log.model.LogInfo;
import org.hy.log.model.LogRegister;
import org.hy.common.Return;





/**
 * 注册者操作日志DAO接口
 *
 * @author      ZhengWei
 * @createDate  2014-12-14
 * @version     v1.0
 */
public interface ILogRegisterDAO
{
    
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
    public Return<String> addLogInfo(LogInfo io_LogInfo);
    
    
    
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
    public Return<String> createTable(String i_TableName);
    
    
    
    /**
     * 注册者的：创建日志表任务
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-15
     * @version     v1.0
     *
     */
    public void taskCreateTable();
    
    
    
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
    public Return<String> addRegister(LogRegister io_LogRegister);
    
    
    
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
    public Return<String> updateRegister(LogRegister io_LogRegister);
    
    
    
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
    public Return<String> updateBuild(LogRegister io_LogRegister);
    
    
    
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
    public LogRegister queryRegister(String i_SysID);
    
    
    
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
    public LogRegister queryRegister(LogRegister i_LogRegister);
    
    
    
    /**
     * 注册者的：查询所有配置信息
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @return               异常时返回空null
     */
    public Map<String ,LogRegister> queryRegisters();
    
}
