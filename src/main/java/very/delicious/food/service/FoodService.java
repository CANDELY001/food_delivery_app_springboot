package very.delicious.food.service;

import org.springframework.web.multipart.MultipartFile;
import very.delicious.food.io.FoodRequest;
import very.delicious.food.io.FoodResponse;

import java.util.List;

public interface FoodService {
    String uploadFile(MultipartFile file);

    //Create Food
   FoodResponse addFood(FoodRequest request, MultipartFile file);

   //Read all food
   List<FoodResponse> readFoods();

   //Read food by ID
    FoodResponse readFood(String id);

    //Delete Food image from s3 bucket
    boolean deleteFile(String filename);

    //delete food by id
    void deleteFood(String id);
}
