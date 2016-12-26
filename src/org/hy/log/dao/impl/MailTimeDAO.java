package org.hy.log.dao.impl;

import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.mail.MailOwnerInfo;
import org.hy.common.mail.MailSendInfo;
import org.hy.common.mail.SimpleMail;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.plugins.XSQLGroup;
import org.hy.common.xml.plugins.XSQLGroupResult;





/**
 * 定时发送邮件
 * 
 * @author      ZhengWei(HY)
 * @createDate  2016-05-26
 * @version     v1.0
 */
@Xjava
public class MailTimeDAO
{
    
    private static boolean       $IsInit = false;
    
    private static MailOwnerInfo $Owner  = null;
    
    
    
    private synchronized static MailOwnerInfo getMailOwner()
    {
        if ( $Owner == null )
        {
            $Owner = new MailOwnerInfo();
            $Owner.setSendHost("smtp.qiye.163.com");
            $Owner.setSendPort(25);
            $Owner.setValidate(true);
            $Owner.setUserName("");
            $Owner.setPassword("");
        }
        
        return $Owner;
    }
    
    
    
    public MailTimeDAO() throws Exception
    {
        if ( !$IsInit )
        {
            $IsInit = true;
            System.out.println("\n-- " + Date.getNowTime().getFullMilli() + " MTS Starting.");
        }
    }
    
    
    
    /**
     * 定时扫描，当有邮件时发送
     * 
     * @author      ZhengWei(HY)
     * @createDate  2016-05-26
     * @version     v1.0
     *
     */
    public void scanMails()
    {
        XSQLGroup       v_GXSQL = ((XSQLGroup)XJava.getObject("GXSQL_MailTime"));
        XSQLGroupResult v_Ret   = v_GXSQL.executes();
        
        if ( v_Ret.isSuccess() )
        {
            if ( v_Ret.getExecSumCount().getSumValue() >= 1 )
            {
                System.out.println("\n-- " + Date.getNowTime().getFullMilli() + " MT" + v_Ret.getExecSumCount().getSumValue() + ".");
            }
        }
        else
        {
            v_GXSQL.logReturn(v_Ret);
        }
    }
    
    
    
    /**
     * 发送邮件
     * 
     * @author      ZhengWei(HY)
     * @createDate  2016-05-26
     * @version     v1.0
     *
     * @param i_Params    执行或查询参数。
     * @param io_Returns  通过returnID标记的，返回出去的多个查询结果集。
     * @return            表示是否执行成功
     */
    public boolean sendMail(Map<String ,Object> i_Params ,Map<String ,Object> io_Returns)
    {
        MailSendInfo v_SendInfo = new MailSendInfo();
        
        String [] v_Recivers = Help.getValueIgnoreCase(i_Params ,"Reciver").toString().split(";");
        for (String v_Reciver : v_Recivers)
        {
            v_SendInfo.setEmail(v_Reciver);
        }
        v_SendInfo.setSubject(Help.getValueIgnoreCase(i_Params ,"Title")  .toString());
        v_SendInfo.setContent(Help.getValueIgnoreCase(i_Params ,"Content").toString());
        
        SimpleMail.sendTextMail(getMailOwner() ,v_SendInfo);
        
        return true;
    }
    
}
