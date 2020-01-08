package entities.dto;

import entities.CityInfo;

/**
 *
 * @author Martin Frederiksen
 */
public class CityInfoDTO {
    private Long id;
    private String cityName;
    private String zip;

    public CityInfoDTO() {
    }

    public CityInfoDTO(CityInfo cityInfo) {
        this.id = cityInfo.getId();
        this.cityName = cityInfo.getCityName();
        this.zip = cityInfo.getZip();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
