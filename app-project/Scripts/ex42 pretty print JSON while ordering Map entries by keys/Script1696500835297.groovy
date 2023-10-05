import com.kazurayam.ks.JsonPrettyPrinter

// ex42 pretty print JSON while ordering Map entries by keys

// disorderd JSON
String originalText = """{
    "week_number": 39,
    "day_of_week": 0,
    "utc_datetime": "2023-10-01T13:15:18.742317+00:00",
    "datetime": "2023-10-01T22:15:18.742317+09:00",
    "client_ip": "163.131.26.17",
    "timezone": "Asia/Tokyo",
    "unixtime": 1696166118,
    "day_of_year": 274,
    "raw_offset": 32400,
    "dst" : {
        "dst_until": null,
        "dst_offset": 0,
        "dst_until": null,
        "dst": false,
        "dst_from": null
    },
    "utc_offset": "+09:00",
    "abbreviation": "JST"
}
"""

File out = new File("./build/tmp/testOutput/ex42-output.json")

// pretty print JSON
JsonPrettyPrinter jpp = new JsonPrettyPrinter()
String ordered = jpp.orderMapEntriesByKeys(originalText)

out.text = ordered
print ordered

