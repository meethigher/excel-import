package top.meethigher.domain.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

/**
 * 档案
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 08:50
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Doc {

    /**
     * 业务主键
     */
    @Id
    private String id;


    /**
     * 名称
     */
    private String name;


    /**
     * 居住地址经度
     */
    private Double locLon;

    /**
     * 居住地址纬度
     */
    private Double locLat;

    /**
     * 居住地址
     */
    private String loc;

    /**
     * 地址编号
     */
    private String locId;

    /**
     * 机器码
     */
    private String machineCode;

    /**
     * 身份证号
     */
    private String cardId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLocLon() {
        return locLon;
    }

    public void setLocLon(Double locLon) {
        this.locLon = locLon;
    }

    public Double getLocLat() {
        return locLat;
    }

    public void setLocLat(Double locLat) {
        this.locLat = locLat;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
