package com.example.repmanager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Bu sınıf, veri tabanındaki "package" kayıtlarını temsil eder.
 * Her bir nesne, bir paketi (name, version, author, vs.) veri tabanında saklar.
 */
@Entity // Bu anotasyon, sınıfı bir JPA entity'si (veri tabanı tablosu) olarak işaretler.
@Data // Lombok anotasyonu: Getter, Setter, toString, equals ve hashCode metodlarını otomatik üretir.
@Builder // Lombok anotasyonu: Builder tasarım desenini kullanarak nesne oluşturmayı sağlar.
@NoArgsConstructor // Parametresiz (boş) constructor üretir.
@AllArgsConstructor // Tüm alanları içeren constructor üretir.
public class PackageEntity {

    @Id // Bu alanın birincil anahtar (primary key) olduğunu belirtir.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID değerinin veri tabanı tarafından otomatik oluşturulmasını sağlar.
    private Long id;

    private String name;    // Paketin adını tutar (örneğin: mypackage).
    private String version; // Paketin versiyonunu tutar (örneğin: 1.0.0).
    private String author;  // Paketin yazarını belirtir.

    @Lob // Büyük veri nesnelerini (Large Object) saklamak için kullanılır.
    @Column(name = "package_binary", columnDefinition = "TEXT") // Veri tabanında bu alan "TEXT" tipi olarak oluşturulur.
    private String packageBinary; // Paketin içerdiği meta.json veya binary veriyi string formatında saklar.

    @Column(name = "storage_path") // Depolanan dosyanın sistemdeki konumunu tutar.
    private String storagePath;

    @Column(name = "uploaded_at") // Paketin sisteme yüklendiği zamanı tutar.
    private LocalDateTime uploadedAt;
}
