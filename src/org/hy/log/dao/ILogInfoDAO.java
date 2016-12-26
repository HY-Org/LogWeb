package org.hy.log.dao; 

import org.hy.log.model.LogInfo;
import org.hy.common.Return;





/**
 * 操作日志DAO接口
 *
 * @author      ZhengWei
 * @createDate  2014-12-12
 * @version     v1.0
 */
public interface ILogInfoDAO 
{
    
    /**
     * 非注册者的：业务操作日志添加
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-12
     * @version     v1.0
     *
     * @param io_LogInfo
     * @return           Return.paramStr 在成功时返回记录主键编号id的值
     *                   Return.paramObj 在异常时返回异常信息
     */
    public Return<String> addLogInfo(LogInfo io_LogInfo);
    
}
