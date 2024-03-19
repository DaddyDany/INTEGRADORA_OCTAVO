package utez.edu.mx.orderApp.FirebaseIntegrations;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        // Nombre del archivo en el storage
        String fileName = UUID.randomUUID().toString().concat(this.getExtension(multipartFile.getOriginalFilename()));

        // Carga las credenciales desde el archivo
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ClassPathResource("orderapp-eaca6-firebase-adminsdk-qtn2q-d2105e1f00.json").getInputStream() // Asegúrate de que este sea el path correcto
        );

        // Configura las opciones de Storage con las credenciales
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        // Crea el ID y la info del blob
        BlobId blobId = BlobId.of("orderapp-eaca6.appspot.com", "uploads/" + fileName); // Asegúrate de reemplazar "your-bucket-name" con el nombre real de tu bucket
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(multipartFile.getContentType()).build();

        // Sube el archivo
        storage.create(blobInfo, multipartFile.getBytes());

        // Genera la URL de descarga
        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/orderapp-eaca6.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode("uploads/" + fileName, java.nio.charset.StandardCharsets.UTF_8));
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
