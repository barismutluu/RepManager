package com.example.repmanager.core.config;

import com.example.repmanager.storage.FileSystemStorageService;
import com.example.repmanager.storage.ObjectStorageService;
import com.example.repmanager.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bu sınıf, uygulama çalışırken hangi storage stratejisinin (file-system veya object-storage) kullanılacağını belirler.
 * StorageService tipinde bir bean üretir ve Spring konteyner'ına ekler.
 */
@Configuration // Bu anotasyon, Spring'e bu sınıfın bir konfigürasyon sınıfı olduğunu söyler.
@RequiredArgsConstructor // Lombok anotasyonu: final değişkenler için constructor'ı otomatik oluşturur.
public class StorageConfig {

    // application.properties dosyasından storage.strategy değerini okur.
    // Eğer değer belirtilmezse default olarak "file-system" kullanılır.
    @Value("${storage.strategy:file-system}")
    private String strategy;

    private final FileSystemStorageService fileSystemStorageService; // Dosya sistemine yazan servis.
    private final ObjectStorageService objectStorageService;         // Nesne tabanlı storage (örneğin MinIO) servisi.

    /**
     * Kullanılacak storage stratejisini belirler ve uygun StorageService implementasyonunu döner.
     *
     * @return StorageService implementasyonu (FileSystem veya ObjectStorage)
     */
    @Bean
    public StorageService storageService() {
        // Eğer strateji değeri "object-storage" ise ObjectStorageService kullanılır.
        if (strategy.equalsIgnoreCase("object-storage")) {
            return objectStorageService;
        }
        // Aksi takdirde (veya yanlış bir değer gelirse) varsayılan olarak FileSystemStorageService kullanılır.
        else {
            return fileSystemStorageService;
        }
    }
}
