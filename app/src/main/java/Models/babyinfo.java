package Models;

public class babyinfo {
    private String weight;
    private String sleepDuration;
    private String breastfeedingRate;
    private String length;

    public babyinfo() {
        // Constructeur vide requis pour Firebase
    }

    public babyinfo(String weight, String sleepDuration, String breastfeedingRate, String length) {
        this.weight = weight;
        this.sleepDuration = sleepDuration;
        this.breastfeedingRate = breastfeedingRate;
        this.length = length;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
// Getters et setters

    public String getSleepDuration() {
        return sleepDuration;
    }

    public void setSleepDuration(String sleepDuration) {
        this.sleepDuration = sleepDuration;
    }

    public String getBreastfeedingRate() {
        return breastfeedingRate;
    }

    public void setBreastfeedingRate(String breastfeedingRate) {
        this.breastfeedingRate = breastfeedingRate;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}

