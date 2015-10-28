package activr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Nic on 4/30/2015.
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidUsernameException extends RuntimeException {
    public InvalidUsernameException(String reason){
        super("Invalid username: " + reason);
    }
}
