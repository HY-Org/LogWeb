package org.hy.log.dao;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xsql;





/**
 * 消息密钥DAO接口 
 *
 * @author      ZhengWei(HY)
 * @createDate  2016-02-23
 * @version     v1.0
 */
@Xjava(id="MessageKeyDAO" ,value=XType.XSQL)
public interface IMessageKeyDAO
{
    
    /**
     * 缓存消息密钥（接口级）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2016-02-23
     * @version     v1.0
     *
     */
    @Xsql(id="XSQL_MessageKey_Query_SSID" ,updateCacheID="AppMsgKeySSID" ,log="更新消息密钥(接口级)")
    public void cacheMessageKeySSID();
    
    
    
    /**
     * 缓存消息密钥（系统级）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2017-12-19
     * @version     v1.0
     *
     */
    @Xsql(id="XSQL_MessageKey_Query_SysID" ,updateCacheID="AppMsgKeySysID" ,log="更新消息密钥(系统级)")
    public void cacheMessageKeySysID();
    
}
