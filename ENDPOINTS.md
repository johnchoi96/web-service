# Endpoints

### Test `/api/test`

* [GET] `/helloWorld`
* [GET] `/ping`

### Version `/api/version`

* [GET] `/app`
    - RequestParam: `appName`

### Email `/api/email`

* [POST] `/send`
    - RequestBody (JSON)
      ```
      {
        "subject": "SUBJECT HERE",
        "body": "BODY HERE",
        "contactInfo": "OPTIONAL CONTACT INFO HERE"
      }
      ```