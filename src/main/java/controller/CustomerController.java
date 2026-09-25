package controller;

import mapper.CustomerMapper;
import model.Customer;
import util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    // GET /api/customers - Recupera tutti i clienti
    @GET
    public Response getAllCustomers() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            List<Customer> customers = mapper.getAll();
            return Response.ok(customers).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore nel server: " + e.getMessage()).build();
        }
    }

    // GET /api/customers/{id} - Recupera un cliente per ID
    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") Long id) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            Customer customer = mapper.getById(id);
            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Cliente non trovato con id: " + id).build();
            }
            return Response.ok(customer).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore nel server: " + e.getMessage()).build();
        }
    }

    // POST /api/customers - Crea un nuovo cliente
    @POST
    public Response createCustomer(Customer newCustomer) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            mapper.insert(newCustomer);
            return Response.status(Response.Status.CREATED).entity(newCustomer).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore durante l'inserimento: " + e.getMessage()).build();
        }
    }

    // PUT /api/customers/{id} - Aggiorna un cliente esistente
    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Long id, Customer customer) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            // Verifica che il cliente esista
            Customer existing = mapper.getById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Cliente non trovato con id: " + id).build();
            }
            // Imposta l'ID dal path
            customer.setId(id);
            mapper.update(customer);
            return Response.ok(customer).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore durante l'aggiornamento: " + e.getMessage()).build();
        }
    }

    // DELETE /api/customers/{id} - Cancella un cliente
    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            // Verifica che il cliente esista
            Customer existing = mapper.getById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Cliente non trovato con id: " + id).build();
            }
            mapper.delete(id);
            return Response.ok().entity("Cliente eliminato con id: " + id).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore durante l'eliminazione: " + e.getMessage()).build();
        }
    }
}