package model;


import java.util.LinkedList;
import java.util.List;

/**
 * Data class for player
 */

public class Player {
    private long account;
    private String name, password;
    private long id;
    private List<String> listOperationAccount, listAuditAction;

    public Player(String name, String password, long id) {
        this.account = 0;
        this.name = name;
        this.id = id;
        this.password = password;
        this.listOperationAccount = new LinkedList<>();
        this.listAuditAction =  new LinkedList<>();

    }

    public long getAccount() {
        return account;
    }

    public void setAccount(long account) {
        this.account = account;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getListOperationAccount() {
        return listOperationAccount;
    }

    public void setListOperationAccount(List<String> listOperationAccount) {
        this.listOperationAccount = listOperationAccount;
    }

    public List<String> getListAuditAction() {
        return listAuditAction;
    }

    public void setListAuditAction(List<String> listAuditAction) {
        this.listAuditAction = listAuditAction;
    }

    @Override
    public String toString() {
        return "id=" + this.id + ", name=" + this.name + ", account=" + this.account;
    }
}
