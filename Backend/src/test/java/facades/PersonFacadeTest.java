/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.dto.PersonDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.EMF_Creator;

/**
 *
 * @author Martin Frederiksen
 */
public class PersonFacadeTest {
    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person p1, p2;
    private static Address a1;
    private static CityInfo cI1;
    private static Hobby h1, h2;
    private static List<Hobby> hobbies;
    private static List<PersonDTO> persons;
    
    public PersonFacadeTest() {
    }
    
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
        facade = PersonFacade.getPersonFacade(emf);
    }
    
    @BeforeAll
    public static void setUpClassV2() {
       emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,EMF_Creator.Strategy.DROP_AND_CREATE);
       facade = PersonFacade.getPersonFacade(emf);
    }

    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        persons = new ArrayList();
        hobbies = new ArrayList();
        
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            
            cI1 = new CityInfo("Agentcity", "007");
            a1 = new Address("SilenceStreet", cI1);
            em.persist(cI1);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(a1);
            em.getTransaction().commit();
            em.getTransaction().begin();
            
            h1 = new Hobby("Programming", "Coding all day");
            h2 = new Hobby("Hacking", "Hacking wifi");
            em.persist(h1);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(h2);
            em.getTransaction().commit();
            hobbies.add(h1);
            hobbies.add(h2);
            
            p1 = new Person("MyMail@google.com", "11223344", "Bond", "James");
            p2 = new Person("Maily@google.com", "12345678", "Agent", "47");
            
            p1.setAddress(a1);
            p1.setHobbies(hobbies);
            p2.setAddress(a1);
            p2.setHobbies(hobbies);
            
            em.getTransaction().begin();
            em.persist(p1);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(p2);
            em.getTransaction().commit();
            persons.add(new PersonDTO(p1));
            persons.add(new PersonDTO(p2));
            
        } finally {
            em.close();
        }
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getPersonFacade method, of class PersonFacade.
     */
    @Test
    public void testGetPersonFacade() {
        EntityManagerFactory _emf = emf.createEntityManager().getEntityManagerFactory();
        PersonFacade expResult = facade;
        PersonFacade result = PersonFacade.getPersonFacade(_emf);
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllPersons method, of class PersonFacade.
     */
    @Test
    public void testGetAllPersons() {
        List<PersonDTO> expResult = persons;
        List<PersonDTO> result = facade.getAllPersons();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPersonById method, of class PersonFacade.
     */
    @Test
    public void testGetPersonById() {
        PersonDTO expResult = new PersonDTO(p1);
        PersonDTO result = facade.getPersonById(p1.getId());
        assertEquals(expResult, result);
    }

    /**
     * Test of addPerson method, of class PersonFacade.
     */
    @Test
    public void testAddPersonTrue() {
        Person p3 = new Person("Test@google.com", "11111111", "TestFrist", "TestLast");
        PersonDTO expResult = facade.addPerson(p3);
        PersonDTO result = facade.getPersonById(p3.getId());
        assertEquals(expResult, result);
    }
    
    /**
     * Test of addPerson method, of class PersonFacade.
     */
    @Test
    public void testAddPersonFalse() {
        facade.addPerson(p2);
        List<PersonDTO> result = facade.getAllPersons();
        assertNotEquals(3, result);
    }

    /**
     * Test of deletePerson method, of class PersonFacade.
     */
    @Test
    public void testDeletePerson() {
        EntityManager em = emf.createEntityManager();
        Person p3 = new Person("Test@google.com", "11111111", "TestFrist", "TestLast");
        try {
            em.getTransaction().begin();
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        PersonDTO expResult = new PersonDTO(p3);
        PersonDTO result = facade.deletePerson(p3.getId());
        assertEquals(expResult, result);
    }

    /**
     * Test of editPerson method, of class PersonFacade.
     */
    @Test
    public void testEditPerson() {
        p2.setFirstName("TestFirstName");
        PersonDTO expResult = new PersonDTO(p2);
        PersonDTO result = facade.editPerson(p2);
        assertEquals(expResult, result);
    }
    
}
