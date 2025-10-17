package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Schema(description = "Resposta paginada da API")
public class PagedResponse<T> {
    
    @Schema(description = "Lista de conteúdo da página atual")
    private List<T> content;
    
    @Schema(description = "Informações de paginação")
    private PageInfo page;
    
    @Schema(description = "Links de navegação")
    private Map<String, String> links;
    
    public PagedResponse() {
        this.links = new HashMap<>();
    }
    
    public PagedResponse(List<T> content, PageInfo page) {
        this.content = content;
        this.page = page;
        this.links = new HashMap<>();
    }
    
    public static <T> PagedResponse<T> of(Page<T> springPage, String baseUrl) {
        PageInfo pageInfo = new PageInfo(
            springPage.getNumber(),
            springPage.getSize(),
            springPage.getTotalElements(),
            springPage.getTotalPages()
        );
        
        PagedResponse<T> response = new PagedResponse<>(springPage.getContent(), pageInfo);
        response.buildLinks(baseUrl, springPage);
        
        return response;
    }
    
    private void buildLinks(String baseUrl, Page<T> springPage) {
        // Remove query parameters if they exist
        String cleanBaseUrl = baseUrl.split("\\?")[0];
        
        // First page
        links.put("first", cleanBaseUrl + "?page=0&size=" + springPage.getSize());
        
        // Last page
        links.put("last", cleanBaseUrl + "?page=" + (springPage.getTotalPages() - 1) + "&size=" + springPage.getSize());
        
        // Previous page
        if (springPage.hasPrevious()) {
            links.put("prev", cleanBaseUrl + "?page=" + (springPage.getNumber() - 1) + "&size=" + springPage.getSize());
        }
        
        // Next page
        if (springPage.hasNext()) {
            links.put("next", cleanBaseUrl + "?page=" + (springPage.getNumber() + 1) + "&size=" + springPage.getSize());
        }
        
        // Current page
        links.put("self", cleanBaseUrl + "?page=" + springPage.getNumber() + "&size=" + springPage.getSize());
    }
    
    // Getters and Setters
    public List<T> getContent() {
        return content;
    }
    
    public void setContent(List<T> content) {
        this.content = content;
    }
    
    public PageInfo getPage() {
        return page;
    }
    
    public void setPage(PageInfo page) {
        this.page = page;
    }
    
    public Map<String, String> getLinks() {
        return links;
    }
    
    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}