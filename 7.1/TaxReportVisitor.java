public class TaxReportVisitor implements InstrumentVisitor {
    private double totalTaxLiability = 0.0;
    private StringBuilder reportBuilder = new StringBuilder();

    @Override
    public void visit(Stock stock) {
        double tax = stock.getCurrentPriceValue() * 0.15;
        this.totalTaxLiability += tax;
        reportBuilder.append("Stock ").append(stock.getSymbol())
                .append(" Tax: $").append(tax).append("\n");
    }

    @Override
    public void visit(Bond bond) {
        // Thuế 30% dựa trên tiền lãi của 1 unit
        double tax = bond.annualCouponPayment(1) * 0.30;
        this.totalTaxLiability += tax;
        reportBuilder.append("Bond ").append(bond.getSymbol())
                .append(" Tax: $").append(tax).append("\n");
    }

    @Override
    public void visit(Option option) {
        double tax = option.getCurrentPriceValue() * 0.20;
        this.totalTaxLiability += tax;
        reportBuilder.append("Option ").append(option.getSymbol())
                .append(" Tax: $").append(tax).append("\n");
    }

    @Override
    public void visit(Future future) {
        double tax = future.getCurrentPriceValue() * 0.20;
        this.totalTaxLiability += tax;
        reportBuilder.append("Future ").append(future.getSymbol())
                .append(" Tax: $").append(tax).append("\n");
    }

    public double getTotalTaxLiability() {
        return this.totalTaxLiability;
    }

    public String getReport() {
        return this.reportBuilder.toString();
    }
}