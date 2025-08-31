package very.delicious.food.repository;

import lombok.Data;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import very.delicious.food.entities.CartEntity;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<CartEntity, String> {
    Optional<CartEntity> findByUserId(String userId);

    void deleteByUserId(String userId);
}
