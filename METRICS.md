# Prometheus Metrics

## User & Session & Mail

- hyperauth_users (Gauge)
  - help : Total number of user
  - label : realm
  
- hyperauth_total_user_sessions (Gauge)
  - help : Total number of user sessions
  - label : realm
  
- hyperauth_user_sessions (Gauge)
  - help : Number of user sessions per client
  - label : realm, client_id
   
- hyperauth_mail_request (Counter)
  - help : Total number of mail send request
  - label : realm, mailServer
  
- hyperauth_mail_send (Counter)
  - help : Total number of mail send success
  - label : realm, mailServer
  
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

- process_cpu_seconds_total (Counter)
  - help : Total user and system CPU time spent in seconds

- process_start_time_seconds (Counter)
  - help : Start time of the process since unix epoch in seconds

- process_virtual_memory_bytes (Gauge)
  - help : Virtual memory size in bytes

- process_resident_memory_bytes (Gauge)
  - help : Resident memory size in bytes

- jvm_memory_bytes_used (Gauge)
  - help : Used bytes of a given JVM memory area

- jvm_memory_bytes_committed (Gauge)
  - help : Committed (bytes) of a given JVM memory area

- jvm_memory_bytes_max (Gauge)
  - help : Max (bytes) of a given JVM memory area

- jvm_memory_bytes_init (Gauge)
  - help : Initial bytes of a given JVM memory area

- jvm_memory_pool_bytes_used (Gauge)
  - help : Used bytes of a given JVM memory pool

- jvm_memory_pool_bytes_committed (Gauge)
  - help : Committed bytes of a given JVM memory pool
  
- jvm_memory_pool_bytes_max (Gauge)
  - help : Max bytes of a given JVM memory pool

- jvm_memory_pool_bytes_init (Gauge)
  - help : Initial bytes of a given JVM memory pool

- jvm_memory_pool_allocated_bytes_total (Counter)
  - help : Total bytes allocated in a given JVM memory pool. Only updated after GC, not continuously

- jvm_buffer_pool_used_bytes (Gauge)
  - help : Used bytes of a given JVM buffer pool
  
- jvm_buffer_pool_capacity_bytes (Gauge)
  - help : Bytes capacity of a given JVM buffer pool

- jvm_buffer_pool_used_buffers (Gauge)
  - help : Used buffers of a given JVM buffer pool

- jvm_gc_collection_seconds (Summary)
  - help : Time spent in a given JVM garbage collector in seconds

- jvm_threads_current (Gauge)
  - help : Current thread count of a JVM
 
- jvm_threads_daemon (Gauge)
  - help : Current thread count of a JVM

- jvm_threads_peak (Gauge)
  - help : Peak thread count of a JVM
  
- jvm_threads_started_total (Count)
  - help : Started thread count of a JVM
  
- jvm_threads_deadlocked (Gauge)
  - help : Cycles of JVM-threads that are in deadlock waiting to acquire object monitors or ownable synchronizers

- jvm_threads_deadlocked_monitor (Gauge)
  - help : Cycles of JVM-threads that are in deadlock waiting to acquire object monitors

- jvm_threads_state (Gauge)
  - help : Current count of threads by state

- jvm_classes_loaded (Gauge)
  - help : The number of classes that are currently loaded in the JVM

- jvm_classes_loaded_total (Counter)
  - help : The total number of classes that have been loaded since the JVM has started execution

- jvm_classes_unloaded_total (Counter)
  - help : The total number of classes that have been unloaded since the JVM has started execution

- jvm_info (Gauge)
  - help : JVM version info












