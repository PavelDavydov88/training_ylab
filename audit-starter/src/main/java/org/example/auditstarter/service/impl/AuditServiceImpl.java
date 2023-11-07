//package org.example.auditstarter.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.example.auditstarter.repository.AuditRepository;
//import org.example.auditstarter.service.AuditService;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.stereotype.Component;
//
//import java.sql.SQLException;
//import java.util.List;
//
///**
// * Класс предоставляет сервис аудита игрока
// */
//@Component
//@RequiredArgsConstructor
//@ConditionalOnMissingBean
//public class AuditServiceImpl implements AuditService {
//
//    private final AuditRepository auditRepository;
//
//    /**
//     * Метод отправляет события для записи в репозиторий
//     *
//     * @param playerId     ID игрока
//     * @param auditMessage текст события
//     * @throws SQLException
//     */
//    @Override
//    public void sendEvent(long playerId, String auditMessage) throws SQLException {
//        auditRepository.save(playerId, auditMessage);
//    }
//
//    /**
//     * Метод для получения аудита действий игрока
//     *
//     * @param idPlayer токен игрока
//     * @return лист операций игрока
//     */
//    @Override
//    public List<String> getListAuditAction(long idPlayer) throws Exception {
//        return auditRepository.findAllById(idPlayer);
//    }
//}
