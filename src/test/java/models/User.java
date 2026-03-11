package models;

public class User {

    private String name;
    private String username;
    private String email;
    private Address address;
    private String phone;
    private String website;

    public User(String name, String username, String email, String street, String city, String phone, String website) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = new Address(street,city);
        this.phone = phone;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public static class Address{
        private String street;
        private String city;

        public Address(String street, String city) {
            this.street = street;
            this.city = city;
        }

        public String getStreet() {
            return street;
        }

        public String getCity() {
            return city;
        }
    }
}
