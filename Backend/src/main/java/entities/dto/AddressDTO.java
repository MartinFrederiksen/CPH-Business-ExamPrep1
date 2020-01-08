package entities.dto;

import java.util.List;
import entities.Address;
import entities.Person;
import java.util.ArrayList;

/**
 *
 * @author Martin Frederiksen
 */
public class AddressDTO {
    private Long id;
    private String streetName;
    private CityInfoDTO cityInfo;
    private List<PersonDTO> persons;

    public AddressDTO() {
    }

    public AddressDTO(Address address) {
        this.id = address.getId();
        this.streetName = address.getStreetName();
        this.cityInfo = new CityInfoDTO(address.getCityInfo());
        
        persons = new ArrayList();
        for(Person p : address.getPersons()){
            persons.add(new PersonDTO(p));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public CityInfoDTO getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfoDTO cityInfo) {
        this.cityInfo = cityInfo;
    }

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }
    
}
