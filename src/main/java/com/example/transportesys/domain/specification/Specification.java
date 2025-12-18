package com.example.transportesys.domain.specification;

/**
 * Interfaz genérica para el Specification Pattern.
 * Permite encapsular reglas de negocio complejas de forma reutilizable.
 *
 * @param <T> El tipo de entidad que se va a validar
 */
public interface Specification<T> {

    /**
     * Verifica si la entidad satisface la especificación.
     *
     * @param entity La entidad a validar
     * @return true si cumple con la especificación, false en caso contrario
     */
    boolean isSatisfiedBy(T entity);

    /**
     * Combina esta especificación con otra usando AND lógico.
     */
    default Specification<T> and(Specification<T> other) {
        return entity -> this.isSatisfiedBy(entity) && other.isSatisfiedBy(entity);
    }

    /**
     * Combina esta especificación con otra usando OR lógico.
     */
    default Specification<T> or(Specification<T> other) {
        return entity -> this.isSatisfiedBy(entity) || other.isSatisfiedBy(entity);
    }

    /**
     * Niega esta especificación.
     */
    default Specification<T> not() {
        return entity -> !this.isSatisfiedBy(entity);
    }
}
