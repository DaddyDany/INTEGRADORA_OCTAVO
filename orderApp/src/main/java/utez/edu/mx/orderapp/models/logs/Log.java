package utez.edu.mx.orderapp.models.logs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name= "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Long id;

    @Column(name = "operation_type", nullable = false)
    private String operationType;

    @Column(name = "affected_table", nullable = false)
    private String affectedTable;

    @Column(name = "operation_description", nullable = false, columnDefinition = "TEXT")
    private String operationDescription;

    @Column(name = "operation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;
}
