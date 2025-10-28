# Artist

## 1. 개요
아티스트 정보를 관리하는 API입니다.

## 2. Endpoints

| Method | URI | Description |
| --- | --- | --- |
| GET | /api/ent/artists | 전체 아티스트 조회 |
| GET | /api/ent/artists/{artistId} | 상세 아티스트 조회 |
| POST | /api/ent/artists | 아티스트 등록 |
| PUT | /api/ent/artists/{artistId} | 아티스트 수정 |
| DELETE | /api/ent/artists/{artistId} | 아티스트 삭제 |
| GET | /api/ent/artists?partnerId={partnerId} | 특정 파트너의 아티스트 조회 |

## 3. Data Model

### Artist.java
```java
package com.sinse.universe.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "ARTIST")
@Entity
@Data
@ToString(exclude = "partner")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_ID")
    private int id;

    @Column(name = "AR_NAME")
    private String name;

    @Column(name = "AR_DESC")
    private String description;

    @Column(name = "AR_IMG")
    private String img;

    @Column(name = "AR_LOGO_IMG")
    private String logoImg;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "AR_DEBUT_DATE")
    private LocalDate debutDate;

    @Column(name = "AR_INSTA")
    private String insta;

    @Column(name = "AR_YOUTUBE")
    private String youtube;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PT_ID")
    @JsonIgnore  // JSON 응답에서 제외
    private Partner partner;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Color> colors = new ArrayList<>();

}
```

### ArtistRequest.java
```java
package com.sinse.universe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ArtistRequest(

        @NotBlank(message = "아티스트 이름은 필수입니다.")
        String name,

        String description,

        LocalDate debutDate,

        String insta,

        String youtube,

        int partnerId,

        // 이미지 삭제 여부 플래그 (null 허용, 기본값 false로 컨트롤러에서 처리 가능)
        Boolean deleteMainImage,
        Boolean deleteLogoImage
) {}
```

### ArtistResponse.java
```java
package com.sinse.universe.dto.response;

import com.sinse.universe.domain.Artist;

import java.time.LocalDate;

public record ArtistResponse(
        int id,
        String name,
        String description,
        String img,
        String logoImg,
        LocalDate debutDate,
        String insta,
        String youtube,
        String partnerName
        // , List<MemberResponse> members  // 필요할 때만 포함
) {
    public static ArtistResponse from(Artist artist) {
        return new ArtistResponse(
                artist.getId(),
                artist.getName(),
                artist.getDescription(),
                artist.getImg(),
                artist.getLogoImg(),
                artist.getDebutDate(),
                artist.getInsta(),
                artist.getYoutube(),
                artist.getPartner() != null ? artist.getPartner().getName() : null
        );
    }
}
```

## 4. Service Logic

### ArtistService.java
```java
package com.sinse.universe.model.artist;

import com.sinse.universe.dto.response.PartnerArtistResponse;
import com.sinse.universe.domain.Artist;
import com.sinse.universe.dto.request.ArtistRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArtistService {

    // 아티스트 전체 조회
    public List<Artist> selectAll();

    // 아티스트 1건 조회
    public Artist select(int artistId);

    // 아티스트 등록
    public void regist(ArtistRequest request);

    // 아티스트 수정
    public void update(Artist artist, MultipartFile mainImage, MultipartFile logoImage, boolean deleteMainImage, boolean deleteLogoImage) throws IOException;

    // 아티스트 삭제
    public void delete(int artistId);

    // 소속사(Partner) ID로 아티스트 정보 조회
    public List<Artist> findByPartnerId(int partnerId);

    // 소속사(Partner) ID로 아티스트 이름만 조회
    public List<PartnerArtistResponse> selectByPartnerId (int partnerId);
}
```
