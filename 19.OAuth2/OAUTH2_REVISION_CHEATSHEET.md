# OAuth2 Revision Cheat Sheet for Spring Boot

This file is a one-place revision guide for the OAuth2 work in `19.OAuth2`.

It combines:

- OAuth 2.0 core theory
- OpenID Connect basics
- Spring Security OAuth2 Login usage
- Google and GitHub login examples
- commands, config, interview points, and security notes

It is written as a fast revision sheet, but with enough depth to actually understand what is happening.

---

## 1. What OAuth2 Is

OAuth 2.0 is an authorization framework.

Its main goal is:

- let an application access a user's protected resources
- without sharing the user's password with that application

Example:

- you click `Login with Google`
- your app redirects you to Google
- Google authenticates you
- Google asks for consent
- Google sends your app an authorization code
- your app exchanges that code for tokens
- your app uses those tokens to identify you or call APIs

Important:

- OAuth2 is mainly about authorization
- OpenID Connect adds authentication on top of OAuth2

That is why:

- `Login with Google` is usually OpenID Connect on top of OAuth2
- `Login with GitHub` is commonly plain OAuth2 login

---

## 2. What Problem OAuth2 Solves

Without OAuth2:

- app asks user for Google password
- app stores or handles password directly
- this is insecure and a bad design

With OAuth2:

- user logs in only at the provider
- provider gives limited tokens to the app
- app gets only approved access

This gives:

- better security
- scoped access
- revocation support
- less password exposure

---

## 3. Core Roles You Must Remember

OAuth2 has 4 main roles.

### 1. Resource Owner

The user.

Example:

- you, logging in with your Google account

### 2. Client

The application requesting access.

Example:

- your Spring Boot app

### 3. Authorization Server

The server that authenticates the user and issues tokens.

Example:

- Google authorization server
- GitHub authorization server

### 4. Resource Server

The server that hosts protected user data or APIs.

Example:

- Google People API
- GitHub API

In some systems, authorization server and resource server are separate. In others, they belong to the same provider.

---

## 4. Important Terms

### Access Token

A credential used to call protected APIs.

Example:

```http
Authorization: Bearer eyJ...
```

### Refresh Token

Used to obtain a new access token without asking the user to log in again.

### Authorization Code

A short-lived code returned to the client after user approval.

It is exchanged for tokens at the token endpoint.

### Scope

Defines what access is being requested.

Examples:

- `openid`
- `profile`
- `email`
- `repo`
- `user:email`

### Redirect URI

The callback URL where the provider sends the user after authorization.

Spring default for OAuth2 login:

```text
{baseUrl}/login/oauth2/code/{registrationId}
```

Example:

```text
http://localhost:8080/login/oauth2/code/google
http://localhost:8080/login/oauth2/code/github
```

### Client ID

Public identifier of your app.

### Client Secret

Secret known only to the provider and your backend app.

Never commit this to GitHub.

---

## 5. OAuth2 vs OpenID Connect

This is one of the most important interview and revision points.

### OAuth2

- solves authorization
- gives access tokens
- tells what the app may access

### OpenID Connect

- identity layer on top of OAuth2
- solves login and user identity
- adds `id_token` and standard user identity claims

### Easy Memory Trick

- OAuth2 = "Can this app access something?"
- OIDC = "Who is this user?"

### Why Google Login Feels Different

Google login commonly uses OIDC.

That means it can return:

- access token
- ID token
- user identity claims like name, email, subject

GitHub OAuth login is commonly handled as OAuth2 login, not full OIDC in the classic OAuth app flow.

---

## 6. OAuth2 Grant Types You Should Know

Not every grant type is equally recommended today.

### 1. Authorization Code Grant

Most important for server-side web apps.

Used by:

- Spring Boot web apps
- `Login with Google`
- `Login with GitHub`

Flow:

1. user is redirected to provider
2. provider authenticates user
3. provider returns authorization code
4. backend exchanges code for token

This is the main flow for your project.

### 2. Authorization Code + PKCE

Best practice for public clients and now widely recommended.

PKCE protects the authorization code flow from code interception.

Usually required for:

- mobile apps
- SPA/public clients
- native apps

### 3. Client Credentials Grant

Used when the app talks to another service as itself, not on behalf of a user.

Example:

- microservice to microservice communication

### 4. Refresh Token Grant

Used to obtain a new access token using a refresh token.

### 5. Device Authorization Grant

Used for devices with limited input.

Example:

- TV apps
- CLI devices

### 6. Resource Owner Password Credentials Grant

