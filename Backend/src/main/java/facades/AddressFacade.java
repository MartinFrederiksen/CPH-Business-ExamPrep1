package facades;

import entities.Address;
import entities.dto.AddressDTO;
import entities.dto.PersonListDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Martin Frederiksen
 */
public class AddressFacade implements IFacade<AddressDTO> {
    private static EntityManagerFactory emf;
    private static AddressFacade instance;
    
    private AddressFacade(){}
    public static AddressFacade getFacade (EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AddressFacade();
        }
        return instance;
    }
    
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    @Override
    public List<AddressDTO> getAll() {
        return getEntityManager().createQuery("SELECT address FROM Address address").getResultList();
    }

    @Override
    public AddressDTO getById(long id) {
        Address address = getEntityManager().find(Address.class, id);
        if(address == null) {
            throw new WebApplicationException("Could not find Address with id: " + id);
        }
        AddressDTO aDto = new AddressDTO(address);
        aDto.setPersons(new PersonListDTO(address.getPersons()).getPersonsDTO());
        return aDto;
    }

    @Override
    public AddressDTO add(AddressDTO addressDTO) {
        EntityManager em = getEntityManager();
        try {
            List<Address> lookupAddress = em.createQuery("SELECT address FROM Address address WHERE address.street = :street AND address.city = :city AND address.zip = :zip").setParameter("street", addressDTO.getStreet()).setParameter("city", addressDTO.getCity()).setParameter("zip", addressDTO.getZip()).getResultList();
            if(lookupAddress.size() != 0) {
                return new AddressDTO(lookupAddress.get(0));
            }
            Address addAddress = new Address(addressDTO.getStreet(), addressDTO.getCity(), addressDTO.getZip(), null);
            em.getTransaction().begin();
            em.persist(addAddress);
            em.getTransaction().commit();
            return new AddressDTO(addAddress);
        } finally {
            em.close();
        }
    }

    @Override
    public AddressDTO delete(long id) {
        EntityManager em = getEntityManager();
        try {
            Address address = em.find(Address.class, id);
            if(address == null) {
                throw new WebApplicationException("Could not find Address with id: " + id);
            }
            em.getTransaction().begin();
            em.remove(address);
            em.getTransaction().commit();
            return new AddressDTO(address);
        } finally {
            em.close();
        }
    }

    @Override
    public AddressDTO edit(AddressDTO addressDTO) {
        EntityManager em = getEntityManager();
        try {
            Address address = em.find(Address.class, addressDTO.getId());
            if(address == null) {
                throw new WebApplicationException("Could not find Address with id: " + addressDTO.getId());
            }
            address.setStreet(addressDTO.getStreet());
            address.setCity(addressDTO.getCity());
            address.setZip(addressDTO.getZip());
            em.getTransaction().begin();
            em.merge(address);
            em.getTransaction().commit();
            return new AddressDTO(address);
        } finally {
            em.close();
        }
    }

}
