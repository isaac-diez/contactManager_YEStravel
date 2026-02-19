package com.yestravel.contactsmanager.dto;

import java.time.LocalDate;
import java.util.List;

public class ContactFormDTO {
    private Long id;
    private String familyNameEng;
    private String firstNameEng;
    private String familyNameCh;
    private String firstNameCh;
    private String mobilePhone;
    private String email;
    private boolean birthdayReminder;
    private LocalDate birthDate;
    private List<UserDisplayDTO> potentialOwners;
}
