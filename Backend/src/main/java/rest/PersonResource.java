package rest;

import entities.dto.PersonDTO;
import facades.PersonFacade;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

/**
 * REST Web Service
 *
 * @author Martin Frederiksen
 */
@Path("person")
public class PersonResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/Examprep1",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
    private static final PersonFacade facade =  PersonFacade.getFacade(EMF);

    public PersonResource() {
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PersonDTO> getAll() {
        return facade.getAll();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PersonDTO getById(@PathParam("id") long id) throws Exception{
        return facade.getById(id);
    }
    
    @GET
    @Path("email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public PersonDTO getByEmail(@PathParam("email") String email) throws Exception{
        return facade.getByEmail(email);
    }
    
    @GET
    @Path("phone/{phone}")
    @Produces(MediaType.APPLICATION_JSON)
    public PersonDTO getByPhone(@PathParam("phone") String phone) throws Exception{
        return facade.getByPhone(phone);
    }
    
    @POST
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public PersonDTO add(PersonDTO person) throws Exception {
        return facade.add(person);
    }
    
    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public PersonDTO delete(@PathParam("id") long id) throws Exception {
        return facade.delete(id);
    }
    
    @POST
    @Path("edit")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public PersonDTO edit(PersonDTO person) throws Exception {
        return facade.edit(person);
    }
}
