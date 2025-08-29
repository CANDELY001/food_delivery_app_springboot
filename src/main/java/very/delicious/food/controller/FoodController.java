package very.delicious.food.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import very.delicious.food.io.FoodRequest;
import very.delicious.food.io.FoodResponse;
import very.delicious.food.service.FoodService;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {
    private final FoodService foodService;
    private final ObjectMapper objectMapper;

    public FoodController(FoodService foodService, ObjectMapper objectMapper) {
        this.foodService = foodService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<FoodResponse> addFood(
            @RequestPart("food") String foodString,
            @RequestPart("file") MultipartFile file,
            UriComponentsBuilder uriBuilder
    ) {

        try {
            FoodRequest request = objectMapper.readValue(foodString, FoodRequest.class);
            // Call service to save food
            FoodResponse response = foodService.addFood(request, file);
            var uri = uriBuilder
                    .path("/api/foods/{id}")
                    .buildAndExpand(response.getId())
                    .toUri();

            // Return 201 Created with Location header + response body
            return ResponseEntity.created(uri).body(response);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format", e);
        }
    }
    @GetMapping
    public List<FoodResponse> readFoods() {
        return foodService.readFoods();
    }
    @GetMapping("/{id}")
    public FoodResponse readFood(@PathVariable("id") String id) {
    return  foodService.readFood(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable String id) {

        foodService.deleteFood(id);
    }

}
