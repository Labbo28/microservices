package mapper;

import model.Customer;
import java.util.List;

public interface CustomerMapper {
    
    List<Customer> getAll();
    
    Customer getById(Long id);

    void insert(Customer customer);

    void update(Customer customer);

    void delete(Long id);
}