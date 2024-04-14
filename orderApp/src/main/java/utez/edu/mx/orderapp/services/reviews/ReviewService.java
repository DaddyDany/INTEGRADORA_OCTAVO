package utez.edu.mx.orderapp.services.reviews;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderapp.controllers.categories.dtos.CategoryDto;
import utez.edu.mx.orderapp.controllers.orders.dtos.OrderDto;
import utez.edu.mx.orderapp.controllers.reviews.dtos.ReviewDto;
import utez.edu.mx.orderapp.models.orders.Order;
import utez.edu.mx.orderapp.models.reviews.Review;
import utez.edu.mx.orderapp.repositories.orders.OrderRepository;
import utez.edu.mx.orderapp.repositories.reviews.ReviewRepository;
import utez.edu.mx.orderapp.utils.EncryptionService;
import utez.edu.mx.orderapp.utils.Response;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReviewService {
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;
    @Autowired
    public ReviewService(OrderRepository orderRepository, ReviewRepository reviewRepository, EncryptionService encryptionService, ObjectMapper objectMapper){
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.encryptionService = encryptionService;
        this.objectMapper = objectMapper;
    }

    public Response<String> saveReview(String encryptedData, Long userId) throws Exception{
        String correctedData = encryptedData.replace("\"", "");
        String decryptedDataJson = encryptionService.decrypt(correctedData);
        ReviewDto reviewDto = objectMapper.readValue(decryptedDataJson, ReviewDto.class);
        Long orderId = Long.parseLong(reviewDto.getOrderId());
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        if (!"Orden completada :)".equals(order.getOrderState())) {
            return new Response<>(null, true, 400, "La orden no ha sido completada");
        }

        Optional<Review> existingReview = reviewRepository.findByOrderOrderId(orderId);
        if (existingReview.isPresent()) {
            return new Response<>(null, true, 400, "Ya existe una reseña para esta orden, no puedes dejar otra.");
        }

        Review review = new Review();
        review.setOrder(order);
        review.setReviewDescription(reviewDto.getReviewDescription());
        review.setPackCombName(reviewDto.getPackCombName());
        review.setScore(Integer.parseInt(reviewDto.getScore()));
        review.setUserId(userId);
        review = reviewRepository.save(review);
        reviewDto.setOrderId(String.valueOf(review.getOrder().getOrderId()));
        return new Response<>("Creada", false, 200, "Review creada con éxito");
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getAll() {
        return reviewRepository.findAll().stream()
                .map(category -> {
                    try{
                        return convertToReviewDto(category);
                    }catch (Exception e){
                        e.printStackTrace();
                        return null;
                    }
                }).filter(Objects::nonNull)
                .toList();
    }

    private ReviewDto convertToReviewDto(Review review) throws Exception {
        ReviewDto dto = new ReviewDto();
        dto.setReviewDescription(encryptionService.encrypt(review.getReviewDescription()));
        dto.setScore(encryptionService.encrypt(String.valueOf(review.getScore())));
        dto.setPackCombName(encryptionService.encrypt(review.getPackCombName()));
        return dto;
    }
}
