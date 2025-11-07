package org.moto.motravel.model;

import jakarta.validation.constraints.*;

public class ItineraryItem {
    @Min(1)
    private int dayNumber;

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    @Size(max = 2000)
    private String description;

    @Size(max = 100)
    private String mealPlan; // e.g., Breakfast/Lunch/Dinner

    // Getters and setters
    public int getDayNumber() { return dayNumber; }
    public void setDayNumber(int dayNumber) { this.dayNumber = dayNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMealPlan() { return mealPlan; }
    public void setMealPlan(String mealPlan) { this.mealPlan = mealPlan; }
}