Old and discouraged for modern systems.

Avoid in new applications.

### 7. Implicit Grant

Historically used in browsers.

Now largely discouraged in modern secure applications.

---

## 7. The Authorization Code Flow Step by Step

This is the flow behind Spring Security OAuth2 login.

### Step 1. User clicks login

Example URLs:

```text
/oauth2/authorization/google
/oauth2/authorization/github
```

### Step 2. Spring redirects to provider

Example authorize request shape:

```http
GET /authorize?
 response_type=code
 &client_id=...
 &scope=openid%20profile%20email
 &redirect_uri=http://localhost:8080/login/oauth2/code/google
 &state=abc123
```

### Step 3. Provider authenticates user

The user logs in at Google or GitHub.

### Step 4. Provider asks for consent

The user approves scopes.

### Step 5. Provider redirects back with code

Example:

```text
http://localhost:8080/login/oauth2/code/google?code=4%2F0AQ...&state=abc123
```

### Step 6. Backend exchanges code for token

This happens server-to-server.

Example token request shape:

```http
POST /token
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code
&code=...
&redirect_uri=http://localhost:8080/login/oauth2/code/google
&client_id=...
&client_secret=...
```

### Step 7. Provider returns tokens

Example response shape:

```json
{
  "access_token": "ya29....",
  "expires_in": 3599,
  "scope": "openid profile email",
  "token_type": "Bearer",
  "id_token": "eyJ..."
}
```

### Step 8. Spring Security creates authenticated session/security context

Your app now treats the user as logged in.

---

## 8. Where Spring Security Fits

In your project, this class enables OAuth2 login:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults());

        return httpSecurity.build();
    }
}
```

Meaning:

- every request needs authentication
- Spring Security provides OAuth2 login support
- if an unauthenticated user tries to access `/`, Spring redirects to login

Important built-in behavior:

- Spring handles the redirect to provider
- Spring handles callback processing
- Spring exchanges code for tokens
- Spring creates the authenticated principal

---

## 9. Dependencies for Spring Boot

Your project already uses:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security-oauth2-client</artifactId>
</dependency>
```

This starter gives you:

- Spring Security
- OAuth2 client support
- OAuth2 login support
- provider/client registration support

---

## 10. Minimal `application.properties` Setup

### Google Login Example

```properties
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=openid,profile,email
```

### GitHub Login Example

```properties
spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
spring.security.oauth2.client.registration.github.scope=read:user,user:email
```

Important:

- use environment variables
- do not hardcode secrets in `application.properties`
- do not commit downloaded client secret JSON files

---

## 11. Environment Variables

### Windows PowerShell

Temporary for current terminal:

```powershell
$env:GOOGLE_CLIENT_ID="your-google-client-id"
$env:GOOGLE_CLIENT_SECRET="your-google-client-secret"
$env:GITHUB_CLIENT_ID="your-github-client-id"
$env:GITHUB_CLIENT_SECRET="your-github-client-secret"
```

### Windows Permanent

```powershell
setx GOOGLE_CLIENT_ID "your-google-client-id"
setx GOOGLE_CLIENT_SECRET "your-google-client-secret"
setx GITHUB_CLIENT_ID "your-github-client-id"
setx GITHUB_CLIENT_SECRET "your-github-client-secret"
```

Restart the terminal after `setx`.

### macOS/Linux

```bash
export GOOGLE_CLIENT_ID="your-google-client-id"
export GOOGLE_CLIENT_SECRET="your-google-client-secret"
export GITHUB_CLIENT_ID="your-github-client-id"
export GITHUB_CLIENT_SECRET="your-github-client-secret"
```

---

## 12. Provider Setup: Google

### What Google Uses

Google login is typically OpenID Connect on top of OAuth2.

### Typical scopes

- `openid`
- `profile`
- `email`

### Common redirect URI

```text
http://localhost:8080/login/oauth2/code/google
```

### Setup Steps

1. Create OAuth client in Google Cloud Console
2. Choose web application
3. Add authorized redirect URI
4. copy client ID and client secret
5. place them in environment variables

### Common Errors

- `redirect_uri_mismatch`
- invalid client secret
- using wrong Google Cloud project
- not enabling correct consent screen configuration

---

## 13. Provider Setup: GitHub

### What GitHub Uses

GitHub OAuth apps support the authorization code flow and device flow.

For your login demo, use authorization code flow.

### Typical scopes

- `read:user`
- `user:email`

If you need repo access:

- `repo`

### Common callback URL

```text
http://localhost:8080/login/oauth2/code/github
```

