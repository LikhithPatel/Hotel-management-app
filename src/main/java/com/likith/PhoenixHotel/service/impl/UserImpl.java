package com.likith.PhoenixHotel.service.impl;

import com.likith.PhoenixHotel.dto.LoginRequest;
import com.likith.PhoenixHotel.dto.ResponseDTO;
import com.likith.PhoenixHotel.dto.UserDTO;
import com.likith.PhoenixHotel.entity.User;
import com.likith.PhoenixHotel.exception.OurException;
import com.likith.PhoenixHotel.repo.BookingRepository;
import com.likith.PhoenixHotel.repo.UserRepository;
import com.likith.PhoenixHotel.service.interfac.UserService;
import com.likith.PhoenixHotel.utils.JWTUtils;
import com.likith.PhoenixHotel.utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public ResponseDTO register(User user) {
        ResponseDTO response=new ResponseDTO();
        try{
            if(user.getRole()==null|| user.getRole().isBlank()){
                user.setRole("ROLE_USER");
            }

            if(userRepository.existsByEmail(user.getEmail())){
                throw new OurException(user.getEmail()+" already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser=userRepository.save(user);
            UserDTO userDTO= Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);

        }catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured during user registration "+e.getMessage());
        }
        return response ;
    }

    @Override
    public ResponseDTO login(LoginRequest loginRequest) {
        ResponseDTO response=new ResponseDTO();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user=userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new OurException("User not found!"));
            var jwt=jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("Successful");

        }catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occured during user registration "+e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getAllUsers() {
        ResponseDTO response=new ResponseDTO();
        try{
            List<User> userList=userRepository.findAll();
            List<UserDTO> userDTOList=Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUserList(userDTOList);
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all users "+e.getMessage());
        }
        return response ;
    }

    @Override
    public ResponseDTO getUserBookingHistory(String userId) {

        ResponseDTO response=new ResponseDTO();
        try{
            User user=userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User not found!"));
            UserDTO userDTO=Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDTO);
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting booking history "+e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO deleteUser(String userId) {

        ResponseDTO response=new ResponseDTO();
        try{
            userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User not found!"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("Successful");
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting booking history "+e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDTO getUserById(String userId) {

        ResponseDTO response=new ResponseDTO();
        try{
            User user=userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User not found!"));
            UserDTO userDTO=Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDTO);
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting booking history "+e.getMessage());
        }
        return response;
    }


    @Override
    public ResponseDTO getMyInfo(String email) {

        ResponseDTO response=new ResponseDTO();
        try{
            User user=userRepository.findByEmail(email).orElseThrow(()->new OurException("User not found!"));
            UserDTO userDTO=Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDTO);
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting booking history "+e.getMessage());
        }
        return response;

    }
}
