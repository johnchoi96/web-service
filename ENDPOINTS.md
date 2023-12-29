# Endpoints

### Root `/`

* [GET] `/`

### Test `/api/test`

* [GET] `/helloWorld`
* [GET] `/ping`
* [GET] `/uptime`

### Version `/api/version`

* [GET] `/app`
    - RequestParam: `appId`

### Email `/api/email`

* [POST] `/contactme`
    - RequestParam: `appId`
    - RequestBody (JSON)
      ```
      {
        "subject": "SUBJECT HERE",
        "body": "BODY HERE",
        "contactInfo": "OPTIONAL CONTACT INFO HERE"
      }
      ```
* [GET] `/contactme`
    - RequestParam: `appId`
    - RequestParam: `subject`
    - RequestParam: `body`
    - RequestParam: `email` (optional)

### Petfinder `/api/petfinder`

* [GET] `/all`
* [GET] `/filtered`
    - RequestParam: `limit` (optional)

### Metal Price `/api/metal-price`

* [GET] `/trigger-report`

### FCM `/api/v1/fcm`

* [GET] `/send-test-notification/{topic}`
    - RequestParam: `key`
    - PathVariable `topic`: target notification topic

### CFB `/api/cfb`

* [GET] `/upsets`
* [GET] `/upsets/{timestamp}`
    - PathVariable `timestamp`: in `yyyy-MM-dd` format