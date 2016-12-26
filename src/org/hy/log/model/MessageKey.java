package org.hy.log.model;

import org.hy.log.common.BaseModel;





/**
 * 消息密钥
 *
 * @author      ZhengWei(HY)
 * @createDate  2016-02-23
 * @version     v1.0
 */
public class MessageKey extends BaseModel
{
    private static final long serialVersionUID = 6687810097606990508L;
    
    /** 系统编号 */
    private String sysid;
    
    /** 接口编号 */
    private String sid;
    
    /** 消息密钥 */
    private String msgKey;
    
    
    
    public String getRowKey()
    { 
        return this.getSysid() + this.getSid() + this.getMsgKey();
    }


    
    /**
     * 获取：系统编号
     */
    public String getSysid()
    {
        return sysid;
    }


    
    /**
     * 设置：系统编号
     * 
     * @param sysid 
     */
    public void setSysid(String sysid)
    {
        this.sysid = sysid;
    }


    
    /**
     * 获取：接口编号
     */
    public String getSid()
    {
        return sid;
    }


    
    /**
     * 设置：接口编号
     * 
     * @param sid 
     */
    public void setSid(String sid)
    {
        this.sid = sid;
    }


    
    /**
     * 获取：消息密钥
     */
    public String getMsgKey()
    {
        return msgKey;
    }


    
    /**
     * 设置：消息密钥
     * 
     * @param msgKey 
     */
    public void setMsgKey(String msgKey)
    {
        this.msgKey = msgKey;
    }
    
}
