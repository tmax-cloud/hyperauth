# Prometheus Metrics

## User & Session & Mail

- hyperauth_users (Gauge)
  - help : Total number of user
  - label : realm
  - 
- hyperauth_total_user_sessions (Gauge)
  - help : Total number of user sessions
  - label : realm
  - 
- hyperauth_user_sessions (Gauge)
  - help : Number of user sessions per client
  - label : realm, client_id
  - 
- hyperauth_mail_request (Counter)
  - help : Total number of mail send request
  - label : realm, mailServer
  - 
- hyperauth_mail_send (Counter)
  - help : Total number of mail send success
  - label : realm, mailServer
  - 
- hyperauth_failed_mail_send (Counter)
  - help : Total number of mail send failed
  - label : realm, mailServer


## Events

- hyperauth_login_attempts (Counter)
  - help : Total number of login attempts
  - label : realm, provider, client_id
  
- hyperauth_logins (Counter)
  - help : Total successful logins
  - label : realm, provider, client_id
   
- hyperauth_failed_login_attempts (Counter)
  - help : Total failed login attempts
  - label : realm, provider, error, client_id
   
- hyperauth_registrations (Counter)
  - help : hyperauth_registrations
  - label : realm, provider, client_id
 
- hyperauth_registrations_errors (Counter)
  - help : Total errors on registrations
  - label : realm, provider, error, client_id
   
- hyperauth_refresh_tokens (Counter)
  - help : Total number of successful token refreshes
  - label : realm, provider, client_id

- hyperauth_refresh_tokens_errors (Counter)
  - help : Total number of failed token refreshes
  - label : realm, provider, error, client_id

- hyperauth_client_logins (Counter)
  - help : Total successful client logins
  - label : realm, provider, client_id

- hyperauth_failed_client_login_attempts (Counter)
  - help : hyperauth_failed_client_login_attempts
  - label : realm, provider, error, client_id

- hyperauth_code_to_tokens (Counter)
  - help : Total number of successful code to token
  - label : realm, provider, client_id

- hyperauth_code_to_tokens_errors (Counter)
  - help : Total number of failed code to token
  - label : realm, provider, error, client_id

- hyperauth_response_total (Counter)
  - help : Total number of responses
  - label : code, method, resource

- hyperauth_response_errors (Counter)
  - help : Total number of error responses
  - label : code, method, resource

- hyperauth_request_duration (Histogram)
  - help : Request duration
  - buckets : 50, 100, 250, 500, 1000, 2000, 10000, 30000
  - label : method, resource

## JVM 



