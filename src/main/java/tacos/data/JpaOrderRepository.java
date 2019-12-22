package tacos.data;

import org.springframework.data.repository.CrudRepository;
import tacos.domain.Order;

public interface JpaOrderRepository extends CrudRepository<Order,Long> {
}
