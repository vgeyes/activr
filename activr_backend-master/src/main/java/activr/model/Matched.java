package activr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by Nic on 4/30/2015.
 */
@Entity
public class Matched {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Account account1;

    @ManyToOne
    @JoinColumn
    private Account account2;

    @Enumerated(EnumType.STRING)
    private Activity activity;

    private Boolean accepted;

    public Matched() {

    }

    public Matched(Account account1, Account account2, Activity activity, Boolean accepted) {
        this.account1 = account1;
        this.account2 = account2;
        this.activity = activity;
        this.accepted = accepted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getAccount2() {
        return account2;
    }

    public void setAccount2(Account account2) {
        this.account2 = account2;
    }

    public Account getAccount1() {
        return account1;
    }

    public void setAccount1(Account account1) {
        this.account1 = account1;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
