package utez.edu.mx.orderapp.services.externals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utez.edu.mx.orderapp.controllers.orders.dtos.ProductDetailsDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class StripeService {
    @Value("${stripe.keys.secret}")
    private String stripeSecretKey;
    private final ObjectMapper objectMapper;


    @Autowired
    public StripeService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public String createCheckoutSessionFromEncodedData(String encData) throws Exception {
        Stripe.apiKey = stripeSecretKey;

        ObjectMapper objectMapper = new ObjectMapper();
        ProductDetailsDto productDetails = objectMapper.readValue(encData, ProductDetailsDto.class);

        Long priceInCents = productDetails.getTotalPrice() * 100;

        List<SessionCreateParams.LineItem> items = new ArrayList<>();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("mxn")
                        .setUnitAmount(priceInCents)
                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(productDetails.getOrderName())
                                .setDescription("DETALLES DEL PRODUCTO: \n" +
                                        productDetails.getOrderDescription() +
                                        "\nNúmero de trabajadores: " + productDetails.getWorkersNumber()
                                        + "\nDuración del paquete: " + productDetails.getDesignatedHours())
                                .build())
                        .build())
                .build();

        items.add(lineItem);

        SessionCreateParams params = SessionCreateParams.builder()
                .setSuccessUrl("http://localhost:5173/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:5173/cancel")
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .putExtraParam("payment_intent_data[capture_method]", "manual")
                .addAllLineItem(items)
                .build();

        Session session = Session.create(params);

        return session.getUrl();
    }

}
