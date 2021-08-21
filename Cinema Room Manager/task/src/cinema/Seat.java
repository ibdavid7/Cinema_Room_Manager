package cinema;

public class Seat {

    private double price;
    private boolean isBooked = false;

    public Seat(Double price) {
        this.price = price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return isBooked ? "B" : "S";
    }

    public boolean book() {
        if (isBooked) {
            return false;
        } else {
            return this.isBooked = true;
        }
    }

    public boolean isBooked() {
        return isBooked;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "price=" + getPrice() +
                ", description='" + getDescription() + '\'' +
                ", isBooked=" + isBooked() +
                '}';
    }

    public void printPrice() {
        System.out.printf("Ticket price: $%.0f\n", this.getPrice());
    }
}
