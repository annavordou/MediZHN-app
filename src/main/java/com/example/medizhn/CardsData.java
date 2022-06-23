package com.example.medizhn;

/**
 * Σε αυτήν την κλάση υλοποιούνται οι κάρτες που υπάρχουν στην εφαρμογή
 */
public class CardsData
{
    private String title;
    private String secondaryText;
    private int id;

    public CardsData(String title, String secondaryText)
    {
        this.title = title;
        this.secondaryText = secondaryText;
        this.id= -5;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getSecondaryText()
    {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText)
    {
        this.secondaryText = secondaryText;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return  id;
    }

}
