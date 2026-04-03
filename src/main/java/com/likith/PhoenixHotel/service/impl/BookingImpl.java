package com.likith.PhoenixHotel.service.impl;

import com.likith.PhoenixHotel.dto.BookingDTO;
import com.likith.PhoenixHotel.dto.ResponseDTO;
import com.likith.PhoenixHotel.entity.Booking;
import com.likith.PhoenixHotel.entity.Room;
import com.likith.PhoenixHotel.entity.User;
import com.likith.PhoenixHotel.exception.OurException;
import com.likith.PhoenixHotel.repo.BookingRepository;
import com.likith.PhoenixHotel.repo.RoomRepository;
import com.likith.PhoenixHotel.repo.UserRepository;
import com.likith.PhoenixHotel.service.interfac.BookingService;
import com.likith.PhoenixHotel.service.interfac.RoomService;
import com.likith.PhoenixHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookingImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseDTO saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        ResponseDTO response = new ResponseDTO();
        try{
            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
                throw new IllegalArgumentException("Check in date must come after check out date");
            }
            Room room=roomRepository.findById(roomId).orElseThrow(()->new OurException("Room Not Found"));
            User user=userRepository.findById(userId).orElseThrow(()->new OurException("User not found"));
            List<Booking> existingBookings=room.getBookings();
            if(!roomIsAvailable(bookingRequest,existingBookings)){
                throw new OurException("Room Not Available for selected date range");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode= Utils.generateRandomAlphanumeric(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Booking saved successfully");
            
        }catch(OurException oe){
            response.setStatusCode(404);
            response.setMessage(oe.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a booking "+e.getMessage());
        }
        return response;

    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings){
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }

    @Override
    public ResponseDTO findBookingByConfirmationCode(String confirmationCode) {

        ResponseDTO response = new ResponseDTO();
        try{
            Booking booking=bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()->new OurException("Booking Not Found"));
            BookingDTO bookingDTO=Utils.mapBookingEntityToBookingDTO(booking);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBooking(bookingDTO);

        }catch(OurException oe){
            response.setStatusCode(404);
            response.setMessage(oe.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error finding bookings "+e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getAllBookings() {
        ResponseDTO response = new ResponseDTO();
        try{
            List<Booking> bookingList=bookingRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            List<BookingDTO> bookingDTOList=Utils.mapBookingistEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBooking((BookingDTO) bookingDTOList);

        }catch(OurException oe){
            response.setStatusCode(404);
            response.setMessage(oe.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting bookings "+e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO cancelBooking(Long bookingId) {
        ResponseDTO response = new ResponseDTO();
        try {
            List<Booking> bookings = Collections.singletonList(bookingRepository.findById(bookingId).orElseThrow(() -> new OurException("Booking does not exist")));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("Successful");

        } catch (OurException oe) {
            response.setStatusCode(404);
            response.setMessage(oe.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting a booking " + e.getMessage());
        }
        return response;
    }
}
