package com.ledgerly.app.expense_tracker.service.aiScanner;

import org.springframework.web.multipart.MultipartFile;

public interface AIScannerService {
    String scanReceipt(MultipartFile file);
}
