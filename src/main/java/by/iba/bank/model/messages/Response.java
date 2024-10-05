package by.iba.bank.model.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private ResponseStatus responseStatus;
    private String responseMessage;
    private String responseData;
}
