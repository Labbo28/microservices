package mapper;

import model.Booking;
import java.util.List;

public interface BookingMapper {

    List<Booking> getAll();

    Booking getById(Long id);

    void insert(Booking booking);

    void update(Booking booking);

    void delete(Long id);
}
