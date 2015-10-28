package activr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;

/**
 *
 * Should consider making this an abstract class(maybe interface but likely not) so different types of 'interests' can exist
 * i.e - one time thing looking for someone for time between x and y vs. just always looking to find someone.
 *
 *
 * Created by Nic on 4/14/2015.
 */

@Entity
public class Interest implements Serializable{

    //This bidirectional relationship is not a good thing but its the only way I could get JPA to play nice.
    //I haven't used JPA before and it might just be simpler to run straight JDBC and write the SQL ourselves...
    @JsonIgnore
    @ManyToOne
    private Account account;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Activity activity;

    private Integer skill;

    private Boolean isPrimary;

    public Interest() {
        //For jpa
    }

    public Interest(Account account, Activity activity, Integer skill, boolean isPrimary) {
        this.activity = activity;
        this.skill = skill;
        this.account = account;
        this.isPrimary = isPrimary;
    }

    public Account getAccount() { return account; }

    public void setAccount(Account account) { this.account = account; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Activity getActivity() { return activity; }

    public void setActivity(Activity activity) { this.activity = activity; }

    public Integer getSkill() { return skill; }

    public void setSkill(Integer skill) { this.skill = skill; }

    public Boolean isPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
