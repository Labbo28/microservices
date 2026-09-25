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

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") Long id) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            Customer customer = mapper.getById(id);
            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(customer).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore nel server: " + e.getMessage()).build();
        }
    }

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

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Long id, Customer customer) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            Customer existing = mapper.getById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            customer.setId(id);
            mapper.update(customer);
            return Response.ok(customer).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore durante l'aggiornamento: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            Customer existing = mapper.getById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            mapper.delete(id);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore durante l'eliminazione: " + e.getMessage()).build();
        }
    }
}
