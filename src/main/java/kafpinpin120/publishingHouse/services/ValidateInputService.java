package kafpinpin120.publishingHouse.services;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;



@Service
public class ValidateInputService {

    public String getErrors(BindingResult bindingResult){

        return bindingResult
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList()
                .toString();
    }
}
