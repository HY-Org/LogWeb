package org.hy.log.dao.impl;

import org.hy.common.Date;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.log.common.BaseDAO;
import org.hy.log.dao.IMessageKeyDAO;





/**
 * 消息密钥DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2016-02-23
 * @version     v1.0
 */
@Xjava(id="MessageKeyDAO")
public class MessageKeyDAOImpl extends BaseDAO implements IMessageKeyDAO
{
    
    /**
     * 缓存消息密钥
     * 
     * @author      ZhengWei(HY)
     * @createDate  2016-02-23
     * @version     v1.0
     *
     */
    public void cacheMessageKeys()
    {
        System.out.println("-- " + Date.getNowTime().getFullMilli() + " : 更新消息密钥... ...");
        XJava.putObject("AppMsgKeySSID"  ,this.getXSQL("XSQL_MessageKey_Query_SSID") .query());
        XJava.putObject("AppMsgKeySysID" ,this.getXSQL("XSQL_MessageKey_Query_SysID").query());
        System.out.println("-- " + Date.getNowTime().getFullMilli() + " : 更新消息密钥... ...完成。");
    }
    
}