### Setup Steps

1. Go to GitHub Developer Settings
2. Create OAuth App
3. Set homepage URL
4. set authorization callback URL
5. copy client ID and client secret
6. place them in environment variables

---

## 14. The Most Important Endpoints

### Authorization Endpoint

Used to start login/consent.

Examples:

- Google authorization endpoint is provider-managed
- GitHub uses an authorization endpoint for app authorization

### Token Endpoint

Used by backend to exchange code for tokens.

### UserInfo Endpoint

Often used to fetch user profile.

### JWK Set URI

Used in OIDC/JWT-based systems to validate token signatures.

---

## 15. Common OAuth2 Request Parameters

### Authorization Request Parameters

- `response_type=code`
- `client_id`
- `redirect_uri`
- `scope`
- `state`

If OIDC:

- `nonce`

### Token Request Parameters

- `grant_type=authorization_code`
- `code`
- `redirect_uri`
- `client_id`
- `client_secret`

For refresh:

- `grant_type=refresh_token`
- `refresh_token`

---

## 16. `state` and `nonce`

### `state`

Used to prevent CSRF attacks and preserve request state.

You should remember:

- client sends `state`
- provider returns the same `state`
- app verifies it

### `nonce`

Mainly used in OpenID Connect.

It helps protect ID token flows against replay and token substitution attacks.

---

## 17. PKCE in Simple Words

PKCE stands for Proof Key for Code Exchange.

It protects authorization code flow from interception.

Basic idea:

1. client creates random secret called `code_verifier`
2. client sends hashed version called `code_challenge`
3. later, during token exchange, client sends original `code_verifier`
4. server verifies they match

This makes stolen authorization codes harder to abuse.

Memory point:

- Authorization Code is strong
- Authorization Code + PKCE is stronger, especially for public clients

---

## 18. Session-Based OAuth2 Login vs Stateless JWT

This is a common confusion.

### OAuth2 Login in Spring MVC App

Usually:

- user logs in through provider
- Spring Security stores authenticated user in session
- browser sends session cookie on later requests

### JWT API Security

Usually:

- server issues JWT
- client sends JWT on every request
- no server session required

Your current project is closer to:

- OAuth2 login for a web app
- session-based authentication after login

It is not the same thing as:

- issuing your own JWT after OAuth login

Though both can be combined.

---

## 19. What Happens in Your Current Project

With your current `SecurityConfig` and `HelloController`:

1. user opens `http://localhost:8080/`
2. route is protected
3. Spring redirects user to OAuth2 login
4. user chooses Google or GitHub
5. provider authenticates user
6. Spring receives callback
7. Spring logs user in
8. controller returns:

```text
Welcome to My World, yashu.com
```

---

## 20. Better Example: Show Logged-In User Info

This is very useful for learning.

```java
package com.yashu.SpringOauth2Demo;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User user) {
        return user.getAttributes();
    }
}
```

This helps you inspect:

- Google user attributes
- GitHub user attributes
- provider-specific fields

---

## 21. Custom Login Page Example

Instead of Spring's default login page, you can create your own links:

```html
<a href="/oauth2/authorization/google">Login with Google</a>
<a href="/oauth2/authorization/github">Login with GitHub</a>
```

Those URLs are important to remember.

---

## 22. SecurityConfig Example with Public Home and Protected Profile

```java
package com.yashu.SpringOauth2Demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login").permitAll()
                .requestMatchers("/me").authenticated()
                .anyRequest().authenticated())
            .oauth2Login(Customizer.withDefaults())
            .logout(logout -> logout.logoutSuccessUrl("/"));

        return http.build();
    }
}
```

---

## 23. Controller Example with Provider Name

```java
package com.yashu.SpringOauth2Demo;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken token) {
            String provider = token.getAuthorizedClientRegistrationId();
            String name = authentication.getName();
            return "Logged in with " + provider + " as " + name;
        }
        return "Welcome";
    }
}
```

---

## 24. Useful Maven Commands

From `19.OAuth2/SpringOauth2Demo`:

### Run the app

```powershell
.\mvnw spring-boot:run
```

### Run tests

```powershell
.\mvnw test
```

### Clean and package

```powershell
.\mvnw clean package
```

### Skip tests

```powershell
.\mvnw clean package -DskipTests
```

### Show dependency tree

```powershell
.\mvnw dependency:tree
```

---

## 25. Useful Git Commands for OAuth2 Projects

### Check status

```bash
git status
```

### See latest commits

```bash
git log --oneline --decorate -n 5
```

### Ignore local secret files

