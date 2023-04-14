# Endpoints

### Test `/api/test`

* [GET] `/helloWorld`
* [GET] `/ping`
* [GET] `/uptime`

### Version `/api/version`

* [GET] `/app`
    - RequestParam: `appName`

### Email `/api/email`

* [POST] `/contactme`
    - RequestBody (JSON)
      ```
      {
        "subject": "SUBJECT HERE",
        "body": "BODY HERE",
        "contactInfo": "OPTIONAL CONTACT INFO HERE"
      }
      ```
* [GET] `/contactme`
    - RequestParam: `subject`
    - RequestParam: `body`
    - RequestParam: `email` (optional)

### Petfinder `/api/petfinder`

* [GET] `/all`
* [GET] `/filtered`