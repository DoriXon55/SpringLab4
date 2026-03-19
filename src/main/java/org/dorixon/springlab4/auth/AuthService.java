package org.dorixon.springlab4.auth;

import org.dorixon.springlab4.model.Student;

public interface AuthService {
    void register(Student student);
    Tokens authenticate(Credentials credentials);
    Tokens refreshTokens(Tokens tokens);
}
