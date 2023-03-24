package OperatingClasses;

public class AlphaBetaFilter {
    private double alpha;
    private double beta;
    private double filteredValue;
    private double prevFilteredValue;

    public AlphaBetaFilter(double alpha, double beta, double initialValue) {
        this.alpha = alpha;
        this.beta = beta;
        this.filteredValue = initialValue;
        this.prevFilteredValue = initialValue;
    }

    public double filter(double measurement, double dt) {
        // Calculate the predicted value
        double predictedValue = filteredValue + dt * (prevFilteredValue + dt * (measurement - filteredValue));

        // Update the filtered value using the alpha-beta filter
        filteredValue = alpha * measurement + (1 - alpha) * predictedValue;
        filteredValue = beta * filteredValue + (1 - beta) * prevFilteredValue;

        // Update the previous filtered value for the next iteration
        prevFilteredValue = filteredValue;

        return filteredValue;
    }
}
