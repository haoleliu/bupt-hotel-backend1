package com.haole.bupthotelbackend.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName room
 */
@TableName(value ="room")
@Data
public class Room implements Serializable {
    /**
     * 
     */
    @TableId
    private String roomNumber;

    /**
     * 
     */
    private Integer isOccupied;

    /**
     * 
     */
    private Date checkInDate;

    /**
     * 
     */
    private Date checkOutDate;

    /**
     * 
     */
    private BigDecimal totalFee;

    /**
     * 
     */
    private Integer environmentTemperature;

    /**
     * 
     */
    private BigDecimal currentTemperature;

    /**
     * 
     */
    private Integer power;

    /**
     * 
     */
    private Integer acUsageTime;

    /**
     * 
     */
    private BigDecimal acFee;

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
        Room other = (Room) that;
        return (this.getRoomNumber() == null ? other.getRoomNumber() == null : this.getRoomNumber().equals(other.getRoomNumber()))
            && (this.getIsOccupied() == null ? other.getIsOccupied() == null : this.getIsOccupied().equals(other.getIsOccupied()))
            && (this.getCheckInDate() == null ? other.getCheckInDate() == null : this.getCheckInDate().equals(other.getCheckInDate()))
            && (this.getCheckOutDate() == null ? other.getCheckOutDate() == null : this.getCheckOutDate().equals(other.getCheckOutDate()))
            && (this.getTotalFee() == null ? other.getTotalFee() == null : this.getTotalFee().equals(other.getTotalFee()))
            && (this.getEnvironmentTemperature() == null ? other.getEnvironmentTemperature() == null : this.getEnvironmentTemperature().equals(other.getEnvironmentTemperature()))
            && (this.getCurrentTemperature() == null ? other.getCurrentTemperature() == null : this.getCurrentTemperature().equals(other.getCurrentTemperature()))
            && (this.getPower() == null ? other.getPower() == null : this.getPower().equals(other.getPower()))
            && (this.getAcUsageTime() == null ? other.getAcUsageTime() == null : this.getAcUsageTime().equals(other.getAcUsageTime()))
            && (this.getAcFee() == null ? other.getAcFee() == null : this.getAcFee().equals(other.getAcFee()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRoomNumber() == null) ? 0 : getRoomNumber().hashCode());
        result = prime * result + ((getIsOccupied() == null) ? 0 : getIsOccupied().hashCode());
        result = prime * result + ((getCheckInDate() == null) ? 0 : getCheckInDate().hashCode());
        result = prime * result + ((getCheckOutDate() == null) ? 0 : getCheckOutDate().hashCode());
        result = prime * result + ((getTotalFee() == null) ? 0 : getTotalFee().hashCode());
        result = prime * result + ((getEnvironmentTemperature() == null) ? 0 : getEnvironmentTemperature().hashCode());
        result = prime * result + ((getCurrentTemperature() == null) ? 0 : getCurrentTemperature().hashCode());
        result = prime * result + ((getPower() == null) ? 0 : getPower().hashCode());
        result = prime * result + ((getAcUsageTime() == null) ? 0 : getAcUsageTime().hashCode());
        result = prime * result + ((getAcFee() == null) ? 0 : getAcFee().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", roomNumber=").append(roomNumber);
        sb.append(", isOccupied=").append(isOccupied);
        sb.append(", checkInDate=").append(checkInDate);
        sb.append(", checkOutDate=").append(checkOutDate);
        sb.append(", totalFee=").append(totalFee);
        sb.append(", environmentTemperature=").append(environmentTemperature);
        sb.append(", currentTemperature=").append(currentTemperature);
        sb.append(", power=").append(power);
        sb.append(", acUsageTime=").append(acUsageTime);
        sb.append(", acFee=").append(acFee);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}