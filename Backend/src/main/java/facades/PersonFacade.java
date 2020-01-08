package facades;

import entities.Person;
import entities.dto.PersonDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Martin Frederiksen
 */
public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<PersonDTO> getAllPersons() {
        return getEntityManager().createQuery("SELECT new entities.dto.PersonDTO(person) FROM Person person").getResultList();
    }

    public PersonDTO getPersonById(Long id) {
        return new PersonDTO(getEntityManager().find(Person.class, id));
    }

    public PersonDTO addPerson(Person newPerson) {
        EntityManager em = getEntityManager();
        List<Person> person = em.createQuery("SELECT person FROM Person person WHERE person.firstName = :firstName AND person.lastName = :lastName AND person.phone = :phone AND person.email = :email", Person.class).setParameter("firstName", newPerson.getFirstName()).setParameter("lastName", newPerson.getLastName()).setParameter("phone", newPerson.getPhone()).setParameter("email", newPerson.getEmail()).getResultList();
        try {
            if (person.isEmpty()) {
                em.getTransaction().begin();
                em.persist(newPerson);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }
        return new PersonDTO(newPerson);
    }

    public PersonDTO deletePerson(Long id) {
        EntityManager em = getEntityManager();
        Person person = em.find(Person.class, id);
        try {
            em.getTransaction().begin();
            em.remove(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }

    public PersonDTO editPerson(Person person) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }
}
