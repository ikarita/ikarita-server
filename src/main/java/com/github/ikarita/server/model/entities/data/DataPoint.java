package com.github.ikarita.server.model.entities.data;

import com.github.ikarita.server.model.entities.community.Community;
import com.github.ikarita.server.model.entities.user.LocalUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ikarita_data_point")
public class DataPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Float longitude;

    private Float latitude;

    @Enumerated(EnumType.STRING)
    private DataPointStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Community community;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private LocalUser localUser;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

}
