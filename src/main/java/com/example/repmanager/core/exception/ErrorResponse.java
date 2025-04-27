package com.example.repmanager.core.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Bu sınıf, API hataları için standart bir yanıt yapısı tanımlar.
 * Bir hata oluştuğunda kullanıcıya status, message ve timestamp bilgileri ile dönüş yapılır.
 */
@Data // Lombok anotasyonu: Getter, Setter, toString, equals ve hashCode metodlarını otomatik oluşturur.
@AllArgsConstructor // Lombok anotasyonu: Tüm alanları (field) içeren bir constructor üretir.
public class ErrorResponse {

    private int status;           // HTTP hata kodunu tutar (örneğin: 404, 500 gibi).
    private String message;       // Hata ile ilgili açıklayıcı mesajı içerir.
    private LocalDateTime timestamp; // Hatanın oluştuğu zamanı belirtir.
}
