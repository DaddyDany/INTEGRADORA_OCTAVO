package utez.edu.mx.orderapp.services.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.orderapp.controllers.reviews.dtos.ReviewDto;
import utez.edu.mx.orderapp.models.orders.Order;
import utez.edu.mx.orderapp.models.reviews.Review;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.repositories.orders.OrderRepository;
import utez.edu.mx.orderapp.repositories.reviews.ReviewRepository;
import utez.edu.mx.orderapp.utils.Response;

@Service
public class ReviewService {
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final CommonUserRepository commonUserRepository;
    @Autowired
    public ReviewService(OrderRepository orderRepository, ReviewRepository reviewRepository, CommonUserRepository commonUserRepository){
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.commonUserRepository = commonUserRepository;
    }

    public Response<ReviewDto> saveReview(ReviewDto reviewDto, Long userId) {
        Order order = orderRepository.findById(reviewDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        if (!"Servida".equals(order.getOrderState())) {
            return new Response<>(null, true, 400, "La orden no está en estado 'Servida'");
        }
        Review review = new Review();
        review.setOrder(order);
        review.setReview(reviewDto.getReview());
        review.setScore(reviewDto.getScore());
        review.setUserId(userId);
        review = reviewRepository.save(review);
        reviewDto.setOrderId(review.getOrder().getOrderId());

        return new Response<>(reviewDto, false, 200, "Review creada con éxito");
    }
}
