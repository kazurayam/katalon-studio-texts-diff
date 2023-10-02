- original: `build/tmp/testOutput/ex31/text1.json`
- revised : `build/tmp/testOutput/ex31/text2.json`

**DIFFERENT**

- inserted rows: 0
- deleted rows : 0
- changed rows : 3
- equal rows:  : 14

|line#|S|original|revised|
|-----|-|--------|-------|
|1| |{|{|
|2| |    "abbreviation": "JST",|    "abbreviation": "JST",|
|3| |    "client_ip": "163.131.26.171",|    "client_ip": "163.131.26.171",|
|4|C|    "datetime": "2023-10-<span style="color:red; font-weight:bold; background-color:#ffeef0">01T22:15:18</span>.<span style="color:red; font-weight:bold; background-color:#ffeef0">742317</span>+09:00",|    "datetime": "2023-10-<span style="color:green; font-weight:bold; background-color:#e6ffec">01T22:15:22</span>.<span style="color:green; font-weight:bold; background-color:#e6ffec">058915</span>+09:00",|
|5| |    "day_of_week": 0,|    "day_of_week": 0,|
|6| |    "day_of_year": 274,|    "day_of_year": 274,|
|7| |    "dst": false,|    "dst": false,|
|8| |    "dst_from": null,|    "dst_from": null,|
|9| |    "dst_offset": 0,|    "dst_offset": 0,|
|10| |    "dst_until": null,|    "dst_until": null,|
|11| |    "raw_offset": 32400,|    "raw_offset": 32400,|
|12| |    "timezone": "Asia/Tokyo",|    "timezone": "Asia/Tokyo",|
|13|C|    "unixtime": <span style="color:red; font-weight:bold; background-color:#ffeef0">1696166118</span>,|    "unixtime": <span style="color:green; font-weight:bold; background-color:#e6ffec">1696166122</span>,|
|14|C|    "utc_datetime": "2023-10-<span style="color:red; font-weight:bold; background-color:#ffeef0">01T13:15:18</span>.<span style="color:red; font-weight:bold; background-color:#ffeef0">742317</span>+00:00",|    "utc_datetime": "2023-10-<span style="color:green; font-weight:bold; background-color:#e6ffec">01T13:15:22</span>.<span style="color:green; font-weight:bold; background-color:#e6ffec">058915</span>+00:00",|
|15| |    "utc_offset": "+09:00",|    "utc_offset": "+09:00",|
|16| |    "week_number": 39|    "week_number": 39|
|17| |}|}|
