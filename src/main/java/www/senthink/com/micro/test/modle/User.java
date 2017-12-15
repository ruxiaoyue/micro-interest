package www.senthink.com.micro.test.modle;

import io.vertx.core.json.JsonObject;

/**
 * Created by hyacinth on 2017/12/12.
 */
public class User {

    private String username;

    private String password;

    private String account;

    private Integer registration;



    public User() {}

    /**
     * 由json构造User
     * @param body
     */
    public User(JsonObject body) {
        if (body.getValue("username") instanceof String) {
            this.setUsername((String)body.getValue("username"));
        }
        if (body.getValue("password") instanceof String) {
            this.setPassword((String) body.getValue("password"));
        }
        if (body.getValue("account") instanceof String) {
            this.setAccount((String) body.getValue("account"));
        }
        if (body.getValue("registration") instanceof Integer) {
            this.setRegistration((Integer) body.getValue("registration"));
        }
    }

    /**
     * 转换为json对象
     * @return
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        if (this.getUsername() != null) {
            json.put("username", this.getUsername());
        }
        if (this.getPassword() != null) {
            json.put("password", this.getPassword());
        }
        if (this.getAccount() != null) {
            json.put("account", this.getAccount());
        }
        if (this.getRegistration() != null) {
            json.put("registration", this.getRegistration());
        }
        return json;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getRegistration() {
        return registration;
    }

    public void setRegistration(Integer registration) {
        this.registration = registration;
    }
}
