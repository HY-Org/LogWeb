package org.hy.log.common;





/**
 * 基础数据模型类
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-09-23
 * @version     v1.0  
 */
public abstract class BaseModel extends BaseEntity implements Comparable<BaseModel>
{
    
    private static final long serialVersionUID = -3598457321671729930L;
    
    
    
    /**
     * 获取比较、对比、相等操作的数值
     */
    public String getCompareValue()
    {
        return this.getRowKey();
    }
    
    
    
    public int compareTo(BaseModel i_Other)
    {
        if ( i_Other == null )
        {
            return 1;
        }
        else if ( this == i_Other )
        {
            return 0;
        }
        else if ( this.getClass().equals(i_Other.getClass()) )
        {
            if ( i_Other.getCompareValue() == null )
            {
                return 1;
            }
            else if ( this.getCompareValue() == null )
            {
                return -1;
            }
            else
            {
                return this.getCompareValue().compareTo(i_Other.getCompareValue());
            }
        }
        else
        {
            return 1;
        }
    }
    
    
    
    @Override
    public int hashCode()
    {
        if ( this.getCompareValue() == null )
        {
            return -1;
        }
        else
        {
            return this.getCompareValue().hashCode();
        }
    }


    
    @Override
    public boolean equals(Object i_Other)
    {
        if ( i_Other == null )
        {
            return false;
        }
        else if ( this == i_Other )
        {
            return true;
        }
        else if ( this.getClass().equals(i_Other.getClass()) )
        {
            BaseModel v_Other = (BaseModel)i_Other;
            if ( v_Other.getCompareValue() == null )
            {
                return false;
            }
            else if ( this.getCompareValue() == null )
            {
                return false;
            }
            else
            {
                return this.getCompareValue().equals(v_Other.getCompareValue());
            }
        }
        else if ( i_Other.getClass().equals(String.class) )
        {
            return i_Other.toString().equals(this.getRowKey());
        }
        else
        {
            return false;
        }
    }

    
    
    /**
     * 获取行主键
     */
    public abstract String getRowKey();
    
}
