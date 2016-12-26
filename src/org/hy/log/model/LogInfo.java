package org.hy.log.model;

import org.hy.log.common.BaseModel;
import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.xml.annotation.Doc;





/**
 * 日志信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-12-12
 * @version     v1.0
 */
public class LogInfo extends BaseModel
{
    private static final long serialVersionUID = 8859982104548101972L;

    
    /** 主键编号(系统采用消息流水号) */
    private String               id;
    
    /** 外部调用系统的标识 */
    private String               sysID;
    
    /** 日志编号。由外部调用系统传递，当然也可以为空 */
    @Doc(index="01" ,info="日志编号。由外部调用系统传递，当然也可以为空。最大长度:64英文" ,value={"Log.Create.A001"})
    private String               logID;
    
    /** 日志分组类型 */
    @Doc(index="02" ,info="日志分组类型。最大长度:1000英文"                          ,value={"Log.Create.A001"})
    private String               logType;
    
    /** 日志分组种类 */
    @Doc(index="03" ,info="日志分组种类。最大长度:1000英文"                          ,value={"Log.Create.A001"})
    private String               logClass;
    
    /** 日志内容 */
    @Doc(index="04" ,info="日志内容。最大长度:65535英文"                            ,value={"Log.Create.A001"})
    private String               logContent;
    
    /** 日志信息 */
    @Doc(index="05" ,info="日志信息。最大长度:65535英文"                            ,value={"Log.Create.A001"})
    private String               logInfo;
    
    /** 操作人编号 */
    @Doc(index="06" ,info="操作人编号。最大长度:64英文"                             ,value={"Log.Create.A001"})
    private String               operatorNo;
    
    /** 操作类型。如，添加、删除、修改、查询等 */
    @Doc(index="07" ,info="操作类型。如，添加、删除、修改、查询等。最大长度:64英文"     ,value={"Log.Create.A001"})
    private String               operationType;
    
    /** 操作备注 */
    @Doc(index="08" ,info="操作备注。最大长度:1000英文"                            ,value={"Log.Create.A001"})
    private String               operationRemark;
    
    /** 操作发生的时间。当为空时，自动填充为接收消息的时间 */
    @Doc(index="09" ,info="操作发生的时间。当为空时，自动填充为接收消息的时间"          ,value={"Log.Create.A001"})
    private Date                 operationTime;
    
    /** 消息请求时间。此值不直接写入数据库或文件中 */
    private Date                 msgRequestTime; 
    
    /** 等待写入数据库或写入文件的时间时长(毫秒) */
    private int                  waitTime;
    
    /** 有效数据的个数 */
    private int                  validDataCount;
    
    
    
    public LogInfo()
    {
        this.sysID           = "";
        this.logID           = "";
        this.logType         = "";
        this.logContent      = "";
        this.logInfo         = "";
        this.operatorNo      = "";
        this.operationType   = "";
        this.operationRemark = "";
        this.operationTime   = null;
        this.msgRequestTime  = null;
        this.waitTime        = 0;
        this.validDataCount  = 0;
    }
    
    
    
    @Override
    public String getRowKey()
    { 
        return this.getId();
    }


    
    /**
     * 获取：主键编号(系统采用消息流水号)
     */
    public String getId()
    {
        return id;
    }

    
    
