package very.delicious.food.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import very.delicious.food.entities.FoodEntity;

@Repository
public interface FoodRepository extends MongoRepository<FoodEntity, String> {
}
