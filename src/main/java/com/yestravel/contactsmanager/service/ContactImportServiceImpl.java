package com.yestravel.contactsmanager.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // ¡Nuevo!
import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.repo.ContactRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ContactImportServiceImpl implements ContactImportService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    @Transactional
    public void importContacts(MultipartFile file) throws IOException {

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

            contacts.forEach(c -> c.setId(null));

            contactRepo.saveAll(contacts);
        }
    }
}