package com.yinghai.macao.common.model;

import java.util.Date;

public class SpcarDriver {
	  /**
     * 驗證碼提示
     */
    public static final String msg = "澳門專車司機端驗證碼：";
	/**
     * 重置密码code
     */
    public static final String reset_code = "driverReset";
    /**
     * 绑定手机号code
     */
    public static final String bind_code = "driverBind";

    /**
     * 载客中
     */
    public static final String DRIVER_WORK_STATUS="0";
    /**
     * 上线中
     */
    public static final String DRIVER_ONLINE_STATUS="1";
    /**
     * 已指派訂單
     */
    public static final String DRIVER_ASSIGNED_STATUS="2";
    /**
     * 前往载客
     */
    public static final String DRIVER_READY_STATUS="3";
    /**
     * 到达目的地等待
     */
    public static final String DRIVER_ARRIVE="4";
    /**
     * 离线
     */
    public static final String DRIVER_OFFLINE_STATUS="999";

    private Integer spcarDriverId;

    private String name;

    private Double locX;

    private Double locY;

    private String status;

    private String license;

    private String image;

    private String driverType;

    private String spcarType;

    private Date createTime;

    private Date lastUpdate;

    private String deviceId;

    private String verification;

    private String countryCode;

    private String tel;

    private String givenName;

    private String familyName;

    private Integer userid;

    private Double rating;

    private Boolean englishCapability;

    private String profileImage;

    private Integer orderCount;

    private Integer cancelCount;

    private Integer finishCount;
    
    private Integer finishAmount;

	private Boolean deleted;

    private String licenseTrue;

    private Date licenseTill;

    private Date driverSetoutTime;
    
    private String password;

    private Time driverSetoutTimeL;
    
    public Integer getFinishAmount() {
		return finishAmount;
	}

	public void setFinishAmount(Integer finishAmount) {
		this.finishAmount = finishAmount;
	}

    public Date getDriverSetoutTime() {
        return driverSetoutTime;
    }

    public void setDriverSetoutTime(Date driverSetoutTime) {
        this.driverSetoutTime = driverSetoutTime;
    }

    public Time getDriverSetoutTimeL() {
        if(this.driverSetoutTime==null){
            return new Time();
        }
        return getTime(this.driverSetoutTime);
    }

    public void setDriverSetoutTimeL(Time driverSetoutTimeL) {
        if(this.driverSetoutTime==null){
            this.driverSetoutTimeL = new Time();
        }
        this.driverSetoutTimeL = getTime(driverSetoutTime);
    }

    private Date driverArriveTime;

    private Time driverArriveTimeL;

    public Date getDriverArriveTime() {
        return driverArriveTime;
    }

    public void setDriverArriveTime(Date driverArriveTime) {
        this.driverArriveTime = driverArriveTime;
    }

    public Time getDriverArriveTimeL() {
        if(this.driverArriveTime==null){
            return new Time();
        }
        return getTime(this.driverArriveTime);
    }

    public void setDriverArriveTimeL(Time driverArriveTimeL) {
        if(this.driverArriveTime==null){
            this.driverArriveTimeL = new Time();
        }
        this.driverArriveTimeL = getTime(driverArriveTime);
    }
    private Date passengerGetinTime;

    private Time passengerGetinTimeL;

    public Date getPassengerGetinTime() {
        return passengerGetinTime;
    }

    public void setPassengerGetinTime(Date passengerGetinTime) {
        this.passengerGetinTime = passengerGetinTime;
    }

    public Time getPassengerGetinTimeL() {
        if(this.passengerGetinTime==null){
            return new Time();
        }
        return getTime(this.passengerGetinTime);
    }

    public void setPassengerGetinTimeL(Time passengerGetinTimeL) {
        if(this.passengerGetinTime==null){
            this.passengerGetinTimeL = new Time();
        }
        this.passengerGetinTimeL = getTime(passengerGetinTime);
    }

    private Time getTime(Date date){
        Date now = new Date();
        long temp = now.getTime()-date.getTime();
        long day = temp / (24 * 60 * 60 * 1000);
        long hour = (temp / (60 * 60 * 1000) - day * 24);
        long min = ((temp / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (temp / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return new Time(hour+day*24,min,s);
    }
    private String shift;

    private String deviceType;

    private String locStr;
    private String imName;
    
    private String sign;

    public String getImName() {
		return imName;
	}

	public void setImName(String imName) {
		this.imName = imName;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

    public Integer getSpcarDriverId() {
        return spcarDriverId;
    }

    public void setSpcarDriverId(Integer spcarDriverId) {
        this.spcarDriverId = spcarDriverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Double getLocX() {
        return locX;
    }

    public void setLocX(Double locX) {
        this.locX = locX;
    }

    public Double getLocY() {
        return locY;
    }

    public void setLocY(Double locY) {
        this.locY = locY;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license == null ? null : license.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType == null ? null : driverType.trim();
    }

    public String getSpcarType() {
        return spcarType;
    }

    public void setSpcarType(String spcarType) {
        this.spcarType = spcarType == null ? null : spcarType.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification == null ? null : verification.trim();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode == null ? null : countryCode.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName == null ? null : givenName.trim();
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName == null ? null : familyName.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getEnglishCapability() {
        return englishCapability;
    }

    public void setEnglishCapability(Boolean englishCapability) {
        this.englishCapability = englishCapability;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage == null ? null : profileImage.trim();
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(Integer cancelCount) {
        this.cancelCount = cancelCount;
    }

    public Integer getFinishCount() {
        return finishCount;
    }

    public void setFinishCount(Integer finishCount) {
        this.finishCount = finishCount;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getLicenseTrue() {
        return licenseTrue;
    }

    public void setLicenseTrue(String licenseTrue) {
        this.licenseTrue = licenseTrue == null ? null : licenseTrue.trim();
    }

    public Date getLicenseTill() {
        return licenseTill;
    }

    public void setLicenseTill(Date licenseTill) {
        this.licenseTill = licenseTill;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift == null ? null : shift.trim();
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }

    public String getLocStr() {
        return locStr;
    }

    public void setLocStr(String locStr) {
        this.locStr = locStr == null ? null : locStr.trim();
    }
    
    private String countryCode2;
    
    private String tel2;
    
    private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCountryCode2() {
		return countryCode2;
	}

	public void setCountryCode2(String countryCode2) {
		this.countryCode2 = countryCode2;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
}