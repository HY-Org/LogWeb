package org.hy.log.enums;

import org.hy.log.common.BaseEnum;
import org.hy.common.Help;





/**
 * 注册者的注册类型
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-12-14
 * @version     v1.0
 */
public enum ERegisterType implements BaseEnum<String>
{
    
    /** 普通类型：单表保存日志 */
    $Normal("NORMAL"),
    
    /** 分区类型：按年分区保存日志 */
    $Partition_Year( "P_YEAR"),
    
    /** 分区类型：按月分区保存日志 */
    $Partition_Month("P_MONTH"),
    
    /** 分区类型：按旬分区保存日志 */
    $Partition_10Day("P_10DAY"),
    
    /** 分区类型：按星期分区保存日志 */
    $Partition_Week( "P_WEEK"),
    
    /** 分区类型：按天分区保存日志 */
    $Partition_Day(  "P_DAY"),
    
    /** 分区类型：按小时分区保存日志 */
    $Partition_Hour( "P_HOUR");
    
    
    
    private String value;
    
    
    
    public static ERegisterType get(String i_Value)
    {
        if ( Help.isNull(i_Value) )
        {
            return null;
        }
        
        String v_Value = i_Value.trim();
        
        for (ERegisterType v_Enum : ERegisterType.values()) 
        {
            if ( v_Enum.value.equalsIgnoreCase(v_Value) ) 
            {
                return v_Enum;
            }
        }
        
        return null;
    }
    
    
    
    ERegisterType(String i_Value)
    {
        this.value = i_Value;
    }

    

    @Override
    public String getValue()
    {
        return this.value;
    }
    
}
