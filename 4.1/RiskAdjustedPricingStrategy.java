public class RiskAdjustedPricingStrategy implements PricingStrategy {

    @Override
    public double calculateFairValue(Instrument instrument) {
        double riskPremium = 1.0 + (0.01 * instrument.riskScore());
        return instrument.getCurrentPriceValue() * riskPremium;
    }

    @Override
    public String strategyName() {
        return "RiskAdjustedPricingStrategy";
    }
}