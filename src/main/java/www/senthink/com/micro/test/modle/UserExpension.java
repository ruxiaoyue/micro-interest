package www.senthink.com.micro.test.modle;

/**
 * Created by hyacinth on 2017/12/19.
 */
public class UserExpension {

    /**
     * 用户名称
     */
    private String username;
    /**
     * 最近登录时间
     */
    private String lastLoginTime;
    /**
    创建时间
     */
    private String createTime;
    /**
     * 邮箱验证码
     */
    private String code;
    /**
     * 邮箱的有效时间
     */
    private Long activeTime;

    /**
     * 昵称
     */
    private String nickName;
    /**
     * 兴趣
     */
    private String interest;
    /**
     * 地址
     */
    private String address;
    /**
     * 婚姻
     */
    private String marriage;
    /**
     * 性别
     */
    private String sex;
    /**
     * 电话
     */
    private String phone;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 邮箱
     */
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
    }
}
