package org.hy.log.common;

import java.util.Random;
import java.util.UUID;

import org.hy.common.Date;
import org.hy.common.StringHelp;





/**
 * 数据库操作层--基类
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-09-23
 * @version     v1.0  
 */
public class BaseDAO extends Base
{
    public  static final Date $MaxDate   = new Date("2100-01-01 00:00:00");
    
    public  static final Date $MinDate   = new Date("2000-01-01 00:00:00");
    
    private static final int  $RandomMax = 999999;
    
    private static final int  $RandomLen = ("" + $RandomMax).length();
    
    
    
    protected String makeID()
    {
        return UUID.randomUUID().toString().replace("-" ,"").toUpperCase();
    }
    
    
    
    /**
     * 生成主键(按时间顺序)
     * 
     * @return
     */
    protected synchronized String makeID_Time()
    {
        return this.makeID_Time("");
    }
    
    
    
    /**
     * 生成主键(按时间顺序)
     * 
     * @return
     */
    protected synchronized String makeID_Time(String i_OrderBy)
    {
        Date   v_Date   = Date.getNowTime();
        Random v_Random = new Random();
        
        return v_Date.getFullMilli_ID() + i_OrderBy + StringHelp.lpad(v_Random.nextInt($RandomMax) ,$RandomLen ,"0");
    }
    
    
    
    /**
     * 生成主键(按时间倒序)
     * 
     * @return
     */
    protected synchronized String makeID_ReverseTime()
    {
        return this.makeID_ReverseTime("");
    }
    
    
    
    /**
     * 生成主键(按时间倒序)
     * 
     * @return
     */
    protected synchronized String makeID_ReverseTime(String i_OrderBy)
    {
        Date   v_Date   = Date.getNowTime();
        Random v_Random = new Random();
        
        v_Date = new Date($MaxDate.getTime() - v_Date.getTime() + $MinDate.getTime());
        
        return v_Date.getFullMilli_ID() + i_OrderBy + StringHelp.lpad(v_Random.nextInt($RandomMax) ,$RandomLen ,"0");
    }
    
}
