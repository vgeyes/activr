package activr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Nic on 5/7/2015.
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UploadException extends RuntimeException {

    public UploadException(String uploadName){
        super("Upload of " + uploadName + " failed.");
    }
}
