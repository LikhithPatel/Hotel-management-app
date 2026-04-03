package com.likith.PhoenixHotel.service.impl;

import com.amazonaws.Response;
import com.likith.PhoenixHotel.dto.ResponseDTO;
import com.likith.PhoenixHotel.dto.RoomDTO;
import com.likith.PhoenixHotel.entity.Room;
import com.likith.PhoenixHotel.exception.OurException;
import com.likith.PhoenixHotel.repo.BookingRepository;
import com.likith.PhoenixHotel.repo.RoomRepository;
import com.likith.PhoenixHotel.repo.UserRepository;
import com.likith.PhoenixHotel.service.AwsS3Service;
import com.likith.PhoenixHotel.service.interfac.RoomService;
import com.likith.PhoenixHotel.service.interfac.UserService;
import com.likith.PhoenixHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AwsS3Service awsS3Service;

    @Override
    public ResponseDTO addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {

        ResponseDTO response=new ResponseDTO();
        try {
            String imageUrl = photo.getOriginalFilename();
            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);
            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setMessage("successfully added new room");
            response.setRoom(roomDTO);
        }

        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room "+e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {

        List<String> roomTypeList=roomRepository.findDistinctRoomTypes();
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public ResponseDTO getAllRooms() {
        ResponseDTO response=new ResponseDTO();
        try{
            List<Room> roomList=roomRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            List<RoomDTO> roomDTOList=Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("successfully added new room");
            response.setRoomList(roomDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room "+e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO deleteRoom(Long roomId) {
        ResponseDTO response=new ResponseDTO();
        try{
            roomRepository.findById(roomId).orElseThrow(()->new OurException("Room not found!"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("successfully deleted room");

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage("Error deleting room "+e.getMessage());
        }

        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room "+e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        ResponseDTO response=new ResponseDTO();
        try{
            String imageUrl=null;
            if(photo!=null && !photo.isEmpty()){
                imageUrl=awsS3Service.saveImageToS3(photo);
            }
            Room room=roomRepository.findById(roomId).orElseThrow(()->new OurException("Room not found!"));
            if(roomType!=null) room.setRoomType(roomType);
            if(roomPrice!=null) room.setRoomPrice(roomPrice);
            if(description!=null) room.setRoomDescription(roomType);
            if(imageUrl!=null) room.setRoomPhotoUrl(roomType);

            Room updatedRoom=roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("successfully deleted room");
            response.setRoom(roomDTO);

        }catch(OurException e){
            response.setStatusCode(404);
            response.setMessage("Error updating room "+e.getMessage());
        }

        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room "+e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getRoomById(Long roomId) {
        ResponseDTO response=new ResponseDTO();
        try {
            roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not found!"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(roomRepository.findById(roomId).get());
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("Error deleting room " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        ResponseDTO response=new ResponseDTO();
        try {
            List<Room> availableRooms=roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate,checkOutDate);
            List<RoomDTO> roomDTOList=Utils.mapRoomListEntityToRoomListDTO(availableRooms);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("Error" + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error" + e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getAllAvailableRooms() {
        ResponseDTO response=new ResponseDTO();
        try {
            List<Room> roomList=roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList=Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("Error" + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error" + e.getMessage());
        }
        return response;
    }
}
