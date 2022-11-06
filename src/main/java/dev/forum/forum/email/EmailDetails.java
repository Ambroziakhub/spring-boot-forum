package dev.forum.forum.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class EmailDetails {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}