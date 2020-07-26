package Model;

/**
 * Created by katelanum on 4/17/20.
 * outsourced part class
 */
public class OutsourcedPart extends Part{
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public OutsourcedPart(int id, String name, double price, int stock, int min, int max, String companyName) {

        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
}