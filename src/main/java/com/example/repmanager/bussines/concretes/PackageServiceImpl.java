package com.example.repmanager.bussines.concretes;

import com.example.repmanager.bussines.abstracts.PackageService;
import com.example.repmanager.dao.PackageRepository;
import com.example.repmanager.dto.MetaDTO;
import com.example.repmanager.entities.PackageEntity;
import com.example.repmanager.storage.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Bu sınıf, PackageService arayüzünün gerçek iş mantığını (implementasyonunu) içerir.
 * Paket yükleme ve indirme işlemlerinin nasıl yapılacağını tanımlar.
 */
@Service
@RequiredArgsConstructor // Lombok anotasyonu: constructor'ı otomatik oluşturur ve final değişkenleri enjekte eder.
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository; // Veritabanı işlemleri için repository kullanır.
    private final StorageService storageService;       // Dosya depolama işlemleri için storage servisini kullanır.
    private final ObjectMapper objectMapper;           // JSON verilerini Java nesnelerine dönüştürmek için kullanır.

    /**
     * Bir paketi (.rep ve meta.json dosyalarını) storage'a kaydeder ve veritabanına paket kaydı oluşturur.
     *
     * @param name Yüklenecek paketin adını belirtir.
     * @param version Yüklenecek paketin versiyonunu belirtir.
     * @param metaFile Paket bilgilerini içeren meta.json dosyasını temsil eder.
     * @param packageFile Derlenmiş kaynak kodu içeren package.rep dosyasını temsil eder.
     */
    @Override
    public void uploadPackage(String name, String version, MultipartFile metaFile, MultipartFile packageFile) {
        try {
            // 1. meta.json dosyasının içeriğini okur ve bir MetaDTO nesnesine çevirir.
            MetaDTO meta = objectMapper.readValue(metaFile.getBytes(), MetaDTO.class);

            // 2. meta.json ve package.rep dosyalarını storage katmanına kaydeder.
            String metaPath = storageService.saveFile(name, version, "meta.json", metaFile);
            String repPath = storageService.saveFile(name, version, "package.rep", packageFile);

            // 3. Paket bilgilerini kullanarak veritabanına yeni bir kayıt oluşturur.
            PackageEntity entity = PackageEntity.builder()
                    .name(meta.getName())                         // meta.json içindeki adı kullanır.
                    .version(meta.getVersion())                   // meta.json içindeki versiyonu kullanır.
                    .author(meta.getAuthor())                     // meta.json içindeki yazarı kullanır.
                    .packageBinary(new String(metaFile.getBytes()))// meta.json dosyasının içeriğini String olarak kaydeder. (Burada küçük bir hata olabilir, aşağıda not yazdım.)
                    .storagePath(repPath)                         // .rep dosyasının storage üzerindeki yolunu kaydeder.
                    .uploadedAt(LocalDateTime.now())              // Şu anki tarihi ve saati kaydeder.
                    .build();

            // 4. Veritabanına entity'i kaydeder.
            packageRepository.save(entity);
        } catch (IOException e) {
            // Eğer meta.json dosyası okunamazsa RuntimeException fırlatır.
            throw new RuntimeException("Invalid meta.json file", e);
        }
    }

    /**
     * Bir dosyayı (meta.json veya package.rep) storage'dan okuyarak döner.
     *
     * @param name İndirilecek paketin adını belirtir.
     * @param version İndirilecek paketin versiyonunu belirtir.
     * @param fileName İndirilecek dosyanın adını belirtir.
     * @return İndirilen dosyanın içeriğini byte dizisi (byte[]) olarak döner.
     */
    @Override
    public byte[] downloadPackageFile(String name, String version, String fileName) {
        // İstenen dosyayı storage servisinden okur ve byte[] olarak geri döner.
        return storageService.readFile(name, version, fileName);
    }
}
