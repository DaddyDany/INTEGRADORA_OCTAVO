package utez.edu.mx.orderapp.controllers.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderapp.controllers.reviews.dtos.ReviewDto;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.services.reviews.ReviewService;
import utez.edu.mx.orderapp.utils.Response;

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

    @PostMapping
    public ResponseEntity<Response<ReviewDto>> createReview(@RequestBody ReviewDto reviewDto, Authentication authentication) {
        String username = authentication.getName();
        CommonUser user = commonUserRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Response<ReviewDto> response = reviewService.saveReview(reviewDto, user.getCommonUserId());
        if (!response.isError()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }
}
