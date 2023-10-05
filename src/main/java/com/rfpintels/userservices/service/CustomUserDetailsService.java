/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rfpintels.userservices.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rfpintels.userservices.model.User;
import com.rfpintels.userservices.model.CustomUserDetails;
import com.rfpintels.userservices.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> dbUser = userRepository.findByEmail(email);
        LOGGER.info("Fetched user : " + dbUser + " by " + email);
        return dbUser.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user email in the database for " + email));
    }

    public UserDetails loadUserByEmailId(String email) {
        Optional<User> dbUser = userRepository.findByEmail(email);
        LOGGER.info("Fetched user : " + dbUser + " by " + email);
        return dbUser.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user id in the database for " + email));
    }
}
