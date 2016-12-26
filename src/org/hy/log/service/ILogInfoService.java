package org.hy.log.service;

import org.hy.log.msg.MsgLogInfoRequest;
import org.hy.log.msg.MsgLogRegisterRequest;
import org.hy.common.Return;
import org.hy.common.xml.plugins.AppMessage;





/**
 * 日志信息的服务层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-12-12
 * @version     v1.0
 */
public interface ILogInfoService
{
    
    /**
     * 记录日志
     *
     * @author      ZhengWei(HY)
     * @createDate  2014-12-12
     * @version     v1.0
     *
     * @param i_AppMsg
     * @return            Return.paramStr  异常时返回错误编号
     */
    public Return<Object> log(AppMessage<MsgLogInfoRequest> i_AppMsg);
    
    
    
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
    public Return<Object> register(AppMessage<MsgLogRegisterRequest> io_AppMsg);
    
}
