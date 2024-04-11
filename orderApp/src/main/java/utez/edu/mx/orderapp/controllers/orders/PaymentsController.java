package utez.edu.mx.orderapp.controllers.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderapp.services.externals.StripeService;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentsController {
    private final StripeService stripeService;
    @Autowired
    public PaymentsController(StripeService stripeService){
        this.stripeService = stripeService;
    }
    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestPart("data") String encData) {
        try {
            String url = stripeService.createCheckoutSessionFromEncodedData(encData);
            return ResponseEntity.ok().body(Map.of("url", url));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la sesión de checkout: " + e.getMessage());
        }
    }
}
