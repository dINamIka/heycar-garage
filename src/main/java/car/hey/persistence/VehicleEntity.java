package car.hey.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "vehicle",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"dealer_id", "code"})}
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "dealer_id", nullable = false)
    @EqualsAndHashCode.Include
    private DealerEntity dealer;
    @Column(name = "code", nullable = false, length = 20)
    @EqualsAndHashCode.Include
    private String code;
    @Column(name = "make", nullable = false, length = 40)
    private String make;
    @Column(name = "model", nullable = false, length = 40)
    private String model;
    @Column(name = "power_in_kw", nullable = false)
    private int powerInKW;
    @Column(name = "year", nullable = false)
    private int year;
    @Column(name = "color", length = 24)
    private String color;
    @Column(name = "price", nullable = false)
    @PositiveOrZero
    private long price;

}
