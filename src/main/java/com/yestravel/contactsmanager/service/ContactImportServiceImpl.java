package com.yestravel.contactsmanager.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // ¡Nuevo!
import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.model.User;
import com.yestravel.contactsmanager.repo.ContactRepo;
import com.yestravel.contactsmanager.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ContactImportServiceImpl implements ContactImportService {

    private final ContactRepo contactRepo;
    private final UserRepo userRepo;

    public ContactImportServiceImpl(ContactRepo contactRepo, UserRepo userRepo) {

        this.contactRepo = contactRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public void importContacts(MultipartFile file, String ownerName) throws IOException {

        User owner = userRepo.findByUsername(ownerName)
                .orElseThrow(() -> new IllegalArgumentException("Owner user not found: " + ownerName));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("Only CSV files are allowed");
        }

        CsvMapper mapper = new CsvMapper();
        mapper.registerModule(new JavaTimeModule());

        CsvSchema schema = CsvSchema.builder()
                .addColumn("familyNameEng")
                .addColumn("firstNameEng")
                .addColumn("familyNameCh")
                .addColumn("firstNameCh")
                .addColumn("mobilePhone")
                .addColumn("email")
                .addColumn("birthdayReminder")
                .addColumn("birthDate")
                .setUseHeader(true)
                .setReorderColumns(true)
                .build();

        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {

            MappingIterator<Contact> it = mapper.readerFor(Contact.class)
                    .with(schema)
                    .readValues(reader);

            List<Contact> contacts = it.readAll();

            contacts.forEach(c -> {
                c.setId(null);
                c.setUser(owner);
            });

            contactRepo.saveAll(contacts);
        }
    }
}