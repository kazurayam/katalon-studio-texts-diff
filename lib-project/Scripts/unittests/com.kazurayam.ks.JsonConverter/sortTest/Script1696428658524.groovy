import com.kazurayam.ks.JsonConverter

import groovy.json.JsonOutput

String original = """{
    "day_of_week": 0,
    "datetime": "2023-10-01T22:15:18.742317+09:00",
    "client_ip": "163.131.26.17",
    "timezone": "Asia/Tokyo",
    "unixtime": 1696166118,
    "day_of_year": 274,
    "raw_offset": 32400,
    "abbreviation": "JST",
    "dst" : {
        "dst_until": null,
        "dst_offset": 0,
        "dst_until": null,
        "dst": false,
        "dst_from": null
    },
    "utc_datetime": "2023-10-01T13:15:18.742317+00:00",
    "utc_offset": "+09:00",
    "week_number": 39
}
"""

JsonConverter converter = new JsonConverter()
String actual = converter.sort(original)

String pp = JsonOutput.prettyPrint(actual)
println pp

assert pp.indexOf("abbreviation") < pp.indexOf("client_ip")
assert pp.indexOf("client_ip") < pp.indexOf("datetime")
assert pp.indexOf("datetime") < pp.indexOf("day_of_week")

/*
String expected = """{
    "abbreviation": "JST",
    "client_ip": "163.131.26.17",
    "datetime": "2023-10-01T22:15:18.742317+09:00",
    "day_of_week": 0,
    "day_of_year": 274,
    "dst": false,
    "dst_from": null,
    "dst_offset": 0,
    "dst_until": null,
    "raw_offset": 32400,
    "timezone": "Asia/Tokyo",
    "unixtime": 1696166118,
    "utc_datetime": "2023-10-01T13:15:18.742317+00:00",
    "utc_offset": "+09:00",
    "week_number": 39
}
"""
 */