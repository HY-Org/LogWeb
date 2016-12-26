package org.hy.log.dao.impl;

import org.hy.log.common.BaseDAO;
import org.hy.log.dao.ILogInfoDAO;
import org.hy.log.model.LogInfo;
import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.xml.annotation.Xjava;





/**
 * 业务操作日志DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-12-12
 * @version     v1.0
 */
@Xjava(id="LogInfoDAO")
public class LogInfoDAOImpl extends BaseDAO implements ILogInfoDAO
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
    @Override
    public Return<String> addLogInfo(LogInfo io_LogInfo)
    {
        Return<String> v_Ret = new Return<String>(false);
        
        if ( io_LogInfo == null )
        {
            return v_Ret;
        }
        
        io_LogInfo.setWaitTime((int)(Date.getNowTime().getTime() - io_LogInfo.getMsgRequestTime().getTime()));
        
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
            
            v_Ret.set(1 == this.getXSQL("XSQL_LogInfo_Add").executeUpdate(io_LogInfo));
        }
        catch (Exception e)
        {
            v_Ret.paramObj = e.getMessage();
            v_Ret.set(false);
        }
        
        return v_Ret;
    }
    
}
