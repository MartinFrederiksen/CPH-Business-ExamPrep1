/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Hobby;
import entities.dto.HobbyDTO;
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
 * @author Joe
 */
public class HobbyFacadeTest {
    private static EntityManagerFactory emf;
    private static HobbyFacade facade;
    private static Hobby h1, h2;
    private static List<HobbyDTO> hobbies;
    
    public HobbyFacadeTest() {
    }

    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
        facade = HobbyFacade.getHobbyFacade(emf);
    }
    
    @BeforeAll
    public static void setUpClassV2() {
       emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,EMF_Creator.Strategy.DROP_AND_CREATE);
       facade = HobbyFacade.getHobbyFacade(emf);
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        hobbies = new ArrayList();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            h1 = new Hobby("Programming", "Coding all day");
            h2 = new Hobby("Hacking", "Hacking wifi");
            em.persist(h1);
            em.getTransaction().commit();
            em.persist(h2);
            em.getTransaction().begin();
            em.getTransaction().commit();
            hobbies.add(new HobbyDTO(h1));
            hobbies.add(new HobbyDTO(h2));
        } finally {
            em.close();
        }
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getHobbyFacade method, of class HobbyFacade.
     */
    @Test
    public void testGetHobbyFacade() {
        System.out.println("getHobbyFacade");
        EntityManagerFactory _emf = emf.createEntityManager().getEntityManagerFactory();
        HobbyFacade expResult = facade;
        HobbyFacade result = HobbyFacade.getHobbyFacade(_emf);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetAllHobbies() {
        List<HobbyDTO> expResult = hobbies;
        List<HobbyDTO> result = facade.getHobbies();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetHobbyByID() {
        HobbyDTO expResult = hobbies.get(0);
        HobbyDTO result = facade.getHobbyById(hobbies.get(0).getId());
        assertEquals(expResult, result);
    }
    
    @Test
    public void testAddHobbyTrue() {
        Hobby h3 = new Hobby("TestHobby", "Test test test");
        HobbyDTO expResult = facade.addHobby(h3);
        HobbyDTO result = facade.getHobbyById(h3.getId());
        assertEquals(expResult, result);
    }
    
    @Test
    public void testAddHobbyFalse() {
        facade.addHobby(h2);
        List<HobbyDTO> result = facade.getHobbies();
        assertNotEquals(3, result.size());
    }
    
    @Test
    public void testDeleteHobby() {
        EntityManager em = emf.createEntityManager();
        Hobby h3 = new Hobby("TestHobby", "Test test test");
        try {
            em.getTransaction().begin();
            em.persist(h3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        HobbyDTO expResult = new HobbyDTO(h3);
        HobbyDTO result = facade.deleteHobby(h3.getId());
        assertEquals(expResult, result);
    }
    
    @Test
    public void testEditHobby() {
        h2.setName("New name for test");
        HobbyDTO expResult = new HobbyDTO(h2);
        HobbyDTO result = facade.editHobby(h2);
        assertEquals(expResult, result);
    }
}
