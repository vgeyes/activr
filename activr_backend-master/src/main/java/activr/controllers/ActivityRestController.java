package activr.controllers;

import activr.model.Activity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Created by nicholas on 4/22/15.
 *
 * Make sure the front end stays up to date with all the awesome activities we support
 *
 */
@RestController
@RequestMapping("/activities")
public class ActivityRestController {

    @RequestMapping(method = RequestMethod.GET)
    public Activity[] get(){
        return Activity.values();
    }
}
