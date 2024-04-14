package utez.edu.mx.orderapp.models.reviews;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderapp.models.orders.Order;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;
    @OneToOne
    @JoinColumn(name = "orderId")
    private Order order;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @NotNull(message = "El nombre del paquete o combo de la reseña no debe ser nulo")
    @NotBlank(message = "El nombre del paquete o combo de la reseña no debe ir vacío")
    @Column(name = "package_or_combo_name",nullable = false)
    private String packCombName;

    @NotNull(message = "El comentario de la reseña no debe ser nulo")
    @NotBlank(message = "El comentario de la reseña no debe ir vacío")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ0-9.,;¿?¡!\\s()]*$", message = "El comentario solo puede contener letras, puntos, comas, caracteres acentuados, números, signos de interrogación, admiración, paréntesis y punto y coma.")
    @Size(min = 30, max = 500, message = "El comentario debe tener como mínimo 30 y como máximo 500 caracteres")
    @Column(name = "review_description")
    private String reviewDescription;

    @NotNull(message = "El puntaje no puede ser nulo")
    @Min(value = 0, message = "El puntaje minimo es 0")
    @Max(value = 5, message = "El puntaje no puede ser mayor que 5")
    @Column(nullable = false)
    private Integer score;
}
