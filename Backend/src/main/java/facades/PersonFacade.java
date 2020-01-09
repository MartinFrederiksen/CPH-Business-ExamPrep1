package facades;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.dto.HobbyDTO;
import entities.dto.PersonDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Martin Frederiksen
 */
public class PersonFacade implements IFacade<PersonDTO>{
    private static EntityManagerFactory emf;
    private static PersonFacade instance;
    
    private PersonFacade(){}
    public static PersonFacade getFacade (EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }
    
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    @Override
    public List<PersonDTO> getAll() {
        return getEntityManager().createQuery("SELECT person FROM Person person").getResultList();
    }

    @Override
    public PersonDTO getById(long id) {
        Person person = getEntityManager().find(Person.class, id);
        if(person == null) {
            throw new WebApplicationException("Could not find Person with id: " + id);
        }
        return new PersonDTO(person);
    }
    
    public PersonDTO getByEmail(String email) {
        try {
            PersonDTO pDTO = getEntityManager().createQuery("SELECT new entities.dto.PersonDTO(person) FROM Person person WHERE person.email = :email", PersonDTO.class).setParameter("email", email).getSingleResult();
            return pDTO;
        } catch(NoResultException ex) {
            throw new WebApplicationException("Could not find Person with email: " + email);
        }
    }
    
    public PersonDTO getByPhone(String phone) {
        try {
            PersonDTO pDTO = getEntityManager().createQuery("SELECT new entities.dto.PersonDTO(person) FROM Person person WHERE person.phone = :phone", PersonDTO.class).setParameter("phone", phone).getSingleResult();
            return pDTO;
        } catch(NoResultException ex) {
            throw new WebApplicationException("Could not find Person with phone: " + phone);
        }
    }

    @Override
    public PersonDTO add(PersonDTO personDTO) {
        EntityManager em = getEntityManager();
        try {
            List<Person> lookupPerson = em.createQuery("SELECT person FROM Person person WHERE person.email =: email AND person.phone = :phone AND person.firstName = :firstName AND person.lastName = :lastName").setParameter("email", personDTO.getEmail()).setParameter("phone", personDTO.getPhone()).setParameter("firstName", personDTO.getFirstName()).setParameter("lastName", personDTO.getLastName()).getResultList();
            if(lookupPerson.size() != 0) {
                return new PersonDTO(lookupPerson.get(0));
            }
            List<Hobby> addHobbies = new ArrayList();
            for(HobbyDTO h : personDTO.getHobbies()){
                addHobbies.add(new Hobby(h.getName(), h.getDescription(), null));
            }
            Address addAddress = new Address(personDTO.getAddress().getStreet(), personDTO.getAddress().getCity(), personDTO.getAddress().getZip(), null);
            Person addPerson = new Person(personDTO.getEmail(), personDTO.getPhone(), personDTO.getFirstName(), personDTO.getLastName(), addAddress, addHobbies);
            em.getTransaction().begin();
            em.persist(addPerson);
            em.getTransaction().commit();
            return new PersonDTO(addPerson);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO delete(long id) {
        EntityManager em = getEntityManager();
        try {
            Person person = em.find(Person.class, id);
            if(person == null) {
                throw new WebApplicationException("Could not find Person with id: " + id);
            }
            em.getTransaction().begin();
            em.remove(person);
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO edit(PersonDTO personDTO) {
        EntityManager em = getEntityManager();
        try {
            Person person = em.find(Person.class, personDTO.getId());
            if(person == null) {
                throw new WebApplicationException("Could not find Person with id: " + personDTO.getId());
            }
            person.setEmail(personDTO.getEmail());
            person.setPhone(personDTO.getPhone());
            person.setFirstName(personDTO.getFirstName());
            person.setLastName(personDTO.getLastName());
            Address address = new Address(personDTO.getAddress().getStreet(), personDTO.getAddress().getCity(), personDTO.getAddress().getZip(), null);
            List<Hobby> hobbies = new ArrayList();
            for(HobbyDTO h : personDTO.getHobbies()){
                hobbies.add(new Hobby(h.getName(), h.getDescription(), null));
            }
            person.setAddress(address);
            person.setHobbies(hobbies);
            
            
            em.getTransaction().begin();
            em.merge(person);
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }
}
