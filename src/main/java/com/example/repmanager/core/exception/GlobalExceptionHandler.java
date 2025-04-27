package com.example.repmanager.core.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Bu sınıf, uygulamadaki tüm istisnaları (exception) global olarak yakalar ve standart bir hata yanıtı (ErrorResponse) üretir.
 * Böylece kullanıcıya daha okunabilir ve tutarlı hata mesajları döner.
 */
@RestControllerAdvice // Tüm Controller'lar için global exception yönetimi sağlar.
@Slf4j // Lombok anotasyonu: Logger (log nesnesi) üretir. log.error gibi kullanıma izin verir.
public class GlobalExceptionHandler {

    /**
     * Tüm RuntimeException türündeki hataları yakalar.
     *
     * @param ex Oluşan RuntimeException hatasıdır.
     * @return Hata bilgisi içeren bir ResponseEntity döner.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        log.error("Runtime Exception: ", ex); // Hata detayını log'lar.
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage()); // 400 Bad Request döner.
    }

    /**
     * Validation hatalarını yakalar (örneğin: @Valid anotasyonu geçmezse).
     *
     * @param ex Geçersiz method argümanı nedeniyle oluşan istisnadır.
     * @return Hata bilgisi içeren bir ResponseEntity döner.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        // İlk bulunan validasyon hatasının mesajını alır.
        String msg = ex.getBindingResult().getFieldError().getDefaultMessage();
        return buildError(HttpStatus.BAD_REQUEST, msg); // 400 Bad Request ile birlikte hata mesajı döner.
    }

    /**
     * JSON parse hatalarını yakalar (örneğin meta.json dosyası yanlış formatta ise).
     *
     * @param ex JSON okuma/parsing sırasında oluşan hatadır.
     * @return Hata bilgisi içeren bir ResponseEntity döner.
     */
    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<ErrorResponse> handleJsonParse(MismatchedInputException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "Invalid JSON format in meta.json"); // JSON hatası için özel mesaj döner.
    }

    /**
     * Uygulamada beklenmeyen tüm diğer hataları yakalar.
     *
     * @param ex Bilinmeyen bir istisna durumudur.
     * @return Hata bilgisi içeren bir ResponseEntity döner.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        log.error("Unhandled Exception: ", ex); // Beklenmeyen hataları log'lar.
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred"); // 500 Internal Server Error döner.
    }

    /**
     * Standart bir hata yanıtı (ErrorResponse) üretir.
     *
     * @param status HTTP durum kodudur.
     * @param message Kullanıcıya gösterilecek hata mesajıdır.
     * @return ErrorResponse içeren bir ResponseEntity döner.
     */
    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message) {
        return new ResponseEntity<>(
                new ErrorResponse(status.value(), message, LocalDateTime.now()), // Hata detaylarını ErrorResponse içinde oluşturur.
                status
        );
    }
}
