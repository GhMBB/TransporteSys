package com.example.transportesys.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa la tabla de conductores en la base de datos.
 */
@Entity
@Table(name = "conductores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConductorEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "licencia", nullable = false, unique = true, length = 50)
    private String licencia;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "conductor_vehiculos", joinColumns = @JoinColumn(name = "conductor_id"))
    @Column(name = "vehiculo_id")
    private List<Long> vehiculosIds = new ArrayList<>();
}
