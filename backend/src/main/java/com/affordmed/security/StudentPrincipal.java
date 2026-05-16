package com.affordmed.security;

import java.io.Serializable;

import lombok.Value;

@Value
public class StudentPrincipal implements Serializable {
    String studentId;
}
