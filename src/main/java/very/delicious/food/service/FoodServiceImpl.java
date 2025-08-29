package very.delicious.food.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import very.delicious.food.entities.FoodEntity;
import very.delicious.food.io.FoodRequest;
import very.delicious.food.io.FoodResponse;
import very.delicious.food.repository.FoodRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private  S3Client s3Client;
    @Autowired
    private  FoodRepository foodRepository;

    @Value("${aws.s3.bucketname}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String key = UUID.randomUUID().toString()+"."+filenameExtension;
        try{
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName).key(key).acl("public-read")
                    .contentType(file.getContentType()).build();
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
       if(response.sdkHttpResponse().isSuccessful()){
           return "https://"+bucketName+".s3.amazonaws.com/"+key;
       } else{
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file");
       }
        }catch(IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while uploading the file");
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
    FoodEntity newFoodEntity = convertToEntity(request);
     String imageUrl = uploadFile(file);
     newFoodEntity.setImageUrl(imageUrl);
     newFoodEntity = foodRepository.save(newFoodEntity);
     return convertToResponse(newFoodEntity);
    }

    @Override
    public List<FoodResponse> readFoods() {
        List<FoodEntity>  dataEntity= foodRepository.findAll();
        return dataEntity.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());
    }

    @Override
    public FoodResponse readFood(String id) {
        FoodEntity foodEnt = foodRepository.findById(id).orElseThrow(() -> new RuntimeException("No Food Found for this ID:"+ id));
        return convertToResponse(foodEnt);
    }

    @Override
    public boolean deleteFile(String filename) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName).key(filename).build();
        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }

    @Override
    public void deleteFood(String id) {
        FoodResponse foodResponse = readFood(id);
        String imageUrl = foodResponse.getImageUrl();
        String filename = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        boolean isFileDeleted = deleteFile(filename);
        if(isFileDeleted){
            foodRepository.deleteById(foodResponse.getId());
        }

    }

    private FoodEntity convertToEntity(FoodRequest foodRequest){
  return  FoodEntity.builder()
            .name(foodRequest.getName())
            .description(foodRequest.getDescription())
            .price(foodRequest.getPrice())
          .category(foodRequest.getCategory())
            .build();
}
private FoodResponse convertToResponse(FoodEntity entity){
       return FoodResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .category(entity.getCategory())
                .imageUrl(entity.getImageUrl())
                .build();

}

}
