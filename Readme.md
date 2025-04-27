# RepManager

Minimal bir **package management** sistemi.  
Spring Boot ile geliştirilmiş REST API üzerinden `.rep` ve `meta.json` dosyaları yükleyip indirebilirsiniz.

## Gereksinimler
- Java 17 veya üstü
- Docker
- PostgreSQL (Veritabanı için)
- MinIO (Object Storage seçilirse)

## Başlangıç

### 1. Veritabanı Hazırlığı
PostgreSQL üzerinde aşağıdaki bilgilerle bir veritabanı oluşturulmalıdır:
- **Database Adı:** repodb
- **Username:** postgres
- **Password:** postgres

(Tüm bilgiler `application.properties` dosyasında tanımlıdır.)

### 2. MinIO Başlatılması (Object Storage Kullanımı için)

Eğer `application.properties` içinde `storage.strategy=object-storage` seçilirse, önce mutlaka MinIO sunucusunun çalışması gerekir.  
MinIO'yu Docker ile hızlıca başlatmak için:

```bash
docker run -p 9000:9000 -p 9001:9001 --name minio -e MINIO_ROOT_USER=minioadmin -e MINIO_ROOT_PASSWORD=minioadmin minio/minio server /data --console-address ":9001"
```

> **Not:**
> - Bu komut MinIO'yu 9000 (API) ve 9001 (admin paneli) portlarında çalıştırır.
> - Eğer MinIO başlatılmazsa ve `storage.strategy=object-storage` aktifse, uygulama çalışmayacaktır.

Admin paneline erişmek için: [http://localhost:9001](http://localhost:9001)

Giriş bilgileri (minioadmin / minioadmin)

---

### 3. Uygulama Başlatılması

Projeyi çalıştırmak için:

```bash
./mvnw spring-boot:run
```
veya doğrudan IntelliJ IDEA, Eclipse gibi IDE'lerden `RepManagerApplication` sınıfı üzerinden başlatabilirsiniz.

---

## API Kullanımı

### Paket Yükleme (Upload)

- **Endpoint:** `POST /api/packages/{packageName}/{version}`
- **İçerik Tipi:** `multipart/form-data`
- **Parametreler:**
    - `metaFile`: Paket metadata dosyası (`meta.json`)
    - `packageFile`: Paket dosyası (`package.rep`)

Örnek kullanım:

```bash
curl -X POST "http://localhost:8080/api/packages/mypackage/1.0.0" -F "metaFile=@/path/to/meta.json" -F "packageFile=@/path/to/package.rep"
```

---

### Paket İndirme (Download)

- **Endpoint:** `GET /api/packages/{packageName}/{version}/{fileName}`

Örnek kullanım:

```bash
curl -X GET "http://localhost:8080/api/packages/mypackage/1.0.0/meta.json" -o meta.json
```

veya

```bash
curl -X GET "http://localhost:8080/api/packages/mypackage/1.0.0/package.rep" -o package.rep
```

---

## Swagger API Dökümantasyonu

Projede tüm API uç noktalarını kolayca görmek için:  
👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Storage Stratejisi

`application.properties` dosyasında aşağıdaki satır ile belirlenir:

```properties
storage.strategy=file-system
```
veya

```properties
storage.strategy=object-storage
```

- `file-system`: Dosyalar yerel klasörde (`storage/packages`) saklanır.
- `object-storage`: Dosyalar MinIO gibi object storage sistemlerinde saklanır.

---

## Notlar
- `.rep` dosyalarının içeriği kontrol edilmez. Sadece .zip formatında dosya olarak kabul edilir.
- `meta.json` dosyasının formatı doğrulamalara tabidir (örneğin `version` alanı `1.0.0` formatında olmalıdır).
- Hatalı yüklemelerde anlamlı hata mesajları döner (HTTP 400 veya 500 kodları).

---