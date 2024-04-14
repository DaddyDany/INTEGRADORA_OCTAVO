package utez.edu.mx.orderapp.controllers.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderapp.controllers.categories.dtos.CategoryDto;
import utez.edu.mx.orderapp.controllers.orders.dtos.OrderInfoDto;
import utez.edu.mx.orderapp.controllers.reviews.dtos.ReviewDto;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.services.reviews.ReviewService;
import utez.edu.mx.orderapp.utils.Response;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/my-reviews")
    public ResponseEntity<List<ReviewDto>> getMyReviews(Authentication authentication){
        String username = authentication.getName();
        Optional<CommonUser> user = commonUserRepository.findByUserEmail(username);
        if (user.isPresent()) {
            List<ReviewDto> reviews = reviewService.findReviewsByUserId(user.get().getCommonUserId());
            return ResponseEntity.ok(reviews);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response<String>> deleteReview(@RequestBody String encryptedData, Authentication authentication) throws Exception {
        String username = authentication.getName();
        Optional<CommonUser> user = commonUserRepository.findByUserEmail(username);
        if (user.isPresent()) {
            Response<String> response = reviewService.deleteReview(encryptedData, user.get().getCommonUserId());
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response<>("No autorizado", true, 403, "Usuario no encontrado o no autorizado"));
        }
    }
}
