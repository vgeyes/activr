package activr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Nic on 5/1/2015.
 */
@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoMatchFoundException extends RuntimeException {

    public NoMatchFoundException(String userId){
        super("No potential matches found for the user: " + userId);
    }
}
