package pl.devzyra.restwebservice.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationStatus {

    private String method;
    private String status;

}
