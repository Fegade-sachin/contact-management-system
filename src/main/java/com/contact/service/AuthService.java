
package com.contact.service;

import com.contact.dto.request.RegisterRequest;
import com.contact.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);
}
