package com.gifthub.gifticon.entity;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.global.BaseTimeEntity;
import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gifticon_storage")
@Setter
public class GifticonStorage extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_gifticon_storage", sequenceName = "seq_gifticon_storage", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gifticon_storage")
    @Column(name = "gifticon_storage_id")
    private Long id;

    private String barcode;
    private LocalDate due;
    private String brandName;
    private String productName;
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gifticon_image_id")
    private GifticonImage gifticonImage;

//    private GifticonStorageStatus status;



}