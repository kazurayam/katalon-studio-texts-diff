import groovy.json.JsonOutput

String original = """
{ "abbreviation" : "JST", "client_ip" : "163.131.26.17", "datetime" : "2023-10-01T22:15:18.742317+09:00", "day_of_week" : 0, "day_of_year" : 274, "dst" : { "dst" : false, "dst_from" : null, "dst_offset" : 0, "dst_until" : null }, "raw_offset" : 32400, "timezone" : "Asia/Tokyo", "unixtime" : 1696166118, "utc_datetime" : "2023-10-01T13:15:18.742317+00:00", "utc_offset" : "+09:00", "week_number" : 39 }
"""

String pretty = JsonOutput.prettyPrint(original)
print pretty

File out = new File("./build/tmp/testOutput/ex41-output.json")
out.text = pretty