    /**
     * 设置：主键编号(系统采用消息流水号)
     * 
     * @param id 
     */
    public void setId(String id)
    {
        this.id = id;
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
            this.sysID = "";
        }
        else
        {
            this.validDataCount++;
            this.sysID = this.checkMaxLen(i_SysID.trim() ,64);
        }
    }



    /**
     * 获取：日志编号。由外部调用系统传递，当然也可以为空
     */
    public String getLogID()
    {
        return logID;
    }
    

    
    /**
     * 设置：日志编号。由外部调用系统传递，当然也可以为空
     * 
     * @param i_LogID 
     */
    public void setLogID(String i_LogID)
    {
        if ( Help.isNull(i_LogID) )
        {
            this.logID = "";
        }
        else
        {
            this.validDataCount++;
            this.logID = this.checkMaxLen(i_LogID.trim() ,64);
        }
    }
    

    
    /**
     * 获取：日志类型
     */
    public String getLogType()
    {
        return logType;
    }


    
    /**
     * 设置：日志类型
     * 
     * @param i_LogType 
     */
    public void setLogType(String i_LogType)
    {
        if ( Help.isNull(i_LogType) )
        {
            this.logType = "";
        }
        else
        {
            this.validDataCount++;
            this.logType = this.checkMaxLen(i_LogType.trim() ,1000);
        }
    }
    
    
    
    /**
     * 获取：日志种类
     */
    public String getLogClass()
    {
        return logClass;
    }


    
    /**
     * 设置：日志种类
     * 
     * @param i_LogClass 
     */
    public void setLogClass(String i_LogClass)
    {
        if ( Help.isNull(i_LogClass) )
        {
            this.logClass = "";
        }
        else
        {
            this.validDataCount++;
            this.logClass = this.checkMaxLen(i_LogClass.trim() ,1000);
        }
    }


    
    /**
     * 获取：日志内容
     */
    public String getLogContent()
    {
        return logContent;
    }


    
    /**
     * 设置：日志内容
     * 
     * @param i_LogContent 
     */
    public void setLogContent(String i_LogContent)
    {
        if ( Help.isNull(i_LogContent) )
        {
            this.logContent = "";
        }
        else
        {
            this.validDataCount++;
            this.logContent = this.checkMaxLen(i_LogContent.trim() ,65535);
        }
    }
    
    
    
    /**
     * 获取：日志信息
     */
    public String getLogInfo()
    {
        return logInfo;
    }


    
    /**
     * 设置：日志信息
     * 
     * @param i_LogContent 
     */
    public void setLogInfo(String i_LogInfo)
    {
        if ( Help.isNull(i_LogInfo) )
        {
            this.logInfo = "";
        }
        else
        {
            this.validDataCount++;
            this.logInfo = this.checkMaxLen(i_LogInfo.trim() ,65535);
        }
    }


    
    /**
     * 获取：操作人编号
     */
    public String getOperatorNo()
    {
        return operatorNo;
    }


    
    /**
     * 设置：操作人编号
     * 
     * @param i_OperatorNo 
     */
    public void setOperatorNo(String i_OperatorNo)
    {
        if ( Help.isNull(i_OperatorNo) )
        {
            this.operatorNo = "";
        }
        else
        {
            this.validDataCount++;
            this.operatorNo = this.checkMaxLen(i_OperatorNo.trim() ,64);
        }
    }


    
    /**
     * 获取：操作类型
     */
    public String getOperationType()
    {
        return operationType;
    }


    
    /**
     * 设置：操作类型
     * 
     * @param i_OperationType 
     */
    public void setOperationType(String i_OperationType)
    {
        if ( Help.isNull(i_OperationType) )
        {
            this.operationType = "";
        }
        else
        {
            this.validDataCount++;
            this.operationType = this.checkMaxLen(i_OperationType.trim() ,64);
        }
    }


    
    /**
     * 获取：操作备注
     */
    public String getOperationRemark()
    {
        return operationRemark;
    }


    
    /**
     * 设置：操作备注
     * 
     * @param i_OperationRemark 
     */
    public void setOperationRemark(String i_OperationRemark)
    {
        if ( Help.isNull(i_OperationRemark) )
        {
            this.operationRemark = "";
        }
        else
        {
            this.validDataCount++;
            this.operationRemark = this.checkMaxLen(i_OperationRemark.trim() ,1000);
        }
    }


    
    /**
     * 获取：操作发生的时间。当为空时，自动填充为接收消息的时间
     */
    public Date getOperationTime()
    {
        return operationTime;
    }


    
    /**
     * 设置：操作发生的时间。当为空时，自动填充为接收消息的时间
     * 
     * @param i_OperationTime 
     */
    public void setOperationTime(Date i_OperationTime)
    {
        if ( Help.isNull(i_OperationTime) )
        {
            this.operationTime = null;
        }
        else
        {
            this.validDataCount++;
            this.operationTime = i_OperationTime;
        }
    }

    
    
    /**
     * 获取：消息请求时间。此值不直接写入数据库或文件中
     */
    public Date getMsgRequestTime()
    {
        return msgRequestTime;
    }


    
    /**
     * 设置：消息请求时间。此值不直接写入数据库或文件中
     * 
     * @param msgRequestTime 
     */
    public void setMsgRequestTime(Date msgRequestTime)
    {
        this.msgRequestTime = msgRequestTime;
    }


    
    /**
     * 获取：等待写入数据库或写入文件的时间时长(毫秒)
     */
    public int getWaitTime()
    {
        return waitTime;
    }


    
    /**
     * 设置：等待写入数据库或写入文件的时间时长(毫秒)
     * 
     * @param waitTime 
     */
    public void setWaitTime(int waitTime)
    {
        this.waitTime = waitTime;
    }



    /**
     * 获取：有效数据的个数
     */
    public int getValidDataCount()
    {
        return validDataCount;
    }

}
