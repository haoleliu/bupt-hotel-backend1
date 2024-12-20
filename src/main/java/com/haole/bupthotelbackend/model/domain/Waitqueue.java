package com.haole.bupthotelbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName waitqueue
 */
@TableName(value ="waitqueue")
@Data
public class Waitqueue implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer roomNum;

    /**
     * 
     */
    private Integer weight;

    /**
     * 
     */
    private Date lastRequestTime;

    /**
     * 
     */
    private Integer isWaiting;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Waitqueue other = (Waitqueue) that;
        return (this.getRoomNum() == null ? other.getRoomNum() == null : this.getRoomNum().equals(other.getRoomNum()))
            && (this.getWeight() == null ? other.getWeight() == null : this.getWeight().equals(other.getWeight()))
            && (this.getLastRequestTime() == null ? other.getLastRequestTime() == null : this.getLastRequestTime().equals(other.getLastRequestTime()))
            && (this.getIsWaiting() == null ? other.getIsWaiting() == null : this.getIsWaiting().equals(other.getIsWaiting()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRoomNum() == null) ? 0 : getRoomNum().hashCode());
        result = prime * result + ((getWeight() == null) ? 0 : getWeight().hashCode());
        result = prime * result + ((getLastRequestTime() == null) ? 0 : getLastRequestTime().hashCode());
        result = prime * result + ((getIsWaiting() == null) ? 0 : getIsWaiting().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", roomNum=").append(roomNum);
        sb.append(", weight=").append(weight);
        sb.append(", lastRequestTime=").append(lastRequestTime);
        sb.append(", isWaiting=").append(isWaiting);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}