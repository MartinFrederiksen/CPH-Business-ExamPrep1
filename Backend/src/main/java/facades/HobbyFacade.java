package facades;

import entities.Hobby;
import entities.dto.HobbyDTO;
import entities.dto.PersonListDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Martin Frederiksen
 */
public class HobbyFacade implements IFacade<HobbyDTO>{
    private static EntityManagerFactory emf;
    private static HobbyFacade instance;
    
    private HobbyFacade(){}
    public static HobbyFacade getFacade (EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }
    
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    @Override
    public List<HobbyDTO> getAll() {
        return getEntityManager().createQuery("SELECT new entities.dto.HobbyDTO(hobby) FROM Hobby hobby", HobbyDTO.class).getResultList();
    }

    @Override
    public HobbyDTO getById(long id) {
        Hobby hobby = getEntityManager().find(Hobby.class, id);
        if(hobby == null){
            throw new WebApplicationException("Could not find hobby with id: " + id);
        }
        HobbyDTO hDto = new HobbyDTO(hobby);
        hDto.setPersons(new PersonListDTO(hobby.getPersons()).getPersonsDTO());
        return hDto;
    }

    @Override
    public HobbyDTO add(HobbyDTO hobbyDTO) {
        //Hobby hobby = getEntityManager().find(Hobby.class, id);
        //if hobby==null????
        EntityManager em = getEntityManager();
        try {
            List<Hobby> lookupHobby = em.createQuery("SELECT hobby FROM Hobby hobby WHERE hobby.name = :name AND hobby.description = :description").setParameter("name", hobbyDTO.getName()).setParameter("description", hobbyDTO.getDescription()).getResultList();
            if(lookupHobby.size() != 0) {
                return new HobbyDTO(lookupHobby.get(0));
            }
            Hobby addHobby = new Hobby(hobbyDTO.getName(), hobbyDTO.getDescription(), null);
            em.getTransaction().begin();
            em.persist(addHobby);
            em.getTransaction().commit();
            return new HobbyDTO(addHobby);
        } finally {
            em.close();
        }
    }

    @Override
    public HobbyDTO delete(long id) {
        EntityManager em = getEntityManager();
        try {
            Hobby hobby = em.find(Hobby.class, id);
            if(hobby == null) {
                throw new WebApplicationException("Could not find hobby with id: " + id);
            }
            em.getTransaction().begin();
            em.remove(hobby);
            em.getTransaction().commit();
            return new HobbyDTO(hobby);
        } finally {
            em.close();
        }
    }

    @Override
    public HobbyDTO edit(HobbyDTO hobbyDTO) {
        EntityManager em = getEntityManager();
        try {
            Hobby hobby = em.find(Hobby.class, hobbyDTO.getId());
            if(hobby == null) {
                throw new WebApplicationException("Could not find hobby with id: " + hobbyDTO.getId());
            }
            hobby.setName(hobbyDTO.getName());
            hobby.setDescription(hobbyDTO.getDescription());
            em.getTransaction().begin();
            em.merge(hobby);
            em.getTransaction().commit();
            return new HobbyDTO(hobby);
        } finally {
            em.close();
        }
    }

}
