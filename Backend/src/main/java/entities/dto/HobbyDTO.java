package entities.dto;

import entities.Hobby;
import entities.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Martin Frederiksen
 */
public class HobbyDTO {
    private Long id;
    private String name;
    private String description;
    private List<PersonDTO> persons;

    public HobbyDTO() {
    }

    public HobbyDTO(Hobby hobby) {
        this.id = hobby.getId();
        this.name = hobby.getName();
        this.description = hobby.getDescription();
        
        /*persons = new ArrayList();
        for(Person p : hobby.getPersons()) {
            persons.add(new PersonDTO(p));
        }*/
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.description);
        hash = 97 * hash + Objects.hashCode(this.persons);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HobbyDTO other = (HobbyDTO) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.persons, other.persons)) {
            return false;
        }
        return true;
    }
    
}
