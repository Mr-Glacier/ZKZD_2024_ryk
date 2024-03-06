package org.example.Entity;

public class BeanCityVersion {

    private int C_ID;
    private String VersionId;
    private String VersionName;
    private String CityId;
    private String CityName;
    private String PriceUrl;
    private String C_DownState;
    private String C_DownTime;

    public int getC_ID() {
        return C_ID;
    }

    public void setC_ID(int c_ID) {
        C_ID = c_ID;
    }

    public String getVersionId() {
        return VersionId;
    }

    public void setVersionId(String versionId) {
        VersionId = versionId;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String cityId) {
        CityId = cityId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getPriceUrl() {
        return PriceUrl;
    }

    public void setPriceUrl(String priceUrl) {
        this.PriceUrl = priceUrl;
    }

    public String getC_DownState() {
        return C_DownState;
    }

    public void setC_DownState(String c_DownState) {
        C_DownState = c_DownState;
    }

    public String getC_DownTime() {
        return C_DownTime;
    }

    public void setC_DownTime(String c_DownTime) {
        C_DownTime = c_DownTime;
    }
}
