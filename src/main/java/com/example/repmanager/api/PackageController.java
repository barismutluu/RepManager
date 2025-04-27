package com.example.repmanager.api;

import com.example.repmanager.bussines.abstracts.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Bu sınıf, paket yükleme ve indirme işlemleri için REST API uç noktalarını yönetir.
 */
@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor // Lombok anotasyonu: final değişkenler için constructor'ı otomatik olarak oluşturur.
public class PackageController {

    // Service katmanı ile iletişimi sağlar. İş mantığı burada değil, service içerisinde bulunur.
    private final PackageService packageService;

    /**
     * Bir paketi (.rep dosyası) ve buna ait meta.json dosyasını sunucuya yükler.
     *
     * @param packageName Yüklenecek paketin adını belirtir.
     * @param version Yüklenecek paketin versiyonunu belirtir.
     * @param metaFile Paketle ilgili bilgileri içeren meta.json dosyasını temsil eder.
     * @param packageFile Derlenmiş kodları içeren .rep (zip formatlı) dosyasını temsil eder.
     * @return Yükleme başarılıysa HTTP 200 OK cevabı döner.
     */
    @Operation(summary = "Bir paket ve meta.json dosyasını yükler") // Swagger dökümantasyonunda görünmesi için açıklama ekler.
    @PostMapping(
            value = "/{packageName}/{version}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE // Bu endpoint multipart/form-data formatında dosya alır.
    )
    public ResponseEntity<Void> uploadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestParam("metaFile") MultipartFile metaFile,
            @RequestParam("packageFile") MultipartFile packageFile
    ) {
        // Yükleme işlemini iş katmanına (service) yönlendirir.
        packageService.uploadPackage(packageName, version, metaFile, packageFile);
        return ResponseEntity.ok().build(); // Başarılı olduğunda boş bir 200 OK cevabı döner.
    }

    /**
     * Bir paket dosyasını (.rep) veya ona ait meta.json dosyasını sunucudan indirir.
     *
     * @param packageName İndirilecek paketin adını belirtir.
     * @param version İndirilecek paketin versiyonunu belirtir.
     * @param fileName İndirilecek dosyanın adını belirtir (örneğin: meta.json veya package.rep).
     * @return İstenen dosyayı byte[] olarak döner ve HTTP cevabı içerisine yerleştirir.
     */
    @Operation(summary = "Bir paket dosyasını veya meta.json dosyasını indirir") // Swagger dökümantasyonu için açıklama sağlar.
    @GetMapping("/{packageName}/{version}/{fileName}")
    public ResponseEntity<byte[]> downloadPackageFile(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName
    ) {
        // İstenen dosyayı iş katmanından (service) alır.
        byte[] data = packageService.downloadPackageFile(packageName, version, fileName);

        // Dosyayı binary formatta geri döner ve Content-Type olarak application/octet-stream ayarlar.
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }
}
