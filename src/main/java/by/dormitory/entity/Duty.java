package by.dormitory.entity;

import lombok.*;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Table
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Duty {
    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private Date date;

    @NonNull
    private int half;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;

    @Override
    public boolean equals(Object o) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Duty duty = (Duty) o;
        return half == duty.half &&
                dateFormat.format(date).equals(dateFormat.format(duty.date));
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, half, user);
    }
}
