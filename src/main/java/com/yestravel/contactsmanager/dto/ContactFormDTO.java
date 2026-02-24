package com.yestravel.contactsmanager.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class ContactFormDTO {
    private Long id;
    private String familyNameEng;
    private String firstNameEng;
    private String familyNameCh;
    private String firstNameCh;
    private String mobilePhone;
    private String email;
    private boolean birthdayReminder;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private Long userId;
    private String userName;

    private List<UserDisplayDTO> potentialOwners;
}
