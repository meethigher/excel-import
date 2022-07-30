package top.meethigher.domain.command;


import top.meethigher.utils.ValidateUtil;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * doc上传
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 16:26
 */
public class ImportDocCommand {

    /**
     * 业务主键
     */
    @NotBlank(message = "编号不能为空")
    private String id;


    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;


    /**
     * 居住地址经度
     */
    @NotNull(message = "经度不能为空")
    @Max(value = 180, message = "不能超过180度")
    @Min(value = -180, message = "不能低于-180度")
    private Double locLon;

    /**
     * 居住地址纬度
     */
    @NotNull(message = "纬度不能为空")
    @Max(value = 90, message = "不能超过90度")
    @Min(value = -90, message = "不能低于-90度")
    private Double locLat;

    /**
     * 居住地址
     */
    @NotBlank(message = "居住地址不能为空")
    private String loc;

    /**
     * 地址编号
     */
    private String locId;

    /**
     * 机器码
     */
    @NotBlank(message = "机器码不能为空")
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

    /**
     * 构造函数
     *
     * @param id
     * @param name
     * @param locLon
     * @param locLat
     * @param loc
     * @param locId
     * @param machineCode
     * @param cardId
     */
    private ImportDocCommand(String id, String name, Double locLon,
                             Double locLat, String loc, String locId,
                             String machineCode, String cardId) {
        this.id = id;
        this.name = name;
        this.locLon = locLon;
        this.locLat = locLat;
        this.loc = loc;
        this.locId = locId;
        this.machineCode = machineCode;
        this.cardId = cardId;
    }

    /**
     * 校验
     *
     * @param id
     * @param name
     * @param locLon
     * @param locLat
     * @param loc
     * @param locId
     * @param machineCode
     * @param cardId
     * @return
     */
    public static VerifyResult<ImportDocCommand> verify(String id, String name, Double locLon,
                                                        Double locLat, String loc, String locId,
                                                        String machineCode, String cardId) {
        ImportDocCommand command = new ImportDocCommand(id, name, locLon, locLat, loc, locId, machineCode, cardId);
        VerifyResult<ImportDocCommand> result;
        try {
            ValidateUtil.validate(command);
            result = new VerifyResult<>(true, null, command);
        } catch (Exception e) {
            result = new VerifyResult<>(false, e.getMessage(), null);
        }
        return result;
    }
}
