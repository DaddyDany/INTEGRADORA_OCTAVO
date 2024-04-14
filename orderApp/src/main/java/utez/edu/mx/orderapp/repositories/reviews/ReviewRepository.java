package utez.edu.mx.orderapp.repositories.reviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderapp.models.reviews.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByOrderOrderId(Long orderId);

    List<Review> findByUserId(Long userId);

}
