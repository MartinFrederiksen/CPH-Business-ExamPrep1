package rest;

import entities.dto.AddressDTO;
import facades.AddressFacade;
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
@Path("address")
public class AddressResource {
private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/Examprep1",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
    private static final AddressFacade facade =  AddressFacade.getFacade(EMF);

    public AddressResource() {
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AddressDTO> getAll() {
        return facade.getAll();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AddressDTO getById(@PathParam("id") long id) throws Exception{
        return facade.getById(id);
    }
    
    @POST
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public AddressDTO add(AddressDTO address) throws Exception {
        return facade.add(address);
    }
    
    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public AddressDTO delete(@PathParam("id") long id) throws Exception {
        return facade.delete(id);
    }
    
    @POST
    @Path("edit")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public AddressDTO edit(AddressDTO address) throws Exception {
        return facade.edit(address);
    }
}
