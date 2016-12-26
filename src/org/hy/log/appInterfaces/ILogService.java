package org.hy.log.appInterfaces;

import org.hy.log.msg.MsgLogInfoRequest;
import org.hy.log.msg.MsgLogRegisterRequest;
import org.hy.common.xml.plugins.AppMessage;





/**
 * 日志信息的接口
 * 
 * 接口服务端: 我方
 * 接口客户端: 通用
 * 接口协议为: 通用
 * 对方联调人: -
 * 相关文档有: -
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-12-12
 * @version     v1.0  
 */
public interface ILogService
{
    
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
    public AppMessage<Object> log(AppMessage<MsgLogInfoRequest> i_AppMsg);
    
    
    
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
    public AppMessage<Object> register(AppMessage<MsgLogRegisterRequest> i_AppMsg);
    
}
