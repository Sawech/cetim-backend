package com.cetim.labs.dto;

public class TestElementDTO {
    private Long id; // Add ID field
    private String type;
    private String content;
    private String tableContent;
    private int position; // Add position field
    private boolean important; // Add this field
    private String side;
    // // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTableContent() { return tableContent; }
    public void setTableContent(String tableContent) { this.tableContent = tableContent; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public boolean isImportant() { return important; }
    public void setImportant(boolean important) { this.important = important; }
    public String getSide() { return side; }
    public void setSide(String side) { this.side = side; }
}