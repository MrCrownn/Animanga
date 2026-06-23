package com.animanga.ms_social.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.animanga.ms_social.model.Resena;

public class ResenasPorAnimangaResponse {
    private Long idAnimanga;
    private BigDecimal promedio;
    private int totalResenas;
    private List<Resena> resenas;

    public ResenasPorAnimangaResponse(Long idAnimanga, List<Resena> resenas) {
        this.idAnimanga = idAnimanga;
        this.resenas = resenas;
        this.totalResenas = resenas.size();
        this.promedio = calcularPromedio(resenas);
    }

    private BigDecimal calcularPromedio(List<Resena> resenas) {
        if (resenas == null || resenas.isEmpty()) {
            return BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP);
        }
        BigDecimal suma = resenas.stream()
                .map(Resena::getPuntuacion)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return suma.divide(BigDecimal.valueOf(resenas.size()), 1, RoundingMode.HALF_UP);
    }

    public Long getIdAnimanga() { return idAnimanga; }
    public BigDecimal getPromedio() { return promedio; }
    public int getTotalResenas() { return totalResenas; }
    public List<Resena> getResenas() { return resenas; }
}
