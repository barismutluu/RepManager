package com.example.repmanager.storage;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Bu sınıf, MinIO kullanarak nesne tabanlı (object storage) dosya kaydetme ve okuma işlemlerini gerçekleştirir.
 * StorageService arayüzünü implemente eder.
 */
@Service // Spring tarafından bir servis bileşeni (bean) olarak tanımlanır.
public class ObjectStorageService implements StorageService {

    private MinioClient minioClient; // MinIO sunucusuyla iletişim kurmak için kullanılır.

    // MinIO bağlantı bilgileri application.properties dosyasından okunur.
    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * Uygulama başlarken MinIO client'ı oluşturur.
     * @PostConstruct anotasyonu sayesinde Spring, bean oluşturulduğunda bu metodu otomatik çağırır.
     */
    @PostConstruct
    public void init() {
        minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * Gelen dosyayı (package.rep veya meta.json) MinIO object storage'a kaydeder.
     *
     * @param packageName Paketin adını belirtir.
     * @param version Paketin versiyonunu belirtir.
     * @param fileName Kaydedilecek dosyanın adını belirtir.
     * @param file MultipartFile nesnesi (yüklenecek dosya).
     * @return Kaydedilen nesnenin adı (object name).
     */
    @Override
    public String saveFile(String packageName, String version, String fileName, MultipartFile file) {
        try {
            // Dosyanın MinIO içindeki tam yolunu oluşturur (örnek: mypackage/1.0.0/meta.json).
            String objectName = packageName + "/" + version + "/" + fileName;

            // Bucket'ın var olup olmadığını kontrol eder.
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );

            // Eğer bucket yoksa, yeni bir bucket oluşturur.
            if (!bucketExists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
            }

            // Dosyayı belirtilen bucket ve nesne adı ile MinIO'ya yükler.
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1) // Dosyayı input stream olarak gönderir.
                            .contentType(file.getContentType()) // İçerik türünü (MIME type) belirtir.
                            .build()
            );

            return objectName; // Başarılı kayıttan sonra nesnenin yolunu döner.
        } catch (Exception e) {
            throw new RuntimeException("Object storage save failed", e); // Hata olursa özel bir RuntimeException fırlatır.
        }
    }

    /**
     * Belirtilen nesneyi (object) MinIO üzerinden okur ve içeriğini byte[] olarak döner.
     *
     * @param packageName Paketin adını belirtir.
     * @param version Paketin versiyonunu belirtir.
     * @param fileName Okunacak dosyanın adını belirtir.
     * @return Dosya içeriğini byte dizisi (byte[]) olarak döner.
     */
    @Override
    public byte[] readFile(String packageName, String version, String fileName) {
        try {
            // Okunacak nesnenin tam yolunu oluşturur.
            String objectName = packageName + "/" + version + "/" + fileName;

            // MinIO'dan nesneyi okur (InputStream olarak).
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );

            // Okunan veriyi byte[] formatında döner.
            return inputStream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Object storage read failed", e); // Hata durumunda özel bir RuntimeException fırlatır.
        }
    }
}
