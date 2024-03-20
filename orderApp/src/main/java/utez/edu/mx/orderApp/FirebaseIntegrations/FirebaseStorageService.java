package utez.edu.mx.orderApp.FirebaseIntegrations;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID().toString().concat(this.getExtension(multipartFile.getOriginalFilename()));

        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ClassPathResource("orderapp-eaca6-firebase-adminsdk-qtn2q-d2105e1f00.json").getInputStream()
        );
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        BlobId blobId = BlobId.of("orderapp-eaca6.appspot.com", "package-images/" + fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(multipartFile.getContentType()).build();
        storage.create(blobInfo, multipartFile.getBytes());
        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/orderapp-eaca6.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode("package-images/" + fileName, java.nio.charset.StandardCharsets.UTF_8));
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public void deleteFileFromFirebase(String fileUrl) throws IOException {
        try {
            String[] parts = fileUrl.split("/o/");
            String encodedFileName = parts[1].split("\\?")[0];
            String fileName = URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8.name());
            fileName = fileName.replace("package-images%2F", "package-images/");
            System.out.println(fileName);
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new ClassPathResource("orderapp-eaca6-firebase-adminsdk-qtn2q-d2105e1f00.json").getInputStream()
            );
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            BlobId blobId = BlobId.of("orderapp-eaca6.appspot.com", fileName);
            boolean deleted = storage.delete(blobId);
            if (!deleted) {
                throw new IOException("No se pudo eliminar el archivo de Firebase Storage");
            }
        } catch (Exception e) {
            throw new IOException("Error al eliminar el archivo de Firebase Storage", e);
        }
    }
}
