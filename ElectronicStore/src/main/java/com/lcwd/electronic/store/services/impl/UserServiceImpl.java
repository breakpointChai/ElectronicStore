package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;
import net.bytebuddy.TypeCache;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${user.profile.image.path}")
    private String imagePath;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDto createUser(UserDto userDto) {

        //generate unique id in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);

        // dto->entity
        User user = dtoToEntity(userDto);
        //password encode
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //get the normal role

        Role role = new Role();
        role.setRoleId(UUID.randomUUID().toString());
        role.setName("ROLE_" + AppConstants.ROLE_NORMAL);

        Role roleNormal = roleRepository.findByName("ROLE_" + AppConstants.ROLE_NORMAL).orElse(role);

        user.setRoles(List.of(roleNormal));
        User savedUser = userRepository.save(user);
        //entity -> dto
        UserDto newDto = entityToDto(savedUser);
        return newDto;
    }




    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));
        user.setName(userDto.getName());
        //email update
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setImageName(userDto.getImageName());

        // assign normal role to user
        //by detail jo bhi api se user banega usko ham  log normal user banayenge

        //save data
        User updatedUser = userRepository.save(user);
        UserDto updatedDto = entityToDto(updatedUser);
        return updatedDto;
    }


    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found by id: " + userId));

        // delete user profile image
        //images/users/abc.png
        String fullPath = imagePath + user.getImageName();
      try {
          Path path= Paths.get(fullPath);
          Files.delete(path);
      }
      catch (NoSuchFileException ex){
          logger.info("User image not found in folder");
          ex.printStackTrace();
      }
      catch (IOException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
      }

      //delete user
        userRepository.delete(user);

    }


    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<User> page = userRepository.findAll(pageable);

        /*
        List<User> users = page.getContent();
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        PageableResponse<UserDto> response=new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalElements(page.getTotalElements());
        response.setLastPage(page.isLast());
        response.setTotalPages(page.getTotalPages());
        return response;

         */

        //yha agr above dkhe to very hacktick hai becz
        //manually kb tk yha response me set krte rhenge
        //to make it easy make another helper fuction jha ye set vala function implement kr de
        //make reusable code


        //make it easy
        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
        return response;

    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found by id: " + userId));

        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found by email: " + email));

        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

        return dtoList ;
    }



    private User dtoToEntity(UserDto userDto) {
        //manually we are converting
//        User user=User.builder()
//                .userId(userDto.getUserId())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .name(userDto.getName())
//                .about(userDto.getAbout())
//                .imageName(userDto.getImageName())
//                .gender(userDto.getGender()).build();
//        return user;

        //by using model mapper we do it easly
        return modelMapper.map(userDto, User.class);
    }

    private UserDto entityToDto(User savedUser) {

        //manually we are converting
//        UserDto userDto=UserDto.builder()
//                .userId(savedUser.getUserId())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .name(savedUser.getName())
//                .about(savedUser.getAbout())
//                .imageName(savedUser.getImageName())
//                .gender(savedUser.getGender()).build();
//        return userDto;

        //using model mapper we do it easy
        return  modelMapper.map(savedUser, UserDto.class);


    }
}
