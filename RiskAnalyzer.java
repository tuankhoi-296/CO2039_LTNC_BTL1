import java.util.*;

public class RiskAnalyzer<T extends Instrument> {
    private final List<T> instruments = new ArrayList<>();

    public void add(T instrument) {
        if (instrument != null) {
            this.instruments.add(instrument);
        }
    }

    public double averageRisk() {
        if (this.instruments.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (T inst : this.instruments) {
            sum += inst.riskScore();
        }
        return sum / this.instruments.size();
    }

    public T highestRisk() {
        if (this.instruments.isEmpty()) {
            return null;
        }
        T highest = this.instruments.get(0);
        for (T inst : this.instruments) {
            if (inst.riskScore() > highest.riskScore()) {
                highest = inst;
            }
        }
        return highest;
    }

    public T lowestRisk() {
        if (this.instruments.isEmpty()) {
            return null;
        }
        T lowest = this.instruments.get(0);
        for (T inst : this.instruments) {
            if (inst.riskScore() < lowest.riskScore()) {
                lowest = inst;
            }
        }
        return lowest;
    }

    public List<T> getAboveRiskThreshold(double threshold) {
        List<T> result = new ArrayList<>();
        for (T inst : this.instruments) {
            if (inst.riskScore() > threshold) {
                result.add(inst);
            }
        }
        return result;
    }
}
