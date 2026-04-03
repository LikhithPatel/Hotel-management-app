package com.likith.PhoenixHotel.controller;

import com.likith.PhoenixHotel.dto.LoginRequest;
import com.likith.PhoenixHotel.dto.ResponseDTO;
import com.likith.PhoenixHotel.dto.UserDTO;
import com.likith.PhoenixHotel.entity.Booking;
import com.likith.PhoenixHotel.entity.User;
import com.likith.PhoenixHotel.service.interfac.BookingService;
import com.likith.PhoenixHotel.service.interfac.UserService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")

public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/book-room/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ResponseDTO> saveBookings(@PathVariable Long roomId,
                                                    @PathVariable Long userId,
                                                    @RequestBody Booking bookingRequest) {
        ResponseDTO responseDTO = bookingService.saveBooking(roomId, userId, bookingRequest);
        return ResponseEntity.status(responseDTO.getStatusCode()).body(responseDTO);
    }

    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseDTO> getBookingByConfirmationCode(@RequestBody String confirmationCode) {
        ResponseDTO responseDTO =bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(responseDTO.getStatusCode()).body(responseDTO);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ResponseDTO> cancelBookings(@PathVariable Long bookingId) {
        ResponseDTO responseDTO = bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(responseDTO.getStatusCode()).body(responseDTO);
    }
}
