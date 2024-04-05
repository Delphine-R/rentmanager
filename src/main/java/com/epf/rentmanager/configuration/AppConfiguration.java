package com.epf.rentmanager.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
@ComponentScan({"com.epf.rentmanager.service","com.epf.rentmanager.dao", "com.epf.rentmanager.persistence"})
public class AppConfiguration {
}

