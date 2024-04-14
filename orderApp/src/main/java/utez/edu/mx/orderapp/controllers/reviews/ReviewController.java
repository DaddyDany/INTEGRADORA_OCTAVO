package utez.edu.mx.orderapp.controllers.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderapp.controllers.categories.dtos.CategoryDto;
import utez.edu.mx.orderapp.controllers.reviews.dtos.ReviewDto;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.services.reviews.ReviewService;
import utez.edu.mx.orderapp.utils.Response;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewController  {
    private final ReviewService reviewService;
    private final CommonUserRepository commonUserRepository;
    @Autowired
    public ReviewController(ReviewService reviewService, CommonUserRepository commonUserRepository){
        this.reviewService = reviewService;
        this.commonUserRepository = commonUserRepository;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAll() {
        List<ReviewDto> reviews = reviewService.getAll();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<Response<String>> createReview(@RequestBody String encryptedData, Authentication authentication) throws Exception {
        String username = authentication.getName();
        CommonUser user = commonUserRepository.findByUserEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Long userId = user.getCommonUserId();
        Response<String> response = reviewService.saveReview(encryptedData, userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
