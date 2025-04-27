package com.example.repmanager.core.config;

/**
 * Bu enum, sistemde kullanılabilecek depolama (storage) stratejilerini tanımlar.
 * Hangi stratejinin kullanılacağını belirlemek için kullanılır.
 */
public enum StorageStrategyType {

    FILE_SYSTEM,    // Dosya sistemi üzerinde (örneğin bir klasör yapısında) depolama yapılır.
    OBJECT_STORAGE  // Nesne tabanlı bir depolama sistemi (örneğin MinIO, AWS S3 gibi) kullanılır.
}
