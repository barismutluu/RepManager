package com.example.repmanager.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Bu sınıf, dosyaları yerel dosya sistemine kaydeder ve oradan okur.
 * StorageService arayüzünü implemente eder.
 */
@Service // Spring tarafından otomatik olarak Bean olarak tanımlanır.
@Slf4j   // Lombok anotasyonu: Loglama (log.info, log.error) işlemleri için logger oluşturur.
public class FileSystemStorageService implements StorageService {

    private final String rootDir = "storage/packages"; // Tüm dosyaların saklanacağı ana klasör yoludur.

    /**
     * Bir dosyayı belirtilen paketin ve versiyonun altında dosya sistemine kaydeder.
     *
     * @param packageName Paketin adını belirtir.
     * @param version Paketin versiyonunu belirtir.
     * @param fileName Kaydedilecek dosyanın adını belirtir.
     * @param file Kaydedilecek MultipartFile nesnesidir (gelen dosya).
     * @return Kaydedilen dosyanın dosya sistemi üzerindeki tam yolu.
     */
    @Override
    public String saveFile(String packageName, String version, String fileName, MultipartFile file) {
        try {
            // Kayıt edilecek klasör yolunu oluşturur (örnek: storage/packages/mypackage/1.0.0).
            Path dirPath = Paths.get(rootDir, packageName, version);
            Files.createDirectories(dirPath); // Eğer klasör yoksa oluşturur.

            // Dosyanın tam yolunu oluşturur.
            Path filePath = dirPath.resolve(fileName);

            // Gelen dosyayı belirlenen konuma yazar (kaydeder).
            file.transferTo(filePath);

            // Dosya yolu String olarak geri döner.
            return filePath.toString();
        } catch (IOException e) {
            // Hata oluşursa RuntimeException fırlatır.
            throw new RuntimeException("Could not save file: " + fileName, e);
        }
    }

    /**
     * Bir dosyayı belirtilen paketin ve versiyonun altından okur ve byte dizisi olarak döner.
     *
     * @param packageName Paketin adını belirtir.
     * @param version Paketin versiyonunu belirtir.
     * @param fileName Okunacak dosyanın adını belirtir.
     * @return Dosya içeriğini byte[] (bayt dizisi) olarak döner.
     */
    @Override
    public byte[] readFile(String packageName, String version, String fileName) {
        try {
            // Okunacak dosyanın tam yolunu oluşturur.
            Path filePath = Paths.get(rootDir, packageName, version, fileName);

            // Dosyayı byte dizisi (byte[]) olarak okur.
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            // Hata oluşursa RuntimeException fırlatır.
            throw new RuntimeException("Could not read file: " + fileName, e);
        }
    }
}
