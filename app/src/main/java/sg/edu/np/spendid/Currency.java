package sg.edu.np.spendid;

public class Currency {
    private String foreign;
    private double rate;
    private String date ;

    public String getForeign() {
        return foreign;
    }

    public void setForeign(String foreign) {
        this.foreign = foreign;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Currency() {
    }

    public Currency(String foreign, double rate, String date) {
        this.foreign = foreign;
        this.rate = rate;
        this.date = date;
    }
}
