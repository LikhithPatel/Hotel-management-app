package com.likith.PhoenixHotel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message="Check-in date is required")
    private LocalDate checkInDate;

    @NotNull(message="Check-out date must be in future")
    private LocalDate checkOutDate;

    @Min(value=1,message="Number of adults must not be less than 1")
    private int numOfAdults;

    @Min(value=0,message="Number of adults must not be less than 0")
    private int numOfChildren;

    private int totalNumOfGuests;

    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room room;

    public void calculateTotalNumOfGuests() {
        this.totalNumOfGuests = numOfAdults + numOfChildren;
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumOfGuests();
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumOfGuests();
    }

    @Override
    public String toString() {
        return "Booking{"+
                "id="+id+
                ",checkInDate="+checkInDate+
                ",checkOutDate="+checkOutDate+
                ",numOfAdults="+numOfAdults+
                ",numOfChildren="+numOfChildren+
                ",totalNumOfGuests="+totalNumOfGuests+
                ",bookingConfirmationCode="+bookingConfirmationCode+
                ",user="+user+
                ",room="+room+
                "}";
    }
}
