package org.hy.log.model;

import java.util.ArrayList;
import java.util.List;

import org.hy.log.common.BaseModel;
import org.hy.log.enums.ERegisterType;
import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.xml.annotation.Doc;





/**
 * 日志注册者的配置信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-12-13
 * @version     v1.0
 */
public class LogRegister extends BaseModel
{

    private static final long serialVersionUID = 8892603955836412263L;

    
    /** 外部调用系统的标识 */
    private String           sysID;
    
    /** 注册类型 */
    @Doc(index="01" ,info="注册类型。NORMAL:普通；P_YEAR:按年；P_MONTH:按月；P_10DAY:按旬；P_WEEK:按周；P_DAY:按天；P_HOUR:按小时" ,value={"Log.Register.A001"})
    private ERegisterType    registerType;
    
    /** 数据库构建时间 */
    private Date             dbBuildTime;
    
    /** 注册时间 */
    private Date             createTime;
    
    /** 注册更新时间 */
    private Date             updateTime;
    
    
    
    public LogRegister()
    {
        
    }
    
    
    
    public LogRegister(String i_SysID)
    {
        this.setSysID(i_SysID);
    }
    
    
    
    public List<String> makeLRTNameByInit()
    {
        return this.makeLRTNameByInit(new Date());
    }
    
    
    
    public List<String> makeLRTNameByInit(Date i_Date)
    {
        List<String> v_Ret = new ArrayList<String>();
        
        if ( this.registerType == null || i_Date == null )
        {
            return v_Ret;
        }
        
        if ( this.registerType == ERegisterType.$Normal )
        {
            v_Ret.add(this.makeLRTName(i_Date));
        }
        else if ( this.registerType == ERegisterType.$Partition_Year )
        {
            int v_Year = i_Date.getYear();
            
            v_Ret.add(this.makeLRTName(new Date((v_Year - 1) + "-01-01")));
            v_Ret.add(this.makeLRTName(new Date((v_Year + 0) + "-01-01")));
            v_Ret.add(this.makeLRTName(new Date((v_Year + 1) + "-01-01")));
        }
        else if ( this.registerType == ERegisterType.$Partition_Month )
        {
            v_Ret.add(this.makeLRTName(i_Date.getPreviousMonth()));
            v_Ret.add(this.makeLRTName(i_Date));
            v_Ret.add(this.makeLRTName(i_Date.getNextMonth()));
        }
        else if ( this.registerType == ERegisterType.$Partition_10Day )
        {
            v_Ret.add(this.makeLRTName(i_Date.getPreviousMonth()));
            v_Ret.add(this.makeLRTName(i_Date.getFirstDayOfMonth()));
            v_Ret.add(this.makeLRTName(new Date(i_Date.getYM() + "-15")));
            v_Ret.add(this.makeLRTName(i_Date.getLastDayOfMonth()));
            v_Ret.add(this.makeLRTName(i_Date.getNextMonth()));
        }
        else if ( this.registerType == ERegisterType.$Partition_Week )
        {
            v_Ret.add(this.makeLRTName(i_Date.getDate(-7)));
            v_Ret.add(this.makeLRTName(i_Date));
            v_Ret.add(this.makeLRTName(i_Date.getDate(+7)));
        }
        else if ( this.registerType == ERegisterType.$Partition_Day )
        {
            v_Ret.add(this.makeLRTName(i_Date.getPreviousDay()));
            v_Ret.add(this.makeLRTName(i_Date));
            v_Ret.add(this.makeLRTName(i_Date.getNextDay()));
        }
        else if ( this.registerType == ERegisterType.$Partition_Hour )
        {
            Date v_Time = i_Date.getPreviousDay().getFirstTimeOfDay();
            
            for (int v_Day=0; v_Day<3; v_Day++)
            {
                for (int i=0; i<24; i++)
                {
                    v_Ret.add(this.makeLRTName(v_Time));
                    
                    v_Time = v_Time.getNextHour();
                }
            }
        }
        
        return v_Ret;
    }
    
    
    
    public List<String> makeLRTNameByNext()
    {
        return this.makeLRTNameByNext(new Date());
    }
    
    
    
    public List<String> makeLRTNameByNext(Date i_Date)
    {
        List<String> v_Ret = new ArrayList<String>();
        
        if ( this.registerType == null || i_Date == null )
        {
            return v_Ret;
        }
        
        if ( this.registerType == ERegisterType.$Normal )
        {
            v_Ret.add(this.makeLRTName(i_Date));
        }
        else if ( this.registerType == ERegisterType.$Partition_Year )
        {
            int v_Year = i_Date.getYear();
            
            v_Ret.add(this.makeLRTName(new Date((v_Year + 1) + "-01-01")));
        }
        else if ( this.registerType == ERegisterType.$Partition_Month )
        {
            v_Ret.add(this.makeLRTName(i_Date.getNextMonth()));
        }
        else if ( this.registerType == ERegisterType.$Partition_10Day )
        {
            int v_Day = i_Date.getDay();
            
            if ( v_Day <= 10 )
            {
                v_Ret.add(this.makeLRTName(i_Date.getDate(10)));
            }
            else if ( v_Day <= 20 )
            {
                v_Ret.add(this.makeLRTName(i_Date.getDate(20 - v_Day + 1)));
            }
            else
            {
                v_Ret.add(this.makeLRTName(i_Date.getNextMonth().getFirstDayOfMonth()));
            }
        }
        else if ( this.registerType == ERegisterType.$Partition_Week )
        {
            v_Ret.add(this.makeLRTName(i_Date.getDate(+7)));
        }
        else if ( this.registerType == ERegisterType.$Partition_Day )
        {
            v_Ret.add(this.makeLRTName(i_Date.getNextDay()));
        }
        else if ( this.registerType == ERegisterType.$Partition_Hour )
        {
            Date v_Time = new Date(i_Date);
            
            // 10 为容错10小时的时差范围
            for (int i=10; i>=1 ; i--)
            {
                v_Time = v_Time.getPreviousHour();
                v_Ret.add(this.makeLRTName(v_Time));
            }
            
            v_Time = new Date(i_Date);
            
            for (int i=0; i<24 + (24 - i_Date.getHours()) ; i++)
            {
                v_Ret.add(this.makeLRTName(v_Time));
                v_Time = v_Time.getNextHour();
            }
        }
        
        return v_Ret;
    }
    
    
    
