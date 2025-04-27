package com.example.repmanager.bussines.abstracts;

import com.example.repmanager.dto.MetaDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Bu arayüz (interface), paket yükleme ve indirme işlemleri için gerekli olan servis metodlarını tanımlar.
 * Uygulamada gerçek iş mantığı bu interface'i implemente eden sınıfta (örneğin PackageServiceImpl) bulunur.
 */
public interface PackageService {

    /**
     * Bir paketi (.rep dosyası) ve ilgili meta.json dosyasını sunucuya yükler.
     *
     * @param name Yüklenecek paketin adını belirtir.
     * @param version Yüklenecek paketin versiyonunu belirtir.
     * @param metaFile Paketle ilgili bilgileri içeren meta.json dosyasını temsil eder.
     * @param packageFile Derlenmiş kaynak kodları içeren .rep uzantılı dosyayı temsil eder.
     */
    void uploadPackage(String name, String version, MultipartFile metaFile, MultipartFile packageFile);

    /**
     * İstenilen bir paket dosyasını (.rep veya meta.json) sunucudan indirir.
     *
     * @param name İndirilecek paketin adını belirtir.
     * @param version İndirilecek paketin versiyonunu belirtir.
     * @param fileName İndirilecek dosyanın adını belirtir (örneğin: package.rep veya meta.json).
     * @return İndirilen dosyanın byte dizisi (byte array) olarak içeriğini döner.
     */
    byte[] downloadPackageFile(String name, String version, String fileName);
}
