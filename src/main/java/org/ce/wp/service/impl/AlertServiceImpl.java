package org.ce.wp.service.impl;

import org.ce.wp.dto.AlertReportResponseDto;
import org.ce.wp.entity.Terminal;
import org.ce.wp.entity.Url;
import org.ce.wp.exception.CredentialsException;
import org.ce.wp.exception.InvalidUrlIdException;
import org.ce.wp.repository.TerminalRepository;
import org.ce.wp.repository.UrlRepository;
import org.ce.wp.service.AlertService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Arourin Nazarian
 * @since 22.01.23
 */
@Service
public class AlertServiceImpl implements AlertService {
    private final TerminalRepository terminalRepository;
    private final UrlRepository urlRepository;
    private final Date requestTime;

    public AlertServiceImpl(TerminalRepository terminalRepository, UrlRepository urlRepository) {
        this.terminalRepository = terminalRepository;
        this.urlRepository = urlRepository;
        this.requestTime = new Date();
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AlertReportResponseDto getAlertReport(String urlId, String username) throws InvalidUrlIdException, CredentialsException {
        Optional<Url> urlOptional = urlRepository.findById(Long.parseLong(urlId));
        if (urlOptional.isEmpty()) {
            throw new InvalidUrlIdException("Invalid Url Id:" + urlId);
        }
        if (!urlOptional.get().getUser().getUsername().equals(username)) {
            throw new CredentialsException("UrlId :" + urlId + "not belong to username: " + username);
        }
        List<Terminal> requests = terminalRepository.findAllByUrlAndRequestTimeAfter(urlOptional.get(), requestTime);
        AtomicInteger alertCount = new AtomicInteger(0);
        AtomicInteger unsuccessful = new AtomicInteger(0);
        requests.forEach(terminal -> {
            if (Objects.isNull(terminal.getStatusCode()) || terminal.getStatusCode() % 100 != 2) {
                unsuccessful.getAndIncrement();
            }
            if (unsuccessful.get() >= urlOptional.get().getThreshold()) {
                alertCount.getAndIncrement();
                unsuccessful.set(0);
            }
        });
        return new AlertReportResponseDto(alertCount.get(), urlOptional.get());
    }
}
