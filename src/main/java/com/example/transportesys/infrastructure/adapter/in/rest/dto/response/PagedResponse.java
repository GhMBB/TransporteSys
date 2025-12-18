package com.example.transportesys.infrastructure.adapter.in.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO genérico para respuestas paginadas.
 * Incluye metadata completa de paginación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta paginada con metadata")
public class PagedResponse<T> {

    @Schema(description = "Lista de elementos de la página actual")
    @JsonProperty("content")
    private List<T> content;

    @Schema(description = "Información de paginación")
    @JsonProperty("page")
    private PageMetadata page;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Metadata de paginación")
    public static class PageMetadata {

        @Schema(description = "Número de página actual (0-indexed)", example = "0")
        @JsonProperty("number")
        private int number;

        @Schema(description = "Tamaño de página (elementos por página)", example = "10")
        @JsonProperty("size")
        private int size;

        @Schema(description = "Total de elementos en todas las páginas", example = "100")
        @JsonProperty("totalElements")
        private long totalElements;

        @Schema(description = "Total de páginas", example = "10")
        @JsonProperty("totalPages")
        private int totalPages;

        @Schema(description = "Indica si es la primera página", example = "true")
        @JsonProperty("first")
        private boolean first;

        @Schema(description = "Indica si es la última página", example = "false")
        @JsonProperty("last")
        private boolean last;

        @Schema(description = "Indica si hay una página siguiente", example = "true")
        @JsonProperty("hasNext")
        private boolean hasNext;

        @Schema(description = "Indica si hay una página anterior", example = "false")
        @JsonProperty("hasPrevious")
        private boolean hasPrevious;

        @Schema(description = "Número de elementos en la página actual", example = "10")
        @JsonProperty("numberOfElements")
        private int numberOfElements;

        @Schema(description = "Indica si la página está vacía", example = "false")
        @JsonProperty("empty")
        private boolean empty;
    }

    /**
     * Crea una PagedResponse desde un Page de Spring.
     */
    public static <T> PagedResponse<T> of(org.springframework.data.domain.Page<?> page, List<T> content) {
        PageMetadata metadata = new PageMetadata(
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isFirst(),
            page.isLast(),
            page.hasNext(),
            page.hasPrevious(),
            page.getNumberOfElements(),
            page.isEmpty()
        );

        return new PagedResponse<>(content, metadata);
    }
}