    public String makeLRTName()
    {
        return this.makeLRTName(new Date());
    }
    
    
    
    public String makeLRTName(Date i_Date)
    {
        if ( this.registerType == null || i_Date == null )
        {
            return null;
        }

        if ( this.registerType == ERegisterType.$Normal )
        {
            return this.sysID;
        }
        
        if ( this.registerType == ERegisterType.$Partition_Year )
        {
            return this.sysID + i_Date.getYear();
        }
        else if ( this.registerType == ERegisterType.$Partition_Month )
        {
            return this.sysID + i_Date.getYM_ID();
        }
        else if ( this.registerType == ERegisterType.$Partition_10Day )
        {
            int v_Day = i_Date.getDay();
            
            if ( v_Day <= 10 )
            {
                return this.sysID + i_Date.getYM_ID() + "A";
            }
            else if ( v_Day <= 20 )
            {
                return this.sysID + i_Date.getYM_ID() + "B";
            }
            else
            {
                return this.sysID + i_Date.getYM_ID() + "C";
            }
        }
        else if ( this.registerType == ERegisterType.$Partition_Week )
        {
            int v_WeekNo = i_Date.getWeekNoOfYear();
            
            if ( v_WeekNo == 1 )
            {
                if ( i_Date.getMonth() == 12 && i_Date.getDay() >= 24 )
                {
                    return this.sysID + (i_Date.getYear() + 1) + "W01";
                }
                else
                {
                    return this.sysID + i_Date.getYear() + "W52";
                }
            }
            else
            {
                return this.sysID + i_Date.getYear() + "W" + StringHelp.lpad(v_WeekNo - 1 ,2 ,"0");
            }
        }
        else if ( this.registerType == ERegisterType.$Partition_Day )
        {
            return this.sysID + i_Date.getYMD_ID();
        }
        else if ( this.registerType == ERegisterType.$Partition_Hour )
        {
            return this.sysID + i_Date.getYMDH_ID();
        }
        else
        {
            return null;
        }
    }
    
    
    
    @Override
    public String getRowKey()
    {
        return this.getSysID();
    }


    
    /**
     * 获取：外部调用系统的标识
     */
    public String getSysID()
    {
        return sysID;
    }


    
    /**
     * 设置：外部调用系统的标识
     * 
     * @param i_SysID 
     */
    public void setSysID(String i_SysID)
    {
        if ( Help.isNull(i_SysID) )
        {
            this.sysID = null;
        }
        else
        {
            this.sysID = i_SysID.trim();
        }
    }
    
    
    
    /**
     * 获取：注册类型枚举类
     * 
     * 注：有意使Getter与Setter方法不成对出现
     */
    public ERegisterType getRegisterTypeEnum()
    {
        return this.registerType;
    }


    
    /**
     * 设置：注册类型枚举类
     * 
     * 注：有意使Getter与Setter方法不成对出现
     * 
     * @param registerType 
     */
    public void setRegisterTypeEnumInfo(ERegisterType i_RegisterType)
    {
        this.registerType = i_RegisterType;
    }


    
    /**
     * 获取：注册类型
     */
    public String getRegisterType()
    {
        return this.registerType.getValue();
    }


    
    /**
     * 设置：注册类型
     * 
     * @param registerType 
     */
    public void setRegisterType(String i_RegisterType)
    {
        this.registerType = ERegisterType.get(i_RegisterType);
    }


    
    /**
     * 获取：数据库构建时间
     */
    public Date getDbBuildTime()
    {
        return dbBuildTime;
    }


    
    /**
     * 设置：数据库构建时间
     * 
     * @param dbBuildTime 
     */
    public void setDbBuildTime(Date dbBuildTime)
    {
        this.dbBuildTime = dbBuildTime;
    }



    /**
     * 获取：注册时间
     */
    public Date getCreateTime()
    {
        return createTime;
    }


    
    /**
     * 设置：注册时间
     * 
     * @param createTime 
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }


    
    /**
     * 获取：注册更新时间
     */
    public Date getUpdateTime()
    {
        return updateTime;
    }


    
    /**
     * 设置：注册更新时间
     * 
     * @param updateTime 
     */
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
    
}
