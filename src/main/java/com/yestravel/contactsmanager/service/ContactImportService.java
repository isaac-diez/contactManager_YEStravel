package com.yestravel.contactsmanager.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ContactImportService {
    public void importContacts(MultipartFile file, String ownerName) throws IOException;
}
