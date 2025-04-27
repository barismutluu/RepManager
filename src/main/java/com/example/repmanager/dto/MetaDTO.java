package com.example.repmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

/**
 * Bu sınıf, bir paketin meta verilerini (name, version, author, dependencies) taşımak için kullanılır.
 * Kullanıcıdan alınan meta.json dosyasındaki bilgileri karşılar.
 */
@Data // Lombok anotasyonu: Getter, Setter, toString, equals ve hashCode metodlarını otomatik üretir.
@NoArgsConstructor // Lombok anotasyonu: Parametresiz (boş) bir constructor üretir.
@AllArgsConstructor // Lombok anotasyonu: Tüm alanları içeren bir constructor üretir.
public class MetaDTO {

    @NotBlank // Değer boş veya sadece boşluk olamaz. (Validation kuralı)
    private String name; // Paket adı (örnek: mypackage)

    @NotBlank
    @Pattern(regexp = "\\d+\\.\\d+\\.\\d+", message = "Version format must be like 1.0.0")
    private String version; // Paket versiyonu (örnek: 1.0.0 formatında olmalı)

    @NotBlank
    private String author; // Paketi oluşturan yazarın adı.

    private List<Dependency> dependencies; // Paketin bağımlılıklarını temsil eden liste.

    /**
     * İç sınıf (static nested class) olarak tanımlanır.
     * Her bağımlılığın (dependency) adını ve versiyonunu tutar.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dependency {

        @NotBlank
        private String packageName; // Bağımlı olunan paket adı.

        @NotBlank
        @Pattern(regexp = "\\d+\\.\\d+\\.\\d+") // Versiyon formatı yine 1.0.0 şeklinde olmalıdır.
        private String version; // Bağımlı olunan paketin versiyonu.
    }
}
