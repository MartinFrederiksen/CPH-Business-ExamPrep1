package facades;

import entities.Hobby;
import entities.dto.HobbyDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Martin Frederiksen
 */
public class HobbyFacade {
    private static HobbyFacade instance;
    private static EntityManagerFactory emf;
    
    private HobbyFacade() {}
    
    
    public static HobbyFacade getHobbyFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public List<HobbyDTO> getHobbies() {
        return getEntityManager().createQuery("SELECT new entities.dto.HobbyDTO(hobby) FROM Hobby hobby").getResultList();
    }
    
    public HobbyDTO getHobbyById(Long id) {
        return new HobbyDTO(getEntityManager().find(Hobby.class, id));
    }
    
    public HobbyDTO addHobby(Hobby newHobby) {
        EntityManager em = getEntityManager();
        List<Hobby> hobby = em.createQuery("SELECT hobby FROM Hobby hobby WHERE hobby.name = :name AND hobby.description = :description", Hobby.class).setParameter("name", newHobby.getName()).setParameter("description", newHobby.getDescription()).getResultList();
        try {
            if(hobby.isEmpty()) {
                em.getTransaction().begin();
                em.persist(newHobby);
                em.getTransaction().commit();
            }  
        } finally {
            em.close();
        }
        return new HobbyDTO(newHobby);
    }
    
    public HobbyDTO deleteHobby(Long id) {
        EntityManager em = getEntityManager();
        Hobby hobby = em.find(Hobby.class, id);
        try {
            em.getTransaction().begin();
            em.remove(hobby);
            em.getTransaction().commit();
        } finally {
           em.close();
        }
        return new HobbyDTO(hobby);
    }
    
    public HobbyDTO editHobby(Hobby hobby) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(hobby);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new HobbyDTO(hobby);
    }
    
    
}
