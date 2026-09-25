package controller;

import mapper.BookingMapper;
import model.Booking;
import util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingController {

    private static final String CUSTOMERS_SERVICE_URL = "http://localhost:8080/customers-service/api/customers";

    private boolean customerExists(Long customerId) {
        try {
            URL url = new URL(CUSTOMERS_SERVICE_URL + "/" + customerId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            connection.disconnect();

            return responseCode == 200;
        } catch (Exception e) {
            return false;
        }
    }

    @GET
    public Response getAllBookings() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            BookingMapper mapper = session.getMapper(BookingMapper.class);
            List<Booking> bookings = mapper.getAll();
            return Response.ok(bookings).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore nel server: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getBookingById(@PathParam("id") Long id) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            BookingMapper mapper = session.getMapper(BookingMapper.class);
            Booking booking = mapper.getById(id);
            if (booking == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(booking).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore nel server: " + e.getMessage()).build();
        }
    }

    @POST
    public Response createBooking(Booking newBooking) {
        if (!customerExists(newBooking.getCustomerId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Cliente non valido o inesistente.").build();
        }

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            BookingMapper mapper = session.getMapper(BookingMapper.class);
            mapper.insert(newBooking);
            return Response.status(Response.Status.CREATED).entity(newBooking).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore durante l'inserimento: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateBooking(@PathParam("id") Long id, Booking booking) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            BookingMapper mapper = session.getMapper(BookingMapper.class);
            Booking existing = mapper.getById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            booking.setId(id);
            mapper.update(booking);
            return Response.ok(booking).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore durante l'aggiornamento: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBooking(@PathParam("id") Long id) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            BookingMapper mapper = session.getMapper(BookingMapper.class);
            Booking existing = mapper.getById(id);
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
