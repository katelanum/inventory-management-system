package Model;

/**
 * Created by katelanum on 4/17/20.
 */
public class InhousePart extends Part {
    private int machineId;

    public InhousePart(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}