Example `.gitignore` entries:

```gitignore
.env
*.env
client_secret*.json
application-local.properties
```

### Remove a tracked secret file

```bash
git rm --cached path/to/secret-file
git commit -m "Remove tracked secret file"
```

---

## 26. Common Interview Questions

### What is the difference between authentication and authorization?

- authentication = who are you
- authorization = what can you access

### Is OAuth2 authentication?

Not by itself.

OAuth2 is authorization.
Login scenarios usually use OpenID Connect on top of OAuth2.

### What is an ID token?

An OIDC token that carries identity information about the user.

### Why use `state`?

To prevent CSRF and validate the auth response.

### Why use PKCE?

To protect authorization code flow from interception attacks.

### Why not store client secrets in Git?

Because anyone who gets them can impersonate your app at the provider.

---

## 27. Common Mistakes

- committing `client-secret` to GitHub
- using wrong redirect URI
- mixing up OAuth2 and OIDC
- requesting too many scopes
- forgetting that Google login usually needs `openid`
- assuming access token format is always JWT
- assuming every provider returns the same user attributes
- using implicit flow in new apps
- not rotating exposed secrets

---

## 28. Security Best Practices

- always use HTTPS in real deployments
- never commit client secrets
- prefer authorization code flow
- use PKCE for public clients
- validate redirect URIs carefully
- keep scopes minimal
- rotate secrets if exposed
- store secrets in env vars or secret managers
- use provider libraries/framework defaults where possible
- do not assume token contents unless provider documents them

---

## 29. Quick Comparison Table

| Topic | OAuth2 | OpenID Connect |
|---|---|---|
| Main purpose | Authorization | Authentication + identity |
| Token focus | Access token | ID token + access token |
| Answers | "What can app access?" | "Who is the user?" |
| Login usage | Not enough alone | Yes |
| Example | GitHub API access | Login with Google |

---

## 30. Quick Memory Map

If you forget everything, remember this:

1. user clicks `Login with Google/GitHub`
2. app redirects to provider
3. provider authenticates user
4. provider returns authorization code
5. backend exchanges code for token
6. Spring Security creates authenticated user context
7. protected pages now work

---

## 31. One Full Spring Boot Example

### `application.properties`

```properties
spring.application.name=SpringOauth2Demo

spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=openid,profile,email

spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
spring.security.oauth2.client.registration.github.scope=read:user,user:email
```

### `SecurityConfig`

```java
package com.yashu.SpringOauth2Demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login").permitAll()
                .anyRequest().authenticated())
            .oauth2Login(Customizer.withDefaults());

        return http.build();
    }
}
```

### `HelloController`

```java
package com.yashu.SpringOauth2Demo;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String greet(Authentication authentication) {
        if (authentication == null) {
            return "Welcome";
        }
        return "Welcome " + authentication.getName();
    }
}
```

---

## 32. How to Test This Locally

1. set environment variables for Google and/or GitHub
2. run the app
3. open `http://localhost:8080`
4. log in with provider
5. verify callback works
6. hit `/me` if you add a user-info endpoint

---

## 33. What to Revise the Night Before an Interview

- OAuth2 roles
- access token vs refresh token vs authorization code
- OAuth2 vs OIDC
- authorization code flow
- PKCE
- `state`
- redirect URI
- Spring endpoints `/oauth2/authorization/{registrationId}`
- Spring callback `/login/oauth2/code/{registrationId}`
- Google scopes `openid, profile, email`
- GitHub scopes `read:user, user:email`
- never commit client secrets

---

## 34. Final 10-Second Summary

OAuth2 lets your app access protected resources without handling user passwords directly.

For Spring Boot login:

- use `spring-boot-starter-security-oauth2-client`
- configure provider client id and secret
- use `.oauth2Login()`
- redirect user to provider
- receive authorization code
- exchange code for tokens
- Spring logs the user in

For Google login, remember:

- it is usually OpenID Connect on top of OAuth2

For GitHub login, remember:

- it is commonly OAuth2 authorization code flow

---

## 35. Reference Notes Used for This Cheat Sheet

This revision sheet was aligned with:

- RFC 6749 OAuth 2.0 Authorization Framework
- RFC 7636 PKCE
- RFC 8252 OAuth 2.0 for Native Apps
- OpenID Connect Core 1.0
- Spring Security OAuth2 Login reference
- Google OAuth 2.0 for Web Server Applications
- GitHub OAuth app authorization docs

These references were checked online while preparing this sheet so the theory and Spring usage stay aligned with current guidance.
