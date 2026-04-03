package com.likith.PhoenixHotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
    private BookingDTO booking;
    private UserDTO user;
    private RoomDTO room;

    private int statusCode;

    private String message;

    private String token;

    private String role;

    private String expirationTime;

    private int totalNumOfGuests;

    private String bookingConfirmationCode;

    private List<RoomDTO> roomList;
    private List<BookingDTO> bookingList;
    private List<UserDTO> userList;


}
