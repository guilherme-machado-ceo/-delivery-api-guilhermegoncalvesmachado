package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações de paginação")
public class PageInfo {
    
    @Schema(description = "Número da página atual (baseado em zero)", example = "0")
    private int number;
    
    @Schema(description = "Tamanho da página", example = "10")
    private int size;
    
    @Schema(description = "Total de elementos", example = "50")
    private long totalElements;
    
    @Schema(description = "Total de páginas", example = "5")
    private int totalPages;
    
    @Schema(description = "Indica se é a primeira página", example = "true")
    private boolean first;
    
    @Schema(description = "Indica se é a última página", example = "false")
    private boolean last;
    
    @Schema(description = "Indica se há próxima página", example = "true")
    private boolean hasNext;
    
    @Schema(description = "Indica se há página anterior", example = "false")
    private boolean hasPrevious;
    
    public PageInfo() {}
    
    public PageInfo(int number, int size, long totalElements, int totalPages) {
        this.number = number;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = number == 0;
        this.last = number == totalPages - 1;
        this.hasNext = number < totalPages - 1;
        this.hasPrevious = number > 0;
    }
    
    // Getters and Setters
    public int getNumber() {
        return number;
    }
    
    public void setNumber(int number) {
        this.number = number;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public long getTotalElements() {
        return totalElements;
    }
    
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    public boolean isFirst() {
        return first;
    }
    
    public void setFirst(boolean first) {
        this.first = first;
    }
    
    public boolean isLast() {
        return last;
    }
    
    public void setLast(boolean last) {
        this.last = last;
    }
    
    public boolean isHasNext() {
        return hasNext;
    }
    
    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
    
    public boolean isHasPrevious() {
        return hasPrevious;
    }
    
    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}