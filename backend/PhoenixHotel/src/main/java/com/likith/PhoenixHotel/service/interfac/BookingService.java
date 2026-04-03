package com.likith.PhoenixHotel.service.interfac;

import com.likith.PhoenixHotel.dto.ResponseDTO;
import com.likith.PhoenixHotel.entity.Booking;

public interface BookingService {

    ResponseDTO saveBooking(Long roomId, Long userId, Booking bookingRequest);

    ResponseDTO findBookingByConfirmationCode(String confirmationCode);

    ResponseDTO getAllBookings();

    ResponseDTO cancelBooking(Long bookingId);
}
