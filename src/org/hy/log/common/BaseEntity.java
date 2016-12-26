package org.hy.log.common;

import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.xml.SerializableDef;





/**
 * 基础实体类，可序列化的类
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-11-20
 * @version     v1.0
 */
public class BaseEntity extends SerializableDef
{
    
    private static final long serialVersionUID = 1406383050091755803L;

    
    /** 时间戳 */
    private Long t;
    
    
    
    /**
     * 用序列化的另一个类初始化'我自己'。
     * 
     * 主要用于父类无法强转为子类的情况
     * 
     * @author      WangZhanBin
     * @createDate  2014-11-20
     * @version     v1.0
     *
     * @param i_SerialObj
     */
    public void init(SerializableDef i_SerialObj)
    {
        super.init(i_SerialObj);
    }
    
    
    
    /**
     * 用Map集合初始化'我自己'。
     * 
     * @author      WangZhanBin
     * @createDate  2014-11-20
     * @version     v1.0
     *
     * @param i_Datas
     */
    public void init(Map<String ,Object> i_Datas)
    {
        super.init(i_Datas);
    }
    
    
    
    /**
     * 限制性截取字符串
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-13
     * @version     v1.0
     *
     * @param i_Text
     * @param i_MaxLen   限制最大长度
     * @return
     */
    protected String checkMaxLen(String i_Text ,int i_MaxLen)
    {
        if ( i_Text.length() > i_MaxLen )
        {
            return i_Text.substring(0 ,i_MaxLen);
        }
        else
        {
            return i_Text;
        }
    }
    
    
    
    public Long getT()
    {
        this.t = Help.NVL(this.t ,(new Date()).getTime());
        return this.t;
    }


    
    public void setT(Long t)
    {
        this.t = t;
    }
    
}
