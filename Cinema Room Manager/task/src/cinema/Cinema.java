package cinema;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cinema {

    private int rows;
    private int seats;
    private Seat[][] arr;
    private static final double frontHalfSeatPrice = 10.0;
    private static final double backHalfSeatPrice = 8.0;
    private static final int seatCountPriceCutoff = 60;

    public static void menu() {
        // 1. Initialize Cinema instance
        Cinema cinema = Cinema.create();

        // 2. Enter looping menu

        int choice;
        Scanner in = new Scanner(System.in);
        do {
            System.out.printf("1. Show the seats\n" +
                    "2. Buy a ticket\n" +
                    "3. Statistics\n" +
                    "0. Exit\n");
            choice = in.nextInt();

            switch (choice) {
                case 1:
                    cinema.print();
                    continue;
                case 2:
                    cinema.bookSeat();
                    continue;
                case 3:
                    cinema.printStatistics();
                default:
                    continue;
            }

        } while (choice != 0);

    }

    public static Cinema create() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the number of rows:");
        int rows = in.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int seats = in.nextInt();
        return new Cinema(rows, seats);
    }

    public Cinema(int r, int s) {
        this.arr = new Seat[r][s];
        this.rows = r;
        this.seats = s;
        boolean isWithinSeatCountPriceCutoff = rows * seats <= Cinema.seatCountPriceCutoff;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seats; j++) {
                arr[i][j] = isWithinSeatCountPriceCutoff || i < rows / 2 ?
                        new Seat(Cinema.frontHalfSeatPrice) :
                        new Seat(Cinema.backHalfSeatPrice);
            }
        }
    }

    public double getIncome() {
        return Arrays.stream(this.arr)
                .flatMapToDouble(row -> Arrays.stream(row).mapToDouble(Seat::getPrice))
                .sum();
    }

//    public void printIncome() {
//        System.out.format("Total Income:\n$%.0f", this.getIncome());
//    }

    public Optional<Seat> getSeat(int r, int s) {
        try {
            return Optional.ofNullable(this.arr[r - 1][s - 1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }


    }

    public void bookSeat() {

        AtomicBoolean bookingComplete = new AtomicBoolean(false);

        do {

            Scanner in = new Scanner(System.in);
            System.out.printf("Enter a row number:\n");
            int row = in.nextInt();
            System.out.printf("Enter a seat number in that row:\n");
            int seat = in.nextInt();

            getSeat(row, seat).ifPresentOrElse(s -> {
                s.printPrice();

                if (s.book()) {
                    bookingComplete.set(true);
                } else {
                    System.out.println("That ticket has already been purchased!");
                }

            }, () -> {
                System.out.println("Wrong input!");
                return;
            });


        } while (!bookingComplete.get());
    }

    public void print() {
        System.out.println(this.toString());
    }

    public void printStatistics() {

        long numberOfPurchaseTickets = Arrays.stream(this.arr)
                .flatMap(row -> Arrays.stream(row))
                .filter(Seat::isBooked)
                .count();
        double percentOccupancy = 100.0 * numberOfPurchaseTickets / (this.rows * this.seats);

        double currentIncome = Arrays.stream(this.arr)
                .flatMap(row -> Arrays.stream(row))
                .filter(Seat::isBooked)
                .mapToDouble(Seat::getPrice)
                .sum();

        double totalIncomePotential = Arrays.stream(this.arr)
                .flatMapToDouble(row -> Arrays.stream(row).mapToDouble(Seat::getPrice))
                .sum();

        System.out.printf("Number of purchased tickets: %d\nPercentage: %.2f%%\nCurrent income: $%.0f\nTotal income: $%.0f\n",
                numberOfPurchaseTickets,
                percentOccupancy,
                currentIncome,
                totalIncomePotential);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        //Header
        sb.append("Cinema:\n");

        //1st row
        sb.append(IntStream.rangeClosed(1, arr[0].length)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" ", "  ", "\n"))
        );

        //Subsequent rows
        AtomicInteger counter = new AtomicInteger(1);

        Arrays.stream(this.arr)
                .forEach(row -> {

                    sb.append(Arrays.stream(row)
//                            .map(Seat::isBooked)
//                            .map(Seat::getPrice)
//                            .map(String::valueOf)
                                    .map(Seat::getDescription)
                                    .collect(Collectors.joining(" ", counter.getAndIncrement() + " ", "\n"))
                    );
                });


        return sb.toString();
    }

    public static void main(String[] args) {
        // Write your code here

        Cinema.menu();

    }

}

