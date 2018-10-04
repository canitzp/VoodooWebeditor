package de.canitzp.voodoowebeditor.internal.user;

public class User{
    
    private String username;
    private String email;
    private String icon = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/User_font_awesome.svg/240px-User_font_awesome.svg.png";
    private String pwdHash; // SHA-256
    
    // DigestUtils.sha256Hex(String data)
    public boolean arePasswordsTheSame(String otherPwdHas){
        return otherPwdHas.equals(pwdHash);
    }
    
    public User setUsername(String username){
        this.username = username;
        return this;
    }
    
    public User setEmail(String email){
        this.email = email;
        return this;
    }
    
    public User setIcon(String icon){
        this.icon = icon;
        return this;
    }
    
    public User setPwdHash(String pwdHash){
        this.pwdHash = pwdHash;
        return this;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getFileNameSaveUsername(){
        return this.username.replaceAll("[^a-zA-z]", "_");
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getIcon(){
        return icon;
    }
}
