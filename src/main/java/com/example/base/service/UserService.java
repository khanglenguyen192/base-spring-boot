package com.example.base.service;

import java.util.List;

public interface UserService {
    List<String> getRolesByMail(String email);
}
