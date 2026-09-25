package controller;

import mapper.BookingMapper;
import model.Booking;
import util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingController {

    // GET /api/bookings - Recupera tutte le prenotazioni
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

    // GET /api/bookings/{id} - Recupera una prenotazione per ID
    @GET
    @Path("/{id}")
    public Response getBookingById(@PathParam("id") Long id) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            BookingMapper mapper = session.getMapper(BookingMapper.class);
            Booking booking = mapper.getById(id);
            if (booking == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Prenotazione non trovata con id: " + id).build();
            }
            return Response.ok(booking).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore nel server: " + e.getMessage()).build();
        }
    }

    // POST /api/bookings - Crea una nuova prenotazione
    @POST
    public Response createBooking(Booking newBooking) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            BookingMapper mapper = session.getMapper(BookingMapper.class);
            mapper.insert(newBooking);
            return Response.status(Response.Status.CREATED).entity(newBooking).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore durante l'inserimento: " + e.getMessage()).build();
        }
    }

    // PUT /api/bookings/{id} - Aggiorna una prenotazione esistente
    @PUT
    @Path("/{id}")
    public Response updateBooking(@PathParam("id") Long id, Booking booking) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            BookingMapper mapper = session.getMapper(BookingMapper.class);
            Booking existing = mapper.getById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Prenotazione non trovata con id: " + id).build();
            }
            booking.setId(id);
            mapper.update(booking);
            return Response.ok(booking).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore durante l'aggiornamento: " + e.getMessage()).build();
        }
    }

    // DELETE /api/bookings/{id} - Cancella una prenotazione
    @DELETE
    @Path("/{id}")
    public Response deleteBooking(@PathParam("id") Long id) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            BookingMapper mapper = session.getMapper(BookingMapper.class);
            Booking existing = mapper.getById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Prenotazione non trovata con id: " + id).build();
            }
            mapper.delete(id);
            return Response.ok().entity("Prenotazione eliminata con id: " + id).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore durante l'eliminazione: " + e.getMessage()).build();
        }
    }
}
