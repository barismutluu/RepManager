package com.example.repmanager.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * Bu arayüz, farklı depolama (storage) stratejileri için temel operasyonları tanımlar.
 * Dosya kaydetme ve dosya okuma işlemleri için standart bir yapı sunar.
 */
public interface StorageService {

    /**
     * Bir dosyayı verilen paket adı, versiyon ve dosya adı bilgilerine göre kaydeder.
     *
     * @param packageName Paketin adını belirtir.
     * @param version Paketin versiyonunu belirtir.
     * @param fileName Kaydedilecek dosyanın adını belirtir.
     * @param file Kaydedilecek MultipartFile nesnesidir.
     * @return Kaydedilen dosyanın yolu veya nesne adı (storage stratejisine göre değişir).
     */
    String saveFile(String packageName, String version, String fileName, MultipartFile file);

    /**
     * Verilen paket adı, versiyon ve dosya adına göre bir dosyayı okur ve içeriğini döner.
     *
     * @param packageName Paketin adını belirtir.
     * @param version Paketin versiyonunu belirtir.
     * @param fileName Okunacak dosyanın adını belirtir.
     * @return Dosya içeriğini byte dizisi (byte[]) olarak döner.
     */
    byte[] readFile(String packageName, String version, String fileName);
}
