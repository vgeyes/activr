package activr.model;


import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nic on 4/14/2015.
 */
@Entity
public class Account implements Serializable {

    @OneToMany (mappedBy = "account")
    private Set<Interest> interests = new HashSet<Interest>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @NotNull
    private String password;

    @Column(unique=true)
    @NotNull
    private String username;

    private String f_name;

    private String l_name;

    private Gender gender;

    private String phoneNumber;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name= "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<>();

    public Account() {}

    public Account(String name, String password, Set<Role> roles) {
        this.username = name;
        this.password = password;
        this.roles    = roles;
    }

    public Account(String name, String password, String f_name, String l_name, Gender gender,String phoneNumber, Set<Role> roles) {
        this.username = name;
        this.password = password;
        this.f_name = f_name;
        this.l_name = l_name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.roles    = roles;
    }

    public Account(Account account) {
        this.id        = account.id;
        this.password  = account.password;
        this.username  = account.username;
        this.interests = account.interests;

    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Set<Interest> getInterests() { return interests; }

    public long getId() { return id; }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
