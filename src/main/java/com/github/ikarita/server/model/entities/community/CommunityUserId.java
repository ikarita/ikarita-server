package com.github.ikarita.server.model.entities.community;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityUserId implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "community_id")
    private Long communityId;
}
