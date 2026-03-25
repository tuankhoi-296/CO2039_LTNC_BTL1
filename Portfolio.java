import java.util.*;

public class Portfolio implements Observable<String> {
    private final String portfolioId;
    private final String ownerName;
    private final List<Position> positions;
    private final List<Observer<String>> observers;

    public Portfolio(String portfolioId, String ownerName) {
        this.portfolioId = portfolioId;
        this.ownerName = ownerName;
        this.positions = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void addPosition(Instrument inst, int qty, double costBasis) {
        boolean found = false;
        for (Position pos : this.positions) {
            if (pos.getInstrument().getSymbol().equals(inst.getSymbol())) {
                pos.addQuantity(qty, costBasis);
                found = true;
                break;
            }
        }
        if (!found) {
            this.positions.add(new Position(inst, qty, costBasis));
        }
        notifyObservers("ADDED: " + inst.getSymbol() + " x" + qty);
    }

    public void removePosition(String symbol) throws PositionNotFoundException {
        Position toRemove = null;
        for (Position pos : this.positions) {
            if (pos.getInstrument().getSymbol().equals(symbol)) {
                toRemove = pos;
                break;
            }
        }

        if (toRemove == null) {
            throw new PositionNotFoundException("Cannot remove: Position " + symbol + " not found.");
        }

        this.positions.remove(toRemove);
        notifyObservers("REMOVED: " + symbol);
    }

    public double totalMarketValue() {
        double total = 0.0;
        for (Position pos : this.positions) {
            total += pos.marketValue();
        }
        return total;
    }

    public double totalUnrealizedPnL() {
        double total = 0.0;
        for (Position pos : this.positions) {
            total += pos.unrealizedPnL();
        }
        return total;
    }

    public Position getPosition(String symbol) throws PositionNotFoundException {
        for (Position pos : this.positions) {
            if (pos.getInstrument().getSymbol().equals(symbol)) {
                return pos;
            }
        }
        throw new PositionNotFoundException("Position " + symbol + " not found.");
    }

    public List<Position> getPositionsSortedByValue() {
        List<Position> sorted = new ArrayList<>(this.positions);
        // Sắp xếp giảm dần (descending) theo marketValue
        sorted.sort((p1, p2) -> Double.compare(p2.marketValue(), p1.marketValue()));
        return sorted;
    }

    public Map<String, Double> allocationByAssetClass() {
        Map<String, Double> allocation = new HashMap<>();
        double totalValue = totalMarketValue();

        if (totalValue == 0.0) {
            return allocation;
        }

        for (Position pos : this.positions) {
            String assetClass = pos.getInstrument().assetClass();
            // Tính tỷ trọng % của vị thế này
            double percent = (pos.marketValue() / totalValue) * 100.0;
            // Cộng dồn vào map
            allocation.put(assetClass, allocation.getOrDefault(assetClass, 0.0) + percent);
        }
        return allocation;
    }

    public void revalueAll(PricingStrategy strategy) {
        for (Position pos : this.positions) {
            Instrument inst = pos.getInstrument();
            double newPrice = strategy.calculateFairValue(inst);
            inst.updatePrice(newPrice);
        }
        notifyObservers("REVALUED: " + strategy.strategyName());
    }

    @Override
    public void addObserver(Observer<String> observer) {
        if (!this.observers.contains(observer)) {
            this.observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer<String> observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event) {
        for (Observer<String> obs : this.observers) {
            obs.onEvent(event);
        }
    }

    public String getPortfolioId() {
        return this.portfolioId;
    }

    public String getOwnerName() {
        return this.ownerName;
    }
}
