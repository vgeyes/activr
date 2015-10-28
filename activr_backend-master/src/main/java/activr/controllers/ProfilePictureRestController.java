package activr.controllers;

import activr.exceptions.ForbiddenException;
import activr.exceptions.UploadException;
import activr.exceptions.UserNotFoundException;
import activr.model.Account;
import activr.repository.AccountRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.util.Optional;

/**
 * Created by Nic on 5/7/2015.
 */
@RestController
@RequestMapping("/{userId}/profilePicture")
public class ProfilePictureRestController {

    private AccountRepository accountRepository;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> update(@PathVariable String userId, @RequestParam("file") MultipartFile file, OAuth2Authentication auth) {
        byte[] bytes;
        File newFile;
        String extension;
        Optional<Account> account;
        if(!userId.equals(auth.getName())) {
            throw new ForbiddenException(auth.getName());
        }
        account = accountRepository.findByUsername(userId);

        if(!account.isPresent()){
            throw new UserNotFoundException(userId);
        }

        extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if(!file.isEmpty()) {
            try {
                newFile = new File("/home/maki/activr/images/profilePics/" + userId + "/profilePicture." + extension);
                System.out.println(newFile.getAbsolutePath());
                if(!newFile.exists()) {
                    newFile.getParentFile().mkdirs();
                    newFile.createNewFile();
                }
                bytes = file.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(newFile));
                stream.write(bytes);
                stream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new UploadException(file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                throw new UploadException(file.getOriginalFilename());
            }
        }

        //Respond with the current state of the account to keep the front end up to date just in case they forgot what they just told me.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/public/{userId}/profilePicture." + extension)
                .buildAndExpand(userId).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }

    @Autowired
    public ProfilePictureRestController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
