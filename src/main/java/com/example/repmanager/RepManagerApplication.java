package com.example.repmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Bu sınıf, RepManager Spring Boot uygulamasının giriş (başlangıç) noktasıdır.
 * Uygulama çalıştırıldığında Spring Boot burada başlar.
 */
@SpringBootApplication // Bu anotasyon, @Configuration, @EnableAutoConfiguration ve @ComponentScan anotasyonlarını birleştirir.
public class RepManagerApplication {

    /**
     * Uygulamayı başlatır. Spring Boot context'i oluşturur ve tüm bileşenleri başlatır.
     *
     * @param args Komut satırı argümanlarını alır (opsiyonel).
     */
    public static void main(String[] args) {
        SpringApplication.run(RepManagerApplication.class, args); // Uygulamayı ayağa kaldırır.
    }

}
