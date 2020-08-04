package pl.devzyra.restwebservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.devzyra.restwebservice.model.entities.UserEntity;
import pl.devzyra.restwebservice.model.entities.VerificationTokenEntity;
import pl.devzyra.restwebservice.services.UserService;
import pl.devzyra.restwebservice.services.VerificationTokenService;

import java.sql.Timestamp;

@Controller
@RequestMapping("/activation")
public class ActivationController {

    private final VerificationTokenService verificationTokenService;
    private final UserService userService;

    public ActivationController(VerificationTokenService verificationTokenService, UserService userService) {
        this.verificationTokenService = verificationTokenService;
        this.userService = userService;
    }

    @GetMapping
public String activation(@RequestParam("token") String token, Model model){

        VerificationTokenEntity verificationToken = verificationTokenService.findByToken(token);
        if(verificationToken == null){
            model.addAttribute("message","Your verification token is not confirmed");
        }else{

            UserEntity userEntity = verificationToken.getUserEntity();

            if(userEntity.getEmailVerificationStatus() == false){

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                if(verificationToken.getExpirationDate().before(timestamp)){
                    model.addAttribute("message","Token has expired");
                }else{
                    userEntity.setEmailVerificationStatus(true);
                    userService.save(userEntity);
                    model.addAttribute("message","Your email has been confirmed");
                }
            }else{
                model.addAttribute("message","Your account is already activated");
            }

        }

    return "activation";
}


}
