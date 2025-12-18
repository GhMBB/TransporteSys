package com.example.transportesys.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Wrapper genérico para resultados paginados en el dominio.
 * No depende de frameworks externos (Clean Architecture).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;
    private int numberOfElements;
    private boolean empty;

    /**
     * Calcula si hay página siguiente.
     */
    public boolean hasNext() {
        return !last;
    }

    /**
     * Calcula si hay página anterior.
     */
    public boolean hasPrevious() {
        return !first;
    }
}
