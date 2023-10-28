package service;

import aop.annotations.Audit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import repository.HistoryCreditDebitRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс предоставляет сервис хранения операций со счетом игрока
 */
@RequiredArgsConstructor
@Slf4j
public class HistoryCreditDebitServiceImpl implements HistoryCreditDebitService {

    private final AuthService authService;
    private final HistoryCreditDebitRepository historyCreditDebitRepository;

    /**
     * Метод отправляет действие со счетом игрока для записи в репозиторий
     *
     * @param playerId
     * @param historyText
     * @throws SQLException
     */
    @Override
    public void sendHistory(long playerId, String historyText) throws SQLException {
        historyCreditDebitRepository.save(playerId, historyText);
    }


    /**
     * Метод для получения истории пополнения/снятия средств игроком
     *
     * @param token токен игрока
     * @return лист операций игрока
     */
    @Audit(success = "operation request history of credit/debit operations")
    @Override
    public List<String> getListOperationAccount(String token) throws Exception {
        if (authService.find(token).isEmpty()) {
            log.info("invalid token");
            throw new Exception("invalid token");
        }
        int id = Integer.parseInt(authService.decodeJWT(token).getId());
        try {
            return historyCreditDebitRepository.findById(id);
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
}
