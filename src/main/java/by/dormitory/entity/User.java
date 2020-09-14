package by.dormitory.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "usr")
@ToString
public class User {
    @Id
    @NonNull
    private Integer id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String domain;

    @Column(nullable = true)
    private Integer grp;

    private String room;

    @Transient
    private boolean isClosed;

    @Transient
    private boolean canAccessClosed;

    @Transient
    private Integer invitedBy;

    @Transient
    private String type;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, mappedBy = "user")
    private List<Duty> duties;
}
