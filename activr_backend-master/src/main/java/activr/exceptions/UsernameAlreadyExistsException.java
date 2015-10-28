package activr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Nic on 4/25/2015.
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username){ super("Username already exists: '" + username + "'."); }
}
