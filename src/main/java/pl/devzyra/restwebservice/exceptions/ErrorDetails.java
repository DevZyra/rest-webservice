package pl.devzyra.restwebservice.exceptions;

import lombok.*;

import java.util.Date;

@Getter
@Setter
public class ErrorDetails {

    private Date timestamp;
    private String message;

    public ErrorDetails(Date timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }
}
